package com.zeher.dimensionalpockets.core.pocket;

import net.minecraft.entity.player.EntityPlayer;

public class PocketShiftPreparation {
	public enum Direction {
		INTO_POCKET, OUT_OF_POCKET
	}

	private int ticksBeforeTeleport;
	private final EntityPlayer player;
	private final Pocket pocket;
	private final Direction direction;

	public PocketShiftPreparation(EntityPlayer player, int ticksBeforeTeleport, Pocket pocket, Direction direction) {
		this.player = player;
		this.ticksBeforeTeleport = ticksBeforeTeleport;
		this.pocket = pocket;
		this.direction = direction;
	}

	public boolean doPrepareTick() {
		if (ticksBeforeTeleport < 0)
			return true;

		ticksBeforeTeleport--;

		if (ticksBeforeTeleport <= 0) {
			if (direction == Direction.INTO_POCKET) {
				pocket.teleportTo(player);
			} else {
				pocket.teleportFrom(player);
			}
			return true;
		}
		return false;
	}
}
