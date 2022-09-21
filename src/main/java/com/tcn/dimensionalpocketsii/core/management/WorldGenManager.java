package com.tcn.dimensionalpocketsii.core.management;

import java.util.List;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.tcn.cosmoslibrary.data.worldgen.CosmosWorldGenHelper;
import com.tcn.dimensionalpocketsii.DimensionalPockets;

import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class WorldGenManager {

	public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, DimensionalPockets.MOD_ID);
	public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, DimensionalPockets.MOD_ID);
	
	public static final Supplier<List<OreConfiguration.TargetBlockState>> DIMENSIONAL_ORES_OVERWORLD = Suppliers.memoize(() -> List.of(
		OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ObjectManager.block_dimensional_ore.defaultBlockState()),
		OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ObjectManager.block_deepslate_dimensional_ore.defaultBlockState())
	));
	
	public static final Supplier<List<OreConfiguration.TargetBlockState>> DIMENSIONAL_ORES_NETHER = Suppliers.memoize(() -> List.of(
		OreConfiguration.target(OreFeatures.NETHER_ORE_REPLACEABLES, ObjectManager.block_dimensional_ore_nether.defaultBlockState())
	));
	
	public static final Supplier<List<OreConfiguration.TargetBlockState>> DIMENSIONAL_ORES_END = Suppliers.memoize(() -> List.of(
		OreConfiguration.target(new BlockMatchTest(Blocks.END_STONE), ObjectManager.block_dimensional_ore_end.defaultBlockState())
	));
	
	public static final RegistryObject<ConfiguredFeature<?, ?>> CONFIGURED_DIMENSIONAL_ORE_OVERWORLD = CONFIGURED_FEATURES.register("configured_dimensional_ore", () -> 
		new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(DIMENSIONAL_ORES_OVERWORLD.get(), 6))
	);
	public static final RegistryObject<PlacedFeature> PLACED_DIMENSIONAL_ORE_OVERWORLD = PLACED_FEATURES.register("placed_dimensional_ore", () -> 
		new PlacedFeature(CONFIGURED_DIMENSIONAL_ORE_OVERWORLD.getHolder().get(), CosmosWorldGenHelper.commonOrePlacement(6, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(80))))
	);
	
	public static final RegistryObject<ConfiguredFeature<?, ?>> CONFIGURED_DIMENSIONAL_ORE_NETHER = CONFIGURED_FEATURES.register("configured_dimensional_ore_nether", () -> 
		new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(DIMENSIONAL_ORES_NETHER.get(), 8))
	);
	public static final RegistryObject<PlacedFeature> PLACED_DIMENSIONAL_ORE_NETHER = PLACED_FEATURES.register("placed_dimensional_ore_nether", () -> 
		new PlacedFeature(CONFIGURED_DIMENSIONAL_ORE_NETHER.getHolder().get(), CosmosWorldGenHelper.commonOrePlacement(6, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(48))))
	);

	public static final RegistryObject<ConfiguredFeature<?, ?>> CONFIGURED_DIMENSIONAL_ORE_END = CONFIGURED_FEATURES.register("configured_dimensional_ore_end", () -> 
		new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(DIMENSIONAL_ORES_END.get(), 10))
	);
	public static final RegistryObject<PlacedFeature> PLACED_DIMENSIONAL_ORE_END = PLACED_FEATURES.register("placed_dimensional_ore_end", () -> 
		new PlacedFeature(CONFIGURED_DIMENSIONAL_ORE_END.getHolder().get(), CosmosWorldGenHelper.commonOrePlacement(6, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(128))))
	);
	
	public static void register(IEventBus bus) {
		CONFIGURED_FEATURES.register(bus);
		PLACED_FEATURES.register(bus);
	}
}
