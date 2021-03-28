package com.tcn.dimensionalpocketsii.pocket.core.management;

import java.util.LinkedHashMap;
import java.util.Map;

import com.tcn.cosmoslibrary.common.math.ChunkPos;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.core.management.CoreDimensionManager;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;

import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class PocketRegistryManager {
	
	private static Map<ChunkPos, Pocket> pocket_map = new LinkedHashMap<>();

	//How far the pockets are apart (This value - 1 is how many chunks between each pocket in the X or Z axis. [NOT DYNAMIC]
	private static final int pocketChunkSpacing = 2;
	
	//The offset in the Y direction of how far each pocket is above the bedrock layer in the Pocket Dimension. (0 is the bedrock layer) [NOT DYNAMIC]
	public static final int pocket_y_offset = 1;
	
	//The interior size of the structure + 1. [Not a dynamic value. If different size pockets are required, extra dimensions will have to be added] [NOT DYNAMIC]
	public static final int pocket_size = 15;
	
	private static PocketGenParameters pocketGenParameters = new PocketGenParameters();

	static class PocketGenParameters {
		private ChunkPos currentChunk = new ChunkPos(0, 0);
		private Direction nextPocketCoordsDirection = Direction.NORTH;
	}

	public static World getWorldForPockets() {
		return ServerLifecycleHooks.getCurrentServer().getLevel(CoreDimensionManager.POCKET_WORLD);
	}
	
	public static Map<ChunkPos, Pocket> getPocketMap() {
		return pocket_map;
	}
	
	public static void clearPocketMap() {
		pocket_map.clear();
		
		DimensionalPockets.LOGGER.info("Pocket Map Cleared");
	}

	public static Pocket getPocketFromChunkPosition(ChunkPos chunkPos) {
		Pocket pocket = new Pocket(null);
		
		if (chunkPos != null){
			if (pocket_map.containsKey(chunkPos)) {
				return pocket_map.get(chunkPos);
			} else {
				DimensionalPockets.LOGGER.warn("Pocket for Chunk Position: [ " + chunkPos + "] is not present in Map.");
				return pocket;
			}
		} else {
			DimensionalPockets.LOGGER.warn("Chunk Position [ NULL ].");
		}
		return pocket;
	}

	public static ChunkPos getNextPocketChunkPosition(ChunkPos currentCoords) {
		ChunkPos result = currentCoords;
		
		while (pocket_map.containsKey(result)) {
			ChunkPos offset = new ChunkPos(0, 0).offset(pocketGenParameters.nextPocketCoordsDirection);
			
			result = new ChunkPos(result.getX(), result.getZ()).add(offset.getX() * pocketChunkSpacing, offset.getZ() * pocketChunkSpacing);
		}
		
		Direction clockwise = pocketGenParameters.nextPocketCoordsDirection.getClockWise();
		ChunkPos probe = new ChunkPos(0, 0).offset(clockwise);
		ChunkPos probe2 = new ChunkPos(probe.getX() * pocketChunkSpacing, probe.getZ() * pocketChunkSpacing);
		
		ChunkPos probed = new ChunkPos(result).add(probe2);
		
		if (!pocket_map.containsKey(probed)) {
			pocketGenParameters.nextPocketCoordsDirection = clockwise;
		}
		
		return result;
	}
	
	public static ChunkPos getChunkPositionForPocket(RegistryKey<World> dimension, BlockPos pos) {
		for(Pocket pocket : pocket_map.values()) {
			RegistryKey<World> pocket_dimension = pocket.getSourceBlockDimension();
			
			if (pocket_dimension.equals(dimension) && pocket.doesBlockArrayContain(pos)) {
				return pocket.getChunkPos();
			}
		}
		
		return null;
	}
	
	public static Pocket getOrCreatePocket(World world, BlockPos coordSetSource) {
		RegistryKey<World> dimIDSource = world.dimension();
		ChunkPos chunk = getChunkPositionForPocket(dimIDSource, coordSetSource);
		Pocket pocket = new Pocket(null);
		
		if (chunk != null && getPocketFromChunkPosition(chunk) != null) {
			pocket = getPocketFromChunkPosition(chunk);
		} else {
			if (!pocket_map.isEmpty()) {
				pocketGenParameters.currentChunk = getNextPocketChunkPosition(pocketGenParameters.currentChunk);
				
				pocket = new Pocket(pocketGenParameters.currentChunk, dimIDSource, coordSetSource);
				pocket_map.put(pocket.getChunkPos(), pocket);
				saveData();
			} else {
				pocket = new Pocket(pocketGenParameters.currentChunk, dimIDSource, coordSetSource);
				pocket_map.put(pocket.getChunkPos(), pocket);
				saveData();
			}
		}
		
		return pocket;
	}

	public static void updatePocket(ChunkPos chunkPos, RegistryKey<World> dimension, BlockPos pos) {
		Pocket link = pocket_map.get(chunkPos);
		
		if (link == null) {
			DimensionalPockets.LOGGER.info("Pocket for Chunk Position: [ " + chunkPos + "] does not exist.");
			return;
		}

		link.setSourceBlockDimension(dimension);
		link.addPosToBlockArray(pos);

		saveData();
	}
	
	public static void saveData() {
		PocketFileSystemManager.saveBackLinkMap(pocket_map);
		PocketFileSystemManager.savePocketGenParams(pocketGenParameters);
		
		DimensionalPockets.LOGGER.info("<Server> Pocket data saved to JSON.");
	}

	public static void loadData() {
		pocket_map = PocketFileSystemManager.loadBackLinkMap();
		pocketGenParameters = PocketFileSystemManager.loadPocketGenParams();
		
		if (!pocket_map.isEmpty()) {
			DimensionalPockets.LOGGER.info("<Server> Pocket data loaded from JSON.");
		} else {
			DimensionalPockets.LOGGER.info("<Server> Pocket Registry empty. Creating new Pocket Registry.");
		}
	}

	@SuppressWarnings("unused")
	public static void initChunkLoading() {
		for (Pocket pocket : pocket_map.values()) {
			//ChunkLoaderManagerRoom.addPocketToChunkLoader(pocket);
		}
	}
}