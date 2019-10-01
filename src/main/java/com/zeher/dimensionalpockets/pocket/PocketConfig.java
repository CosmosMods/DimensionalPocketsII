package com.zeher.dimensionalpockets.pocket;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zeher.dimensionalpockets.core.util.DimLogger;
import com.zeher.dimensionalpockets.pocket.PocketRegistry.PocketGenParameters;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class PocketConfig {

	private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private static final String backLinkFile = "teleportRegistry";
	private static final String pocketGenParamsFile = "pocketGenParameters";
	
	private static File getConfig(String fileName) throws IOException {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance().getServer();
		StringBuilder pathName = new StringBuilder();

		if (server.isSinglePlayer()) {
			pathName.append("saves/");
		}

		pathName.append(server.getFolderName());
		pathName.append("/dimpockets/");
		pathName.append(fileName);
		pathName.append(".json");

		File savefile = server.getFile(pathName.toString());
		if (!savefile.exists()) {
			savefile.getParentFile().mkdirs();
			savefile.createNewFile();
		}
		return savefile;
	}

	public static void saveBackLinkMap(Map<BlockPos, Pocket> backLinkMap) {
		try {
			File registryFile = getConfig(backLinkFile);

			Collection<Pocket> values = backLinkMap.values();
			Pocket[] tempArray = values.toArray(new Pocket[values.size()]);

			try (FileWriter writer = new FileWriter(registryFile)) {
				GSON.toJson(tempArray, writer);
				writer.flush();
			}

		} catch (Exception e) {
			DimLogger.severe("Error when saving backLinkFile", e);
		}
	}

	public static Map<BlockPos, Pocket> loadBackLinkMap() {
		Map<BlockPos, Pocket> backLinkMap = new HashMap<>();
		try {
			File registryFile = getConfig(backLinkFile);

			Pocket[] tempArray = null;
			try (FileReader reader = new FileReader(registryFile)) {
				tempArray = GSON.fromJson(reader, Pocket[].class);
			}

			if (tempArray != null) {
				for (Pocket link : tempArray) {
					backLinkMap.put(link.getChunkPos(), link);
				}
			}

		} catch (Exception e) {
			DimLogger.severe("Error when loading backLinkFile", e);
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
			DimLogger.severe("Error when saving pocketGenParamsFile", e);
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
			DimLogger.severe("Error when loading pocketGenParamsFile", e);
		}
		return new PocketGenParameters();
	}

}
