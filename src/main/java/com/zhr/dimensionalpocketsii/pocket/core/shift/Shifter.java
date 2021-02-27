package com.zhr.dimensionalpocketsii.pocket.core.shift;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

public class Shifter implements ITeleporter {

	public BlockPos targetSet;
	public float spawnYaw;
	public float spawnPitch;

	public ServerWorld world;

	public Shifter(ServerWorld worldServer, BlockPos targetSet, float spawnYaw, float spawnPitch) {
		// DimUtils.enforceServer();
		this.targetSet = targetSet;
		this.spawnYaw = spawnYaw;
		this.spawnPitch = spawnPitch;

		this.world = worldServer;
	}

	public BlockPos getTargetSet() {
		return this.targetSet;
	}

}