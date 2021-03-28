package com.tcn.dimensionalpocketsii.core.management;

import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.core.network.PacketDimensionChange;
import com.tcn.dimensionalpocketsii.core.network.PacketElytraItemUpdate;
import com.tcn.dimensionalpocketsii.core.network.PacketElytraShift;
import com.tcn.dimensionalpocketsii.pocket.network.packet.common.PacketLock;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class CoreNetworkManager {

	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(DimensionalPockets.MOD_ID, "network"), 
			() -> PROTOCOL_VERSION, 
			PROTOCOL_VERSION::equals, 
			PROTOCOL_VERSION::equals);
	
	public static void register() {
		INSTANCE.registerMessage(0, PacketElytraShift.class, PacketElytraShift::encode, PacketElytraShift::new, PacketElytraShift::handle);
		INSTANCE.registerMessage(1, PacketDimensionChange.class, PacketDimensionChange::encode, PacketDimensionChange::new, PacketDimensionChange::handle);
		INSTANCE.registerMessage(2, PacketElytraItemUpdate.class, PacketElytraItemUpdate::encode, PacketElytraItemUpdate::new, PacketElytraItemUpdate::handle);
		
		
		INSTANCE.registerMessage(3, PacketLock.class, PacketLock::encode, PacketLock::new, PacketLock::handle);
		
		DimensionalPockets.LOGGER.info("Packets Registered");
	}

	public static void sendToServer(Object message) {
        INSTANCE.sendToServer(message);
    }
}
