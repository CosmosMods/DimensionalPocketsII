package com.zeher.zeherlib.mod.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ModItem extends Item {
	
	public boolean has_effect;
	
	public ModItem(String name, CreativeTabs tab, int maxStackSize){
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setMaxStackSize(maxStackSize);
		this.setCreativeTab(tab);
		
		this.has_effect = false;
	}
	
	public ModItem(String name, CreativeTabs tab, int maxStackSize, boolean has_effect){
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setMaxStackSize(maxStackSize);
		this.setCreativeTab(tab);
		
		this.has_effect = has_effect;
	}
	
	public ModItem(String name, CreativeTabs tab){
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setCreativeTab(tab);
		
		this.has_effect = false;
	}
	
	public ModItem(String name, CreativeTabs tab, boolean has_effect){
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setCreativeTab(tab);
		
		this.has_effect = has_effect;
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return this.has_effect;
	}
}