package com.tcn.cosmoslibrary.impl.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CosmosItem extends Item {
	
	public boolean has_effect;
	
	public CosmosItem(Item.Properties properties){
		super(properties);
		
		this.has_effect = false;
	}
	
	public CosmosItem(Item.Properties properties, boolean has_effect){
		super(properties);
		
		this.has_effect = has_effect;
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return this.has_effect;
	}
}