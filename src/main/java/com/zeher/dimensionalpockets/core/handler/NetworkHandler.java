package com.zeher.dimensionalpockets.core.handler;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.core.util.DimLogger;
import com.zeher.dimensionalpockets.network.core.packet.*;
import com.zeher.dimensionalpockets.pocket.tileentity.TileEntityDimensionalPocket;

import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {

	public static void preInitialization() {
		DimensionalPockets.NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("dimensionalpockets");
		
		DimensionalPockets.NETWORK.registerMessage(PacketPlayer.Handler.class, PacketPlayer.class, 1, Side.SERVER);
		DimensionalPockets.NETWORK.registerMessage(PacketCreatorServer.Handler.class, PacketCreatorServer.class, 2, Side.SERVER);
		DimensionalPockets.NETWORK.registerMessage(PacketLock.Handler.class, PacketLock.class, 3, Side.SERVER);
		//DimensionalPockets.NETWORK.registerMessage(PacketCreatorClient.Handler.class, PacketCreatorClient.class, 3, Side.CLIENT);
		DimensionalPockets.NETWORK.registerMessage(PacketCycleType.Handler.class, PacketCycleType.class, 4, Side.SERVER);
	}

	public static void initialization() {}

	public static void postInitialization() {}
	
	public static void sendPocketPlayer(String name_to_add, BlockPos pos, boolean add){
		DimensionalPockets.NETWORK.sendToServer(new PacketPlayer(name_to_add, pos, add));
	}
	
	public static void sendCreatorPacketToServer(String name, BlockPos pos) {
		DimensionalPockets.NETWORK.sendToServer(new PacketCreatorServer(name, pos));
	}
	
	public static void sendCreatorPacketToClient(String name, BlockPos pos) {
		//DimensionalPockets.NETWORK.sendToAll(new PacketCreatorClient(name, pos));
	}
	
	public static void sendLockPacket(BlockPos pos, boolean lock) {
		DimensionalPockets.NETWORK.sendToServer(new PacketLock(pos, lock));
	}
	
	public static void sendCycleTypePacket(BlockPos pos) {
		DimensionalPockets.NETWORK.sendToServer(new PacketCycleType(pos));
	}
}