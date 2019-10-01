package com.zeher.dimensionalpockets;

import org.apache.logging.log4j.LogManager;

import com.zeher.dimensionalpockets.core.creativetab.CreativeTabDimensional;
import com.zeher.dimensionalpockets.core.handler.*;
import com.zeher.dimensionalpockets.core.util.DimLogger;
import com.zeher.dimensionalpockets.network.proxy.CommonProxy;
import com.zeher.dimensionalpockets.pocket.PocketRegistry;
import com.zeher.dimensionalpockets.pocket.PocketWorldProvider;
import com.zeher.dimensionalpockets.pocket.handler.PocketChunkLoaderHandler;
import com.zeher.zeherlib.ZeherLib;
import com.zeher.zeherlib.api.interfaces.IProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
@Mod(modid = DimensionalPockets.MOD_ID, name = DimensionalPockets.MOD_NAME, version = DimensionalPockets.MOD_VERSION, dependencies = DimensionalPockets.MOD_DEPENDENCIES)
public class DimensionalPockets {
	
	@SidedProxy(clientSide = "com.zeher.dimensionalpockets.network.proxy.ClientProxy", serverSide = "com.zeher.dimensionalpockets.network.proxy.ServerProxy")
	public static CommonProxy COMMON_PROXY;
	public static IProxy IPROXY;
	
	public static final String MOD_ID = "dimensionalpocketsii";
	public static final String MOD_NAME = "Dimensional Pockets II";
	public static final String MOD_VERSION = "3.1.34-beta";
	public static final String MOD_VERSION_MAX = "3.2.0-beta";
	public static final String MOD_DEPENDENCIES = ZeherLib.VERSION_GROUP;
	public static final String VERSION_GROUP = "required-after:" + MOD_ID + "@[" + MOD_VERSION + "," + MOD_VERSION_MAX + "];";

	public static final int DIMENSION_ID = 98;
	public static final int BIOME_ID = 99;
	public static final boolean KEEP_POCKETS_CHUNKLOADED = true;
	public static final boolean CAN_DESTROY_WALLS_IN_CREATIVE = false;
	public static final boolean CAN_TELEPORT_TO_DIM = true;
	public static final boolean DIMENSIONAL_POCKETS_SYSTEM_MESSAGE = false;
	
	public static final CreativeTabs TAB_DIMENSIONALPOCKETS = new CreativeTabDimensional(CreativeTabs.getNextID(), "tab_dimensionalpockets");
	
	@Instance(MOD_ID)
	public static DimensionalPockets INSTANCE;
	public static SimpleNetworkWrapper NETWORK;
	public static GuiHandler GUI_HANDLER;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		RecipeHandler.registerRecipes();
		RecipeHandler.registerSmelting();

		TileEntityHandler.preInit();

		GameRegistry.registerWorldGenerator(new WorldGenerationHandler(), 0);

		NetworkHandler.preInit();
		
		DimLogger.init(LogManager.getLogger(MOD_ID.replaceAll(" ", "")));
		COMMON_PROXY.preInit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		COMMON_PROXY.init();
		
		DimensionType.register("Pocket Dimension", "_pocket", DIMENSION_ID, PocketWorldProvider.class, true);
		DimensionManager.registerDimension(DIMENSION_ID, DimensionType.getById(DIMENSION_ID));
		
		BiomeHandler.RegistrationHandler.initBiomeManagerAndDictionary();
		
		if(KEEP_POCKETS_CHUNKLOADED) {
			ForgeChunkManager.setForcedChunkLoadingCallback(this, new PocketChunkLoaderHandler());
		}
		
		GUI_HANDLER = new GuiHandler();
		NetworkHandler.init();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		COMMON_PROXY.postInit();
		
		NetworkHandler.postInit();
	}
	
	@EventHandler
	public void onServerAboutToStart(FMLServerAboutToStartEvent event) {
		PocketRegistry.loadData();
		COMMON_PROXY.serverAboutToStart(event);
	}
	
	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		COMMON_PROXY.serverStarting(event);
	}
	
	@EventHandler
	public void onServerStarted(FMLServerStartedEvent event) {
		PocketRegistry.initChunkLoading();
		COMMON_PROXY.serverStarted(event);
	}
	
	@EventHandler
	public void onServerStopping(FMLServerStoppingEvent event) {
		PocketRegistry.saveData();
		PocketChunkLoaderHandler.clearTicketMap();
		COMMON_PROXY.serverStopping(event);
	}
	
}