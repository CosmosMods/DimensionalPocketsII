package com.zeher.dimensionalpockets.core.handlers;

import com.zeher.dimensionalpockets.client.pocket.render.RendererPocketInner;
import com.zeher.dimensionalpockets.core.tileentity.TileEntityDimensionalPocket;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityHandler {
	
	public static void preInit(){
		GameRegistry.registerTileEntity(TileEntityDimensionalPocket.class, "tile_dimensional_pocket");
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDimensionalPocket.class, new RendererPocketInner());
	}

}
