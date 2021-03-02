package com.tcn.cosmoslibrary.a_live;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tcn.cosmoslibrary.CosmosLibrary;
import com.tcn.cosmoslibrary.impl.item.CosmosItemWrench;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod.EventBusSubscriber(modid = CosmosLibrary.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModBusSubscriberCosmos {
	
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String LOGGER_PREFIX = "< CosmosModBus >: ";

	public static final Item COSMOS_WRENCH = new CosmosItemWrench(new Item.Properties().maxStackSize(1).group(ItemGroup.TOOLS));
	
	@SubscribeEvent
	public static void onItemRegistry(final RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(
				setupString("cosmos_wrench", COSMOS_WRENCH)
				
		);
	}
	
	public static <T extends IForgeRegistryEntry<T>> T setupString(final String name, final T entry) {
		return setupLow(entry, new ResourceLocation(CosmosLibrary.MOD_ID, name));
	}
	
	public static <T extends IForgeRegistryEntry<T>> T setupResource(final ResourceLocation name, final T entry) {
		return setupLow(entry, name);
	}

	public static <T extends IForgeRegistryEntry<T>> T setupLow(final T entry, final ResourceLocation registryName) {
		entry.setRegistryName(registryName);
		LOGGER.debug("Object Registered: " + registryName);
		return entry;
	}
}
