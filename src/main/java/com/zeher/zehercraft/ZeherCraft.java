package com.zeher.zehercraft;

import java.util.ArrayList;
import java.util.List;

import com.zeher.zehercraft.core.handler.BlockHandler;
import com.zeher.zehercraft.core.handler.FluidHandler;
import com.zeher.zehercraft.core.handler.GuiHandler;
import com.zeher.zehercraft.core.handler.RegistryHandler;
import com.zeher.zehercraft.core.handler.TileEntityHandler;
import com.zeher.zehercraft.core.handler.recipe.RecipeHandler;
import com.zeher.zehercraft.core.network.proxy.CommonProxy;
import com.zeher.zeherlib.ZLReference;
import com.zeher.zeherlib.ZeherLib;
import com.zeher.zeherlib.core.network.proxy.IProxy;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod.EventBusSubscriber
@Mod(modid = ZeherCraft.MOD_ID, name = ZeherCraft.MOD_NAME, version = ZeherCraft.MOD_VERSION, dependencies = ZeherCraft.MOD_DEPENDENCIES)
public class ZeherCraft {
	
	@SidedProxy(clientSide = "com.zeher.zehercraft.core.network.proxy.ClientProxy", serverSide = "com.zeher.zehercraft.core.network.proxy.CommonProxy")
	public static CommonProxy COMMON_PROXY;
	public static IProxy IPROXY;
	
	public static final String MOD_ID = "zehercraft";
	public static final String MOD_NAME = "ZeherCraft";
	public static final String MOD_VERSION = "1.0.45-beta";
	public static final String MOD_VERSION_MAX = "1.0.45-beta";
	public static final String MOD_DEPENDENCIES = ZCReference.DEPENDENCY.FORGE_DEP + ZCReference.DEPENDENCY.ZEHERLIB_DEP + ZCReference.DEPENDENCY.REDSTONE_DEP;
	public static final String VERSION_GROUP = "required-after:" + MOD_ID + "@[" + MOD_VERSION + "," + MOD_VERSION_MAX + "];";
	
	/**
	 * ZeherCraft's {@link Mod.Instance}.
	 */
	@Instance(MOD_ID)
	public static ZeherCraft INSTANCE;
	
	/**
	 * Network registered to {@link ZeherCraft}.
	 * Used to send packets.
	 */
	public static SimpleNetworkWrapper NETWORK;
	
	/**
	 * Gui Handler for {@link ZeherCraft}
	 */
	public static GuiHandler GUI_HANDLER;
	
	/**
	 * {@link ArrayList} of sounds registered to {@link ZeherCraft}
	 */
	public static final List<SoundEvent> SOUNDS = new ArrayList<>();
	
	public ZeherCraft(){
		FluidRegistry.enableUniversalBucket();
	}
	
	@EventHandler
	public void preInitialization(FMLPreInitializationEvent event){
		FluidHandler.preInitialization();
		BlockHandler.FLUID.preInitialization();
		TileEntityHandler.preInitialization();
		
		//WorldGenerationHandler.preInity();
		//NetworkHandler.preInit();
		
		COMMON_PROXY.preInitialization();
	}
	
	@EventHandler
	public void initialization(FMLInitializationEvent event){
		RecipeHandler.initialization();
		RegistryHandler.ORE_DICTIONARY.registerBlocks();
		
		GUI_HANDLER = new GuiHandler();
		
		COMMON_PROXY.initialization();
	}
	
	@EventHandler
	public void postInitialization(FMLPostInitializationEvent event){
		COMMON_PROXY.postInitialization();
	}
}