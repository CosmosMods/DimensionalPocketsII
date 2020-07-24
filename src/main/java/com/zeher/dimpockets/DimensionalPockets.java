package com.zeher.dimpockets;

import org.apache.logging.log4j.LogManager;

import com.zeher.dimpockets.core.command.CommandDimHelp;
import com.zeher.dimpockets.core.command.CommandDimShift;
import com.zeher.dimpockets.core.command.CommandRecoverPocket;
import com.zeher.dimpockets.core.command.CommandRecoverPocketAdmin;
import com.zeher.dimpockets.core.command.CommandSetSpawn;
import com.zeher.dimpockets.core.log.ModLogger;
import com.zeher.dimpockets.core.manager.ModBiomeManager;
import com.zeher.dimpockets.core.manager.ModConfigManager;
import com.zeher.dimpockets.core.manager.ModGuiManager;
import com.zeher.dimpockets.core.manager.ModNetworkManager;
import com.zeher.dimpockets.core.manager.ModRecipeManager;
import com.zeher.dimpockets.core.manager.ModTileEntityManager;
import com.zeher.dimpockets.core.manager.ModWorldGenManager;
import com.zeher.dimpockets.core.network.proxy.CommonProxy;
import com.zeher.dimpockets.pocket.core.PocketWorldProvider;
import com.zeher.dimpockets.pocket.core.manager.ChunkLoaderManagerBlock;
import com.zeher.dimpockets.pocket.core.manager.ChunkLoaderManagerRoom;
import com.zeher.dimpockets.pocket.core.manager.PocketEventManager;
import com.zeher.dimpockets.pocket.core.manager.PocketRegistryManager;
import com.zeher.zeherlib.core.network.proxy.IProxy;

import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
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
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
@Mod(modid = DimensionalPockets.MOD_ID, name = DimensionalPockets.MOD_NAME, version = DimensionalPockets.MOD_VERSION, dependencies = DimensionalPockets.MOD_DEPENDENCIES)
public class DimensionalPockets {
	
	@SidedProxy(clientSide = "com.zeher.dimpockets.core.network.proxy.ClientProxy", serverSide = "com.zeher.dimpockets.core.network.proxy.ServerProxy")
	public static CommonProxy COMMON_PROXY;
	public static IProxy IPROXY;
	
	public static final String MOD_ID = "dimensionalpocketsii";
	public static final String MOD_NAME = "Dimensional Pockets II";
	public static final String MOD_VERSION = "5.2.61-beta";
	public static final String MOD_VERSION_MAX = "5.3.0-beta";
	public static final String MOD_DEPENDENCIES = DimReference.DEPENDENCY.FORGE_DEP + DimReference.DEPENDENCY.REDSTONE_DEP + DimReference.DEPENDENCY.ZEHERLIB_DEP;
	public static final String VERSION_GROUP = "required-after:" + MOD_ID + "@[" + MOD_VERSION + "," + MOD_VERSION_MAX + "];";

	@Instance(MOD_ID)
	public static DimensionalPockets INSTANCE;
	public static SimpleNetworkWrapper NETWORK;
	public static ModGuiManager GUI_HANDLER;
	
	public DimensionalPockets() {
		FluidRegistry.enableUniversalBucket();
	}
	
	@EventHandler
	public void preInitialization(FMLPreInitializationEvent event){
		ModRecipeManager.preInitialization();
		ModTileEntityManager.preInitialization();
		ModNetworkManager.preInitialization();
		
		GameRegistry.registerWorldGenerator(new ModWorldGenManager(), 0);
		ModLogger.preInitialization(LogManager.getLogger(MOD_ID.replaceAll(" ", "")));
	}
	
	@EventHandler
	public void initialization(FMLInitializationEvent event) {		
		DimensionType.register("Pocket Dimension", "_pocket", DimReference.CONSTANT.POCKET_DIMENSION_ID, PocketWorldProvider.class, true);
		DimensionManager.registerDimension(DimReference.CONSTANT.POCKET_DIMENSION_ID, DimensionType.getById(DimReference.CONSTANT.POCKET_DIMENSION_ID));
		
		ModBiomeManager.initialization();
		ModNetworkManager.initialization();
		ModConfigManager.initialization();
		
		if(ModConfigManager.KEEP_POCKETS_CHUNKLOADED) {
			ForgeChunkManager.setForcedChunkLoadingCallback(this, new ChunkLoaderManagerRoom());
			ForgeChunkManager.setForcedChunkLoadingCallback(this, new ChunkLoaderManagerBlock());
		}
		
		GUI_HANDLER = new ModGuiManager();
	}
	
	@EventHandler
	public void postInitialization(FMLPostInitializationEvent event) {		
		MinecraftForge.EVENT_BUS.register(new PocketEventManager());
		ModNetworkManager.postInitialization();
		
		if (Loader.isModLoaded("digimobs")) {
			ModLogger.warning("[EXTERNAL MOD <digimobs>] Digimobs is installed! Report any issues to the respective Mod Author...", DimensionalPockets.class);
		}
	}
	
	@EventHandler
	public void onServerAboutToStart(FMLServerAboutToStartEvent event) {
		PocketRegistryManager.loadData();
	}
	
	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) { 
		event.registerServerCommand(new CommandDimShift());
		event.registerServerCommand(new CommandDimHelp());
		event.registerServerCommand(new CommandRecoverPocket());
		event.registerServerCommand(new CommandSetSpawn());
		event.registerServerCommand(new CommandRecoverPocketAdmin());
	}
	
	@EventHandler
	public void onServerStarted(FMLServerStartedEvent event) {
		PocketRegistryManager.initChunkLoading(); 
	}
	
	@EventHandler
	public void onServerStopping(FMLServerStoppingEvent event) {
		PocketRegistryManager.saveData();
		ChunkLoaderManagerRoom.clearTicketMap();
		ChunkLoaderManagerBlock.clearTicketMap();
	}
}