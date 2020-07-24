package com.zeher.dimpockets.core.manager;

import com.zeher.dimpockets.DimensionalPockets;
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
public class ModItemManager {
	
	public static final Item DIMENSIONAL_SHARD = new ModItem("dimensional_shard", ModItemGroupManager.GROUP_DIM);
	public static final Item DIMENSIONAL_INGOT = new ModItem("dimensional_ingot", ModItemGroupManager.GROUP_DIM);
	public static final Item DIMENSIONAL_WRENCH = new ZLWrench("dimensional_wrench", ModItemGroupManager.GROUP_DIM);
	
	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(
				DIMENSIONAL_SHARD, DIMENSIONAL_INGOT, 
				DIMENSIONAL_WRENCH);
	}
	
	@SubscribeEvent
	public static void registerModelLocations(final ModelRegistryEvent event) {
		registerItemModelLocation(DIMENSIONAL_SHARD);
		registerItemModelLocation(DIMENSIONAL_INGOT);
		
		registerItemModelLocation(DIMENSIONAL_WRENCH);
	}
	 
	public static void registerItemModelLocation(Item item){
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}