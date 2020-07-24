package com.zeher.dimpockets.pocket.core.manager;

import java.util.HashMap;
import java.util.Map;

import com.zeher.dimpockets.DimReference;
import com.zeher.dimpockets.core.log.ModLogger;
import com.zeher.dimpockets.pocket.core.Pocket;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class PocketRegistryManager {
	
	private static Map<BlockPos, Pocket> backLinkMap = new HashMap<>();

	private static final int pocketChunkSpacing = 2;
	private static PocketGenParameters pocketGenParameters = new PocketGenParameters();

	static class PocketGenParameters {
		private BlockPos currentChunk = new BlockPos(0, 0, 0);
		private EnumFacing nextPocketCoordsDirection = EnumFacing.NORTH;
	}

	public static WorldServer getWorldForPockets() {
		return FMLCommonHandler.instance().getMinecraftServerInstance().getServer().getWorld(DimReference.CONSTANT.POCKET_DIMENSION_ID);
	}
	
	public static Map<BlockPos, Pocket> getMap() {
		return backLinkMap;
	}

	public static Pocket getPocket(BlockPos chunkPos) {
		if (backLinkMap.containsKey(chunkPos)) { 
			return backLinkMap.get(chunkPos);
		} else {
			return null;
		}
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
	
	public static BlockPos getChunkPosForPocket(World world, BlockPos pos) {
		int dim = world.provider.getDimension();
		
		for(Pocket pocket : backLinkMap.values()) {
			if (pocket.getSourceBlockDim() == dim && pocket.doesBlockMapContain(pos)) {
				return pocket.getChunkPos();
			}
		}
		
		return new BlockPos(0, -1, 0);
	}
	
	public static Pocket getOrCreatePocket(World world, BlockPos coordSetSource) {
		Integer dimIDSource = world.provider.getDimension();
		BlockPos chunk = getChunkPosForPocket(world, coordSetSource);
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

	public static void updatePocket(BlockPos chunkPos, int newBlockDimID, BlockPos newBlockCoords) {
		Pocket link = backLinkMap.get(chunkPos);
		if (link == null) {
			ModLogger.severe("No Pocket for chunkPos: " + chunkPos);
			return;
		}

		link.setSourceBlockDim(newBlockDimID);
		link.addPosToBlockMap(newBlockCoords);

		saveData();
	}

	public static void saveData() {
		PocketConfigManager.saveBackLinkMap(backLinkMap);
		PocketConfigManager.savePocketGenParams(pocketGenParameters);
		
		ModLogger.info("Pocket data saved to JSON", PocketRegistryManager.class);
	}

	public static void loadData() {
		backLinkMap = PocketConfigManager.loadBackLinkMap();
		pocketGenParameters = PocketConfigManager.loadPocketGenParams();
		
		ModLogger.info("Pocket data loaded from JSON", PocketRegistryManager.class);
	}

	public static void initChunkLoading() {
		for (Pocket pocket : backLinkMap.values()) {
			ChunkLoaderManagerRoom.addPocketToChunkLoader(pocket);
		}
	}
}