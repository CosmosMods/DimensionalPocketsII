package com.zeher.zeherlib.api.compat.core.impl;

import com.zeher.zeherlib.api.core.interfaces.IWrenchAdvanced;
import com.zeher.zeherlib.mod.item.ModItem;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ZLWrench extends ModItem implements IWrenchAdvanced {
	
	public ZLWrench(Item.Properties properties) {
		super(properties.maxStackSize(1));
	}

	public ActionResultType onItemUse(ItemUseContext context) {
		return ActionResultType.PASS;
	}

	@Override
	public boolean isActive(ItemStack paramstack) {
		return true;
	}

	@Override
	public boolean canPlayerBreakBlockWhileHolding(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
		return false;
	}
}