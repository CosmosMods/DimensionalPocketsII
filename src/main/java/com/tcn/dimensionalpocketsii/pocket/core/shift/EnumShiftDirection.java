package com.tcn.dimensionalpocketsii.pocket.core.shift;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.impl.colour.ChatColour;
import com.tcn.dimensionalpocketsii.core.management.CoreSoundHandler;

import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public enum EnumShiftDirection {
	
	ENTER(0, "enter", "Leave", "Leaving", "pocket.direction.enter", ChatColour.BOLD + ChatColour.GREEN, CoreSoundHandler.GENERIC.PORTAL_IN),
	LEAVE(1, "leave", "Enter", "Entering", "pocket.direction.leave", ChatColour.BOLD + ChatColour.ORANGE, CoreSoundHandler.GENERIC.PORTAL_OUT),
	UNKNOWN(-1, "unknown", "Unknown", "Unknown", "pocket.direction.unknown", ChatColour.RED, null);
	
	private final int index;
	private final String name;
	private final String display_name;
	private final String use_name;
	private final String localized_name;
	private final String display_colour;
	private final SoundEvent sound;
	
	private static final EnumShiftDirection[] VALUES = values();
	
	EnumShiftDirection(int indexIn, String nameIn, String displayNameIn, String useNameIn, String localizedNameIn, String displayColourIn, @Nullable SoundEvent soundIn) {
		this.index = indexIn;
		this.name = nameIn;
		this.display_name = displayNameIn;
		this.use_name = useNameIn;
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

	public String getUseName() {
		return this.use_name;
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

	public String getDisplayColour() {
		return display_colour;
	}

	public static EnumShiftDirection[] getValues() {
		return VALUES;
	}
	
	public String getChatStringForDirection() {
		return ChatColour.CYAN + "You are now: " + this.display_colour + this.use_name + ChatColour.END + ChatColour.CYAN + " the Pocket Dimension." + ChatColour.END;
	}

	public ITextComponent getChatComponentForDirection() {
		return new StringTextComponent(this.getChatStringForDirection());
	}

	public SoundEvent getSound() {
		return this.sound;
	}
}
