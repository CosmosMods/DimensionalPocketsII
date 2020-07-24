package com.zeher.dimpockets.core.manager;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipeManager {
	
	public static void preInitialization() { 
		registerSmelting();
	}
	
	public static void registerSmelting(){
		//GameRegistry.addSmelting(ItemHandler.DIMENSIONAL_SHARD, new ItemStack(ItemHandler.DIMENSIONAL_INGOT), 0.4F);
 	}
}