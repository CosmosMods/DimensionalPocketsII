package com.tcn.dimensionalpocketsii.pocket.core.management;

import java.util.LinkedHashMap;
import java.util.Map;

import com.tcn.cosmoslibrary.common.lib.CosmosChunkPos;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.core.management.DimensionManager;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.gson.PocketChunkInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;

/**
 * @author TheCosmicNebula_
 */
public class PocketRegistryManager {
	private static Map<PocketChunkInfo, Pocket> pocketMap = new LinkedHashMap<>();

	//How far the pockets are apart (This value - 1 is how many chunks between each pocket in the X or Z axis. [NOT DYNAMIC]
	private static final int pocketChunkSpacing = 4;
	
	//The offset in the Y direction of how far each pocket is above the bedrock layer in the Pocket Dimension. (0 is the bedrock layer) [NOT DYNAMIC]
	private static final int pocketYOffset = 1;
	
	//The interior size of the structure + 1. [Not a dynamic value. If different size pockets are required, extra dimensions will have to be added] [NOT DYNAMIC]
	private static final int pocketSize = 15;
	
	//A class that contains information about where [(CosmosChunkPos(X,Z)) and Direction] to generate the next Pocket.
	private static PocketGenParameters pocketGenParameters = new PocketGenParameters();

	static class PocketGenParameters {
		private CosmosChunkPos currentChunk = new CosmosChunkPos(0, 0);
		private Direction nextPocketCoordsDirection = Direction.NORTH;
	}
	
	public static int getPocketYOffset() {
		return pocketYOffset;
	}
	
	public static int getPocketSize() {
		return pocketSize;
	}

	public static ServerLevel getLevelForPockets() {
		return ServerLifecycleHooks.getCurrentServer().getLevel(DimensionManager.POCKET_WORLD);
	}
	
	public static Map<PocketChunkInfo, Pocket> getPocketMap() {
		return pocketMap;
	}
	
	public static void clearPocketMap() {
		pocketMap.clear();
		
		DimensionalPockets.CONSOLE.debugWarn("[Pocket Registry] <clearmap> Pocket Map Cleared");
	}
	
	public static Pocket getPocketFromChunkPosition(CosmosChunkPos chunkPos) {
		Pocket[] pocket = new Pocket[] { new Pocket(null) };
		boolean[] found = new boolean[] { false };
		
		if (chunkPos != null) {
			pocketMap.forEach((info, pock) -> {
				if (info.isChunkContained(chunkPos)) {
					pocket[0] = pock;
					found[0] = true;
				}
			});
			
			if (!found[0]) {
				DimensionalPockets.CONSOLE.debugWarn("[Pocket Registry Error] <getpocket> Unable to retrieve Pocket: { " + chunkPos + " }. Chunk Position not present in map. <ChunkPos>");
			}
		} else {
			DimensionalPockets.CONSOLE.debugWarn("[Pocket Registry Error] <getpocket> Unable to retrieve Pocket: { " + chunkPos + " }. Chunk Position <null>!! <ChunkPos>");
		}
		
		return pocket[0];
	}

	public static Pocket getPocketFromChunkInfo(PocketChunkInfo chunkPos) {
		Pocket[] pocket = new Pocket[] { new Pocket(null) };
		
		if (chunkPos != null) {
			pocketMap.forEach((info, pock) -> {
				if (info.getDominantChunk() == chunkPos.getDominantChunk()) {
					pocket[0] = pock;
				}
			});
			
			if (pocket[0].getChunkInfo() == null) {
				DimensionalPockets.CONSOLE.debugWarn("[Pocket Registry Error] <getpocket> Unable to retrieve Pocket: { " + chunkPos + " }. Chunk Position not present in map. <ChunkInfo>");
			}
			
		} else {
			DimensionalPockets.CONSOLE.debugWarn("[Pocket Registry Error] <getpocket> Unable to retrieve Pocket: { " + chunkPos + " }. Chunk Position <null>!! <ChunkInfo>");
		}
		
		return pocket[0];
	}

