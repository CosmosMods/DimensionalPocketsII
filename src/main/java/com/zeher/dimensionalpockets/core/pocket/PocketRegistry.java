package com.zeher.dimensionalpockets.core.pocket;

import java.util.HashMap;
import java.util.Map;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.core.block.BlockDimensionalPocketWall;
import com.zeher.dimensionalpockets.core.handlers.BlockHandler;
import com.zeher.dimensionalpockets.core.pocket.handlers.PocketChunkLoaderHandler;
import com.zeher.dimensionalpockets.core.util.DimLogger;
import com.zeher.dimensionalpockets.core.util.DimUtils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;


public class PocketRegistry {
	private static Map<BlockPos, Pocket> backLinkMap = new HashMap<>();

	private static final int pocketChunkSpacing = 20;
	private static PocketGenParameters pocketGenParameters = new PocketGenParameters();

	static class PocketGenParameters {
		private BlockPos currentChunk = new BlockPos(0, 0, 0);
		private EnumFacing nextPocketCoordsDirection = EnumFacing.NORTH;
	}

	public static WorldServer getWorldForPockets() {
		return FMLCommonHandler.instance().getMinecraftServerInstance().getServer().worldServerForDimension(DimensionalPockets.dimension_id);
	}

	public static Pocket getPocket(BlockPos chunkPos) {
		System.out.println(backLinkMap + " -- " +  chunkPos);
		
		DimUtils.enforceServer();
		if (backLinkMap.containsKey(chunkPos))
			return backLinkMap.get(chunkPos);
		return null;
	}

	private static BlockPos getNextPocketCoords(BlockPos currentCoords) {
		BlockPos result = currentCoords;
		BlockPos result2 = new BlockPos(0,0,0);
		
		while (backLinkMap.containsKey(result)) {
			BlockPos offset = new BlockPos(0,0,0);
			offset.offset(pocketGenParameters.nextPocketCoordsDirection);
			BlockPos offset2 = new BlockPos(offset.getX() * pocketChunkSpacing, offset.getY(), offset.getZ() * pocketChunkSpacing);
			
			result.add(offset2);
			result2 = new BlockPos(result.getX(), 4, result.getZ());
		}
		
		EnumFacing clockwiseTurn = pocketGenParameters.nextPocketCoordsDirection.rotateY();
		BlockPos probeOffset = new BlockPos(0,0,0);
		probeOffset.offset(clockwiseTurn);
		BlockPos probeOffset2 = new BlockPos(probeOffset.getX() * pocketChunkSpacing, probeOffset.getY(), probeOffset.getZ() * pocketChunkSpacing);
		
		BlockPos probeCoords = result2;
		probeCoords.add(result2);
		
		if (!backLinkMap.containsKey(probeCoords)) {
			pocketGenParameters.nextPocketCoordsDirection = clockwiseTurn;
		}
		
		return result2;
	}

	public static Pocket getOrCreatePocket(World world, BlockPos coordSetSource) {
		DimUtils.enforceServer();
		
		int dimIDSource = world.provider.getDimension();
		for (Pocket pocket : backLinkMap.values()) {
			if (pocket.getBlockDim() == dimIDSource && pocket.getBlockPos().equals(coordSetSource))
				return pocket;
		}
		
		if (!backLinkMap.isEmpty()) {
			pocketGenParameters.currentChunk = getNextPocketCoords(pocketGenParameters.currentChunk);
		}

		Pocket pocket = new Pocket(pocketGenParameters.currentChunk, dimIDSource, coordSetSource);
		backLinkMap.put(pocket.getChunkPos(), pocket);
		
		saveData();
		
		System.out.println(backLinkMap);

		return pocket;
	}

	public static void updatePocket(BlockPos chunkPos, int newBlockDimID, BlockPos newBlockCoords) {
		DimUtils.enforceServer();
		Pocket link = backLinkMap.get(chunkPos);
		if (link == null) {
			DimLogger.severe("No Pocket for chunkPos: " + chunkPos);
			return;
		}

		link.setBlockDim(newBlockDimID);
		link.setBlockPos(newBlockCoords);

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
