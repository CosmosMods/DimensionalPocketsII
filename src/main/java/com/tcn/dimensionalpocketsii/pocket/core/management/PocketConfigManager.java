package com.tcn.dimensionalpocketsii.pocket.core.management;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tcn.cosmoslibrary.impl.registry.gson.GsonAdapterConnectionType;
import com.tcn.cosmoslibrary.impl.registry.gson.GsonAdapterFluidTankCustom;
import com.tcn.cosmoslibrary.impl.registry.gson.GsonAdapterItemStack;
import com.tcn.cosmoslibrary.impl.registry.object.ObjectConnectionType;
import com.tcn.cosmoslibrary.impl.registry.object.ObjectFluidTankCustom;
import com.tcn.cosmoslibrary.math.ChunkPos;
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

public class PocketConfigManager {

	private static Gson GSON = new GsonBuilder()
			.registerTypeAdapter(ItemStack.class, new GsonAdapterItemStack())
			.registerTypeAdapter(ObjectFluidTankCustom.class, new GsonAdapterFluidTankCustom())
			.registerTypeAdapter(ObjectConnectionType.class, new GsonAdapterConnectionType())
			.enableComplexMapKeySerialization().setPrettyPrinting().create();

	private static final String currentBackLinkFile = "pocketRegistry";
	private static final String legacyBackLinkFile = "teleportRegistry";
	private static final String pocketGenParamsFile = "pocketGenParameters";
	
	private static File getConfig(String fileName) throws IOException {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		StringBuilder file_path = new StringBuilder();
		
		if (server != null) {
			if (server.isSinglePlayer()) {
				file_path.append("saves/");
			}
			
			final Object save = ObfuscationReflectionHelper.getPrivateValue(MinecraftServer.class, server, "field_71310_m");
			
			if (save instanceof SaveFormat.LevelSave) {
				String save_name = ((LevelSave) save).getSaveName();
				
				file_path.append(save_name);
				
				System.out.println("File: [" + fileName + "] Accessed for World: [" + save_name + "]");
			}
			
			file_path.append("/dimpockets/");
			file_path.append(fileName);
			file_path.append(".json");
	
			File savefile = server.getFile(file_path.toString());
			if (!savefile.exists()) {
				savefile.getParentFile().mkdirs();
				savefile.createNewFile();
			}
			return savefile;
		}
		return new File(".");
	}
	
