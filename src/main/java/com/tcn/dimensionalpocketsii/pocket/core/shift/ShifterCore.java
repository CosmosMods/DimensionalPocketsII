package com.tcn.dimensionalpocketsii.pocket.core.shift;

import java.util.UUID;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.impl.colour.ChatColour;
import com.tcn.cosmoslibrary.impl.util.CosmosChatUtil;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class ShifterCore {
	
	public static void sendPlayerToBed(PlayerEntity playerIn, @Nullable EnumShiftDirection directionIn) {
		if (playerIn instanceof ServerPlayerEntity) {
			ServerPlayerEntity server_player = (ServerPlayerEntity) playerIn;
			shiftPlayerToDimension(server_player, Shifter.createTeleporter(server_player.func_241141_L_(), directionIn, server_player.func_241140_K_(), playerIn.rotationYaw, playerIn.rotationPitch));
		}
	}
	
	public static void sendPlayerToBedWithMessage(PlayerEntity playerIn, @Nullable EnumShiftDirection directionIn, String messageIn) {
		if (playerIn instanceof ServerPlayerEntity) {
			ServerPlayerEntity server_player = (ServerPlayerEntity) playerIn;
			playerIn.sendMessage(new StringTextComponent(ChatColour.ORANGE + "Unable to shift to correct target. " + ChatColour.BOLD + ChatColour.RED + messageIn + ChatColour.END), UUID.randomUUID());
			shiftPlayerToDimension(server_player, Shifter.createTeleporter(server_player.func_241141_L_(), directionIn, server_player.func_241140_K_(), playerIn.rotationYaw, playerIn.rotationPitch));
		}
	}

	public static void shiftPlayerToDimension(PlayerEntity playerIn, Shifter shifterIn) {
		if (playerIn instanceof ServerPlayerEntity) {
			ServerPlayerEntity server_player = (ServerPlayerEntity) playerIn;
			
			RegistryKey<World> dimension_key = shifterIn.getDimensionKey();
			EnumShiftDirection direction = shifterIn.getDirection();
			
			if (dimension_key != null) {
				ServerWorld server_world = ServerLifecycleHooks.getCurrentServer().getWorld(dimension_key);
				BlockPos target_pos = shifterIn.getTargetPos();
				
				if (server_world != null) {
					if (target_pos != null) {
						int[] position = shifterIn.getTargetPosA();
						float[] rotation = shifterIn.getTargetRotation();
						
						if (direction != null) {
							if (direction.getSound() != null) {
								server_player.playSound(direction.getSound(), 1, 1);
							}
						}
						
						CosmosChatUtil.sendPlayerMessage(server_player, false, direction.getChatStringForDirection());
						server_player.changeDimension(server_world, shifterIn);
						server_player.setPositionAndRotation(position[0] + 0.5F, position[1], position[2] + 0.5F, rotation[0], rotation[1]);
						server_player.setPositionAndUpdate(position[0] + 0.5F, position[1], position[2] + 0.5F);
					} else {
						server_player.sendMessage(new StringTextComponent(ChatColour.RED + "Your respawn point cannot be found. Defaulting to Spawn World position."), UUID.randomUUID());
						server_player.changeDimension(server_world, shifterIn);
					}
				} else {
					server_player.sendMessage(new StringTextComponent(ChatColour.RED + "Server World is null. Please report this issue to the Mod Author."), UUID.randomUUID());
				}
			} else {
				server_player.sendMessage(new StringTextComponent(ChatColour.RED + "Dimension Key is null. Please report this issue to the Mod Author."), UUID.randomUUID());
			}
		}
	}
}