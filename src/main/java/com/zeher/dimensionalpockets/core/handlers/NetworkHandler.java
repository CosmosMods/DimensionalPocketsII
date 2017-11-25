package com.zeher.dimensionalpockets.core.handlers;

import com.zeher.dimensionalpockets.DimensionalPockets;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {

	public static void preInit() {
		DimensionalPockets.network = NetworkRegistry.INSTANCE.newSimpleChannel("dimensionalpockets");
	}

	public void init() {}

	public void postInit() {}
	
}
