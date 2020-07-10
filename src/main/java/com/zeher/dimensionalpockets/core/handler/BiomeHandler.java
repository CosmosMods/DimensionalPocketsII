package com.zeher.dimensionalpockets.core.handler;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.pocket.BiomePocket;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class BiomeHandler {
	
	public static final BiomePocket pocketBiome = new BiomePocket();
	
	@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID)
	public static class RegistrationHandler {
		
		@SubscribeEvent
		public static void registerBiomes(final RegistryEvent.Register<Biome> event) {
			final IForgeRegistry<Biome> registry = event.getRegistry();

			//registerBiome(registry, pocketBiome, "Pocket Dim", BiomeManager.BiomeType.WARM, 1000, BiomeDictionary.Type.MAGICAL);
			registry.register(pocketBiome.setRegistryName(DimensionalPockets.MOD_ID, "Pocket Dim"));
		}

		public static void initBiomeManagerAndDictionary() {
			BiomeManager.addBiome(BiomeType.COOL, new BiomeEntry(pocketBiome, 10));
			BiomeManager.addSpawnBiome(pocketBiome);
			BiomeDictionary.addTypes(pocketBiome, BiomeDictionary.Type.COLD, BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.DRY);
		}
				
		/**private static <T extends Biome> void registerBiome(final IForgeRegistry<Biome> registry, final T biome, final String biomeName, final BiomeManager.BiomeType biomeType, final int weight, final BiomeDictionary.Type... types) {
			registry.register(biome.setRegistryName(DimensionalPockets.mod_id, biomeName));
			//BiomeDictionary.addTypes(biome, types);
			BiomeManager.addBiome(biomeType, new BiomeManager.BiomeEntry(biome, weight));
			BiomeDictionary.addTypes(pocketBiome, Type.MAGICAL);
		}*/
	}
}