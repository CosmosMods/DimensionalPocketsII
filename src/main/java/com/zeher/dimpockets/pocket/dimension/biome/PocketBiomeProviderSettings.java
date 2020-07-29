package com.zeher.dimpockets.pocket.dimension.biome;

import com.zeher.dimpockets.core.manager.ModBiomeManager;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.SingleBiomeProviderSettings;

public class PocketBiomeProviderSettings extends SingleBiomeProviderSettings {
	private Biome biome = ModBiomeManager.POCKET_BIOME;

	public SingleBiomeProviderSettings setBiome(Biome biomeIn) {
		this.biome = biomeIn;
		return this;
	}

	public Biome getBiome() {
		return this.biome;
	}
	
}