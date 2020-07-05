package com.zeher.zehercraft.core.handler;

import com.zeher.zehercraft.ZeherCraft;
import com.zeher.zehercraft.processing.client.tesr.*;
import com.zeher.zehercraft.processing.core.tile.*;
import com.zeher.zehercraft.transport.client.tesr.*;
import com.zeher.zehercraft.transport.core.tile.*;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityHandler {
	
	public static void preInitialization() {
		GameRegistry.registerTileEntity(TileEntityKiln.class, new ResourceLocation(ZeherCraft.MOD_ID + ":" + "tile_kiln"));
		GameRegistry.registerTileEntity(TileEntityGrinder.class, new ResourceLocation(ZeherCraft.MOD_ID + ":" + "tile_grinder"));
		GameRegistry.registerTileEntity(TileEntityCompactor.class, new ResourceLocation(ZeherCraft.MOD_ID + ":" + "tile_compactor"));
		GameRegistry.registerTileEntity(TileEntityCharger.class, new ResourceLocation(ZeherCraft.MOD_ID + ":" + "tile_charger"));
		
		GameRegistry.registerTileEntity(TileEntityEnergyChannel.class, new ResourceLocation(ZeherCraft.MOD_ID + ":" + "tile_energy_channel"));
		GameRegistry.registerTileEntity(TileEntityEnergyChannelTransparent.class, new ResourceLocation(ZeherCraft.MOD_ID + ":" + "tile_energy_channel_transparent"));
		
		GameRegistry.registerTileEntity(TileEntityEnergyChannelSurge.class, new ResourceLocation(ZeherCraft.MOD_ID + ":" + "tile_energy_channel_surge"));
		GameRegistry.registerTileEntity(TileEntityEnergyChannelTransparentSurge.class, new ResourceLocation(ZeherCraft.MOD_ID + ":" + "tile_energy_channel_transparent_surge"));
		
		GameRegistry.registerTileEntity(TileEntitySynthesiser.class, new ResourceLocation(ZeherCraft.MOD_ID + ":" + "tile_synthesiser"));
		GameRegistry.registerTileEntity(TileEntitySynthesiserStand.class, new ResourceLocation(ZeherCraft.MOD_ID + ":" + "tile_synthesiser_stand"));
	}
	
	@SideOnly(Side.CLIENT)
	public static void preInitializationClient() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityKiln.class, new RendererKilnInternals());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGrinder.class, new RendererGrinderInternals());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCompactor.class, new RendererCompactorInternals());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCharger.class, new RendererCharger());
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnergyChannel.class, new RendererEnergyChannel());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnergyChannelTransparent.class, new RendererEnergyChannelTransparent());
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnergyChannelSurge.class, new RendererEnergyChannelSurge());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnergyChannelTransparentSurge.class, new RendererEnergyChannelTransparentSurge());
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySynthesiser.class, new RendererSynthesiser());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySynthesiserStand.class, new RendererSynthesiserStand());
	}
}