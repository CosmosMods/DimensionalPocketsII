package com.zeher.zeherlib.mod.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ModItemEffect extends ModItem {

	public ModItemEffect(Item.Properties prop) {
		super(prop);
	}
	
	@Override
	public boolean hasEffect(ItemStack itemStack) {
		return true;	
	}
}