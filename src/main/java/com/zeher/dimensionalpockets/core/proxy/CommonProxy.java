package com.zeher.dimensionalpockets.core.proxy;

import com.zeher.dimensionalpockets.core.command.CommandDimHelp;
import com.zeher.dimensionalpockets.core.command.CommandDimShift;
import com.zeher.dimensionalpockets.core.handlers.BlockHandler;
import com.zeher.dimensionalpockets.core.handlers.GuiHandler;
import com.zeher.dimensionalpockets.core.handlers.ItemHandler;
import com.zeher.dimensionalpockets.core.handlers.NetworkHandler;
import com.zeher.dimensionalpockets.core.handlers.RecipeHandler;
import com.zeher.dimensionalpockets.core.handlers.TileEntityHandler;
import com.zeher.dimensionalpockets.core.handlers.WorldGenerationHandler;
import com.zeher.dimensionalpockets.core.pocket.handlers.InsidePocketEventHandler;
import com.zeher.dimensionalpockets.core.pocket.handlers.PocketBlockEventHandler;
import com.zeher.trzlib.api.TRZIProxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy implements TRZIProxy {

	public void preInit(FMLPreInitializationEvent event) {
		ItemHandler.preInit();
		ItemHandler.register();

		BlockHandler.preInit();
		BlockHandler.register();

		RecipeHandler.registerRecipes();
		RecipeHandler.registerSmelting();

		TileEntityHandler.preInit();

		GameRegistry.registerWorldGenerator(new WorldGenerationHandler(), 0);

		NetworkHandler.preInit();
	}

	public void init(FMLInitializationEvent event) {
		new GuiHandler();
	}

	public void postInit(FMLPostInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new PocketBlockEventHandler());
		MinecraftForge.EVENT_BUS.register(new InsidePocketEventHandler());
	}
	
	public void serverAboutToStart(FMLServerAboutToStartEvent event) { }
	
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandDimShift());
		event.registerServerCommand(new CommandDimHelp());
	}
	
	public void serverStarted(FMLServerStartedEvent event) { }
	
	public void serverStopping(FMLServerStoppingEvent event) { }
}
