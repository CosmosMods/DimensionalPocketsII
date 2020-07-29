package com.zeher.dimpockets.pocket.core.dimshift;

import com.zeher.zeherlib.mod.util.ModUtil;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlayEntityEffectPacket;
import net.minecraft.network.play.server.SPlaySoundEventPacket;
import net.minecraft.network.play.server.SPlayerAbilitiesPacket;
import net.minecraft.network.play.server.SRespawnPacket;
import net.minecraft.network.play.server.SServerDifficultyPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.hooks.BasicEventHooks;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

@SuppressWarnings("deprecation")
public class ShifterUtil {
	
	public static Shifter createTeleporter(DimensionType dim_id, BlockPos coordSet, float spawnYaw, float spawnPitch) {
		return new Shifter(ServerLifecycleHooks.getCurrentServer().getWorld(dim_id), coordSet, spawnYaw, spawnPitch);
	}
	
	public static void sendPlayerToBed(PlayerEntity player) {
		shiftPlayerToDimension((ServerPlayerEntity) player, player.getSpawnDimension(), createTeleporter(player.getSpawnDimension(), player.getBedLocation(), player.rotationYaw, player.rotationPitch));
	}
	
	public static void sendPlayerToBedWithMessage(PlayerEntity player, World world, String message) {
		ModUtil.sendPlayerMessage(world, player, message);
		shiftPlayerToDimension((ServerPlayerEntity) player, player.getSpawnDimension(), createTeleporter(player.getSpawnDimension(), player.getBedLocation(), player.rotationYaw, player.rotationPitch));
	}
	
	public static void shiftPlayerToDimension(ServerPlayerEntity player, DimensionType type, Shifter teleporter) {
		//entityIn.changeDimension(dimensionTo);
		//entityIn.dimension = dimensionTo;
		//entityIn.setPositionAndUpdate(teleporter.getTargetPos().getX() + 0.5F, teleporter.getTargetPos().getY(), teleporter.getTargetPos().getZ() + 0.5F);
		
		if (!ForgeHooks.onTravelToDimension(player, type)) return;

        DimensionType dimensiontype = player.dimension;

        ServerWorld serverworld = player.server.getWorld(dimensiontype);
        player.dimension = type;
        ServerWorld serverworld1 = player.server.getWorld(type);
        WorldInfo worldinfo = player.world.getWorldInfo();
        player.connection.sendPacket(new SRespawnPacket(type, worldinfo.getGenerator(), player.interactionManager.getGameType()));
        player.connection.sendPacket(new SServerDifficultyPacket(worldinfo.getDifficulty(), worldinfo.isDifficultyLocked()));
        PlayerList playerlist = player.server.getPlayerList();
        playerlist.updatePermissionLevel(player);
        serverworld.removeEntity(player, true);
        player.revive();
        
        float f = teleporter.spawnPitch; //player.rotationPitch;
        float f1 = teleporter.spawnYaw;//player.rotationYaw;

        player.setLocationAndAngles(teleporter.getTargetPos().getX() + 0.5F, teleporter.getTargetPos().getY(), teleporter.getTargetPos().getZ() + 0.5F, f1, f);
        serverworld.getProfiler().endSection();
        serverworld.getProfiler().startSection("placing");
        player.setLocationAndAngles(teleporter.getTargetPos().getX() + 0.5F, teleporter.getTargetPos().getY(), teleporter.getTargetPos().getZ() + 0.5F, f1, f);

        serverworld.getProfiler().endSection();
        player.setWorld(serverworld1);
        serverworld1.func_217447_b(player);
        player.connection.setPlayerLocation(teleporter.getTargetPos().getX() + .5, teleporter.getTargetPos().getY() + .5, teleporter.getTargetPos().getZ() + .5, f1, f);
        player.interactionManager.setWorld(serverworld1);
        player.connection.sendPacket(new SPlayerAbilitiesPacket(player.abilities));
        playerlist.sendWorldInfo(player, serverworld1);
        playerlist.sendInventory(player);

        for(EffectInstance effectinstance : player.getActivePotionEffects()) {
            player.connection.sendPacket(new SPlayEntityEffectPacket(player.getEntityId(), effectinstance));
        }

        player.connection.sendPacket(new SPlaySoundEventPacket(1032, BlockPos.ZERO, 0, false));
        BasicEventHooks.firePlayerChangedDimensionEvent(player, dimensiontype, type);
	}
}