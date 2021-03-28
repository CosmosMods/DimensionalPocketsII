package com.tcn.dimensionalpocketsii.core.command;

import com.mojang.brigadier.CommandDispatcher;
import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper;
import com.tcn.cosmoslibrary.common.math.ChunkPos;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.Vec2Argument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.vector.Vector2f;

public class PocketTransferModeratorCommand {

	public static void register(CommandDispatcher<CommandSource> dispatcherIn) {
		dispatcherIn.register(Commands.literal("dim").then(Commands.literal("pocket").then(Commands.literal("transfer").then(Commands.literal("moderator")).requires((commandSource) -> {
			return commandSource.hasPermission(3);
		}).then(Commands.literal("transfer").then(Commands.argument("chunkPos", Vec2Argument.vec2()).then(Commands.argument("newPlayer", EntityArgument.player()).executes((commandContext) -> {
			return transferPocket(commandContext.getSource(), Vec2Argument.getVec2(commandContext, "chunkPos"), EntityArgument.getPlayer(commandContext, "newPlayer"));
		})))))));
	}
	
	private static int transferPocket(CommandSource commandSourceIn, Vector2f vector, ServerPlayerEntity newPlayer) {
		ChunkPos chunkPos = new ChunkPos(vector.x, vector.y); 
		Pocket pocket = PocketRegistryManager.getPocketFromChunkPosition(chunkPos);
		
		if (pocket.exists()) {
			pocket.updateOwner(null, newPlayer);
		} else {
			commandSourceIn.sendFailure(CosmosCompHelper.locComp("dimensionalpocketsii.command.recoverpocket.error.exists"));
		}
		
		return 1;
	}
}
