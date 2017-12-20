package com.zeher.dimensionalpockets.core.handlers;

import java.util.HashSet;
import java.util.Set;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.trzcore.core.item.*;
import com.zeher.trzcore.tool.item.TRZItemMachineWrench;
import com.zeher.trzcore.tool.item.TRZPickaxe;
import com.zeher.trzcore.tool.item.TRZPickaxeEffect;
import com.zeher.trzcore.tool.item.TRZSword;
import com.zeher.trzcore.tool.item.TRZSwordEffect;
import com.zeher.trzcore.tool.item.TRZSwordEnergy;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ItemHandler {

	public static Item dimensional_shard = new TRZItemBase("dimensional_shard", DimensionalPockets.tab_dimensionalpockets);
	public static Item dimensional_ingot = new TRZItemBase("dimensional_ingot", DimensionalPockets.tab_dimensionalpockets);

	@SubscribeEvent
	public static void registerItemModelLocations(ModelRegistryEvent event) {
		ItemRegistry.registerModelLocation(dimensional_shard);
		ItemRegistry.registerModelLocation(dimensional_ingot);
	}

	@Mod.EventBusSubscriber(modid = DimensionalPockets.mod_id)
	public static class RegistryHandler {
		public static final Set<Item> ITEMS = new HashSet<>();

		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Item> event) {
			final IForgeRegistry<Item> registry = event.getRegistry();

			final Item[] items = { 
					dimensional_shard, 
					dimensional_ingot 
			};

			for (final Item item : items) {
				registry.register(item);
				ITEMS.add(item);
			}
		}
	}

	public static class ItemRegistry {

		public static void registerModelLocation(Item item) {
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		}
	}
	
}