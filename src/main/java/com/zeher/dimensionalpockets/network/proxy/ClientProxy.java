package com.zeher.dimensionalpockets.network.proxy;

import com.zeher.dimensionalpockets.core.handler.TileEntityHandler;
import com.zeher.dimensionalpockets.pocket.event.PocketEventHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy {
	
	@Override
	@SideOnly(Side.CLIENT)
	public void preInitialization() {
		TileEntityHandler.clientPreInitialization();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void initialization() {
		MinecraftForge.EVENT_BUS.register(new PocketEventHandler());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void postInitialization() {
		
	}
}
