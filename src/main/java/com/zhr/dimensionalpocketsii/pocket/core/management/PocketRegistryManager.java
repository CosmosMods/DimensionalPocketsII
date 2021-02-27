package com.zhr.dimensionalpocketsii.pocket.core.management;

import java.util.LinkedHashMap;
import java.util.Map;

import com.zhr.dimensionalpocketsii.DimensionalPockets;
import com.zhr.dimensionalpocketsii.pocket.core.Pocket;

import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class PocketRegistryManager {
	
	private static Map<BlockPos, Pocket> backLinkMap = new LinkedHashMap<>();

	//How far the pockets are apart (This value - 1 is how many chunks between each pocket in the X or Z axis. [NOT DYNAMIC]
	private static final int pocketChunkSpacing = 2;
	
	//The offset in the Y direction of how far each pocket is above the bedrock layer in the Pocket Dimension. (0 is the bedrock layer) [NOT DYNAMIC]
	public static final int pocket_y_offset = 1;
	
	//The interior size of the structure + 1. [Not a dynamic value. If different size pockets are required, extra dimensions will have to be added] [NOT DYNAMIC]
	public static final int pocket_size = 15;
	
	private static PocketGenParameters pocketGenParameters = new PocketGenParameters();

	static class PocketGenParameters {
		private BlockPos currentChunk = new BlockPos(0, 0, 0);
		private Direction nextPocketCoordsDirection = Direction.NORTH;
	}

	public static World getWorldForPockets() {
		return ServerLifecycleHooks.getCurrentServer().getWorld(PocketDimensionManager.POCKET_WORLD);
	}
	
	public static Map<BlockPos, Pocket> getMap() {
		return backLinkMap;
	}
	
	public static void clearMap() {
		backLinkMap.clear();
		
		System.out.println("Map Cleared");
	}

	public static Pocket getPocket(BlockPos chunkPos) {
		if (chunkPos != null){
			if (backLinkMap.containsKey(chunkPos)) {
				return backLinkMap.get(chunkPos);
			} else {
				System.out.println("Attempt to get pocket failed: chunkPos not present in backLinkMap: " + chunkPos);
				return null;
			}
		} else {
			System.out.println("Attempt to get Pocket failed: chunkPos NULL.");
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
	
	public static BlockPos getChunkPosForPocket(RegistryKey<World> dimension, BlockPos pos) {
		for(Pocket pocket : backLinkMap.values()) {
			RegistryKey<World> pocket_dimension = pocket.getSourceBlockDimension();
			
			if (pocket_dimension.equals(dimension) && pocket.doesBlockArrayContain(pos)) {
				return pocket.getChunkPos();
			}
		}
		
		return new BlockPos(0, -1, 0);
	}
	
	public static Pocket getOrCreatePocket(World world, BlockPos coordSetSource) {
		RegistryKey<World> dimIDSource = world.getDimensionKey();
		BlockPos chunk = getChunkPosForPocket(dimIDSource, coordSetSource);
		Pocket pocket = new Pocket();
		
		if (chunk.getY() >= 0 && getPocket(chunk) != null) {
			pocket = getPocket(chunk);
		} else {
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

	public static void updatePocket(BlockPos chunkPos, RegistryKey<World> dimension, BlockPos pos) {
		Pocket link = backLinkMap.get(chunkPos);
		if (link == null) {
			System.out.println("No Pocket for chunkPos: " + chunkPos);
			return;
		}

		link.setSourceBlockDimension(dimension);
		link.addPosToBlockArray(pos);

		saveData();
	}

	public static void saveData() {
		PocketConfigManager.saveBackLinkMap(backLinkMap);
		PocketConfigManager.savePocketGenParams(pocketGenParameters);
		
		DimensionalPockets.LOGGER.info("Pocket data saved to JSON", PocketRegistryManager.class);
		System.out.println("Pocket data saved to JSON");
	}

	public static void loadData() {
		backLinkMap = PocketConfigManager.loadBackLinkMap();
		pocketGenParameters = PocketConfigManager.loadPocketGenParams();
		
		DimensionalPockets.LOGGER.info("Pocket data loaded from JSON", PocketRegistryManager.class);
		System.out.println("Pocket data loaded from JSON [" + backLinkMap + "]");
	}

	@SuppressWarnings("unused")
	public static void initChunkLoading() {
		for (Pocket pocket : backLinkMap.values()) {
			//ChunkLoaderManagerRoom.addPocketToChunkLoader(pocket);
		}
	}
}