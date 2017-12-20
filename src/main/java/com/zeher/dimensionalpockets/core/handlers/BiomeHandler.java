package com.zeher.dimensionalpockets.core.handlers;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.core.pocket.handlers.BiomePocket;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class BiomeHandler {
	
	public static final Biome pocketBiome = new BiomePocket(new BiomeProperties("Pocket Dim")
			.setRainDisabled());
	
	@Mod.EventBusSubscriber(modid = DimensionalPockets.mod_id)
	public static class RegistrationHandler {
		@SubscribeEvent
		public static void registerBiomes(final RegistryEvent.Register<Biome> event) {
			final IForgeRegistry<Biome> registry = event.getRegistry();

			registerBiome(registry, pocketBiome, "Pocket Dim", BiomeManager.BiomeType.COOL, 1000);
		}

		private static <T extends Biome> void registerBiome(final IForgeRegistry<Biome> registry, final T biome, final String biomeName, final BiomeManager.BiomeType biomeType, final int weight, final BiomeDictionary.Type... types) {
			registry.register(biome.setRegistryName(DimensionalPockets.mod_id, biomeName));
			BiomeDictionary.addTypes(biome, types);
			BiomeManager.addBiome(biomeType, new BiomeManager.BiomeEntry(biome, weight));
		}

	}

}
