package com.tcn.dimensionalpocketsii.pocket.network.packet.elytraplate;

import java.util.function.Supplier;

import com.tcn.cosmoslibrary.common.lib.CosmosChunkPos;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

public class EPacketLockToAllowedPlayers  {

	private CosmosChunkPos pos;
	private boolean lock;
	
	public EPacketLockToAllowedPlayers(FriendlyByteBuf buf) {
		this.pos = CosmosChunkPos.convertTo(buf.readBlockPos());
		this.lock = buf.readBoolean();
	}
	
	public EPacketLockToAllowedPlayers(CosmosChunkPos pos, boolean lock) {
		this.pos = pos;
		this.lock = lock;
	}
	
	public static void encode(EPacketLockToAllowedPlayers packet, FriendlyByteBuf buf) {
		buf.writeBlockPos(CosmosChunkPos.convertFrom(packet.pos));
		buf.writeBoolean(packet.lock);
	}
	
	public static void handle(final EPacketLockToAllowedPlayers packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			Pocket pocket = PocketRegistryManager.getPocketFromChunkPosition(packet.pos);
						
			if (pocket.exists()) {
				pocket.setAllowedPlayerState(packet.lock);
			}
				
			PocketRegistryManager.saveData();
		});
		
		ctx.setPacketHandled(true);
	}
}
