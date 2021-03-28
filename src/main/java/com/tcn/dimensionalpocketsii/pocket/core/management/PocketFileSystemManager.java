package com.tcn.dimensionalpocketsii.pocket.core.management;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tcn.cosmoslibrary.common.enums.EnumGeneralAllowState;
import com.tcn.cosmoslibrary.common.enums.EnumGeneratedState;
import com.tcn.cosmoslibrary.common.enums.EnumLockState;
import com.tcn.cosmoslibrary.common.enums.EnumSideState;
import com.tcn.cosmoslibrary.common.enums.EnumTrapState;
import com.tcn.cosmoslibrary.common.math.ChunkPos;
import com.tcn.cosmoslibrary.registry.gson.GsonAdapterConnectionType;
import com.tcn.cosmoslibrary.registry.gson.GsonAdapterFluidTankCustom;
import com.tcn.cosmoslibrary.registry.gson.GsonAdapterGeneralAllowState;
import com.tcn.cosmoslibrary.registry.gson.GsonAdapterGeneratedState;
import com.tcn.cosmoslibrary.registry.gson.GsonAdapterItemStack;
import com.tcn.cosmoslibrary.registry.gson.GsonAdapterLockState;
import com.tcn.cosmoslibrary.registry.gson.GsonAdapterSideState;
import com.tcn.cosmoslibrary.registry.gson.GsonAdapterTrapState;
import com.tcn.cosmoslibrary.registry.gson.object.ObjectConnectionType;
import com.tcn.cosmoslibrary.registry.gson.object.ObjectFluidTankCustom;
import com.tcn.dimensionalpocketsii.DimReference;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager.PocketGenParameters;

import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.NonNullList;
import net.minecraft.world.storage.SaveFormat;
import net.minecraft.world.storage.SaveFormat.LevelSave;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class PocketFileSystemManager {

	private static Gson GSON = new GsonBuilder()
			.registerTypeAdapter(ItemStack.class, new GsonAdapterItemStack())
			.registerTypeAdapter(ObjectFluidTankCustom.class, new GsonAdapterFluidTankCustom())
			.registerTypeAdapter(ObjectConnectionType.class, new GsonAdapterConnectionType())
			.registerTypeAdapter(EnumLockState.class, new GsonAdapterLockState())
			.registerTypeAdapter(EnumSideState.class, new GsonAdapterSideState())
			.registerTypeAdapter(EnumTrapState.class, new GsonAdapterTrapState())
			.registerTypeAdapter(EnumGeneratedState.class, new GsonAdapterGeneratedState())
			.registerTypeAdapter(EnumGeneralAllowState.class, new GsonAdapterGeneralAllowState())
			.enableComplexMapKeySerialization().setPrettyPrinting().create();

	private static final String currentBackLinkFile = "pocketRegistry";
	private static final String pocketGenParamsFile = "pocketGenParameters";
	
	private static File getFile(String fileName) throws IOException {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		StringBuilder file_path = new StringBuilder();
		StringBuilder backup_path = new StringBuilder();
		
		if (server != null) {
			if (server.isSingleplayer()) {
				file_path.append("saves/");
				backup_path.append("saves/");
			}
			
			final Object save = ObfuscationReflectionHelper.getPrivateValue(MinecraftServer.class, server, "field_71310_m");
			
			if (save instanceof SaveFormat.LevelSave) {
				String save_name = ((LevelSave) save).getLevelId();
				
				file_path.append(save_name);
				backup_path.append(save_name);
			}
			
			file_path.append("/dimpockets/" + fileName + ".json");
			backup_path.append("/dimpockets/" + fileName + "_BACKUP.json");
	
			File save_file = server.getFile(file_path.toString());
			File backup_file = server.getFile(backup_path.toString());
			
			if (!save_file.exists()) {
				save_file.getParentFile().mkdirs();
				save_file.createNewFile();
			}
			
			//Makes a backup
			if (!backup_file.exists()) {
				backup_file.getParentFile().mkdirs();
				backup_file.createNewFile();	
				FileUtils.copyFile(save_file, backup_file);
			} else {
				FileUtils.copyFile(save_file, backup_file);
			}
			
			return save_file;
		}
		return new File(".");
	}
	
	public static void saveBackLinkMap(Map<ChunkPos, Pocket> backLinkMap) {		
		try {
			File registryFile = getFile(currentBackLinkFile);

			Collection<Pocket> values = backLinkMap.values();
			Pocket[] tempArray = values.toArray(new Pocket[values.size()]);

			try (FileWriter writer = new FileWriter(registryFile)) {
				GSON.toJson(tempArray, writer);
				writer.flush();
			}

		} catch (Exception e) {
			DimensionalPockets.LOGGER.fatal("Error when saving backLinkFile", e.getCause());
		}
	}

	public static Map<ChunkPos, Pocket> loadBackLinkMap() {
		Map<ChunkPos, Pocket> backLinkMap = new HashMap<>();
		try {
			File registryFile = getFile(currentBackLinkFile);

			Pocket[] pocket_array = null;
			try (FileReader reader = new FileReader(registryFile)) {
				pocket_array = GSON.fromJson(reader, Pocket[].class);
			}

			if (pocket_array != null) {
				for (Pocket link : pocket_array) {

					/** - Legacy Save Checks - */
					if (link.getFluidTank() == null) {
						link.setFluidTank(new FluidTank(256000));
						
						DimensionalPockets.LOGGER.warn("[LEGACY] Pocket {fluid_tank} null! Create new fluid_tank...");
					}
					
					if (link.item_array.size() != DimReference.CONSTANT.POCKET_HELD_ITEMS_SIZE) {
						NonNullList<ItemStack> items_ = NonNullList.<ItemStack>withSize(DimReference.CONSTANT.POCKET_HELD_ITEMS_SIZE, ItemStack.EMPTY);
						
						for (int i = 0; i < link.item_array.size(); i++) {
							items_.set(i, link.item_array.get(i));
						}
						
						link.item_array = items_;
						DimensionalPockets.LOGGER.warn("[LEGACY] Pocket {items} not equal! Creating new <items> and replace...");
					}
					
					backLinkMap.put(link.getChunkPos(), link);
					DimensionalPockets.LOGGER.info("Pocket Loaded: {" + link.getChunkPos() + ", Owner: " + link.getOwnerName() + "}");
				}
			}
		} catch (Exception e) {
			DimensionalPockets.LOGGER.fatal("Error when loading backLinkFile", e.getCause());
		}
		return backLinkMap;
	}

	public static void savePocketGenParams(PocketGenParameters pocketGenParameters) {
		try {
			File dataFile = getFile(pocketGenParamsFile);

			try (FileWriter writer = new FileWriter(dataFile)) {
				GSON.toJson(pocketGenParameters, writer);
			}
		} catch (Exception e) {
			DimensionalPockets.LOGGER.fatal("Error when saving pocketGenParamsFile", e.getCause());
		}
	}

	public static PocketGenParameters loadPocketGenParams() {
		try {
			File dataFile = getFile(pocketGenParamsFile);

			if (dataFile.exists()) {
				try (FileReader dataReader = new FileReader(dataFile)) {
					PocketGenParameters pocketGenParams = GSON.fromJson(dataReader, PocketGenParameters.class);
					if (pocketGenParams != null)
						return pocketGenParams;
				}
			}
		} catch (Exception e) {
			DimensionalPockets.LOGGER.fatal("Error when loading pocketGenParamsFile", e.getCause());
		}
		return new PocketGenParameters();
	}
}