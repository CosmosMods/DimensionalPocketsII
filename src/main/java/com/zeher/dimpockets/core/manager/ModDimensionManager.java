package com.zeher.dimpockets.core.manager;

import com.zeher.dimpockets.DimensionalPockets;
import com.zeher.dimpockets.pocket.dimension.PocketDimensionType;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.DimensionManager;

public class ModDimensionManager {
	
	public static final PocketDimensionType POCKET_DIMENSION = new PocketDimensionType(new ResourceLocation(DimensionalPockets.MOD_ID, "pocket_dimension"));
	
	public static void registerDimensions() {
		DimensionManager.registerDimension(new ResourceLocation(DimensionalPockets.MOD_ID, "pocket_dimension"), POCKET_DIMENSION, new PacketBuffer(Unpooled.buffer(16)), true);
	}
}