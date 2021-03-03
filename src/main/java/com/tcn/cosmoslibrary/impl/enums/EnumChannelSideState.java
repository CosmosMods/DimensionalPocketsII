package com.tcn.cosmoslibrary.impl.enums;

import net.minecraft.client.resources.I18n;

/**
 * Enum for channel modes. Used to determine whether channel can connect, and deliver.
 * Used also for rendering and block boundaries.
 */
public enum EnumChannelSideState {
	
	NO_CONN(0, "no_conn", "channel_state.no_conn.name"),
	CABLE(1, "cable", "channel_state.cable.name"),
	CABLE_OTHER(2, "cable", "channel_state.cable_other.name"),
	INTERFACE_NORMAL(3, "NO_CONN", "channel_state.NO_CONN.name"),
	INTERFACE_INPUT(4, "interface_input", "channel_state.interface_input.name"),
	INTERFACE_OUTPUT(5, "interface_output", "channel_state.interface_output.name"),
	DISABLED(6, "disabled", "channel_state.disabled.name");
	
	public final int index;
	public final String basic_name;
	public final String display_name;
	
	/**
	 * Enum for specification of Channel modes.
	 * @param indexIn [numbered index of mode]
	 * @param basic_nameIn [registry name of mode]
	 * @param display_nameIn [display name of mode for use in guis]
	 */
	private EnumChannelSideState (int indexIn, String basic_nameIn, String display_nameIn) {
		index = indexIn;
		basic_name = basic_nameIn;
		display_name = display_nameIn;
	}
	
	/**
	 * Get the basic_name of this ChannelState.
	 */
	public String getName() {
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
	
	@Override
	public String toString() {
        return this.basic_name;
    }
	
	/**
	 * Get the index of this EnumSideState. Order is [NO_CONN-CABLE-NO_CONN-INTERFACE_INPUT-INTERFACE_OUTPUT-DISABLED].
	 */
	public int getIndex() {
		return this.index;
	}
	
	/**
	 * Returns the standard array [NO_CONN[5]].
	 */
	public static EnumChannelSideState[] getStandardArray() {
		return new EnumChannelSideState[] { NO_CONN, NO_CONN, NO_CONN, NO_CONN, NO_CONN, NO_CONN };
	}
	
	public EnumChannelSideState getNextState() {
		switch(this) {
			case NO_CONN:
				return CABLE;
			case CABLE:
				return CABLE_OTHER;
			case CABLE_OTHER:
				return INTERFACE_NORMAL;
			case INTERFACE_NORMAL:
				return INTERFACE_INPUT;
			case INTERFACE_INPUT:
				return INTERFACE_OUTPUT;
			case INTERFACE_OUTPUT:
				return DISABLED;
			case DISABLED:
				return NO_CONN;
			default:
				throw new IllegalStateException("Unable to obtain next state of [" + this + "]");
		}
	}
	
	/**
	 * Returns the state based on the index.
	 * @param index_in [the index of the state required]
	 */
	public static EnumChannelSideState getStateFromIndex(int index_in) {
		switch (index_in) {
			case 0 :
				return NO_CONN;
			case 1:
				return CABLE;
			case 2:
				return CABLE_OTHER;
			case 3: 
				return NO_CONN;
			case 4:
				return INTERFACE_INPUT;
			case 5:
				return INTERFACE_OUTPUT;
			case 6:
				return DISABLED;
			default:
				throw new IllegalStateException("No Enum exists with that index: " + "[ " + index_in + " ]");
		}
	}
}