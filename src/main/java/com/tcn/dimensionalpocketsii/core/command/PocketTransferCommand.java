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
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.vector.Vector2f;

public class PocketTransferCommand {

	public static void register(CommandDispatcher<CommandSource> dispatcherIn) {
		dispatcherIn.register(Commands.literal("dim").then(Commands.literal("pocket").then(Commands.literal("transfer")).requires((commandSource) -> {
			return commandSource.hasPermission(2);
		}).then(Commands.literal("transfer").then(Commands.argument("chunkPos", Vec2Argument.vec2()).then(Commands.argument("newPlayer", EntityArgument.player()).executes((commandContext) -> {
			return transferPocket(commandContext.getSource(), Vec2Argument.getVec2(commandContext, "chunkPos"), commandContext.getSource().getEntity(), EntityArgument.getPlayer(commandContext, "newPlayer"));
		}))))));
	}
	
	private static int transferPocket(CommandSource commandSourceIn, Vector2f vector, Entity senderEntity, ServerPlayerEntity newPlayer) {
		if (senderEntity instanceof ServerPlayerEntity) {
			ServerPlayerEntity oldPlayer = (ServerPlayerEntity) senderEntity;
		
			if (!(oldPlayer.equals(newPlayer))) {
				ChunkPos chunkPos = new ChunkPos(vector.x, vector.y); 
				Pocket pocket = PocketRegistryManager.getPocketFromChunkPosition(chunkPos);
				
				if (pocket.exists()) {
					if (pocket.checkIfOwner(oldPlayer)) {
						pocket.updateOwner(oldPlayer, newPlayer);
					} else {
						commandSourceIn.sendFailure(CosmosCompHelper.locComp("dimensionalpocketsii.command.transfer.error.not_owner"));
					}
				} else {
					commandSourceIn.sendFailure(CosmosCompHelper.locComp("dimensionalpocketsii.command.recoverpocket.error.exists"));
				}
			} else {
				commandSourceIn.sendFailure(CosmosCompHelper.locComp("dimensionalpocketsii.command.transfer.error.owner_same"));
			}
		}
		
		return 1;
	}
}
