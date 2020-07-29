package com.zeher.zeherlib.api.compat.core.interfaces;

import com.zeher.zeherlib.api.client.util.TextHelper;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.IStringSerializable;

public enum EnumConnectionType implements IStringSerializable {
	
	SCREEN(0, "screen", "connection_type.screen.name", 0xB5B5B5, TextHelper.TEAL),
	ENERGY(1, "energy", "connection_type.energy.name", 0x9700FF, TextHelper.PURPLE),
	FLUID(2, "fluid", "connection_type.fluid.name", 0x00AAFF, TextHelper.LIGHT_BLUE),
	ITEM(3, "item", "connection_type.item.name", 0xF7FF00, TextHelper.YELLOW);

	private final int index;
	private final String basic_name;
	private final String display_name;
	private final int gui_colour;
	private final String text_colour;
	
	/** All states in [SCREEN-ENERGY-FLUID-ITEM] order. */
	public static final EnumConnectionType[] VALUES = new EnumConnectionType[4];
	
	/**
	 * Enum for specification of types.
	 * @param indexIn [numbered index of mode]
	 * @param basic_nameIn [registry name of mode]
	 * @param display_nameIn [display name of mode for use in guis]
	 * @param gui_colourIn [integer value of mode for use in guis]
	 * @param text_colourIn [string value used for display of text in-gui]
	 */
	private EnumConnectionType(int indexIn, String basic_nameIn, String display_nameIn, int gui_colourIn, String text_colourIn) {
		this.index = indexIn;
		this.basic_name = basic_nameIn;
		this.display_name = display_nameIn;
		this.gui_colour = gui_colourIn;
		this.text_colour = text_colourIn;
	}
	
	/**
	 * Get the index of this EnumConnectionType. Order is [NO_CONN-SCREEN-ENERGY-FLUID-ITEM].
	 */
	public int getIndex() {
		return this.index;
	}
	
	/**
	 * Get the basic_name of this EnumConnectionType.
	 */
	public String getName() {
		return this.toString();
	}
	
	public String toString() {
        return this.basic_name;
    }
	
	/**
	 * Get the localised name for display.
	 */
	public String getDisplayName() {
		return I18n.format(display_name).toString();
	}
	
	/**
	 * Get the display_name.
	 */
	public String getDisplayNameUnformatted() {
		return this.display_name;
	}
	
	/**
	 * Get the gui_colour.
	 */
	public int getGuiColour() {
		return this.gui_colour;
	}
	
	/**
	 * Get the colour for displayed text.
	 */
	public String getTextColour() {
		return this.text_colour;
	}
	
	
	public static EnumConnectionType getStandardValue() {
		return SCREEN;
	}
	
	/**
	 * Returns the next state.
	 */
	public EnumConnectionType getNextState() {
		switch(this) {
			case SCREEN:
				return ENERGY;
			case ENERGY:
				return FLUID;
			case FLUID:
				return ITEM;
			case ITEM:
				return SCREEN;
			default:
				throw new IllegalStateException("Unable to obtain next state of [" + this + "]");
		}
	}
	
	/**
	 * Returns the next state from a given state.
	 * @param previous [state from]
	 */
	public static EnumConnectionType getNextStateFromState(EnumConnectionType previous) {
		switch (previous) {
			case SCREEN:
				return ENERGY;
			case ENERGY:
				return FLUID;
			case FLUID:
				return ITEM;
			case ITEM:
				return SCREEN;
			default:
				throw new IllegalStateException("Unable to obtain next state of [" + previous + "]");
		}
	}
	
	/**
	 * Returns the state from the given index.
	 * @param index
	 */
	public static EnumConnectionType getStateFromIndex(int index) {
		switch (index) {
			case 0:
				return SCREEN;
			case 1:
				return ENERGY;
			case 2:
				return FLUID;
			case 3: 
				return ITEM;
			default:
				throw new IllegalStateException("No EnumConnectionType exists with index: [" + index + "]");
		}
	}
}