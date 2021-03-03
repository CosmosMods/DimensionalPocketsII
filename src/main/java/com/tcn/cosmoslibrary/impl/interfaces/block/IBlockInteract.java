package com.tcn.cosmoslibrary.impl.interfaces.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

/**
 * An interface to hand-over functions from a {@link Block} to a
 * {@link TileEntity}.
 */
public interface IBlockInteract {

	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit);

	public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player);
	
}