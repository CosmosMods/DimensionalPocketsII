package com.zeher.zeherlib.mod.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;

public class ModBlockFluid extends FlowingFluidBlock {

	@SuppressWarnings("deprecation")
	public ModBlockFluid(FlowingFluid fluid, Block.Properties properties) {
		super(fluid, properties);
	}
}