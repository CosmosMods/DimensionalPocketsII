package com.zeher.dimensionalpockets.core.pocket;

import java.util.EnumMap;

import com.zeher.dimensionalpockets.core.util.Colour;

import net.minecraft.util.text.translation.I18n;

public enum PocketSideState {
	NONE(Colour.WHITE),
	// used for RF & Items atm.
	ENERGY(Colour.GREEN, "Basic");

	public static PocketSideState[] TEXTURED_STATES = new PocketSideState[] { ENERGY };

	private static EnumMap<PocketSideState, Colour> stateColours;

	static {
		stateColours = new EnumMap<>(PocketSideState.class);
		for (PocketSideState state : values()) {
			stateColours.put(state, state.colour);
		}
	}

	private Colour colour;
	private String textureName;

	PocketSideState(Colour colour) {
		this(colour, "");
	}

	PocketSideState(Colour colour, String textureName) {
		this.colour = colour.copy();
		this.colour.a = 0.392F;
		this.textureName = textureName;
	}

	public String getTextureName() {
		return textureName;
	}

	public Colour getColour() {
		return colour;
	}

}
