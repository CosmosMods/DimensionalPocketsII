package com.tcn.dimensionalpocketsii.pocket.core.shift;

import java.util.function.Function;

import com.tcn.cosmoslibrary.common.lib.MathHelper;
import com.tcn.cosmoslibrary.core.teleport.EnumSafeTeleport;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;

public class Shifter implements ITeleporter {

	private ResourceKey<Level> dimension_key;
	private EnumShiftDirection direction;
	private BlockPos target_pos;
	private float target_yaw;
	private float target_pitch;
	private boolean playVanillaSound;
	private boolean sendMessage;
	private boolean safeSpawn;

	public Shifter(ResourceKey<Level> dimensionKeyIn, EnumShiftDirection directionIn, BlockPos targetPosIn, float targetYawIn, float targetPitchIn, boolean playVanillaSoundIn, boolean sendMessageIn, boolean safeSpawnIn) {
		this.dimension_key = dimensionKeyIn;
		this.direction = directionIn;
		this.target_pos = targetPosIn;
		this.target_yaw = targetYawIn;
		this.target_pitch = targetPitchIn;
		this.playVanillaSound = playVanillaSoundIn;
		this.sendMessage = sendMessageIn;
		this.safeSpawn = safeSpawnIn;
	}

	public BlockPos getTargetPos() {
		return this.target_pos;
	}
	
	public double[] getTargetPosA () {
		return new double[] { this.target_pos.getX(), this.target_pos.getY(), this.target_pos.getZ() };
	}

	public float getTargetYaw() {
		return this.target_yaw;
	}

	public float getTargetPitch() {
		return this.target_pitch;
	}
	
	public float[] getTargetRotation() {
		return new float[] { this.target_yaw, this.target_pitch };
	}

	public ResourceKey<Level> getDimensionKey() {
		return this.dimension_key;
	}

	public EnumShiftDirection getDirection() {
		return this.direction;
	}
	
	public static Shifter createTeleporter(ResourceKey<Level> dimensionKeyIn, EnumShiftDirection directionIn, BlockPos targetPosIn, float targetYawIn, float targetPitchIn, boolean playVanillaSoundIn, boolean sendMessageIn, boolean safeSpawnIn) {
		return new Shifter(dimensionKeyIn, directionIn, targetPosIn, targetYawIn, targetPitchIn, playVanillaSoundIn, sendMessageIn, safeSpawnIn);
	}
	
	public boolean playVanillaSound() {
		return this.playVanillaSound;
	}

	public boolean getSendMessage() {
		return this.sendMessage;
	}
	
	@Override
    public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld) {
    	return this.playVanillaSound;
    }

	@Override
	public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
		return repositionEntity.apply(false);
	}
	
    @Override
    public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
    	if (this.safeSpawn) {
    		EnumSafeTeleport spawnLocation = EnumSafeTeleport.getValidTeleportLocation(destWorld, this.target_pos);
    		
    		if (spawnLocation != EnumSafeTeleport.UNKNOWN) {
    			BlockPos resultPos = spawnLocation.toBlockPos();
    			BlockPos combinedPos = MathHelper.addBlockPos(this.target_pos, resultPos);
    			
    			return new PortalInfo(new Vec3(combinedPos.getX() + 0.5F, combinedPos.getY(), combinedPos.getZ() + 0.5F), Vec3.ZERO, this.getTargetYaw(), this.getTargetPitch());
    		}
    	}
    	
    	return new PortalInfo(new Vec3(target_pos.getX() + 0.5F, target_pos.getY(), target_pos.getZ() + 0.5F), Vec3.ZERO, this.getTargetYaw(), this.getTargetPitch());
    }
}