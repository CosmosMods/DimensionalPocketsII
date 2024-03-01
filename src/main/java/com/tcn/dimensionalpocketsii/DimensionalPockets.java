package com.tcn.dimensionalpocketsii;

import com.tcn.cosmoslibrary.common.runtime.CosmosConsoleManager;
import com.tcn.dimensionalpocketsii.core.advancement.CoreTriggers;
import com.tcn.dimensionalpocketsii.core.management.ConfigurationManager;
import com.tcn.dimensionalpocketsii.core.management.ModBusManager;
import com.tcn.dimensionalpocketsii.core.management.NetworkManager;
import com.tcn.dimensionalpocketsii.core.management.RecipeManager;
import com.tcn.dimensionalpocketsii.core.management.SoundManager;
import com.tcn.dimensionalpocketsii.core.management.WorldGenManager;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketNetworkManager;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DimensionalPockets.MOD_ID)
public final class DimensionalPockets {

	//This must NEVER EVER EVER CHANGE!
	public static final String MOD_ID = "dimensionalpocketsii";
		
	public static CosmosConsoleManager CONSOLE = new CosmosConsoleManager(DimensionalPockets.MOD_ID, true, true);
	
	public DimensionalPockets() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		
		WorldGenManager.register(bus);
		RecipeManager.register(bus);
		ModBusManager.register(bus);
		SoundManager.register(bus);
		
		bus.addListener(this::onFMLCommonSetup);
		bus.addListener(this::onFMLClientSetup);
		
		MinecraftForge.EVENT_BUS.register(this);
		
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigurationManager.spec, "dimensionalpockets-common-rev-5.1.toml");
	}

	public void onFMLCommonSetup(final FMLCommonSetupEvent event) {
		CONSOLE = new CosmosConsoleManager(DimensionalPockets.MOD_ID, ConfigurationManager.getInstance().getDebugMessage(), ConfigurationManager.getInstance().getInfoMessage());
		
		event.enqueueWork(() -> {
			CriteriaTriggers.register(CoreTriggers.USE_SHIFTER_TRIGGER);
			
			NetworkManager.register();
			PocketNetworkManager.register();
		});
		
		CONSOLE.updateDebugEnabled(ConfigurationManager.getInstance().getDebugMessage());
		CONSOLE.updateInfoEnabled(ConfigurationManager.getInstance().getInfoMessage());
		
		CONSOLE.startup("DimensionalPocketsII Common Setup complete.");
	}
	
	public void onFMLClientSetup(final FMLClientSetupEvent event) {
		//IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		final ModLoadingContext context = ModLoadingContext.get();
				
		ModBusManager.registerClient(context);
		ModBusManager.onFMLClientSetup(event);
		
		CONSOLE.startup("Dimensional PocketsII Client Setup complete.");
	}
}