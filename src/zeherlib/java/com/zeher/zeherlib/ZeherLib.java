package com.zeher.zeherlib;

import com.zeher.zeherlib.core.network.proxy.IProxy;
import com.zeher.zeherlib.core.network.proxy.ProxyCommon;

import cofh.redstoneflux.RedstoneFluxProps;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


@Mod(modid = ZeherLib.MOD_ID, name = ZeherLib.MOD_NAME, version = ZeherLib.MOD_VERSION, dependencies = ZLReference.DEPENDENCY.FORGE_DEP)
public class ZeherLib {

	public static final String MOD_ID = "zeherlib";
	public static final String MOD_NAME = "ZeherLib";
	public static final String MOD_VERSION = "7.0.14";
	public static final String MOD_VERSION_MAX = "7.1.0";
	public static final String MOD_DEPENDENCIES = ZLReference.DEPENDENCY.FORGE_DEP;
	
	public static final String VERSION_GROUP = "required-after:" + MOD_ID + "@[" + MOD_VERSION + "," + MOD_VERSION_MAX + "];";
	
	@Instance(MOD_ID)
	public static ZeherLib INSTANCE;
	
	@SidedProxy(clientSide = "com.zeher.zeherlib.core.network.proxy.ProxyClient", serverSide = "com.zeher.zeherlib.core.network.proxy.ProxyCommon")
	public static ProxyCommon COMMON_PROXY;
	public static IProxy IPROXY;
	
	@EventHandler
	public void preInitialization(FMLPreInitializationEvent event){
		COMMON_PROXY.preInitialization();
	}
	
	@EventHandler
	public void initialization(FMLInitializationEvent event){
		COMMON_PROXY.initialization();
	}
	
	@EventHandler
	public void postInitialization(FMLPostInitializationEvent event){
		COMMON_PROXY.postInitialization();
	}
}