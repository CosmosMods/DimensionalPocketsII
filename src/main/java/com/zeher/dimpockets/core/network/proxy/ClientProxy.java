package com.zeher.dimpockets.core.network.proxy;

import com.zeher.dimpockets.core.manager.ModTileEntityManager;
import com.zeher.dimpockets.pocket.core.manager.PocketEventManager;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy {
	
	@Override
	@SideOnly(Side.CLIENT)
	public void preInitialization() {
		ModTileEntityManager.clientPreInitialization();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void initialization() {
		MinecraftForge.EVENT_BUS.register(new PocketEventManager());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void postInitialization() { }
}