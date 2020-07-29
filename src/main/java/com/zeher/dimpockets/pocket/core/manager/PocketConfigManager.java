package com.zeher.dimpockets.pocket.core.manager;

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
import com.zeher.dimpockets.DimReference;
import com.zeher.dimpockets.DimensionalPockets;
import com.zeher.dimpockets.core.manager.ModDimensionManager;
import com.zeher.dimpockets.pocket.core.Pocket;
import com.zeher.dimpockets.pocket.core.manager.PocketRegistryManager.PocketGenParameters;
import com.zeher.zeherlib.api.compat.core.adapter.DimensionTypeAdapter;
import com.zeher.zeherlib.api.compat.core.adapter.FluidTankAdapter;
import com.zeher.zeherlib.api.compat.core.adapter.ItemStackAdapter;

import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class PocketConfigManager {

	private static Gson GSON = new GsonBuilder()
			//.registerTypeAdapter(NonNullList.class, new NonNullListItemStackAdapter())
			.registerTypeAdapter(DimensionType.class, new DimensionTypeAdapter())
			.registerTypeAdapter(ItemStack.class, new ItemStackAdapter())
			.registerTypeAdapter(FluidTank.class, new FluidTankAdapter())
			.enableComplexMapKeySerialization().setPrettyPrinting().create();

	private static final String currentBackLinkFile = "pocketRegistry";
	private static final String legacyBackLinkFile = "teleportRegistry";
	private static final String pocketGenParamsFile = "pocketGenParameters";
	
	private static File getConfig(String fileName) throws IOException {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		StringBuilder file_path = new StringBuilder();

		if (server.isSinglePlayer()) {
			file_path.append("saves/");
		}

		file_path.append(server.getFolderName());
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
	
	private static File getConfigLegacy(String file_name, String legacy_file_name) throws IOException {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		StringBuilder file_path = new StringBuilder();
		StringBuilder legacy_path = new StringBuilder();
		StringBuilder backup_path = new StringBuilder();

		if (!(server.isDedicatedServer())) {
			file_path.append("saves/");
			legacy_path.append("saves/");
			backup_path.append("saves/");
		}
		
		file_path.append(server.getFolderName());
		file_path.append("/dimpockets/");
		file_path.append(file_name);
		file_path.append(".json");
		
		legacy_path.append(server.getFolderName());
		legacy_path.append("/dimpockets/");
		legacy_path.append(legacy_file_name);
		legacy_path.append(".json");

		backup_path.append(server.getFolderName());
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

	public static void saveBackLinkMap(Map<BlockPos, Pocket> backLinkMap) {		
		try {
			File registryFile = getConfigLegacy(currentBackLinkFile, legacyBackLinkFile);

			Collection<Pocket> values = backLinkMap.values();
			Pocket[] tempArray = values.toArray(new Pocket[values.size()]);

			try (FileWriter writer = new FileWriter(registryFile)) {
				GSON.toJson(tempArray, writer);
				writer.flush();
			}

		} catch (Exception e) {
			DimensionalPockets.LOGGER.fatal("Error when saving backLinkFile", e);
		}
	}

	public static Map<BlockPos, Pocket> loadBackLinkMap() {
		Map<BlockPos, Pocket> backLinkMap = new HashMap<>();
		try {
			File registryFile = getConfigLegacy(currentBackLinkFile, legacyBackLinkFile);

			Pocket[] tempArray = null;
			try (FileReader reader = new FileReader(registryFile)) {
				tempArray = GSON.fromJson(reader, Pocket[].class);
			}

			if (tempArray != null) {
				for (Pocket link : tempArray) {

					/** - Legacy Save Checks - */
					if (link.getFluidTank() == null) {
						link.setFluidTank(new FluidTank(256000));
						
						DimensionalPockets.LOGGER.warn("[LEGACY] Pocket {fluid_tank} null! Create new fluid_tank...", PocketConfigManager.class);
					}
					
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
						
						DimensionalPockets.LOGGER.warn("[LEGACY] Pocket {block_dimension_type} null! Setting to legacy: [" + link.getBlockDim() + "]", PocketConfigManager.class);
					}
					
					if (link.getMaxEnergyStored() != DimReference.CONSTANT.POCKET_RF_CAP) {
						link.capacity = DimReference.CONSTANT.POCKET_RF_CAP;
						
						DimensionalPockets.LOGGER.warn("[LEGACY] Pocket {energy_capacity} not equal! Correcting...", PocketConfigManager.class);
					}
					
					if (link.items.size() != DimReference.CONSTANT.POCKET_HELD_ITEMS_SIZE) {
						NonNullList<ItemStack> items_ = NonNullList.<ItemStack>withSize(DimReference.CONSTANT.POCKET_HELD_ITEMS_SIZE, ItemStack.EMPTY);
						
						for (int i = 0; i < link.items.size(); i++) {
							items_.set(i, link.items.get(i));
						}
						
						link.items = items_;
						DimensionalPockets.LOGGER.warn("[LEGACY] Pocket {items} not equal! Creating new <items> and replace...", PocketConfigManager.class);
					}
					
					backLinkMap.put(link.getChunkPos(), link);
					DimensionalPockets.LOGGER.info("Pocket Loaded: {" + link + "}", PocketConfigManager.class);
				}
			}
		} catch (Exception e) {
			DimensionalPockets.LOGGER.fatal("Error when loading backLinkFile", e);
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
			DimensionalPockets.LOGGER.fatal("Error when saving pocketGenParamsFile", e);
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
			DimensionalPockets.LOGGER.fatal("Error when loading pocketGenParamsFile", e);
		}
		return new PocketGenParameters();
	}
}