	private static File getConfigLegacy(String file_name, String legacy_file_name) throws IOException {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		StringBuilder file_path = new StringBuilder();
		StringBuilder legacy_path = new StringBuilder();
		StringBuilder backup_path = new StringBuilder();
		
		if (server != null) {
			if (server.isSinglePlayer()) {
				file_path.append("saves/");
				legacy_path.append("saves/");
				backup_path.append("saves/");
			}
	
			final Object save = ObfuscationReflectionHelper.getPrivateValue(MinecraftServer.class, server, "field_71310_m");
			
			if (save instanceof SaveFormat.LevelSave) {
				String save_name = ((LevelSave) save).getSaveName();
				
				file_path.append(save_name);
				legacy_path.append(save_name);
				backup_path.append(save_name);
				
				System.out.println("File: [" + file_name + "] Accessed for World: [" + save_name + "]");
				System.out.println("File: [" + legacy_file_name + "] Accessed for World: [" + save_name + "]");
				System.out.println("File: [backupRegistry] Accessed for World: [" + save_name + "]");
			}
			
			file_path.append("/dimpockets/");
			file_path.append(file_name);
			file_path.append(".json");
			
			legacy_path.append("/dimpockets/");
			legacy_path.append(legacy_file_name);
			legacy_path.append(".json");
	
			backup_path.append("/dimpockets/");
			backup_path.append("backupRegistry");
			backup_path.append(".json");
	
			File save_file = server.getFile(file_path.toString());
			File legacy_file = server.getFile(legacy_path.toString());
			File backup_file = server.getFile(backup_path.toString());
			
			/**
			 * Checks to see if the current file exists. 
			 * If it doesn't, it loads the data from the legacy file and creates the current file.
			 */
			if (!save_file.exists()) {
				if (!legacy_file.exists()) {
					save_file.getParentFile().mkdirs();
					save_file.createNewFile();
				} else {
					save_file.getParentFile().mkdirs();
					FileUtils.copyFile(legacy_file, save_file);
					return save_file;
				}
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
	
	public static File encryptFile() {
		//Will start encrypting the loaded file so it cannot be edited.
		
		return null;
	}

	public static void saveBackLinkMap(Map<ChunkPos, Pocket> backLinkMap) {		
		try {
			File registryFile = getConfigLegacy(currentBackLinkFile, legacyBackLinkFile);

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
			File registryFile = getConfigLegacy(currentBackLinkFile, legacyBackLinkFile);

			Pocket[] pocket_array = null;
			try (FileReader reader = new FileReader(registryFile)) {
				pocket_array = GSON.fromJson(reader, Pocket[].class);
			}

			if (pocket_array != null) {
				for (Pocket link : pocket_array) {

					/** - Legacy Save Checks - */
					if (link.getFluidTank() == null) {
						link.setFluidTank(new FluidTank(256000));
						
						DimensionalPockets.LOGGER.warn("[LEGACY] Pocket {fluid_tank} null! Create new fluid_tank...", PocketConfigManager.class);
					}
					
					/*
					if (!(link.getBlockDim() == -99) && link.getSourceBlockDimensionType() == null) {
						switch (link.getBlockDim()) {
							case 0:
								link.setSourceBlockDimensionType(DimensionType.OVERWORLD);
							case -1:
								link.setSourceBlockDimensionType(DimensionType.THE_NETHER);
							case 1:
								link.setSourceBlockDimensionType(DimensionType.THE_END);
							case 98:
								link.setSourceBlockDimensionType(ModDimensionManager.POCKET_DIMENSION.getDimensionType());
							default:
								link.setSourceBlockDimensionType(DimensionType.OVERWORLD);
						}
						
						DimensionalPockets.LOGGER.warn("[LEGACY] Pocket {block_dimension_type} null! Setting to legacy {blockDim}: [" + link.getBlockDim() + "]", PocketConfigManager.class);
					}
					
					
					if (link.getMaxEnergyStored() != DimReference.CONSTANT.POCKET_RF_CAP) {
						link.energy_capacity = DimReference.CONSTANT.POCKET_RF_CAP;
						
						DimensionalPockets.LOGGER.warn("[LEGACY] Pocket {energy_capacity} not equal! Correcting...", PocketConfigManager.class);
					}
					*/
					if (link.item_array.size() != DimReference.CONSTANT.POCKET_HELD_ITEMS_SIZE) {
						NonNullList<ItemStack> items_ = NonNullList.<ItemStack>withSize(DimReference.CONSTANT.POCKET_HELD_ITEMS_SIZE, ItemStack.EMPTY);
						
						for (int i = 0; i < link.item_array.size(); i++) {
							items_.set(i, link.item_array.get(i));
						}
						
						link.item_array = items_;
						DimensionalPockets.LOGGER.warn("[LEGACY] Pocket {items} not equal! Creating new <items> and replace...", PocketConfigManager.class);
					}
					
					backLinkMap.put(link.getChunkPos(), link);
					DimensionalPockets.LOGGER.info("Pocket Loaded: {" + link + "}", PocketConfigManager.class);
				}
			}
		} catch (Exception e) {
			DimensionalPockets.LOGGER.fatal("Error when loading backLinkFile", e.getCause());
		}
		return backLinkMap;
	}

	public static void savePocketGenParams(PocketGenParameters pocketGenParameters) {
		try {
			File dataFile = getConfig(pocketGenParamsFile);

			try (FileWriter writer = new FileWriter(dataFile)) {
				GSON.toJson(pocketGenParameters, writer);
			}
		} catch (Exception e) {
			DimensionalPockets.LOGGER.fatal("Error when saving pocketGenParamsFile", e.getCause());
		}
	}

	public static PocketGenParameters loadPocketGenParams() {
		try {
			File dataFile = getConfig(pocketGenParamsFile);

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
	
	public static String getWorldFolder(MinecraftServer server) throws NoSuchFieldException, IllegalAccessException {
		Field folderField = server.getClass().getDeclaredField("anvilConverterForAnvilFile");
		folderField.setAccessible(true);
		return ((SaveFormat.LevelSave) folderField.get(server)).getSaveName();
	}
}