package com.tcn.cosmoslibrary.impl.util;

import net.minecraft.block.Block;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class CosmosFluidUtil {
	
	public static ItemStack useItemSafely(ItemStack stack) {
		if (stack.getCount() == 1) {
			if (stack.getItem().hasContainerItem(stack))
				return stack.getItem().getContainerItem(stack);
			else
				return null;
		} else {
			stack.split(1);
			return stack;
		}
	}

	public static void dropStackInWorld(World world, BlockPos pos, ItemStack stack) {
		if (!world.isRemote && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
			float f = 0.7F;
			double d0 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
			double d1 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
			double d2 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
			ItemEntity entityitem = new ItemEntity(world, (double) pos.getX() + d0, (double) pos.getY() + d1, (double) pos.getZ() + d2, stack);
			entityitem.setPickupDelay(10); 
			world.addEntity(entityitem);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack getTankStackFromData(Block block) {
		ItemStack stack = new ItemStack(Item.getItemFromBlock(block));
		CompoundNBT tag = new CompoundNBT();
		stack.setTag(tag);
		return stack;
	}
}