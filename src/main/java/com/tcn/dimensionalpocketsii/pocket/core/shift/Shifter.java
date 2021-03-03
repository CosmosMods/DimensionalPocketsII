package com.tcn.dimensionalpocketsii.pocket.core.shift;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

public class Shifter implements ITeleporter {

	private RegistryKey<World> dimension_key;
	private EnumShiftDirection direction;
	private BlockPos target_pos;
	private float target_yaw;
	private float target_pitch;

	public Shifter(RegistryKey<World> dimensionKeyIn, EnumShiftDirection directionIn, BlockPos targetPosIn, float targetYawIn, float targetPitchIn) {
		this.dimension_key = dimensionKeyIn;
		this.direction = directionIn;
		this.target_pos = targetPosIn;
		this.target_yaw = targetYawIn;
		this.target_pitch = targetPitchIn;
	}

	public BlockPos getTargetPos() {
		return this.target_pos;
	}
	
	public int[] getTargetPosA () {
		return new int[] { this.target_pos.getX(), this.target_pos.getY(), this.target_pos.getZ() };
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
	
	public static Shifter createTeleporter(RegistryKey<World> dimensionKeyIn, EnumShiftDirection directionIn, BlockPos targetPosIn, float targetYawIn, float targetPitchIn) {
		return new Shifter(dimensionKeyIn, directionIn, targetPosIn, targetYawIn, targetPitchIn);
	}
	
}