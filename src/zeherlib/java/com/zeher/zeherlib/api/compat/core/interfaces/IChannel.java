package com.zeher.zeherlib.api.compat.core.interfaces;

import net.minecraft.util.Direction;

/**
 * Collection of interfaces for use in the implementation of channels.
 * Not to be used on standard blocks.
 */
public class IChannel { 

	/**
	 * This is an extension of {@link ISidedChannelTile} for use on [energy] channels.
	 * This should not be used on standard blocks.
	 */
	public interface Energy extends ISidedChannelTile {
		
		/**
		 * Returns a {@link EnumChannelSideState} for connections to other channels.
		 */
		public EnumChannelSideState getStateForConnection(Direction facing);
	}
	
	/**
	 * This is an extension of {@link ISidedChannelTile} for use on [fluid] channels.
	 * This should not be used on standard blocks.
	 */
	public interface Fluid extends ISidedChannelTile {
		
		/**
		 * Returns a {@link EnumChannelSideState} for connections to other channels.
		 */
		public EnumChannelSideState getStateForConnection(Direction facing);
	}
	
	/**
	 * This is an extension of {@link ISidedChannelTile} for use on [item] channels.
	 * This should not be used on standard blocks.
	 */
	public interface Item extends ISidedChannelTile {
		
		/**
		 * Returns a {@link EnumChannelSideState} for connections to other channels.
		 */
		public EnumChannelSideState getStateForConnection(Direction facing);
	}
}