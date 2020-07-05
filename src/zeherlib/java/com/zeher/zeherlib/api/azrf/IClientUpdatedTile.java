package com.zeher.zeherlib.api.azrf;

/**
 * Used on tiles that require updates for guis etc.
 */
public interface IClientUpdatedTile {

	/**
	 * Returns the current process progress scaled for display.
	 * @param scale [scale inside the Gui]
	 * @return energy [scaled as required]
	 */
	public int getEnergyScaled(int scale);
	
	/**
	 * Returns whether or not the tile has energy.
	 */
	public boolean hasEnergy();
	
	/**
	 * Returns the processing speed of this tile.
	 */
	public int getProcessSpeed();
	
	/**
	 * Returns the time it takes to process.
	 * @param i [number of upgrades]
	 */
	public int getProcessTime(int i);
	
	/**
	 * Returns the current process progress scaled for display.
	 * @param scale [scale inside the Gui]
	 */
	public int getProcessProgressScaled(int scale);
	
	/**
	 * Returns whether the tile can process or not.
	 */
	public boolean canProcess();
}