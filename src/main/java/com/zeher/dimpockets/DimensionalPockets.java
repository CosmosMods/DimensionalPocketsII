package com.zeher.dimpockets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zeher.dimpockets.core.manager.DimConfigManager;
import com.zeher.dimpockets.core.manager.ModDimensionManager;
import com.zeher.dimpockets.core.manager.RecipeHandler;
import com.zeher.dimpockets.pocket.core.manager.PocketEventManager;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DimensionalPockets.MOD_ID)
public final class DimensionalPockets {
	
	public static final String MOD_ID = "dimensionalpocketsii";
	
	// Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String LOGGER_PREFIX = "< " + MOD_ID + " >: ";
    
    public DimensionalPockets() {
    	FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onFMLCommonSetup);
    	FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onFMLClientSetup);
    	
    	MinecraftForge.EVENT_BUS.register(this);
    }
	
	public void onFMLCommonSetup(FMLCommonSetupEvent event){
		LOGGER.info(LOGGER_PREFIX + "[FMLCommonSetupEvent] CommonSetup...");
		
		RecipeHandler.preInitialization();
		
		//TileEntityHandler.preInitialization();
		//GameRegistry.registerWorldGenerator(new WorldGenerationHandler(), 0);
		//NetworkHandler.preInitialization();
		
		ModDimensionManager.registerDimensions();
		
		if(DimConfigManager.KEEP_POCKETS_CHUNKLOADED) {
			//ForgeChunkManager.setForcedChunkLoadingCallback(this, new ChunkLoaderManagerRoom());
			//ForgeChunkManager.setForcedChunkLoadingCallback(this, new ChunkLoaderManagerBlock());
		}
		
		MinecraftForge.EVENT_BUS.register(new PocketEventManager());
	}
	
	
	public void onFMLClientSetup(FMLClientSetupEvent event) {
		LOGGER.info(LOGGER_PREFIX + "[FMLClientSetupEvent] ClientSetup...");
		
	}
}