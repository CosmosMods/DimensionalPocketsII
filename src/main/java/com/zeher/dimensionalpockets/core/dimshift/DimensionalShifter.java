package com.zeher.dimensionalpockets.core.dimshift;

import com.zeher.dimensionalpockets.core.handler.BlockHandler;
import com.zeher.dimensionalpockets.core.util.DimUtils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;


public class DimensionalShifter extends Teleporter {

	BlockPos targetSet;
	float spawnYaw, spawnPitch;

	public DimensionalShifter(WorldServer worldServer, BlockPos targetSet, float spawnYaw, float spawnPitch) {
		super(worldServer);
		DimUtils.enforceServer();
		this.targetSet = targetSet;
		this.spawnYaw = spawnYaw;
		this.spawnPitch = spawnPitch;
	}

	@Override
	public void placeInPortal(Entity entity, float par8) {
		if (!(entity instanceof EntityPlayerMP))
			return;

		EntityPlayerMP player = (EntityPlayerMP) entity;

		double posX = targetSet.getX() + 0.5F;
		double posY = targetSet.getY() + 1;
		double posZ = targetSet.getZ() + 0.5F;

		player.setPositionAndUpdate(posX, posY, posZ);
		player.connection.setPlayerLocation(posX, posY, posZ, this.spawnYaw, this.spawnPitch);
		player.fallDistance = 0;
		
		DimUtils.enforceServer();
	}
}
