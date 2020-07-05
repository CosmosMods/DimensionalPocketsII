package com.zeher.zeherlib.mod.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ModBlockDropsItem extends ModBlock {
	
	private Item itemID;

	public ModBlockDropsItem(String name, Material material, String toolClass, int level, int hardness, int resistance, CreativeTabs tab, Item item) {
		super(name, material, toolClass, level, hardness, resistance, tab);
		this.itemID = item;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return this.itemID;
	}
}
