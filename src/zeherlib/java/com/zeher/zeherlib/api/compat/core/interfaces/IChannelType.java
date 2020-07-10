package com.zeher.zeherlib.api.compat.core.interfaces;

import net.minecraft.util.EnumFacing;

public class IChannelType {

	public interface IChannelEnergy {

		/**
		 * Value added to stop back-feeding and infinite power loops. REV: IEnergyPipe.
		 */
		public EnumFacing last_facing = null;
		
		/**
		 * Value added to control RF tick rate through channels.
		 */
		public int last_rf_rate = 0;
		
		/**
		 * Returns the last_facing value.
		 * @returns {@link EnumFacing}
		 */
		public EnumFacing getLastFacing();
		
		/**
		 * Sets the last_facing value
		 * @param facing [{@link EnumFacing} value to be set]
		 */
		public void setLastFacing(EnumFacing facing);
		
		/**
		 * Returns the last_rf_rate value.
		 * @returns int
		 */
		public int getLastRFRate();
		
		/**
		 * Sets the last_rf_rate value
		 * @param value [ value to be set]
		 */
		public void setLastRFRate(int value);
	}
	
	public interface IChannelFluid {

		/**
		 * Value added to stop back-feeding.
		 */
		public EnumFacing last_facing = null;
		
		/**
		 * Returns the last_facing value.
		 * @returns {@link EnumFacing}
		 */
		public EnumFacing getLastFacing();
		
		/**
		 * Sets the last_facing value
		 * @param facing [{@link EnumFacing} value to be set]
		 */
		public void setLastFacing(EnumFacing facing);
	}
	
	public interface IChannelItem {

		/**
		 * Value added to stop back-feeding.
		 */
		public EnumFacing last_facing = null;
		
		/**
		 * Returns the last_facing value.
		 * @returns {@link EnumFacing}
		 */
		public EnumFacing getLastFacing();
		
		/**
		 * Sets the last_facing value
		 * @param facing [{@link EnumFacing} value to be set]
		 */
		public void setLastFacing(EnumFacing facing);
	}
}