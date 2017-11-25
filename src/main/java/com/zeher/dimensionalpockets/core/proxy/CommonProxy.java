package com.zeher.dimensionalpockets.core.proxy;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.core.command.CommandDimHelp;
import com.zeher.dimensionalpockets.core.command.CommandDimShift;
import com.zeher.dimensionalpockets.core.handlers.*;
import com.zeher.dimensionalpockets.core.pocket.handlers.PocketBlockEventHandler;
import com.zeher.dimensionalpockets.core.pocket.handlers.InsidePocketEventHandler;
import com.zeher.trzcore.api.TRZIProxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

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
