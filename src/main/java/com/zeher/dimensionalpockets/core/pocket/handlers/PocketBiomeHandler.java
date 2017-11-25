package com.zeher.dimensionalpockets.core.pocket.handlers;

import java.util.List;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import com.google.common.collect.Lists;
import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.core.util.DimLogger;


public class PocketBiomeHandler {

	private static Biome pocketBiome;
	private static boolean init = false;

	public static void init() {
		if (init) {
			DimLogger.severe("Tried calling BiomeHelper.init() again!");
			return;
		}
		init = true;
		
		BiomeProperties biome_prop = new BiomeProperties("Pocket Dim");
		biome_prop.setRainDisabled();

		pocketBiome = new Biome(biome_prop) {

			@Override
			public List getSpawnableList(EnumCreatureType par1EnumCreatureType) {
				return Lists.newArrayList();
			}

			@Override
			public float getSpawningChance() {
				return 0.0F;
			}
		};

		Biome.registerBiome(DimensionalPockets.biome_id, "Pocket Dim", pocketBiome);
		BiomeDictionary.addTypes(pocketBiome, Type.MAGICAL);
	}

	public static Biome getPocketBiome() {
		return pocketBiome;
	}
}
