package com.tcn.dimensionalpocketsii.client.colour;

import com.tcn.cosmoslibrary.common.comp.CosmosColour;
import com.tcn.cosmoslibrary.common.math.ChunkPos;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.tileentity.TileEntityConnector;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

public class ColourBlockWall implements IBlockColor {

	@Override
	public int getColor(BlockState stateIn, IBlockDisplayReader displayReaderIn, BlockPos posIn, int tintIndexIn) {
		ChunkPos chunkPos = ChunkPos.scaleToChunkPos(posIn);
		
		if (chunkPos != null) {
			BlockPos scaled = ChunkPos.scaleFromChunkPos(chunkPos);
			BlockPos scaled_y = scaled.offset(Direction.UP.getNormal());
			
			TileEntity entity = displayReaderIn.getBlockEntity(scaled_y);
			
			if (entity != null) {
				if (entity instanceof TileEntityConnector) {
					TileEntityConnector connector = (TileEntityConnector) entity;
					
					if (connector.getPocket() != null) {
						Pocket pocket = connector.getPocket();
						int colour = pocket.getDisplayColour();
						
						return colour;
					}
				}
			}
		}
		
		return CosmosColour.POCKET_PURPLE.dec();
	}
}