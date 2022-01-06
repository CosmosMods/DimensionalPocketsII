package com.tcn.dimensionalpocketsii.core.command;

import java.util.UUID;

import com.mojang.brigadier.CommandDispatcher;
import com.tcn.cosmoslibrary.common.lib.ComponentColour;
import com.tcn.cosmoslibrary.common.lib.ComponentHelper;
import com.tcn.cosmoslibrary.common.lib.CosmosChunkPos;
import com.tcn.cosmoslibrary.registry.gson.object.ObjectPlayerInformation;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.Vec2Argument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;

public class PocketRecoverCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcherIn) {
		dispatcherIn.register(Commands.literal("dim").then(Commands.literal("pocket").then(Commands.literal("recover")).requires((commandSource) -> {
			return commandSource.hasPermission(2);
		}).then(Commands.literal("recover").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("chunkPos", Vec2Argument.vec2()).executes((commandContext) -> { 
			return recoverPocket(commandContext.getSource(), EntityArgument.getPlayer(commandContext, "player"), commandContext.getSource().getLevel(), Vec2Argument.getVec2(commandContext, "chunkPos"));
		}))))));
	}
	
	private static int recoverPocket(CommandSourceStack commandSourceIn, ServerPlayer serverPlayer, ServerLevel serverWorldIn, Vec2 locationIn) {
		CosmosChunkPos chunkPos = new CosmosChunkPos(locationIn.x, locationIn.y);
		
		String playerDisplayName = serverPlayer.getDisplayName().getString();
		UUID playerUUID = serverPlayer.getUUID();
		
		Pocket pocketIn = PocketRegistryManager.getPocketFromChunkPosition(chunkPos);
		
		if (serverPlayer.isCreative()) {
			if (pocketIn.exists()) {
				ObjectPlayerInformation playerInformation = pocketIn.getOwner();
				
				if (playerInformation != null) {
					if (playerDisplayName.equals(playerInformation.getPlayerName()) && playerUUID.equals(playerInformation.getPlayerUUID())) {
						ItemStack pocketStack = pocketIn.generateItemStackWithNBT();
						
						if (serverPlayer.getInventory().add(pocketStack)) {
							commandSourceIn.sendSuccess(ComponentHelper.locComp(ComponentColour.GREEN, false, "dimensionalpocketsii.command.recoverpocket.success.single", ": [ " + chunkPos.getX() + ", " + chunkPos.getZ() + " ]"), true);
							System.out.println("Player: [ " + playerDisplayName + ", " + playerUUID + " ] Just activated the Recover Pocket Command. For Pocket: " + chunkPos);
						} else {
							commandSourceIn.sendFailure(ComponentHelper.locComp("dimensionalpocketsii.command.recoverpocket.error.no_space"));
						}
					} else {
						commandSourceIn.sendFailure(ComponentHelper.locComp("dimensionalpocketsii.command.recoverpocket.error.not_owner"));
					}
				} else {
					ItemStack pocketStack = pocketIn.generateItemStackWithNBT();
					
					if (serverPlayer.getInventory().add(pocketStack)) {
						commandSourceIn.sendSuccess(ComponentHelper.locComp(ComponentColour.GREEN, false, "dimensionalpocketsii.command.recoverpocket.success.single_no_owner", ": [ " + chunkPos.getX() + ", " + chunkPos.getZ() + " ]"), true);
						System.out.println("Player: [ " + playerDisplayName + ", " + playerUUID + " ] Just activated the Recover Pocket Command. For Pocket: " + chunkPos);
					} else {
						commandSourceIn.sendFailure(ComponentHelper.locComp("dimensionalpocketsii.command.recoverpocket.error.no_space"));
					}
				}
			} else {
				commandSourceIn.sendFailure(ComponentHelper.locComp("dimensionalpocketsii.command.recoverpocket.error.exists"));
			}
		} else {
			commandSourceIn.sendFailure(ComponentHelper.locComp("dimensionalpocketsii.command.recoverpocket.error.creative"));
		}
		
		return 1;
	}
}
