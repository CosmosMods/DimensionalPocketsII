package com.zeher.zeherlib.api.core.interfaces.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * An interface to hand-over functions from a {@link Block} to a {@link TileEntity}.
 */
public interface IBlockTileProvider extends ITileEntityProvider {
	
	public TileEntity createNewTileEntity(World worldIn, int meta);
    
}