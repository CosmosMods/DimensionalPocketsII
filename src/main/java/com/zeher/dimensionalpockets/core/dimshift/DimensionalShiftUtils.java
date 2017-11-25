package com.zeher.dimensionalpockets.core.dimshift;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class DimensionalShiftUtils {
	
	public static DimensionalShifter createTeleporter(int dim_id, BlockPos coordSet, float spawnYaw, float spawnPitch) {
		return new DimensionalShifter(FMLCommonHandler.instance().getMinecraftServerInstance().getServer().worldServerForDimension(dim_id), coordSet, spawnYaw, spawnPitch);
	}
	
	public static void shiftPlayerToDimension(EntityPlayerMP player, int dim_id, Teleporter teleporter) {
		MinecraftServer server = player.getEntityWorld().getMinecraftServer();
		WorldServer world_server = server.worldServerForDimension(dim_id);
		
		world_server.getMinecraftServer().getPlayerList().transferPlayerToDimension(player, dim_id, teleporter);
	}
	
	public static void shiftPlayerToDimension(EntityPlayerMP player, int dim_id, Teleporter teleporter, BlockPos pos) {
		MinecraftServer server = player.getEntityWorld().getMinecraftServer();
		WorldServer world_server = server.worldServerForDimension(dim_id);
		
		world_server.getMinecraftServer().getPlayerList().transferPlayerToDimension(player, dim_id, teleporter);
		world_server.getMinecraftServer().getPlayerList().changePlayerDimension(player, dim_id);
	}
}
