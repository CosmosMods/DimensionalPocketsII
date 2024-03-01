package com.tcn.dimensionalpocketsii.core.management;

import com.mojang.brigadier.CommandDispatcher;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.core.command.PocketCommands;
import com.tcn.dimensionalpocketsii.pocket.core.registry.ChunkLoadingManager;
import com.tcn.dimensionalpocketsii.pocket.core.registry.StorageManager;
import com.tcn.dimensionalpocketsii.pocket.core.registry.BackupManager.BackupType;

import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID, bus = EventBusSubscriber.Bus.FORGE)
public class ForgeBusManager {

	@SubscribeEvent
	public static void onServerAboutToStart(final ServerAboutToStartEvent event) {
		StorageManager.clearRegistry();
		StorageManager.loadRegistry();

		ChunkLoadingManager.loadFromStorage();
		
		DimensionalPockets.CONSOLE.startup("[Server Init] {server} <abouttostart> Server about to start.");
	}

	@SubscribeEvent
	public static void onServerStarting(final ServerStartingEvent event) {
		DimensionalPockets.CONSOLE.startup("[Server Init] {server} <starting> Server starting.");
	}

	@SubscribeEvent
	public static void onServerStarted(final ServerStartedEvent event) {
		DimensionalPockets.CONSOLE.startup("[Server Init] {server} <started> Server started.");
	}

	@SubscribeEvent
	public static void onServerStopping(final ServerStoppingEvent event) {
		StorageManager.saveRegistry();
		StorageManager.createBackup(BackupType.SYSTEM);
		
		ChunkLoadingManager.saveToStorage();
		
		DimensionalPockets.CONSOLE.startup("[Server Stopping] {server} <stopping> Server stopping.");
	}
	
	@SubscribeEvent
	public static void onCommandRegistry(RegisterCommandsEvent event) {
		CommandDispatcher<CommandSourceStack> dispatcherIn = event.getDispatcher();
		
		PocketCommands.registerCommands(dispatcherIn);
		
		DimensionalPockets.CONSOLE.startup("Command Registration complete.");
	}
}