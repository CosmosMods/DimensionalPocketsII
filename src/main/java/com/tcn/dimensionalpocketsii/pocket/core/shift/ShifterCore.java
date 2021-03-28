package com.tcn.dimensionalpocketsii.pocket.core.shift;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.common.chat.CosmosChatUtil;
import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper;
import com.tcn.cosmoslibrary.common.comp.CosmosColour;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class ShifterCore {
	
	public static void sendPlayerToBed(PlayerEntity playerIn, @Nullable EnumShiftDirection directionIn) {
		if (playerIn instanceof ServerPlayerEntity) {
			ServerPlayerEntity server_player = (ServerPlayerEntity) playerIn;
			shiftPlayerToDimension(server_player, Shifter.createTeleporter(server_player.getRespawnDimension(), directionIn, server_player.getRespawnPosition(), playerIn.getRotationVector().y, playerIn.getRotationVector().x, true, false));
		}
	}
	
	public static void sendPlayerToBedWithMessage(PlayerEntity playerIn, @Nullable EnumShiftDirection directionIn, String messageIn) {
		if (playerIn instanceof ServerPlayerEntity) {
			ServerPlayerEntity server_player = (ServerPlayerEntity) playerIn;
			CosmosChatUtil.sendServerPlayerMessage(server_player, CosmosCompHelper.getErrorText("dimensionalpocketsii.pocket.shifter_core.target_unknown_custom").append(CosmosCompHelper.locComp(CosmosColour.ORANGE, true, messageIn)));
			shiftPlayerToDimension(server_player, Shifter.createTeleporter(server_player.getRespawnDimension(), directionIn, server_player.getRespawnPosition(), playerIn.getRotationVector().y, playerIn.getRotationVector().x, true, false));
		}
	}

	public static void shiftPlayerToDimension(PlayerEntity playerIn, Shifter shifterIn) {
		if (playerIn instanceof ServerPlayerEntity) {
			ServerPlayerEntity server_player = (ServerPlayerEntity) playerIn;
			World player_world = playerIn.level;
			
			RegistryKey<World> dimension_key = shifterIn.getDimensionKey();
			EnumShiftDirection direction = shifterIn.getDirection();
			
			if (dimension_key != null) {
				if (direction != EnumShiftDirection.UNKNOWN) {
					MinecraftServer Mserver = ServerLifecycleHooks.getCurrentServer();
					ServerWorld server_world = Mserver.getLevel(dimension_key);
					BlockPos target_pos = shifterIn.getTargetPos();
					
					if (server_world != null) {
						if (target_pos != null) {
							double[] position = shifterIn.getTargetPosA();
							
							if (shifterIn.getSendMessage()) {
								CosmosChatUtil.sendPlayerMessageServer(server_player, direction.getChatComponentForDirection());
							}
							
							if (!player_world.isClientSide) {
								server_player.changeDimension(server_world, shifterIn);
								
								if (!shifterIn.playVanillaSound()) {
									server_player.connection.send(new SPlaySoundEffectPacket(direction.getSound(), SoundCategory.AMBIENT, position[0], position[1], position[2], 0.4F, 1));
								}
							}
						} else {
							if (!player_world.isClientSide) {
								server_player.changeDimension(server_world, shifterIn);
							}
							CosmosChatUtil.sendPlayerMessageServer(server_player, CosmosCompHelper.getErrorText("dimensionalpocketsii.pocket.shifter_core.target_unknown"));
						}
					} else {
						CosmosChatUtil.sendPlayerMessageServer(server_player, CosmosCompHelper.getErrorText("dimensionalpocketsii.pocket.shifter_core.server_world_null"));
					}
				} else {
					CosmosChatUtil.sendPlayerMessageServer(server_player, CosmosCompHelper.getErrorText("dimensionalpocketsii.pocket.shifter_core.direction_unknown"));
				}
			} else {
				CosmosChatUtil.sendPlayerMessageServer(server_player, CosmosCompHelper.getErrorText("dimensionalpocketsii.pocket.shifter_core.dimension_null"));
			}
		}
	}
}