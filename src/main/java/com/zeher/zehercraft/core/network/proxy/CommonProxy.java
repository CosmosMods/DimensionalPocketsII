package com.zeher.zehercraft.core.network.proxy;

import com.zeher.zeherlib.core.network.proxy.IProxy;

import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

public class CommonProxy implements IProxy {

	public void serverAboutToStart(FMLServerAboutToStartEvent event) { }
	
	public void serverStarting(FMLServerStartingEvent event) { }
	
	public void serverStarted(FMLServerStartedEvent event) { }
	
	public void serverStopping(FMLServerStoppingEvent event) { }

	@Override
	public void preInitialization() { }

	@Override
	public void initialization() { }

	@Override
	public void postInitialization() { }
}