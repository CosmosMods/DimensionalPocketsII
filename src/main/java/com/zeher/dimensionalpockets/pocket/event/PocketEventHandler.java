package com.zeher.dimensionalpockets.pocket.event;

import com.zeher.dimensionalpockets.DimensionalPockets;

import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID)
public class PocketEventHandler {
	
	@SubscribeEvent
	public void onEnderTeleport(EnderTeleportEvent event) {
		//Disable ender-pearl functionality
		if (event.getEntityLiving().world.provider.getDimension() == DimensionalPockets.DIMENSION_ID) {
			event.setCanceled(true);
		}
	}
}
