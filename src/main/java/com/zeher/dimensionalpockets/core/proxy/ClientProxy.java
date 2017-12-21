package com.zeher.dimensionalpockets.core.proxy;

import com.zeher.dimensionalpockets.core.handlers.BlockHandler;
import com.zeher.dimensionalpockets.core.handlers.ItemHandler;
import com.zeher.dimensionalpockets.core.pocket.handlers.PocketBlockEventHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void init(FMLInitializationEvent event) {
		ItemHandler.registerModelLocations();
		BlockHandler.registerModelLocations();

		MinecraftForge.EVENT_BUS.register(new PocketBlockEventHandler());
	}
	
}
