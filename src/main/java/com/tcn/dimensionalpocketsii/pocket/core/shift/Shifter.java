package com.tcn.dimensionalpocketsii.pocket.core.shift;

import java.util.function.Function;

import net.minecraft.block.PortalInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

public class Shifter implements ITeleporter {

	private RegistryKey<World> dimension_key;
	private EnumShiftDirection direction;
	private BlockPos target_pos;
	private float target_yaw;
	private float target_pitch;
	private boolean playVanillaSound;
	private boolean sendMessage;

	public Shifter(RegistryKey<World> dimensionKeyIn, EnumShiftDirection directionIn, BlockPos targetPosIn, float targetYawIn, float targetPitchIn, boolean playVanillaSoundIn, boolean sendMessage) {
		this.dimension_key = dimensionKeyIn;
		this.direction = directionIn;
		this.target_pos = targetPosIn;
		this.target_yaw = targetYawIn;
		this.target_pitch = targetPitchIn;
		this.playVanillaSound = playVanillaSoundIn;
		this.sendMessage = sendMessage;
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

	public RegistryKey<World> getDimensionKey() {
		return this.dimension_key;
	}

	public EnumShiftDirection getDirection() {
		return this.direction;
	}
	
	public static Shifter createTeleporter(RegistryKey<World> dimensionKeyIn, EnumShiftDirection directionIn, BlockPos targetPosIn, float targetYawIn, float targetPitchIn, boolean playVanillaSoundIn, boolean sendMessage) {
		return new Shifter(dimensionKeyIn, directionIn, targetPosIn, targetYawIn, targetPitchIn, playVanillaSoundIn, sendMessage);
	}
	
	public boolean playVanillaSound() {
		return this.playVanillaSound;
	}

	public boolean getSendMessage() {
		return this.sendMessage;
	}
	
	@Override
    public boolean playTeleportSound(ServerPlayerEntity player, ServerWorld sourceWorld, ServerWorld destWorld) {
    	return this.playVanillaSound;
    }
	
    @Override
    public PortalInfo getPortalInfo(Entity entity, ServerWorld destWorld, Function<ServerWorld, PortalInfo> defaultPortalInfo) {
    	return new PortalInfo(new Vector3d(target_pos.getX() + 0.5F, target_pos.getY(), target_pos.getZ() + 0.5F), Vector3d.ZERO, this.getTargetYaw(), this.getTargetPitch());
    }
}