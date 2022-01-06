package com.tcn.dimensionalpocketsii.pocket.network.packet.elytraplate;

import java.util.function.Supplier;

import com.tcn.cosmoslibrary.common.lib.CosmosChunkPos;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

public class EPacketHostileSpawnState  {

	private CosmosChunkPos pos;
	private boolean value;
	
	public EPacketHostileSpawnState(FriendlyByteBuf buf) {
		this.pos = CosmosChunkPos.convertTo(buf.readBlockPos());
		this.value = buf.readBoolean();
	}
	
	public EPacketHostileSpawnState(CosmosChunkPos posIn, boolean valueIn) {
		this.pos = posIn;
		this.value = valueIn;
	}
	
	public static void encode(EPacketHostileSpawnState packet, FriendlyByteBuf buf) {
		buf.writeBlockPos(CosmosChunkPos.convertFrom(packet.pos));
		buf.writeBoolean(packet.value);
	}
	
	public static void handle(final EPacketHostileSpawnState packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			Pocket pocket = PocketRegistryManager.getPocketFromChunkPosition(packet.pos);
			
			if (pocket.exists()) {
				pocket.setHostileSpawnState(packet.value);
				DimensionalPockets.CONSOLE.debug("[Packet Delivery Success] {elytraplate} <hostilespawnstate> Hostile Spawn setting set to: { " + packet.value +  " } for Pocket: { " + pocket.getChunkPos() + "} ");
			}
			
			PocketRegistryManager.saveData();
		});
		
		ctx.setPacketHandled(true);
	}
}