package com.zeher.dimensionalpockets.core.handler;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.pocket.client.tileentity.TileEntityDimensionalPocket;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID)
public class TileEntityHandler {
	
	public static void preInit(){
		GameRegistry.registerTileEntity(TileEntityDimensionalPocket.class, DimensionalPockets.MOD_ID + ":" + "tile_dimensional_pocket");
	}
	
	@SideOnly(Side.CLIENT)
	public static void clientPreInit() { }

}
