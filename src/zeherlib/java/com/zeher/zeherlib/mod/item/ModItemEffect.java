package com.zeher.zeherlib.mod.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ModItemEffect extends ModItem {

	public ModItemEffect(String name, CreativeTabs tab) {
		super(name, tab);
	}
	
	public ModItemEffect(String name, CreativeTabs tab, int maxStackSize){
		super(name, tab, maxStackSize);
	}
	
	@Override
	public boolean hasEffect(ItemStack itemStack) {
		return true;	
	}
}