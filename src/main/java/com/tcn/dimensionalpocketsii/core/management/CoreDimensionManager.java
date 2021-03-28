package com.tcn.dimensionalpocketsii.core.management;

import com.tcn.dimensionalpocketsii.DimensionalPockets;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Dimension;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

@SuppressWarnings("unused")
public class CoreDimensionManager {
	
	public static final ResourceLocation POCKET_ID = new ResourceLocation(DimensionalPockets.MOD_ID, "pocket");

	public static final RegistryKey<World> POCKET_WORLD = RegistryKey.create(Registry.DIMENSION_REGISTRY, POCKET_ID);
	private static final RegistryKey<DimensionType> POCKET_DIMENSION_TYPE = RegistryKey.create(Registry.DIMENSION_TYPE_REGISTRY, POCKET_ID);
	private static final RegistryKey<Dimension> POCKET_DIMENSION = RegistryKey.create(Registry.LEVEL_STEM_REGISTRY, POCKET_ID);
	private static final RegistryKey<Biome> POCKET_BIOME = RegistryKey.create(Registry.BIOME_REGISTRY, POCKET_ID);
	  
}
