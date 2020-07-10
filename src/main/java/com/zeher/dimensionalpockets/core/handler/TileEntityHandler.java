package com.zeher.dimensionalpockets.core.handler;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.pocket.tileentity.*;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID)
public class TileEntityHandler {
	
	public static void preInitialization(){
		GameRegistry.registerTileEntity(TileEntityDimensionalPocket.class, new ResourceLocation(DimensionalPockets.MOD_ID + ":" + "tile_dimensional_pocket"));
		GameRegistry.registerTileEntity(TileEntityDimensionalPocketWallConnector.class, new ResourceLocation(DimensionalPockets.MOD_ID + ":" + "tile_dimensional_pocket_wall"));
	}
	
	@SideOnly(Side.CLIENT)
	public static void clientPreInitialization() { }

}