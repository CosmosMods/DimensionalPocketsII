package com.zeher.dimensionalpockets.core.handler;

import com.zeher.dimensionalpockets.DimensionalPockets;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeHandler {
	
	public static void registerRecipes(){
		
	}
	
	public static void registerSmelting(){
		GameRegistry.addSmelting(ItemHandler.dimensional_shard, new ItemStack(ItemHandler.dimensional_ingot), 0.4F);
 	}
	
}
