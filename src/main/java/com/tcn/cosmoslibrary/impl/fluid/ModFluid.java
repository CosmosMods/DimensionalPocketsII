package com.tcn.cosmoslibrary.impl.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

@Deprecated
public class ModFluid extends Fluid {

	/**
	public ModFluid(String fluidName, ResourceLocation still, ResourceLocation flowing) {
		super(fluidName, still, flowing);
	}
	*/

	@Override
	protected boolean canDisplace(FluidState fluidState, IBlockReader blockReader, BlockPos pos, Fluid fluid,
			Direction direction) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Vector3d getFlow(IBlockReader blockReader, BlockPos pos, FluidState fluidState) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getActualHeight(FluidState p_215662_1_, IBlockReader p_215662_2_, BlockPos p_215662_3_) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getHeight(FluidState p_223407_1_) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected BlockState getBlockState(FluidState state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSource(FluidState state) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getLevel(FluidState state) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public VoxelShape func_215664_b(FluidState p_215664_1_, IBlockReader p_215664_2_, BlockPos p_215664_3_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Item getFilledBucket() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTickRate(IWorldReader p_205569_1_) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected float getExplosionResistance() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}