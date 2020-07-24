package com.zeher.dimpockets.core.manager;

import com.zeher.dimpockets.DimensionalPockets;
import com.zeher.dimpockets.pocket.core.PocketBiome;

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

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID)
public class ModBiomeManager {
	
	public static final PocketBiome pocketBiome = new PocketBiome();

	public static void initialization() {
		BiomeManager.addBiome(BiomeType.COOL, new BiomeEntry(pocketBiome, 10));
		BiomeManager.addSpawnBiome(pocketBiome);
		BiomeDictionary.addTypes(pocketBiome, BiomeDictionary.Type.COLD, BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.DRY);
	}
	
	@SubscribeEvent
	public static void onBiomeRegistry(final RegistryEvent.Register<Biome> event) {
		final IForgeRegistry<Biome> registry = event.getRegistry();

		//registerBiome(registry, pocketBiome, "Pocket Dim", BiomeManager.BiomeType.WARM, 1000, BiomeDictionary.Type.MAGICAL);
		registry.register(pocketBiome.setRegistryName(DimensionalPockets.MOD_ID, "Pocket Dim"));
	}
}