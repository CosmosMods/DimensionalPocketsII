package com.zeher.zeherlib.api.core.interfaces.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

/**
 * An interface to hand-over functions from a {@link Block} to a
 * {@link TileEntity}.
 */
@SuppressWarnings("deprecation")
public interface IBlockTileProvider extends ITileEntityProvider {

	@Nullable
	TileEntity createNewTileEntity(IBlockReader worldIn);

}