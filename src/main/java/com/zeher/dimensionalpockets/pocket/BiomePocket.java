package com.zeher.dimensionalpockets.pocket;

import net.minecraft.world.biome.Biome;

public class BiomePocket extends Biome {

	public BiomePocket() {
		super(new BiomeProperties("Pocket Dim").setRainDisabled().setTemperature(1.0F));
	}

}
