package com.tcn.dimensionalpocketsii.core.management;

import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CoreEventManager {

	@SubscribeEvent
	public void onPlayerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
		System.out.println("Success");
		
		clearMap();
	}
	
	@OnlyIn(Dist.CLIENT)
	public void clearMap() {
		PocketRegistryManager.clearPocketMap();
	}
}
