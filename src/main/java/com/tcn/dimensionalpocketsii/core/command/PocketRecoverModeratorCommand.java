package com.tcn.dimensionalpocketsii.core.command;

import java.util.UUID;

import com.mojang.brigadier.CommandDispatcher;
import com.tcn.cosmoslibrary.common.comp.CosmosColour;
import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper;
import com.tcn.cosmoslibrary.common.math.ChunkPos;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.Vec2Argument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.world.server.ServerWorld;

public class PocketRecoverModeratorCommand {

	public static void register(CommandDispatcher<CommandSource> dispatcherIn) {
		dispatcherIn.register(Commands.literal("dim").then(Commands.literal("pocket").then(Commands.literal("recover").then(Commands.literal("moderator")).requires((commandSource) -> {
			return commandSource.hasPermission(2);
		}).then(Commands.literal("moderator").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("chunkPos", Vec2Argument.vec2()).executes((commandContext) -> { 
			return recoverPocket(commandContext.getSource(), EntityArgument.getPlayer(commandContext, "player"), commandContext.getSource().getLevel(), Vec2Argument.getVec2(commandContext, "chunkPos")); 
		})))))));
	}
	
	private static int recoverPocket(CommandSource commandSourceIn, ServerPlayerEntity serverPlayer, ServerWorld serverWorldIn, Vector2f locationIn) {
		ChunkPos chunkPos = new ChunkPos(locationIn.x, locationIn.y);
		
		String playerDisplayName = serverPlayer.getDisplayName().getString();
		UUID playerUUID = serverPlayer.getUUID();
		
		Pocket pocketIn = PocketRegistryManager.getPocketFromChunkPosition(chunkPos);
		if (serverPlayer.isCreative()) {
			if (pocketIn.exists()) {
				ItemStack pocketStack = pocketIn.generateItemStackWithNBT();
				
				if (serverPlayer.inventory.add(pocketStack)) {
					commandSourceIn.sendSuccess(CosmosCompHelper.locComp(CosmosColour.GREEN, false, "dimensionalpocketsii.command.recoverpocket.success.single", ": [ " + chunkPos.getX() + ", " + chunkPos.getZ() + " ]"), true);
					System.out.println("Player: [ " + playerDisplayName + ", " + playerUUID + " ] Just activated the Recover Pocket Command. For Pocket: " + chunkPos);
				} else {
					commandSourceIn.sendFailure(CosmosCompHelper.locComp("dimensionalpocketsii.command.recoverpocket.error.no_space"));
				}
			} else {
				commandSourceIn.sendFailure(CosmosCompHelper.locComp("dimensionalpocketsii.command.recoverpocket.error.exists"));
			}
		} else {
			commandSourceIn.sendFailure(CosmosCompHelper.locComp("dimensionalpocketsii.command.recoverpocket.error.creative"));
		}
		
		return 1;
	}
}
