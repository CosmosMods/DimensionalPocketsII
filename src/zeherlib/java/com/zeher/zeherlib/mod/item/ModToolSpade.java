package com.zeher.zeherlib.mod.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;

public class ModToolSpade extends ItemSpade {

	public boolean has_effect;

	public ModToolSpade(ToolMaterial tool_material, CreativeTabs tab, String name, boolean effect) {
		super(tool_material);
		this.setCreativeTab(tab);
		this.setHarvestLevel("shovel", tool_material.getHarvestLevel());

		this.setRegistryName(name);
		this.setUnlocalizedName(name);

		this.has_effect = effect;
	}

	public ModToolSpade(ToolMaterial tool_material, CreativeTabs tab, String name) {
		super(tool_material);
		this.setCreativeTab(tab);
		this.setHarvestLevel("shovel", tool_material.getHarvestLevel());

		this.setRegistryName(name);
		this.setUnlocalizedName(name);

		this.has_effect = false;
	}

	@Override
	public boolean hasEffect(ItemStack itemStack) {
		return this.has_effect;
	}
}