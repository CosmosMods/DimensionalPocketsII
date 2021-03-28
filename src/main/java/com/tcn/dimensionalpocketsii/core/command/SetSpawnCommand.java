package com.tcn.dimensionalpocketsii.core.command;

import com.mojang.brigadier.CommandDispatcher;
import com.tcn.cosmoslibrary.common.comp.CosmosColour;
import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper;
import com.tcn.cosmoslibrary.common.math.ChunkPos;
import com.tcn.dimensionalpocketsii.core.management.CoreDimensionManager;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class SetSpawnCommand {

	public static void register(CommandDispatcher<CommandSource> dispatcherIn) {
		dispatcherIn.register(Commands.literal("dim").then(Commands.literal("setspawn").requires((commandSource) -> { 
			return commandSource.hasPermission(0);
		}).then(Commands.literal("setspawn")).executes((commandContext) -> {
			return setSpawn(commandContext.getSource());
		})));
	}
	
	private static int setSpawn(CommandSource commandSourceIn) {
		Entity entity = commandSourceIn.getEntity();
		
		if (entity instanceof ServerPlayerEntity) {
			ServerPlayerEntity serverPlayer = (ServerPlayerEntity) entity;
			
			if (serverPlayer.level.dimension().equals(CoreDimensionManager.POCKET_WORLD)) {
				BlockPos pos = serverPlayer.blockPosition();
				ChunkPos chunkPos = ChunkPos.scaleToChunkPos(pos);
				Pocket pocket = PocketRegistryManager.getPocketFromChunkPosition(chunkPos);
				BlockPos spawnPos = new BlockPos(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15);
				
				if (pocket.exists()) {
					pocket.setSpawnInPocket(spawnPos, serverPlayer.getRotationVector().y, serverPlayer.getRotationVector().x);
					
					commandSourceIn.sendSuccess(CosmosCompHelper.locComp(CosmosColour.GREEN, false, "dimensionalpocketsii.command.setspawn.success.pre", pos.getX() + ", " + pos.getY() + ", " + pos.getZ(), "dimensionalpocketsii.command.setspawn.success.suff"), true);
				} else {
					commandSourceIn.sendFailure(CosmosCompHelper.locComp("dimensionalpocketsii.pocket.status.action.null"));
				}
			} else {
				commandSourceIn.sendFailure(CosmosCompHelper.locComp("dimensionalpocketsii.command.setspawn.error.dimension"));
			}
		}
		
		return 1;
	}
}
