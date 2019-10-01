package com.zeher.dimensionalpockets.network.proxy;

import com.zeher.dimensionalpockets.core.command.CommandDimHelp;
import com.zeher.dimensionalpockets.core.command.CommandDimShift;
import com.zeher.dimensionalpockets.pocket.event.InsidePocketEvent;
import com.zeher.dimensionalpockets.pocket.event.PocketBlockEvent;
import com.zeher.zeherlib.api.interfaces.IProxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

public class CommonProxy implements IProxy {

	public void serverAboutToStart(FMLServerAboutToStartEvent event) { }
	
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandDimShift());
		event.registerServerCommand(new CommandDimHelp());
	}
	
	public void serverStarted(FMLServerStartedEvent event) { }
	
	public void serverStopping(FMLServerStoppingEvent event) { }

	@Override
	public void preInit() { }

	@Override
	public void init() { }

	@Override
	public void postInit() {
		MinecraftForge.EVENT_BUS.register(new PocketBlockEvent());
		MinecraftForge.EVENT_BUS.register(new InsidePocketEvent());
	}
}
