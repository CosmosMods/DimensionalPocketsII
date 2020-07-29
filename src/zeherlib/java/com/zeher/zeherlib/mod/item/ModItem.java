package com.zeher.zeherlib.mod.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ModItem extends Item {
	
	public boolean has_effect;
	
	public ModItem(Item.Properties properties){
		super(properties);
		
		this.has_effect = false;
	}
	
	public ModItem(Item.Properties properties, boolean has_effect){
		super(properties);
		
		this.has_effect = has_effect;
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return this.has_effect;
	}
}