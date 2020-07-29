package com.zeher.zeherlib.api.core.interfaces;

import net.minecraft.util.Direction;

/**
 * Used to allow rotatable tiles.
 */
public abstract interface IRotatableTile {
	
	/**
	 * Checks if the [object] can be rotated.
	 */
	public abstract boolean canRotate();

	/**
	 * Actually rotates the [object].
	 */
	public abstract void rotate();

	/**
	 * Used to retreive the current direction the [object] is facing. 
	 */
	public abstract Direction getDirectionFacing();
}
