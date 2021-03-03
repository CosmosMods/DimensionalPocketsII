package com.tcn.cosmoslibrary.impl.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItemUseContext;

public class CosmosBlockModelUnplaceable extends CosmosBlock {

	public CosmosBlockModelUnplaceable(Block.Properties properties) {
		super(properties);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return Blocks.AIR.getDefaultState();
	}
}