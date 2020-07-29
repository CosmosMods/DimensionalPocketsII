package com.zeher.dimpockets.pocket.dimension.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

public class PocketBiome extends Biome {

	public PocketBiome() {
		super(new Biome.Builder()
				.precipitation(RainType.NONE)
				.temperature(1.0F)
				.downfall(0)
				.surfaceBuilder(SurfaceBuilder.NOPE, SurfaceBuilder.AIR_CONFIG)
				.depth(0)
				.scale(0)
				.waterColor(0)
				.waterFogColor(0)
				.parent((String) null)
				.category(Category.NONE));
	}
}