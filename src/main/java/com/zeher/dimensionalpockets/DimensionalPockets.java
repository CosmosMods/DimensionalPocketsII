package com.zeher.dimensionalpockets;

import org.apache.logging.log4j.LogManager;

import com.zeher.dimensionalpockets.core.creativetab.CreativeTabDimensional;
import com.zeher.dimensionalpockets.core.handlers.GuiHandler;
import com.zeher.dimensionalpockets.core.pocket.PocketRegistry;
import com.zeher.dimensionalpockets.core.pocket.PocketWorldProvider;
import com.zeher.dimensionalpockets.core.pocket.handlers.PocketBiomeHandler;
import com.zeher.dimensionalpockets.core.pocket.handlers.PocketChunkLoaderHandler;
import com.zeher.dimensionalpockets.core.proxy.CommonProxy;
import com.zeher.dimensionalpockets.core.util.DimLogger;
import com.zeher.trzlib.TRZLib;
import com.zeher.trzlib.api.TRZIProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = DimensionalPockets.mod_id, name = DimensionalPockets.mod_name, version = DimensionalPockets.mod_version, dependencies = DimensionalPockets.mod_dependencies)
public class DimensionalPockets{
	
	@SidedProxy(clientSide = "com.zeher.dimensionalpockets.core.proxy.ClientProxy", serverSide = "com.zeher.dimensionalpockets.core.proxy.ServerProxy")
	public static CommonProxy common_proxy;
	public static TRZIProxy iproxy;
	
	public static final String mod_id = "dimensionalpocketsii";
	public static final String mod_name = "Dimensional Pockets II";
	public static final String mod_version = "0.0.7a";
	public static final String mod_version_max = "0.1.0a";
	public static final String mod_dependencies = TRZLib.version_group;
	
	public static final String version_group = "required-after:" + mod_id + "@[" + mod_version + "," + mod_version_max + "];";

	public static int dimension_id = 98;
	public static int biome_id = 99;
	public static boolean keep_pockets_chunkloaded = true;
	public static boolean can_destroy_walls_in_creative = false;
	public static boolean can_teleport_to_dim = true;
	
	public static CreativeTabs tab_dimensionalpockets = new CreativeTabDimensional(CreativeTabs.getNextID(), "tab_dimensionalpockets");
	
	@Instance(mod_id)
	public static DimensionalPockets instance;
	public static SimpleNetworkWrapper network;
	public static GuiHandler gui_handler;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		DimLogger.init(LogManager.getLogger(this.mod_id.replaceAll(" ", "")));
		common_proxy.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		common_proxy.init(event);
		
		DimensionType.register("Pocket Dimension", "_pocket", dimension_id, PocketWorldProvider.class, true);
		DimensionManager.registerDimension(dimension_id, DimensionType.getById(dimension_id));
		
		PocketBiomeHandler.init();
		
		if(this.keep_pockets_chunkloaded) {
			ForgeChunkManager.setForcedChunkLoadingCallback(this, new PocketChunkLoaderHandler());
		}
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		common_proxy.postInit(event);
	}
	
	@EventHandler
	public void onServerAboutToStart(FMLServerAboutToStartEvent event) {
		PocketRegistry.loadData();
		common_proxy.serverAboutToStart(event);
	}
	
	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		common_proxy.serverStarting(event);
	}
	
	@EventHandler
	public void onServerStarted(FMLServerStartedEvent event) {
		PocketRegistry.initChunkLoading();
		common_proxy.serverStarted(event);
	}
	
	@EventHandler
	public void onServerStopping(FMLServerStoppingEvent event) {
		PocketRegistry.saveData();
		PocketChunkLoaderHandler.clearTicketMap();
		common_proxy.serverStopping(event);
	}
	
}
