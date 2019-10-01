package com.zeher.dimensionalpockets.core.creativetab;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.core.handler.BlockHandler;
import com.zeher.dimensionalpockets.core.handler.ItemHandler;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.*;

public class CreativeTabDimensional extends CreativeTabs{
	
	public CreativeTabDimensional(int par1, String par2str){
		super(par1, par2str);
	}

	public ItemStack getTabIconItem() {
		return new ItemStack(ItemHandler.dimensional_shard);
	}

}