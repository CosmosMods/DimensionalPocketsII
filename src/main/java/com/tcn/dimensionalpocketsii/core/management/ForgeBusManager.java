package com.tcn.dimensionalpocketsii.core.management;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;

import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID, bus = EventBusSubscriber.Bus.FORGE)
public class ForgeBusManager {

	public static final Logger LOGGER = LogManager.getLogger();
	public static final String LOGGER_PREFIX = "< BusSubscriberForge >: ";

	private static final ArrayList<ConfiguredFeature<?, ?>> overworldOres = new ArrayList<ConfiguredFeature<?, ?>>();
	private static final ArrayList<ConfiguredFeature<?, ?>> netherOres = new ArrayList<ConfiguredFeature<?, ?>>();
	private static final ArrayList<ConfiguredFeature<?, ?>> endOres = new ArrayList<ConfiguredFeature<?, ?>>();

	@SubscribeEvent
	public static void onServerAboutToStart(final FMLServerAboutToStartEvent event) {
		PocketRegistryManager.loadData();

		LOGGER.info(LOGGER_PREFIX + "[FMLServerAboutToStartEvent] Server about to start...");
	}

	@SubscribeEvent
	public static void onServerStarting(final FMLServerStartingEvent event) {
		// event.registerServerCommand(new CommandDimShift());
		// event.registerServerCommand(new CommandDimHelp());
		// event.registerServerCommand(new CommandRecoverPocket());

		LOGGER.info(LOGGER_PREFIX + "[FMLServerStartingEvent] Server starting...");
	}

	@SubscribeEvent
	public static void onServerStarted(final FMLServerStartedEvent event) {
		PocketRegistryManager.initChunkLoading();

		LOGGER.info(LOGGER_PREFIX + "[FMLServerStartedEvent] Server started...");
	}

	@SubscribeEvent
	public static void onServerStopping(final FMLServerStoppingEvent event) {
		PocketRegistryManager.saveData();
		// PocketRegistryManager.clearMap();
		// ChunkLoaderManagerRoom.clearTicketMap();
		// ChunkLoaderManagerBlock.clearTicketMap();

		LOGGER.info(LOGGER_PREFIX + "[FMLServerStoppingEvent] Server stopping...");
	}

	public static void registerOres() {
		// BASE_STONE_OVERWORLD is for generating in stone, granite, diorite, and andesite
		// NETHERRACK is for generating in netherrack
		// BASE_STONE_NETHER is for generating in netherrack, basalt and blackstone

		// Overworld Ore Register
		overworldOres.add(register("dimensional_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, ModBusManager.BLOCK_DIMENSIONAL_ORE.getDefaultState(), 4)) // Vein Size
			.range(32).square() // Spawn height start
			.func_242731_b(16))); // Chunk spawn frequency

		// Nether Ore Register
		netherOres.add(register("dimensional_ore_nether", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, ModBusManager.BLOCK_DIMENSIONAL_ORE_NETHER.getDefaultState(), 6))
			.range(48).square()
			.func_242731_b(20)));

		// The End Ore Register
		endOres.add(register("dimensional_ore_end", Feature.ORE.withConfiguration(new OreFeatureConfig(new BlockMatchRuleTest(Blocks.END_STONE), ModBusManager.BLOCK_DIMENSIONAL_ORE_END.getDefaultState(), 8)) // Vein Size
			.range(128).square() // Spawn height start
			.func_242731_b(32))); // Chunk spawn frequency
	}

	@SubscribeEvent
	public static void gen(BiomeLoadingEvent event) {
		BiomeGenerationSettingsBuilder generation = event.getGeneration();

		if (event.getCategory().equals(Biome.Category.NETHER)) {
			for (ConfiguredFeature<?, ?> ore : netherOres) {
				if (ore != null)
					generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ore);
			}
		}
		if (event.getCategory().equals(Biome.Category.THEEND)) {
			for (ConfiguredFeature<?, ?> ore : endOres) {
				if (ore != null)
					generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ore);
			}
		}
		for (ConfiguredFeature<?, ?> ore : overworldOres) {
			if (ore != null)
				generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ore);
		}
	}

	private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
		return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, DimensionalPockets.MOD_ID + ":" + name,
				configuredFeature);
	}

}