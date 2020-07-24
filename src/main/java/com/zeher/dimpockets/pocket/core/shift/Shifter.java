package com.zeher.dimpockets.pocket.core.shift;

import com.zeher.dimpockets.core.manager.ModBlockManager;
import com.zeher.dimpockets.core.util.DimUtils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class Shifter extends Teleporter {

	private BlockPos targetPos;
	float spawnYaw, spawnPitch;

	public Shifter(WorldServer worldServer, BlockPos targetPos, float spawnYaw, float spawnPitch) {
		super(worldServer);
		this.targetPos = targetPos;
		this.spawnYaw = spawnYaw;
		this.spawnPitch = spawnPitch;
		DimUtils.enforceServer();
	}

	@Override
	public void placeInPortal(Entity entity, float par8) {
		if (!(entity instanceof EntityPlayerMP)) {
			return;
		} else {	
			EntityPlayerMP player = (EntityPlayerMP) entity;
	
			double posX = targetPos.getX() + 0.5F;
			double posY = targetPos.getY() + 1;
			double posZ = targetPos.getZ() + 0.5F;
			
			player.connection.setPlayerLocation(posX, posY, posZ, this.spawnYaw, this.spawnPitch);
			player.fallDistance = 0;
			
			DimUtils.enforceServer();
		}
	}
	
	public BlockPos getTargetPos() {
		return this.targetPos;
	}
	
	public BlockPos getOffsetTargetPos() {
		return new BlockPos(targetPos.getX() + 0.5F, targetPos.getY() + 1, targetPos.getZ() + 0.5F);
	}
}