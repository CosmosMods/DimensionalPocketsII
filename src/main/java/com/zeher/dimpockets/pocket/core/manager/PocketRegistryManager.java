package com.zeher.dimpockets.pocket.core.manager;

import java.util.HashMap;
import java.util.Map;

import com.zeher.dimpockets.DimensionalPockets;
import com.zeher.dimpockets.core.manager.ModDimensionManager;
import com.zeher.dimpockets.pocket.core.Pocket;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class PocketRegistryManager {
	
	private static Map<BlockPos, Pocket> backLinkMap = new HashMap<>();

	private static final int pocketChunkSpacing = 2;
	private static PocketGenParameters pocketGenParameters = new PocketGenParameters();

	static class PocketGenParameters {
		private BlockPos currentChunk = new BlockPos(0, 0, 0);
		private Direction nextPocketCoordsDirection = Direction.NORTH;
	}

	public static World getWorldForPockets() {
		return ServerLifecycleHooks.getCurrentServer().getWorld(ModDimensionManager.POCKET_DIMENSION.getDimensionType());
	}
	
	public static Map<BlockPos, Pocket> getMap() {
		return backLinkMap;
	}
	
	public static void clearMap() {
		backLinkMap.clear();
		
		DimensionalPockets.LOGGER.info("Map Cleared");
	}

	public static Pocket getPocket(BlockPos chunkPos) {
		if (backLinkMap.containsKey(chunkPos)) {
			return backLinkMap.get(chunkPos);
		}
		return null;
	}

	public static BlockPos getNextPocketCoords(BlockPos currentCoords) {
		BlockPos result = currentCoords;
		
		while (backLinkMap.containsKey(result)) {
			BlockPos offset = new BlockPos(0, 0, 0).offset(pocketGenParameters.nextPocketCoordsDirection);
			
			result = new BlockPos(result.getX(), 0, result.getZ()).add(offset.getX() * pocketChunkSpacing, offset.getY(), offset.getZ() * pocketChunkSpacing);
		}
		
		Direction clockwise = pocketGenParameters.nextPocketCoordsDirection.rotateY();
		BlockPos probe = new BlockPos(0, 0, 0).offset(clockwise);
		BlockPos probe2 = new BlockPos(probe.getX() * pocketChunkSpacing, probe.getY(), probe.getZ() * pocketChunkSpacing);
		
		BlockPos probed = new BlockPos(result).add(probe2);
		
		if (!backLinkMap.containsKey(probed)) {
			pocketGenParameters.nextPocketCoordsDirection = clockwise;
		}
		
		return result;
	}
	
	public static BlockPos getChunkPosForPocket(World world, BlockPos pos) {
		DimensionType dimension_type = world.getDimension().getType();
		
		for(Pocket pocket : backLinkMap.values()) {
			if (pocket.getSourceBlockDimensionType() == dimension_type && pocket.doesBlockMapContain(pos)) {
				return pocket.getChunkPos();
			}
		}
		
		return new BlockPos(0, -1, 0);
	}
	
	public static Pocket getOrCreatePocket(World world, BlockPos coordSetSource) {
		DimensionType dimIDSource = world.getDimension().getType();
		BlockPos chunk = getChunkPosForPocket(world, coordSetSource);
		Pocket pocket = new Pocket();
		
		if (chunk.getY() >= 0 && getPocket(chunk) != null) {
			pocket = getPocket(chunk);
		} else if (coordSetSource.getY() > 0) {
			if (!backLinkMap.isEmpty()) {
				pocketGenParameters.currentChunk = getNextPocketCoords(pocketGenParameters.currentChunk);
				
				pocket = new Pocket(pocketGenParameters.currentChunk, dimIDSource, coordSetSource);
				backLinkMap.put(pocket.getChunkPos(), pocket);
				saveData();
			} else {
				pocket = new Pocket(pocketGenParameters.currentChunk, dimIDSource, coordSetSource);
				backLinkMap.put(pocket.getChunkPos(), pocket);
				saveData();
			}
		}
		return pocket;
	}

	public static void updatePocket(BlockPos chunkPos, DimensionType dimension_type, BlockPos pos) {
		Pocket link = backLinkMap.get(chunkPos);
		if (link == null) {
			DimensionalPockets.LOGGER.warn("No Pocket for chunkPos: " + chunkPos);
			return;
		}

		link.setSourceBlockDimensionType(dimension_type);
		link.addPosToBlockMap(pos);

		saveData();
	}

	public static void saveData() {
		PocketConfigManager.saveBackLinkMap(backLinkMap);
		PocketConfigManager.savePocketGenParams(pocketGenParameters);
		
		DimensionalPockets.LOGGER.info("Pocket data saved to JSON", PocketRegistryManager.class);
	}

	public static void loadData() {
		backLinkMap = PocketConfigManager.loadBackLinkMap();
		pocketGenParameters = PocketConfigManager.loadPocketGenParams();
		
		DimensionalPockets.LOGGER.info("Pocket data loaded from JSON", PocketRegistryManager.class);
	}

	@SuppressWarnings("unused")
	public static void initChunkLoading() {
		for (Pocket pocket : backLinkMap.values()) {
			//ChunkLoaderManagerRoom.addPocketToChunkLoader(pocket);
		}
	}
}