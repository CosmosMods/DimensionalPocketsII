package com.zeher.dimensionalpockets.core.handler;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.zeherlib.core.item.ItemBase;
import com.zeher.zeherlib.tool.item.MachineWrench;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID)
public class ItemHandler {
	
	public static Item dimensional_shard = new ItemBase("dimensional_shard", DimensionalPockets.TAB_DIMENSIONALPOCKETS);
	public static Item dimensional_ingot = new ItemBase("dimensional_ingot", DimensionalPockets.TAB_DIMENSIONALPOCKETS);
	
	public static Item dimensional_wrench = new MachineWrench("dimensional_wrench", DimensionalPockets.TAB_DIMENSIONALPOCKETS);
	
	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(dimensional_shard, dimensional_ingot, dimensional_wrench);
	}
	
	@SubscribeEvent
	public static void registerModelLocations(final ModelRegistryEvent event) {
		registerItemModelLocation(dimensional_shard);
		registerItemModelLocation(dimensional_ingot);
		registerItemModelLocation(dimensional_wrench);
	}
	 
	public static void registerItemModelLocation(Item item){
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}