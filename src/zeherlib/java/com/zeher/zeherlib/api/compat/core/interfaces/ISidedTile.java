package com.zeher.zeherlib.api.compat.core.interfaces;

import javax.annotation.Nullable;

import net.minecraft.util.Direction;

/**
 * Standard interface for use on blocks with configurable sides.
 * Contains information of the state of each side in an {@link EnumSideState} array.
 */
public interface ISidedTile {

	/**
	 * Array of side-states for a given side {@link EnumSideState}. Order is always [D-U-N-S-W-E].
	 */
	public EnumSideState[] SIDE_STATE_ARRAY = EnumSideState.getStandardArray();
	
	/**
	 * 
	 * @param facing [the side to get from]
	 * @returns EnumSideState [the state of that side]
	 */
	public EnumSideState getSide(@Nullable Direction facing);
	
	/**
	 * Sets the specified side in the array.
	 * @param facing [the side to be set]
	 * @param side_state [the state to be set]
	 */
	public void setSide(@Nullable Direction facing, EnumSideState side_state);
	
	/**
	 * Returns the SIDE_STATE_ARRAY.
	 * @returns {@link EnumSideState} [the array]
	 */
	public EnumSideState[] getSideArray();
	
	/**
	 * Sets the entire SIDE_STATE_ARRAY.
	 * @param new_array [the new array].
	 */
	public void setSideArray(EnumSideState[] new_array);

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
	public void sendUpdates();
	
}