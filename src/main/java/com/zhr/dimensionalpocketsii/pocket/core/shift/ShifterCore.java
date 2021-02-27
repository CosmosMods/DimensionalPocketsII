package com.zhr.dimensionalpocketsii.pocket.core.shift;

import com.zhr.cosmoslibrary.impl._client.util.TextHelper;
import com.zhr.cosmoslibrary.impl.util.ModUtil;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class ShifterCore {
	
	public static Shifter createTeleporter(RegistryKey<World> dim_id, BlockPos coordSet, float spawnYaw, float spawnPitch) {
		return new Shifter(ServerLifecycleHooks.getCurrentServer().getWorld(dim_id), coordSet, spawnYaw, spawnPitch);
	}
	
	public static void sendPlayerToBed(ServerPlayerEntity player) {
		shiftPlayerToDimension(player, player.func_241141_L_(), createTeleporter(player.func_241141_L_(), player.func_241140_K_(), player.rotationYaw, player.rotationPitch));
	}
	
	public static void sendPlayerToBedWithMessage(ServerPlayerEntity player, World world, String message) {
		ModUtil.sendPlayerMessage(world, player, message + TextHelper.GREEN + " Sending you to your bed.");
		shiftPlayerToDimension(player, player.func_241141_L_(), createTeleporter(player.func_241141_L_(), player.func_241140_K_(), player.rotationYaw, player.rotationPitch));
	}

	public static void shiftPlayerToDimension(ServerPlayerEntity player, RegistryKey<World> dimension_key, Shifter shifter) {
		ServerWorld serverworld1 = ServerLifecycleHooks.getCurrentServer().getWorld(dimension_key);
		BlockPos pos = shifter.targetSet;
		int[] pos_ = new int[] { pos.getX(), pos.getY(), pos.getZ()};
		float[] rot = new float[] { shifter.spawnYaw, shifter.spawnPitch };
		player.changeDimension(serverworld1, shifter);
		player.setPositionAndRotation(pos_[0] + 0.5F, pos_[1], pos_[2] + 0.5F, rot[0], rot[1]);
		player.setPositionAndUpdate(pos_[0] + 0.5F, pos_[1], pos_[2] + 0.5F);
	}
}