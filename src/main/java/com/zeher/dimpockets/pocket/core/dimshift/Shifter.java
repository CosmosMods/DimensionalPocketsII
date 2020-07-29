package com.zeher.dimpockets.pocket.core.dimshift;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.server.ServerWorld;


public class Shifter extends Teleporter {

	BlockPos targetSet;
	float spawnYaw, spawnPitch;

	public Shifter(ServerWorld worldServer, BlockPos targetSet, float spawnYaw, float spawnPitch) {
		super(worldServer);
		//DimUtils.enforceServer();
		this.targetSet = targetSet;
		this.spawnYaw = spawnYaw;
		this.spawnPitch = spawnPitch;
	}
	
	@Override
	public boolean func_222268_a(Entity entity, float par8) {
		if (!(entity instanceof ServerPlayerEntity)) {
			return false;
		} else {	
			ServerPlayerEntity player = (ServerPlayerEntity) entity;
	
			double posX = targetSet.getX() + 0.5F;
			double posY = targetSet.getY() + 1;
			double posZ = targetSet.getZ() + 0.5F;
	
			//player.setPositionAndUpdate(posX, posY, posZ);
			player.connection.setPlayerLocation(posX, posY, posZ, this.spawnYaw, this.spawnPitch);
			player.fallDistance = 0;
			
			//DimUtils.enforceServer();
		}
		return true;
	}
	
	public BlockPos getTargetPos() {
		return this.targetSet;
	}
}