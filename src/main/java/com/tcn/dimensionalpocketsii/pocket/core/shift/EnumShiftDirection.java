package com.tcn.dimensionalpocketsii.pocket.core.shift;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.common.comp.CosmosColour;
import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper;
import com.tcn.dimensionalpocketsii.core.management.CoreSoundHandler;

import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;

public enum EnumShiftDirection {
	
	ENTER(0, "enter", "Enter", "dimensionalpocketsii.pocket.direction.enter", CosmosColour.GREEN, CoreSoundHandler.GENERIC.PORTAL),
	LEAVE(1, "leave", "Leave", "dimensionalpocketsii.pocket.direction.leave", CosmosColour.ORANGE, CoreSoundHandler.GENERIC.PORTAL),
	GENERIC(2, "generic", "Generic", "dimensionalpocketsii.pocket.direcion.generic", CosmosColour.CYAN, SoundEvents.PORTAL_TRAVEL),
	UNKNOWN(-1, "unknown", "Unknown", "dimensionalpocketsii.pocket.direction.unknown", CosmosColour.RED, null);
	
	private final int index;
	private final String name;
	private final String display_name;
	private final String localized_name;
	private final CosmosColour display_colour;
	private final SoundEvent sound;
	
	private static final EnumShiftDirection[] VALUES = values();
	
	EnumShiftDirection(int indexIn, String nameIn, String displayNameIn, String localizedNameIn, CosmosColour displayColourIn, @Nullable SoundEvent soundIn) {
		this.index = indexIn;
		this.name = nameIn;
		this.display_name = displayNameIn;
		this.localized_name = localizedNameIn;
		this.display_colour = displayColourIn;
		this.sound = soundIn;
	}

	public int getIndex() {
		return this.index;
	}

	public String getName() {
		return this.name;
	}

	public String getDisplayName() {
		return this.display_name;
	}

	public ITextComponent getUseName() {
		return CosmosCompHelper.locComp(display_colour, true, this.localized_name);
	}

	public String getLocalizedName() {
		return this.localized_name;
	}
	
	public static EnumShiftDirection getDirectionFromIndex(int index) {
		switch (index) {
			case 0:
				return ENTER;
			case 1:
				return LEAVE;
			default:
				return UNKNOWN;
		}
	}

	public CosmosColour getDisplayColour() {
		return display_colour;
	}

	public static EnumShiftDirection[] getValues() {
		return VALUES;
	}
	
	public IFormattableTextComponent getChatComponentForDirection() {
		return CosmosCompHelper.locComp(CosmosColour.CYAN, false, "dimensionalpocketsii.pocket.direction.pre").append(getUseName()).append(CosmosCompHelper.locComp(CosmosColour.CYAN, false, "dimensionalpocketsii.pocket.direction.suff"));
	}

	public SoundEvent getSound() {
		return this.sound;
	}
}
