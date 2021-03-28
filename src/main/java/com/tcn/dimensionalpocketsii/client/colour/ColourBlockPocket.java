package com.tcn.dimensionalpocketsii.client.colour;

import com.tcn.cosmoslibrary.common.comp.CosmosColour;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.tileentity.TileEntityPocket;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

public class ColourBlockPocket implements IBlockColor {

	@Override
	public int getColor(BlockState stateIn, IBlockDisplayReader displayReaderIn, BlockPos posIn, int colourIn) {
		Block block = stateIn.getBlock();
		TileEntity tile = displayReaderIn.getBlockEntity(posIn);
		
		if (block != null) {
			if (tile != null) {
				if (tile instanceof TileEntityPocket) {
					TileEntityPocket pocket_tile = (TileEntityPocket) tile;
					
					Pocket pocket = pocket_tile.getPocket();
					
					if (pocket != null) {
						return pocket.getDisplayColour();
					}
				}
			}
		}
		return CosmosColour.POCKET_PURPLE.dec();
	}

}
