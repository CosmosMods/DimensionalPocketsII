package com.zeher.zeherlib.api.compat.core.interfaces;

import com.zeher.zeherlib.api.client.util.TextHelper;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.IStringSerializable;

public enum EnumSideState implements IStringSerializable {

	INTERFACE_NORMAL(0, "interface_normal", "side_state.interface_normal.name", 0x0000FF, TextHelper.LIGHT_GRAY),
	INTERFACE_OUTPUT(1, "interface_output", "side_state.interface_output.name", 0x00FF00, TextHelper.GREEN),
	INTERFACE_INPUT(2, "interface_input", "side_state.interface_input.name", 0x2ECCFA, TextHelper.LIGHT_BLUE),
	DISABLED(3, "disabled", "side_state.disabled.name", 0xFF0000, TextHelper.GRAY);

	private final int index;
	private final String basic_name;
	private final String display_name;
	private final int gui_colour;
	private final String text_colour;
	
	/** All states in [NO_CONN-INTERFACE_NORMAL-INTERFACE_OUTPUT-INTERFACE_INPUT-DISABLED] order. */
	public static final EnumSideState[] VALUES = new EnumSideState[4];
	
	/**
	 * Enum for specification of modes.
	 * @param indexIn [numbered index of mode]
	 * @param basic_nameIn [registry name of mode]
	 * @param display_nameIn [display name of mode for use in guis]
	 * @param gui_colourIn [integer value of mode for use in guis]
	 * @param text_colourIn [string value used for display of text in-gui]
	 */
	private EnumSideState(int indexIn, String basic_nameIn, String display_nameIn, int gui_colourIn, String text_colourIn) {
		this.index = indexIn;
		this.basic_name = basic_nameIn;
		this.display_name = display_nameIn;
		this.gui_colour = gui_colourIn;
		this.text_colour = text_colourIn;
	}
	
	/**
	 * Get the index of this EnumSideState. Order is [NO_CONN-INTERFACE_NORMAL-INTERFACE_OUTPUT-INTERFACE_INPUT-DISABLED].
	 */
	public int getIndex() {
		return this.index;
	}
	
	/**
	 * Get the basic_name of this EnumSideState.
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
	
	/**
	 * Returns the standard array [NO_CONN[5]].
	 */
	public static EnumSideState[] getStandardArray() {
		return new EnumSideState[] { INTERFACE_NORMAL, INTERFACE_NORMAL, INTERFACE_NORMAL, INTERFACE_NORMAL, INTERFACE_NORMAL, INTERFACE_NORMAL };
	}
	
	/**
	 * Returns the next state.
	 */
	public EnumSideState getNextState() {
		switch(this) {
			case INTERFACE_NORMAL:
				return INTERFACE_OUTPUT;
			case INTERFACE_OUTPUT:
				return INTERFACE_INPUT;
			case INTERFACE_INPUT:
				return DISABLED;
			case DISABLED:
				return INTERFACE_NORMAL;
			default:
				throw new IllegalStateException("Unable to obtain next state of [" + this + "]");
		}
	}
	
	/**
	 * Returns the next state from a given state.
	 * @param previous [state from]
	 */
	public static EnumSideState getNextStateFromState(EnumSideState previous) {
		switch (previous) {
			case INTERFACE_NORMAL:
				return INTERFACE_OUTPUT;
			case INTERFACE_OUTPUT:
				return INTERFACE_INPUT;
			case INTERFACE_INPUT:
				return DISABLED;
			case DISABLED:
				return INTERFACE_NORMAL;
			default:
				throw new IllegalStateException("Unable to obtain next state of [" + previous + "]");
		}
	}
	
	/**
	 * Returns the state from the given index.
	 * @param index
	 */
	public static EnumSideState getStateFromIndex(int index) {
		switch (index) {
			case 0:
				return INTERFACE_NORMAL;
			case 1:
				return INTERFACE_OUTPUT;
			case 2:
				return INTERFACE_INPUT;
			case 3: 
				return DISABLED;
			default:
				throw new IllegalStateException("No EnumSideState exists with index: [" + index + "]");
		}
	}
}