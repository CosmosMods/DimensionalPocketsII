package com.tcn.dimensionalpocketsii;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tcn.dimensionalpocketsii.client.screen.ScreenPocketConfig;
import com.tcn.dimensionalpocketsii.core.advancement.CoreTriggers;
import com.tcn.dimensionalpocketsii.core.management.CoreConfigurationManager;
import com.tcn.dimensionalpocketsii.core.management.CoreRecipeHandler;
import com.tcn.dimensionalpocketsii.core.management.ForgeBusManager;
import com.tcn.dimensionalpocketsii.core.management.ModBusManager;
import com.tcn.dimensionalpocketsii.pocket.client.PocketBlockColour;
import com.tcn.dimensionalpocketsii.pocket.client.PocketItemColour;
import com.tcn.dimensionalpocketsii.pocket.client.PocketWallColour;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketEventManager;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ExtensionPoint;
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
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setRenderLayers);

		MinecraftForge.EVENT_BUS.register(this);

		ModLoadingContext context = ModLoadingContext.get();

		context.registerConfig(ModConfig.Type.COMMON, CoreConfigurationManager.spec, "dimpockets-common.toml");
	}

	public void onFMLCommonSetup(FMLCommonSetupEvent event) {
		CoreRecipeHandler.preInitialization();
		CriteriaTriggers.register(CoreTriggers.USE_SHIFTER_TRIGGER);
		ForgeBusManager.registerOres();
		
		// NetworkHandler.preInitialization();

		if (CoreConfigurationManager.getInstance().getKeepChunksLoaded()) {
			// ForgeChunkManager.setForcedChunkLoadingCallback(this, new ChunkLoaderManagerRoom());
			// ForgeChunkManager.setForcedChunkLoadingCallback(this, new ChunkLoaderManagerBlock());
		}

		MinecraftForge.EVENT_BUS.register(new PocketEventManager());
		LOGGER.info("FMLCommonSetup complete.", DimensionalPockets.class);
	}

	public void onFMLClientSetup(FMLClientSetupEvent event) {
		final ModLoadingContext context = ModLoadingContext.get();

		registerClient(context);
		LOGGER.info("FMLClientSetup complete.", DimensionalPockets.class);
	}
	
	@SuppressWarnings("deprecation")
	@OnlyIn(Dist.CLIENT)
	public void registerClient(ModLoadingContext context) {
		context.registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, screen) -> new ScreenPocketConfig(screen));
		
		Minecraft.getInstance().getBlockColors().register(new PocketWallColour(), ModBusManager.BLOCK_WALL);
		Minecraft.getInstance().getBlockColors().register(new PocketWallColour(), ModBusManager.BLOCK_WALL_EDGE);
		Minecraft.getInstance().getBlockColors().register(new PocketWallColour(), ModBusManager.BLOCK_WALL_CONNECTOR);
		Minecraft.getInstance().getBlockColors().register(new PocketWallColour(), ModBusManager.BLOCK_WALL_CHARGER);
		Minecraft.getInstance().getBlockColors().register(new PocketBlockColour(), ModBusManager.BLOCK_POCKET);
		
		Minecraft.getInstance().getItemColors().register(new PocketItemColour(), ModBusManager.BLOCKITEM_POCKET);
		Minecraft.getInstance().getItemColors().register(new PocketItemColour(), Item.getItemFromBlock(ModBusManager.BLOCK_DIMENSIONAL_CORE));
	}
	

	@SuppressWarnings("unused")
	public void setRenderLayers(FMLClientSetupEvent event) {
		RenderType solid = RenderType.getSolid();
		RenderType cutout_mipped = RenderType.getCutoutMipped();
		RenderType cutout = RenderType.getCutout();
		RenderType translucent = RenderType.getTranslucent();
		RenderType translucent_no_crumbling = RenderType.getTranslucentNoCrumbling();
		
		RenderTypeLookup.setRenderLayer(ModBusManager.BLOCK_POCKET, cutout_mipped);
		RenderTypeLookup.setRenderLayer(ModBusManager.BLOCK_WALL_CHARGER, cutout_mipped);
		RenderTypeLookup.setRenderLayer(ModBusManager.BLOCK_WALL_CONNECTOR, cutout_mipped);
		RenderTypeLookup.setRenderLayer(ModBusManager.BLOCK_DIMENSIONAL_CORE, cutout_mipped);
		
		DimensionalPockets.LOGGER.info("Setting Render Layers");
	}
}