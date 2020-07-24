package com.zeher.dimpockets.core.network.proxy;

import com.zeher.dimpockets.core.command.CommandDimHelp;
import com.zeher.dimpockets.core.command.CommandDimShift;
import com.zeher.dimpockets.core.command.CommandRecoverPocket;
import com.zeher.dimpockets.core.command.CommandRecoverPocketAdmin;
import com.zeher.dimpockets.core.command.CommandSetSpawn;
import com.zeher.dimpockets.pocket.core.manager.PocketEventManager;
import com.zeher.zeherlib.core.network.proxy.IProxy;

import net.minecraftforge.common.MinecraftForge;
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