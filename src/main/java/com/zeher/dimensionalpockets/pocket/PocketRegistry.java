package com.zeher.dimensionalpockets.pocket;

import java.util.HashMap;
import java.util.Map;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.core.util.DimLogger;
import com.zeher.dimensionalpockets.pocket.handler.PocketChunkLoaderHandler;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class PocketRegistry {
	
	private static Map<BlockPos, Pocket> backLinkMap = new HashMap<>();

	private static final int pocketChunkSpacing = 2;
	private static PocketGenParameters pocketGenParameters = new PocketGenParameters();

	static class PocketGenParameters {
		private BlockPos currentChunk = new BlockPos(0, 0, 0);
		private EnumFacing nextPocketCoordsDirection = EnumFacing.NORTH;
	}

	public static WorldServer getWorldForPockets() {
		return FMLCommonHandler.instance().getMinecraftServerInstance().getServer().getWorld(DimensionalPockets.DIMENSION_ID);
	}
	
	public static Map<BlockPos, Pocket> getMap() {
		return backLinkMap;
	}

	public static Pocket getPocket(BlockPos chunkPos) {
		//System.out.println(backLinkMap + " -- " +  chunkPos);
		
		//DimUtils.enforceServer();
		if (backLinkMap.containsKey(chunkPos))
			return backLinkMap.get(chunkPos);
		return null;
	}

	public static BlockPos getNextPocketCoords(BlockPos currentCoords) {
		BlockPos result = currentCoords;
		
		while (backLinkMap.containsKey(result)) {
			BlockPos offset = new BlockPos(0, 0, 0).offset(pocketGenParameters.nextPocketCoordsDirection);
			
			result = new BlockPos(result.getX(), 0, result.getZ()).add(offset.getX() * pocketChunkSpacing, offset.getY(), offset.getZ() * pocketChunkSpacing);
		}
		
		EnumFacing clockwise = pocketGenParameters.nextPocketCoordsDirection.rotateY();
		BlockPos probe = new BlockPos(0, 0, 0).offset(clockwise);
		BlockPos probe2 = new BlockPos(probe.getX() * pocketChunkSpacing, probe.getY(), probe.getZ() * pocketChunkSpacing);
		
		BlockPos probed = new BlockPos(result).add(probe2);
		
		if (!backLinkMap.containsKey(probed)) {
			pocketGenParameters.nextPocketCoordsDirection = clockwise;
		}
		
		return result;
	}

	public static Pocket getOrCreatePocket(World world, BlockPos coordSetSource) {
		//DimUtils.enforceServer();
		
		int dimIDSource = world.provider.getDimension();
		for (Pocket pocket : backLinkMap.values()) {
			if (pocket.getBlockDim() == dimIDSource && pocket.doesArrayContain(coordSetSource))
				return pocket;
		}
		
		if (!backLinkMap.isEmpty()) {
			pocketGenParameters.currentChunk = getNextPocketCoords(pocketGenParameters.currentChunk);
		}

		Pocket pocket_ = new Pocket(pocketGenParameters.currentChunk, dimIDSource, coordSetSource);
		backLinkMap.put(pocket_.getChunkPos(), pocket_);
		
		saveData();
		
		return pocket_;
	}

	public static void updatePocket(BlockPos chunkPos, int newBlockDimID, BlockPos newBlockCoords) {
		//DimUtils.enforceServer();
		Pocket link = backLinkMap.get(chunkPos);
		if (link == null) {
			DimLogger.severe("No Pocket for chunkPos: " + chunkPos);
			return;
		}

		link.setBlockDim(newBlockDimID);
		link.addBlockPosToArray(newBlockCoords);

		saveData();
	}

	public static void saveData() {
		PocketConfig.saveBackLinkMap(backLinkMap);
		PocketConfig.savePocketGenParams(pocketGenParameters);
	}

	public static void loadData() {
		backLinkMap = PocketConfig.loadBackLinkMap();
		pocketGenParameters = PocketConfig.loadPocketGenParams();
	}

	public static void initChunkLoading() {
		for (Pocket pocket : backLinkMap.values()) {
			PocketChunkLoaderHandler.addPocketToChunkLoader(pocket);
		}
	}
}