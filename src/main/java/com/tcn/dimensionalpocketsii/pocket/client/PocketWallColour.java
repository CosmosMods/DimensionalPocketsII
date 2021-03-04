package com.tcn.dimensionalpocketsii.pocket.client;

import com.tcn.cosmoslibrary.impl.colour.EnumMinecraftColour;
import com.tcn.cosmoslibrary.math.ChunkPos;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.tcn.dimensionalpocketsii.pocket.core.util.PocketUtil;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

public class PocketWallColour implements IBlockColor {

	@Override
	public int getColor(BlockState stateIn, IBlockDisplayReader displayReaderIn, BlockPos posIn, int colourIn) {
		ChunkPos chunkPos = PocketUtil.scaleToChunkPos(posIn);
		
		if (chunkPos != null) {
			Pocket pocket = PocketRegistryManager.getPocketFromChunk(chunkPos);
			
			if (pocket != null) {
				return pocket.getDisplayColour();
			}
		}
		
		return EnumMinecraftColour.POCKET_PURPLE.getDecimal();
	}
}