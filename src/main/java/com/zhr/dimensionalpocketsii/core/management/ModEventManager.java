package com.zhr.dimensionalpocketsii.core.management;

import com.zhr.dimensionalpocketsii.DimensionalPockets;
import com.zhr.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventManager {

	@SubscribeEvent
	public void onPlayerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
		clearMap();
	}
	
	@OnlyIn(Dist.CLIENT)
	public void clearMap() {
		PocketRegistryManager.clearMap();
	}
}
