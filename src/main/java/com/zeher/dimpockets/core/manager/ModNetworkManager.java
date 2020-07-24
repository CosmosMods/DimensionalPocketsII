package com.zeher.dimpockets.core.manager;

import com.zeher.dimpockets.DimensionalPockets;
import com.zeher.dimpockets.pocket.network.packet.PacketPocket;
import com.zeher.dimpockets.pocket.network.packet.PacketConnector;
import com.zeher.zeherlib.api.compat.core.interfaces.EnumSideState;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class ModNetworkManager {

	public static void preInitialization() {
		DimensionalPockets.NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("dimensionalpockets_channel");
		
		DimensionalPockets.NETWORK.registerMessage(PacketPocket.Handler.class, PacketPocket.class, 1, Side.SERVER);
		DimensionalPockets.NETWORK.registerMessage(PacketConnector.Handler.class, PacketConnector.class, 2, Side.SERVER);
	}

	public static void initialization() {}

	public static void postInitialization() {}
	
	
	/** TODO - PocketPackets - */
	
	//Used to add or remove an Allowed Player
	public static void sendPocketPlayer(String name_to_add, BlockPos pos, boolean add){
		DimensionalPockets.NETWORK.sendToServer(new PacketPocket(pos, add, name_to_add));
	}
	
	//Used to set the creator of a Pocket
	public static void sendPocketCreator(String creator, BlockPos pos) {
		DimensionalPockets.NETWORK.sendToServer(new PacketPocket(pos, creator));
	}
	
	//Used to lock or unlock a Pocket
	public static void sendPocketLock(BlockPos pos, boolean lock) {
		DimensionalPockets.NETWORK.sendToServer(new PacketPocket(pos, lock));
	}
	
	//Used to show or hide a Pocket Side Guide
	public static void sendPocketSides(BlockPos pos, boolean sides) {
		DimensionalPockets.NETWORK.sendToServer(new PacketPocket(pos, sides, 0));
	}
	
	//Used to cycle a side state of a Pocket Block
	public static void sendPocketCycleSide(BlockPos pos, EnumFacing facing) {
		DimensionalPockets.NETWORK.sendToServer(new PacketPocket(pos, facing));
	}
	
	//Used to cycle a side state of a Pocket Block from inside a Pocket
	public static void sendPocketCycleSideIn(BlockPos pos, int dimension, EnumFacing facing) {
		DimensionalPockets.NETWORK.sendToServer(new PacketPocket(pos, dimension, facing));
	}
	
	//Used to empty the FluidTank of a Pocket
	public static void sendPocketEmpty(BlockPos pos, int meta) {
		DimensionalPockets.NETWORK.sendToServer(new PacketPocket(pos, meta));
	}
	
	
	/** TODO - Connector Packets - */
	
	public static void sendConnectorType(BlockPos pos) {
		DimensionalPockets.NETWORK.sendToServer(new PacketConnector(pos, "cycle"));
	}

	public static void sendConnectorEmpty(BlockPos pos) {
		DimensionalPockets.NETWORK.sendToServer(new PacketConnector(pos, "empty"));
	}
	
	public static void sendConnectorLock(BlockPos pos, boolean lock) {
		DimensionalPockets.NETWORK.sendToServer(new PacketConnector(pos, lock));
	}

	public static void sendConnectorSide(BlockPos pos, EnumFacing facing) {
		DimensionalPockets.NETWORK.sendToServer(new PacketConnector(pos, facing));
	}
}