package com.tcn.dimensionalpocketsii.pocket.network.packet.common;

import java.util.function.Supplier;

import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketRegistryData  {
	
	private boolean save;
	
	public PacketRegistryData(PacketBuffer buf) {
		this.save = buf.readBoolean();
	}
	
	public PacketRegistryData(boolean save) {
		this.save = save;
	}
	
	public static void encode(PacketRegistryData packet, PacketBuffer buf) {
		buf.writeBoolean(packet.save);
		
	}
	
	public static void handle(final PacketRegistryData packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			if (packet.save) {
				PocketRegistryManager.saveData();
			} else {
				PocketRegistryManager.loadData();
			}
		});
		
		ctx.setPacketHandled(true);
	}
}
