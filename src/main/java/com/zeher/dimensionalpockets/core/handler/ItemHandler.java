package com.zeher.dimensionalpockets.core.handler;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.core.item.ItemDimensionalKey;
import com.zeher.zeherlib.api.compat.core.impl.ZLWrench;
import com.zeher.zeherlib.mod.item.ModItem;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID)
public class ItemHandler {
	
	public static final Item DIMENSIONAL_SHARD = new ModItem("dimensional_shard", DimensionalPockets.TAB_DIMENSIONALPOCKETS);
	public static final Item DIMENSIONAL_INGOT = new ModItem("dimensional_ingot", DimensionalPockets.TAB_DIMENSIONALPOCKETS);
	
	public static final Item DIMENSIONAL_KEY = new ItemDimensionalKey("dimensional_key", DimensionalPockets.TAB_DIMENSIONALPOCKETS, 1);
	public static final Item DIMENSIONAL_WRENCH = new ZLWrench("dimensional_wrench", DimensionalPockets.TAB_DIMENSIONALPOCKETS);
	
	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(DIMENSIONAL_SHARD, DIMENSIONAL_INGOT, 
				DIMENSIONAL_KEY, DIMENSIONAL_WRENCH);
	}
	
	@SubscribeEvent
	public static void registerModelLocations(final ModelRegistryEvent event) {
		registerItemModelLocation(DIMENSIONAL_SHARD);
		registerItemModelLocation(DIMENSIONAL_INGOT);
		
		registerItemModelLocation(DIMENSIONAL_KEY);
		registerItemModelLocation(DIMENSIONAL_WRENCH);
	}
	 
	public static void registerItemModelLocation(Item item){
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}