	public static CosmosChunkPos getNextPocketChunkPosition(CosmosChunkPos currentCoords) {
		CosmosChunkPos[] result = new CosmosChunkPos[] { currentCoords };
		
		pocketMap.keySet().stream().forEach((test) -> {
			if (test.getDominantChunk() == result[0]) {
				CosmosChunkPos offset = new CosmosChunkPos(0, 0).offset(pocketGenParameters.nextPocketCoordsDirection);
				
				result[0] = new CosmosChunkPos(result[0].getX(), result[0].getZ()).add(offset.getX() * pocketChunkSpacing, offset.getZ() * pocketChunkSpacing);
			}
		});
		
		Direction clockwise = pocketGenParameters.nextPocketCoordsDirection.getClockWise();
		CosmosChunkPos probe = new CosmosChunkPos(0, 0).offset(clockwise);
		CosmosChunkPos probe2 = new CosmosChunkPos(probe.getX() * pocketChunkSpacing, probe.getZ() * pocketChunkSpacing);
		
		CosmosChunkPos probed = new CosmosChunkPos(result[0]).add(probe2);
		
		pocketMap.keySet().stream().forEach((test) -> {
			if (test.getDominantChunk() == probed) {
				pocketGenParameters.nextPocketCoordsDirection = clockwise;
			}
		});
		
		return result[0];
	}
	
	private static PocketChunkInfo getChunkInfoForPocket(ResourceKey<Level> dimension, BlockPos pos) {
		for(Pocket pocket : pocketMap.values()) {
			ResourceKey<Level> pocket_dimension = pocket.getSourceBlockDimension();
			
			if (pocket_dimension.equals(dimension) && pocket.doesBlockArrayContain(pos, dimension.location())) {
				return pocket.getChunkInfo();
			} 
		}
		
		return null;
	}
	
	public static Pocket getOrCreatePocket(Level world, BlockPos coordSetSource, boolean isSingleChunk){
		ResourceKey<Level> dimIDSource = world.dimension();
		PocketChunkInfo chunk = getChunkInfoForPocket(dimIDSource, coordSetSource);
		//Pocket pocket = new Pocket(null);
		
		if (chunk != null && getPocketFromChunkInfo(chunk) != null) {
			return getPocketFromChunkInfo(chunk);
		} else {
			if (!pocketMap.isEmpty()) {
				pocketGenParameters.currentChunk = getNextPocketChunkPosition(pocketGenParameters.currentChunk);
				
				Pocket pocket = new Pocket(pocketGenParameters.currentChunk, isSingleChunk, dimIDSource, coordSetSource);
				pocketMap.put(new PocketChunkInfo(pocket.getDominantChunkPos(), isSingleChunk), pocket);
				saveData();
				return pocket;
			} else {
				Pocket pocket = new Pocket(pocketGenParameters.currentChunk, isSingleChunk, dimIDSource, coordSetSource);
				pocketMap.put(new PocketChunkInfo(pocket.getDominantChunkPos(), isSingleChunk), pocket);
				saveData();
				return pocket;
			}
		}
	}

	public static void updatePocket(PocketChunkInfo chunkPos, ResourceKey<Level> dimension, BlockPos pos) {
		Pocket link = pocketMap.get(chunkPos);
		
		if (link == null) {
			DimensionalPockets.CONSOLE.debugWarn("[Pocket Registry Error] <updatepocket> Unable to update Pocket: { " + chunkPos + " }. No Pocket exists with this Chunk Posision.");
			return;
		}

		link.setSourceBlockDimension(dimension);
		link.addPosToBlockArray(pos, dimension.location());

		saveData();
	}

	public static void updatePocket(CosmosChunkPos chunkPos, ResourceKey<Level> dimension, BlockPos pos) {
		Pocket[] link = new Pocket[] { new Pocket(null) };
		
		pocketMap.forEach((info, pock) -> {
			if (info.isChunkContained(chunkPos)) {
				link[0] = pock;
			}
		});
		
		if (link[0] == null) {
			DimensionalPockets.CONSOLE.debugWarn("[Pocket Registry Error] <updatepocket> Unable to update Pocket: { " + chunkPos + " }. No Pocket exists with this Chunk Posision.");
			return;
		}

		link[0].setSourceBlockDimension(dimension);
		link[0].addPosToBlockArray(pos, dimension.location());

		saveData();
	}
	
	public static void saveData() {
		PocketFileSystemManager.saveBackLinkMap(pocketMap);
		PocketFileSystemManager.savePocketGenParams(pocketGenParameters);

		DimensionalPockets.CONSOLE.debug("[Pocket Registry] {server} <save> Pocket data saved to File System.");
	}

	public static void loadData() {
		pocketMap = PocketFileSystemManager.loadBackLinkMap();
		pocketGenParameters = PocketFileSystemManager.loadPocketGenParams();
		
		if (!pocketMap.isEmpty()) {
			DimensionalPockets.CONSOLE.debug("[Pocket Registry] {server} <load> Pocket data loaded from File System.");
		} else {
			DimensionalPockets.CONSOLE.debugWarn("[Pocket Registry] {server} <load> Pocket File System returned an empty Map. This must be a new world. Creating a fresh Map.");
		}
	}
	
}