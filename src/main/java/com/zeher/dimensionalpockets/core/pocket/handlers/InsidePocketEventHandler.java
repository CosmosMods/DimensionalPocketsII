package com.zeher.dimensionalpockets.core.pocket.handlers;

import com.zeher.dimensionalpockets.DimensionalPockets;

import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class InsidePocketEventHandler {

	@SubscribeEvent
	public void onEnderTeleport(EnderTeleportEvent event) {
		//Disable ender-pearl functionality
		if (event.getEntityLiving().world.provider.getDimension() == DimensionalPockets.dimension_id) {
			event.setCanceled(true);
		}
	}
}
