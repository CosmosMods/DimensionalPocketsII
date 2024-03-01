package com.tcn.dimensionalpocketsii.core.management;

import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.core.worldgen.DimensionalOreFeature;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class WorldGenManager {

	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, DimensionalPockets.MOD_ID);
	
	public static final RegistryObject<Feature<?>> DIMENSIONAL_ORE_END = FEATURES.register("dimensional_ore_end", DimensionalOreFeature::new);
	public static final RegistryObject<Feature<?>> DIMENSIONAL_ORE_NETHER = FEATURES.register("dimensional_ore_nether", DimensionalOreFeature::new);
	public static final RegistryObject<Feature<?>> DIMENSIONAL_ORE_OVERWORLD = FEATURES.register("dimensional_ore_overworld", DimensionalOreFeature::new);
	
	public static void register(IEventBus bus) {
		FEATURES.register(bus);
	}

}