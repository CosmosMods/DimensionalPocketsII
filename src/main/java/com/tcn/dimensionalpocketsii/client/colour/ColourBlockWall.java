package com.tcn.dimensionalpocketsii.client.colour;

import com.tcn.cosmoslibrary.common.lib.ComponentColour;
import com.tcn.cosmoslibrary.common.lib.CosmosChunkPos;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleConnector;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ColourBlockWall implements BlockColor {

	@Override
	public int getColor(BlockState stateIn, BlockAndTintGetter displayReaderIn, BlockPos posIn, int tintIndexIn) {
		CosmosChunkPos chunkPos = CosmosChunkPos.scaleToChunkPos(posIn);
		
		if (chunkPos != null) {
			BlockPos scaled = CosmosChunkPos.scaleFromChunkPos(chunkPos);
			BlockPos scaled_y = scaled.offset(Direction.UP.getNormal());
			
			BlockEntity entity = displayReaderIn.getBlockEntity(scaled_y);
			
			if (entity != null) {
				if (entity instanceof BlockEntityModuleConnector) {
					BlockEntityModuleConnector connector = (BlockEntityModuleConnector) entity;
					
					if (connector.getPocket() != null) {
						Pocket pocket = connector.getPocket();
						int colour = pocket.getDisplayColour();
						
						return colour;
					}
				}
			}
		}
		
		return ComponentColour.POCKET_PURPLE.dec();
	}
}