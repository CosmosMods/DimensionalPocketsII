package com.zeher.dimensionalpockets.network.proxy;

import com.zeher.dimensionalpockets.core.handler.TileEntityHandler;
import com.zeher.dimensionalpockets.pocket.event.PocketBlockEvent;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy {
	
	@Override
	@SideOnly(Side.CLIENT)
	public void preInit() {
		TileEntityHandler.clientPreInit();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void init() {
		MinecraftForge.EVENT_BUS.register(new PocketBlockEvent());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void postInit() {
		
	}
}
