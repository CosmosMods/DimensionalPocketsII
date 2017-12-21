package com.zeher.dimensionalpockets.core.handlers;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.trzlib.core.item.TRZItemBase;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemHandler {
	
	public static Item dimensional_shard;
	public static Item dimensional_ingot;
	
	public static void preInit(){
		dimensional_shard = new TRZItemBase("dimensional_shard", DimensionalPockets.tab_dimensionalpockets);
		dimensional_ingot = new TRZItemBase("dimensional_ingot", DimensionalPockets.tab_dimensionalpockets);
		
	}
	 
	public static void register(){
		GameRegistry.register(dimensional_shard);
		GameRegistry.register(dimensional_ingot);
		
	}
	
	public static void registerModelLocations(){
		registerItemModelLocation(dimensional_shard);
		registerItemModelLocation(dimensional_ingot);
		
	}
	 
	public static void registerItemModelLocation(Item item){
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}