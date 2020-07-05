package com.zeher.zeherlib.mod.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class ModBlock extends Block {

	public ModBlock(String name, Material material, String tool, int harvest, int hardness, int resistance, CreativeTabs tab) {
		super(material);
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setCreativeTab(tab);
		this.setHarvestLevel(tool, harvest);
		this.setHardness(hardness);
		this.setResistance(resistance);
	}
}