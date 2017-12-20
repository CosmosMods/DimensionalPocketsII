package com.zeher.dimensionalpockets.core.proxy;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.client.DimensionalPocketsClient;
import com.zeher.dimensionalpockets.core.handlers.BlockHandler;
import com.zeher.dimensionalpockets.core.handlers.ItemHandler;
import com.zeher.dimensionalpockets.core.pocket.handlers.PocketBlockEventHandler;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new PocketBlockEventHandler());
	}
	
}
