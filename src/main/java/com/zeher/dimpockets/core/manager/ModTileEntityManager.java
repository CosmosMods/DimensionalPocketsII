package com.zeher.dimpockets.core.manager;

import com.zeher.dimpockets.DimensionalPockets;
import com.zeher.dimpockets.pocket.core.tileentity.*;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID)
public class ModTileEntityManager {
	
	public static void preInitialization(){
		GameRegistry.registerTileEntity(TilePocket.class, new ResourceLocation(DimensionalPockets.MOD_ID + ":" + "tile_dimensional_pocket"));
		GameRegistry.registerTileEntity(TileConnector.class, new ResourceLocation(DimensionalPockets.MOD_ID + ":" + "tile_dimensional_pocket_wall"));
	}
	
	@SideOnly(Side.CLIENT)
	public static void clientPreInitialization() { }

}