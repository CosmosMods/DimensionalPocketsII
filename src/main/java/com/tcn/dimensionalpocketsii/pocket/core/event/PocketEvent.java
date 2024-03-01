package com.tcn.dimensionalpocketsii.pocket.core.event;

import com.tcn.dimensionalpocketsii.pocket.core.Pocket;

import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public class PocketEvent extends Event {

	private final Level level;
	private final Pocket pocket;
	
	public PocketEvent(Level levelIn, Pocket pocketIn) {
		this.level = levelIn;
		this.pocket = pocketIn;
	}
	
	public Level getLevel() {
		return this.level;
	}

	public Pocket getPocket() {
		return this.pocket;
	}

	@Cancelable
	public static class GeneratePocketEvent extends PocketEvent {

		public GeneratePocketEvent(Level levelIn, Pocket pocketIn) {
			super(levelIn, pocketIn);
		}
		
	}
}
