package com.zeher.zehercraft.core.network.proxy;

import com.zeher.zehercraft.core.handler.TileEntityHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy {
	
	@Override
	@SideOnly(Side.CLIENT)
	public void preInitialization() {
		TileEntityHandler.preInitializationClient();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void initialization() { }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void postInitialization() { }
}
