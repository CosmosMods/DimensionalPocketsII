package com.tcn.dimensionalpocketsii.pocket.network.packet;

import java.util.function.Supplier;

import com.tcn.cosmoslibrary.common.lib.CosmosChunkPos;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class PacketTrapPlayers implements PacketPocketNet {

	private CosmosChunkPos pos;
	private boolean lock;
	
	public PacketTrapPlayers(FriendlyByteBuf buf) {
		this.pos = CosmosChunkPos.convertTo(buf.readBlockPos());
		this.lock = buf.readBoolean();
	}
	
	public PacketTrapPlayers(CosmosChunkPos pos, boolean lock) {
		this.pos = pos;
		this.lock = lock;
	}
	
	public static void encode(PacketTrapPlayers packet, FriendlyByteBuf buf) {
		buf.writeBlockPos(CosmosChunkPos.convertFrom(packet.pos));
		buf.writeBoolean(packet.lock);
	}
	
	public static void handle(final PacketTrapPlayers packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			Pocket pocket = PocketRegistryManager.getPocketFromChunkPosition(packet.pos);
						
			if (pocket.exists()) {
				pocket.setTrapState(packet.lock);

				pocket.forceUpdateInsidePocket();
				DimensionalPockets.CONSOLE.debug("[Packet Delivery Success] <trapplayers> Trap Players setting set to: { " + packet.lock +  " } for Pocket: { " + pocket.getDominantChunkPos() + " }");
			}
				
			PocketRegistryManager.saveData();
		});
		
		ctx.setPacketHandled(true);
	}
}
