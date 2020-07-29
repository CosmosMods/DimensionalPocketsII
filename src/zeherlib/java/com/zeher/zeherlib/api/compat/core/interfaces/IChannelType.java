package com.zeher.zeherlib.api.compat.core.interfaces;

import net.minecraft.util.Direction;

public class IChannelType {

	public interface IChannelEnergy {

		/**
		 * Value added to stop back-feeding and infinite power loops. REV: IEnergyPipe.
		 */
		public Direction last_facing = null;
		
		/**
		 * Value added to control RF tick rate through channels.
		 */
		public int last_rf_rate = 0;
		
		/**
		 * Returns the last_facing value.
		 * @returns {@link Direction}
		 */
		public Direction getLastFacing();
		
		/**
		 * Sets the last_facing value
		 * @param facing [{@link Direction} value to be set]
		 */
		public void setLastFacing(Direction facing);
		
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
		public Direction last_facing = null;
		
		/**
		 * Returns the last_facing value.
		 * @returns {@link Direction}
		 */
		public Direction getLastFacing();
		
		/**
		 * Sets the last_facing value
		 * @param facing [{@link Direction} value to be set]
		 */
		public void setLastFacing(Direction facing);
	}
	
	public interface IChannelItem {

		/**
		 * Value added to stop back-feeding.
		 */
		public Direction last_facing = null;
		
		/**
		 * Returns the last_facing value.
		 * @returns {@link Direction}
		 */
		public Direction getLastFacing();
		
		/**
		 * Sets the last_facing value
		 * @param facing [{@link Direction} value to be set]
		 */
		public void setLastFacing(Direction facing);
	}
}