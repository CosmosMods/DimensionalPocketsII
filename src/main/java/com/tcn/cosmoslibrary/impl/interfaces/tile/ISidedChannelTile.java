package com.tcn.cosmoslibrary.impl.interfaces.tile;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.impl.enums.EnumChannelSideState;

import net.minecraft.util.Direction;

/**
 * Standard interface for use on blocks with configurable sides.
 * Contains information of the state of each side in an {@link EnumChannelSideState} array.
 */
public interface ISidedChannelTile {

	/**
	 * Array of side-states for a given side {@link EnumChannelSideState}. Order is always [D-U-N-S-W-E].
	 */
	public EnumChannelSideState[] SIDE_STATE_ARRAY = EnumChannelSideState.getStandardArray();
	
	/**
	 * 
	 * @param facing [the side to get from]
	 * @returns EnumChannelState [the state of that side]
	 */
	public EnumChannelSideState getSide(@Nullable Direction facing);
	
	/**
	 * Sets the specified side in the array.
	 * @param facing [the side to be set]
	 * @param side_state [the state to be set]
	 */
	public void setSide(@Nullable Direction facing, EnumChannelSideState side_state);
	
	/**
	 * Returns the SIDE_STATE_ARRAY.
	 * @returns {@link EnumChannelSideState} [the array]
	 */
	public EnumChannelSideState[] getSideArray();
	
	/**
	 * Sets the entire SIDE_STATE_ARRAY.
	 * @param new_array [the new array].
	 */
	public void setSideArray(EnumChannelSideState[] new_array);

	/**
	 * Cycles the specified side.
	 * @param facing [the side to be cycled]
	 */
	public void cycleSide(@Nullable Direction facing);
	
	/**
	 * Returns true or false given the current side-state
	 * @param facing [the side being checked]
	 * @returns boolean [true or false]
	 */
	public boolean canConnect(Direction facing);
	
	/**
	 * Method to correctly apply block update to show changes.
	 */
	public void updateRenders();
	
}