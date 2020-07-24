package com.zeher.dimpockets.pocket.core;

import net.minecraft.world.biome.Biome;

public class PocketBiome extends Biome {

	public PocketBiome() {
		super(new BiomeProperties("Pocket Dim").setRainDisabled().setTemperature(1.0F));
	}

}
