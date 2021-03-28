package com.tcn.dimensionalpocketsii;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tcn.dimensionalpocketsii.core.advancement.CoreTriggers;
import com.tcn.dimensionalpocketsii.core.management.CoreConfigurationManager;
import com.tcn.dimensionalpocketsii.core.management.CoreEventManager;
import com.tcn.dimensionalpocketsii.core.management.CoreModBusManager;
import com.tcn.dimensionalpocketsii.core.management.CoreNetworkManager;
import com.tcn.dimensionalpocketsii.core.management.CoreRecipeHandler;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketNetworkManager;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DimensionalPockets.MOD_ID)
public final class DimensionalPockets {

	public static final String MOD_ID = "dimensionalpocketsii";
	public static final Logger LOGGER = LogManager.getLogger();
	
	public DimensionalPockets() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onFMLCommonSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onFMLClientSetup);
		MinecraftForge.EVENT_BUS.register(this);
		
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CoreConfigurationManager.spec, "dimensionalpockets-common.toml");
	}

	public void onFMLCommonSetup(FMLCommonSetupEvent event) {
		CoreRecipeHandler.preInitialization();
		CriteriaTriggers.register(CoreTriggers.USE_SHIFTER_TRIGGER);
		CoreEventManager.registerOresForGeneration();
		
		CoreNetworkManager.register();
		PocketNetworkManager.register();
		
		if (CoreConfigurationManager.getInstance().getKeepChunksLoaded()) {
			//ForgeChunkManager.setForcedChunkLoadingCallback(MOD_ID, callback);
			
			//ForgeChunkManager.setForcedChunkLoadingCallback(this, new ChunkLoaderManagerRoom());
			//ForgeChunkManager.setForcedChunkLoadingCallback(this, new ChunkLoaderManagerBlock());
		}
		
		LOGGER.info("FMLCommonSetup complete.");
	}

	public void onFMLClientSetup(FMLClientSetupEvent event) {
		final ModLoadingContext context = ModLoadingContext.get();
		
		CoreModBusManager.registerClient(context);
		CoreModBusManager.onFMLClientSetup(event);
		
		LOGGER.info("FMLClientSetup complete.");
	}
	
}