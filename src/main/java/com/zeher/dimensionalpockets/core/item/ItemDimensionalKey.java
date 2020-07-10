package com.zeher.dimensionalpockets.core.item;

import com.zeher.zeherlib.mod.item.ModItem;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemDimensionalKey extends ModItem {

	public ItemDimensionalKey(String name, CreativeTabs tab, int maxStackSize) {
		super(name, tab, maxStackSize);
	}
	
	@Override
	public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
		return false;
	}

}