package com.zeher.zeherlib.api.core.interfaces.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * An interface to hand-over functions from a {@link Block} to a {@link TileEntity}.
 */
public interface IBlockNotifier {
	
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state);

	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack);
    
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor);

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state);
}