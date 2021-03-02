package com.tcn.cosmoslibrary.impl.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class CosmosItemBypass extends CosmosItem {
	
	public CosmosItemBypass(Item.Properties properties) {
		super(properties);
	}
	
	public CosmosItemBypass(Item.Properties properties, int max_stack) {
		super(properties.maxStackSize(max_stack));
	}
	
	@Override
	public boolean canPlayerBreakBlockWhileHolding(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
		return false;
	}
	
	@Override
	public boolean doesSneakBypassUse(ItemStack stack, IWorldReader world, BlockPos pos, PlayerEntity player) {
		return true;
	}
	
	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
		return ActionResultType.PASS;
	}
}