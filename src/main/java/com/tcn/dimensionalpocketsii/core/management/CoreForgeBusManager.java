package com.tcn.dimensionalpocketsii.core.management;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.CommandDispatcher;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.core.command.PocketCommands;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;

import net.minecraft.command.CommandSource;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID, bus = EventBusSubscriber.Bus.FORGE)
public class CoreForgeBusManager {

	public static final Logger LOGGER = LogManager.getLogger();
	public static final String LOGGER_PREFIX = "< BusSubscriberForge >: ";

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
	
	@SubscribeEvent
	public static void onCommandRegistry(RegisterCommandsEvent event) {
		CommandDispatcher<CommandSource> dispatcherIn = event.getDispatcher();
		
		PocketCommands.registerCommands(dispatcherIn);
	}
}