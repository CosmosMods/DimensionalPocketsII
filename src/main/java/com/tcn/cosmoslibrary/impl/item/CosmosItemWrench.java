package com.tcn.cosmoslibrary.impl.item;

import com.tcn.cosmoslibrary.impl.interfaces.item.IWrenchAdvanced;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class CosmosItemWrench extends CosmosItem implements IWrenchAdvanced {
	
	public CosmosItemWrench(Item.Properties properties) {
		super(properties.maxStackSize(1));
	}

	@Override
	public boolean isActive(ItemStack paramstack) {
		return true;
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