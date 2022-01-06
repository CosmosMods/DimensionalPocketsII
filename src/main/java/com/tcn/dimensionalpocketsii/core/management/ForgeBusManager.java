package com.tcn.dimensionalpocketsii.core.management;

import com.mojang.brigadier.CommandDispatcher;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.core.command.PocketCommands;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;

import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fmlserverevents.FMLServerAboutToStartEvent;
import net.minecraftforge.fmlserverevents.FMLServerStartedEvent;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;
import net.minecraftforge.fmlserverevents.FMLServerStoppingEvent;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID, bus = EventBusSubscriber.Bus.FORGE)
public class ForgeBusManager {

	@SubscribeEvent
	public static void onServerAboutToStart(final FMLServerAboutToStartEvent event) {
		PocketRegistryManager.loadData();

		DimensionalPockets.CONSOLE.startup("[Server Init] {server} <abouttostart> Server about to start.");
	}

	@SubscribeEvent
	public static void onServerStarting(final FMLServerStartingEvent event) {
		DimensionalPockets.CONSOLE.startup("[Server Init] {server} <starting> Server starting.");
	}

	@SubscribeEvent
	public static void onServerStarted(final FMLServerStartedEvent event) {
		PocketRegistryManager.beginChunkLoading();

		DimensionalPockets.CONSOLE.startup("[Server Init] {server} <started> Server started.");
	}

	@SubscribeEvent
	public static void onServerStopping(final FMLServerStoppingEvent event) {
		PocketRegistryManager.saveData();

		DimensionalPockets.CONSOLE.startup("[Server Stopping] {server} <stopping> Server stopping.");
	}
	
	@SubscribeEvent
	public static void onCommandRegistry(RegisterCommandsEvent event) {
		CommandDispatcher<CommandSourceStack> dispatcherIn = event.getDispatcher();
		
		PocketCommands.registerCommands(dispatcherIn);
		
		DimensionalPockets.CONSOLE.startup("Command Registration complete.");
	}
}