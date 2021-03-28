package com.tcn.dimensionalpocketsii.core.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;

public class PocketCommands {

	public static void registerCommands(CommandDispatcher<CommandSource> dispatcherIn) {
		PocketRecoverCommand.register(dispatcherIn);
		PocketRecoverModeratorCommand.register(dispatcherIn);
		PocketTransferCommand.register(dispatcherIn);
		PocketTransferModeratorCommand.register(dispatcherIn);
		DimShiftCommand.register(dispatcherIn);
		SetSpawnCommand.register(dispatcherIn);
	}
}