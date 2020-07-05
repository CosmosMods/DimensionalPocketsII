package com.zeher.zeherlib.api.core.interfaces.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * An interface to hand-over functions from a {@link Block} to a {@link TileEntity}.
 */
public interface IBlockInteract {
	
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ);

	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn);
	
}