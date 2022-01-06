package com.tcn.dimensionalpocketsii.core.management;

import com.tcn.dimensionalpocketsii.DimensionalPockets;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

@SuppressWarnings("unused")
public class DimensionManager {
	
	public static final ResourceLocation POCKET_ID = new ResourceLocation(DimensionalPockets.MOD_ID, "pocket");

	public static final ResourceKey<Level> POCKET_WORLD = ResourceKey.create(Registry.DIMENSION_REGISTRY, POCKET_ID);
	public static final ResourceKey<DimensionType> POCKET_DIMENSION_TYPE = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, POCKET_ID);
	private static final ResourceKey<LevelStem> POCKET_DIMENSION = ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, POCKET_ID);
	private static final ResourceKey<Biome> POCKET_BIOME = ResourceKey.create(Registry.BIOME_REGISTRY, POCKET_ID);
	  
}
