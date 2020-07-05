package com.zeher.zeherlib.mod.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ModBlockDropsBlock extends ModBlock {
	
	private Block block;

	public ModBlockDropsBlock(String name, Material material, String toolClass, int level, int hardness, int resistance, CreativeTabs tab, Block block) {
		super(name, material, toolClass, level, hardness, resistance, tab);
		this.block = block;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(block);
    }
}