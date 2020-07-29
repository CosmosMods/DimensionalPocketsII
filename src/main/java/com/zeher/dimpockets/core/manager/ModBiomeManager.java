package com.zeher.dimpockets.core.manager;

import com.zeher.dimpockets.DimensionalPockets;
import com.zeher.dimpockets.pocket.dimension.biome.PocketBiome;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBiomeManager {
	
	public static final PocketBiome POCKET_BIOME = new PocketBiome();
	
	@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
	public static class RegistrationHandler {
		
		@SubscribeEvent
		public static void registerBiomes(final RegistryEvent.Register<Biome> event) {
			final IForgeRegistry<Biome> registry = event.getRegistry();

			registerBiome(registry, POCKET_BIOME, "pocket_biome", BiomeManager.BiomeType.WARM, 1000, BiomeDictionary.Type.MAGICAL);
		}
				
		private static <T extends Biome> void registerBiome(final IForgeRegistry<Biome> registry, final T biome, final String biomeName, final BiomeManager.BiomeType biomeType, final int weight, final BiomeDictionary.Type... types) {
			registry.register(biome.setRegistryName(DimensionalPockets.MOD_ID, biomeName));
			BiomeManager.addBiome(biomeType, new BiomeManager.BiomeEntry(biome, weight));
			BiomeManager.addSpawnBiome(biome);
			BiomeDictionary.addTypes(POCKET_BIOME, BiomeDictionary.Type.MAGICAL);
		}
	}
}