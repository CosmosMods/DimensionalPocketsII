package com.zeher.zeherlib.core.network.proxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Basic Proxy interface.
 */
public interface IProxy {
	
	/**
	 * Called by your main {@link Mod} class.
	 * Called during the {@link FMLPreInitializationEvent} phase.
	 */
	public void preInitialization();
	
	/**
	 * Called by your main {@link Mod} class.
	 * Called during the {@link FMLInitializationEvent} phase.
	 */
	public void initialization();
	
	/**
	 * Called by your main {@link Mod} class.
	 * Called during the {@link FMLPostInitializationEvent} phase.
	 */
	public void postInitialization();
	
}