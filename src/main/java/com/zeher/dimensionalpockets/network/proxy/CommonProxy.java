package com.zeher.dimensionalpockets.network.proxy;

import com.zeher.dimensionalpockets.core.command.CommandDimHelp;
import com.zeher.dimensionalpockets.core.command.CommandDimShift;
import com.zeher.dimensionalpockets.core.command.CommandRecoverPocket;
import com.zeher.dimensionalpockets.pocket.event.PocketEventHandler;
import com.zeher.zeherlib.core.network.proxy.IProxy;

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
		event.registerServerCommand(new CommandRecoverPocket());
	}
	
	public void serverStarted(FMLServerStartedEvent event) { }
	
	public void serverStopping(FMLServerStoppingEvent event) { }
	
	@Override
	public void preInitialization() { }

	@Override
	public void initialization() { }

	@Override
	public void postInitialization() { 
		MinecraftForge.EVENT_BUS.register(new PocketEventHandler());
	}
}
