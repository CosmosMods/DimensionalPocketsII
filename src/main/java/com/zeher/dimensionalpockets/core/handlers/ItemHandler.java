package com.zeher.dimensionalpockets.core.handlers;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.trzcore.core.item.*;
import com.zeher.trzcore.tool.item.TRZItemMachineWrench;
import com.zeher.trzcore.tool.item.TRZPickaxe;
import com.zeher.trzcore.tool.item.TRZPickaxeEffect;
import com.zeher.trzcore.tool.item.TRZSword;
import com.zeher.trzcore.tool.item.TRZSwordEffect;
import com.zeher.trzcore.tool.item.TRZSwordEnergy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.UniversalBucket;
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