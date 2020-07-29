package com.zeher.zeherlib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * @author TheRealZeher
 */
@Mod("zeherlib")
public final class ZeherLib {

	public static final String MOD_ID = "zeherlib";
	
	// Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String LOGGER_PREFIX = "< " + MOD_ID + " >: ";
    
    public ZeherLib() {
    	FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
    }
	
	public void commonSetup(FMLCommonSetupEvent event){
		LOGGER.info(LOGGER_PREFIX + "[FMLCommonSetupEvent] PreInit...");
	}
	
	@SubscribeEvent
    public void onServerAboutToStart(FMLServerAboutToStartEvent event) {
		LOGGER.info(LOGGER_PREFIX + "[FMLServerAboutToStartEvent] Server about to start...");
    }
	
	@SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
		LOGGER.info(LOGGER_PREFIX + "[FMLServerStartingEvent] Server starting...");
    }
	
	@SubscribeEvent
    public void onServerStarted(FMLServerStartedEvent event) {
		LOGGER.info(LOGGER_PREFIX + "[FMLServerStartedEvent] Server started...");
    }
	
	@SubscribeEvent
    public void onServerStopping(FMLServerStoppingEvent event) {
		LOGGER.info(LOGGER_PREFIX + "[FMLServerStoppingEvent] Server stopping...");
    }
}