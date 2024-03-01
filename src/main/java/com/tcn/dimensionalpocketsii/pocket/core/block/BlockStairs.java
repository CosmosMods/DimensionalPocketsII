package com.tcn.dimensionalpocketsii.pocket.core.block;

import com.google.common.base.Supplier;

import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BlockStairs extends StairBlock {

	public BlockStairs(Supplier<BlockState> blockState, Properties properties) {
		super(blockState, properties);
	}

}