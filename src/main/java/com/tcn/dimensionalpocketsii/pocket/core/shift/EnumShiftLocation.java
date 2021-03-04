package com.tcn.dimensionalpocketsii.pocket.core.shift;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public enum EnumShiftLocation {
	UP(0, 1, 0),
	NORTH(0, 0, -1), NORTH_MINUS_ONE(0, -1, -1),
	SOUTH(0, 0, 1), SOUTH_MINUS_ONE(0, -1, 1),
	WEST(-1, 0, 0), WEST_MINUS_ONE(-1, -1, 0),
	EAST(1, 0, 0), EAST_MINUS_ONE(1, -1, 0),
	NORTHWEST(-1, 0, -1), NORTHWEST_MINUS_ONE(-1, -1, -1),
	NORTHEAST(1, 0, -1), NORTHEAST_MINUS_ONE(1, -1, -1),
	SOUTHWEST(-1, 0, 1), SOUTHWEST_MINUS_ONE(-1, -1, 1),
	SOUTHEAST(1, 0, 1), SOUTHEAST_MINUS_ONE(1, -1, 1),
	
	UP_TWO(0, 2, 0),
	NORTH_TWO(0, 0, -2), NORTH_MINUS_ONE_TWO(0, -2, -2),
	SOUTH_TWO(0, 0, 2), SOUTH_MINUS_ONE_TWO(0, -2, 2),
	WEST_TWO(-2, 0, 0), WEST_MINUS_ONE_TWO(-2, -2, 0),
	EAST_TWO(2, 0, 0), EAST_MINUS_ONE_TWO(2, -2, 0),
	NORTHWEST_TWO(-2, 0, -2), NORTHWEST_MINUS_ONE_TWO(-2, -2, -2),
	NORTHEAST_TWO(2, 0, -2), NORTHEAST_MINUS_ONE_TWO(2, -2, -2),
	SOUTHWEST_TWO(-2, 0, 2), SOUTHWEST_MINUS_ONE_TWO(-2, -2, 2),
	SOUTHEAST_TWO(2, 0, 2), SOUTHEAST_MINUS_ONE_TWO(2, -2, 2),
	UNKNOWN(0, 0, 0);

	public static final EnumShiftLocation[] VALID_DIRECTIONS = {
			UP, NORTH, SOUTH, WEST, EAST,
			NORTH_MINUS_ONE, SOUTH_MINUS_ONE, WEST_MINUS_ONE, EAST_MINUS_ONE,
			NORTHWEST, NORTHEAST, SOUTHWEST, SOUTHEAST,
			NORTHWEST_MINUS_ONE, NORTHEAST_MINUS_ONE, SOUTHWEST_MINUS_ONE, SOUTHEAST_MINUS_ONE,
			
			UP_TWO, NORTH_TWO, SOUTH_TWO, WEST_TWO, EAST_TWO,
			NORTH_MINUS_ONE_TWO, SOUTH_MINUS_ONE_TWO, WEST_MINUS_ONE_TWO, EAST_MINUS_ONE_TWO,
			NORTHWEST_TWO, NORTHEAST_TWO, SOUTHWEST_TWO, SOUTHEAST_TWO,
			NORTHWEST_MINUS_ONE_TWO, NORTHEAST_MINUS_ONE_TWO, SOUTHWEST_MINUS_ONE_TWO, SOUTHEAST_MINUS_ONE
	};

	public final int offsetX;
	public final int offsetY;
	public final int offsetZ;

	private EnumShiftLocation(int x, int y, int z) {
		offsetX = x;
		offsetY = y;
		offsetZ = z;
	}

	private static boolean isAir(World world, BlockPos pos) {
		return world.isAirBlock(pos);
	}

	public static EnumShiftLocation getValidTeleportLocation(World world, BlockPos pos) {
		for (EnumShiftLocation direction : VALID_DIRECTIONS) {
			if (isAir(world, new BlockPos(pos.getX() + direction.offsetX, pos.getY() + direction.offsetY, pos.getZ() + direction.offsetZ))) {
				return direction;
			}
		}
		return UNKNOWN;
	}

	public BlockPos toBlockPos() {
		return new BlockPos(offsetX, offsetY, offsetZ);
	}
}