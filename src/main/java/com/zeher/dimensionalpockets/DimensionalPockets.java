package com.zeher.dimensionalpockets;

import org.apache.logging.log4j.LogManager;

import com.zeher.dimensionalpockets.core.creativetab.*;
import com.zeher.dimensionalpockets.core.handlers.*;
import com.zeher.dimensionalpockets.core.pocket.PocketRegistry;
import com.zeher.dimensionalpockets.core.pocket.PocketWorldProvider;
import com.zeher.dimensionalpockets.core.pocket.handlers.PocketBiomeHandler;
import com.zeher.dimensionalpockets.core.pocket.handlers.PocketChunkLoaderHandler;
import com.zeher.dimensionalpockets.core.proxy.ClientProxy;
import com.zeher.dimensionalpockets.core.proxy.CommonProxy;
import com.zeher.dimensionalpockets.core.util.DimLogger;
import com.zeher.trzcore.TRZCore;
import com.zeher.trzcore.api.TRZIProxy;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = DimensionalPockets.mod_id, name = DimensionalPockets.mod_name, version = DimensionalPockets.mod_version, dependencies = DimensionalPockets.mod_dependencies)
public class DimensionalPockets{
	
	@SidedProxy(clientSide = "com.zeher.dimensionalpockets.core.proxy.ClientProxy", serverSide = "com.zeher.dimensionalpockets.core.proxy.ServerProxy")
	public static CommonProxy common_proxy;
	public static TRZIProxy iproxy;
	
	public static final String mod_id = "dimensionalpocketsii";
	public static final String mod_name = "Dimensional Pockets II";
	public static final String mod_version = "0.0.2a";
	public static final String mod_version_max = "0.1.0a";
	public static final String mod_dependencies = TRZCore.version_group;
	
	public static final String version_group = "required-after:" + mod_id + "@[" + mod_version + "," + mod_version_max + "];";

	public static int dimension_id = 98;
	public static int biome_id = 99;
	public static boolean keep_pockets_chunkloaded = true;
	public static boolean can_destroy_walls_in_creative = false;
	public static boolean can_teleport_to_dim = false;
	
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
