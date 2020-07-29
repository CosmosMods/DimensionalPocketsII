package com.zeher.dimpockets.core.manager;

import com.zeher.dimpockets.DimensionalPockets;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID)
@ObjectHolder(DimensionalPockets.MOD_ID)
public class TileEntityManager {
	
	public static final TileEntityType<?> POCKET = null;
	
	//public static final ContainerType<ContainerDimensionalPocket> POCKET_CONTAINER = ContainerType.register("pocket_container", ContainerType.factory.create());//new ContainerDimensionalPocket());
	
	public static void preInitialization(){
		//ContainerType.register("", ContainerDimensionalPocket::new);
		
		//GameRegistry.registerTileEntity(TileEntityDimensionalPocket.class, new ResourceLocation(DimensionalPockets.MOD_ID + ":" + "tile_dimensional_pocket"));
		//GameRegistry.registerTileEntity(TileEntityDimensionalPocketWallConnector.class, new ResourceLocation(DimensionalPockets.MOD_ID + ":" + "tile_dimensional_pocket_wall"));
	}
	
}