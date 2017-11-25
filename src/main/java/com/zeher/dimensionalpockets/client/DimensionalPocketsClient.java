package com.zeher.dimensionalpockets.client;

import com.zeher.dimensionalpockets.DimensionalPockets;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@SideOnly(Side.CLIENT)
public class DimensionalPocketsClient extends DimensionalPockets implements IResourceManagerReloadListener {

	public static DimensionalPocketsClient instance;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
	}
	
	@EventHandler
	public void Init(FMLPreInitializationEvent event) {
		
	}
	
	@EventHandler
	public void postInit(FMLPreInitializationEvent event) {
		
	}

	@Override
	public void onResourceManagerReload(IResourceManager p_110549_1_) {}

}
