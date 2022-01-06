package com.tcn.dimensionalpocketsii.pocket.network.packet.elytraplate;

import java.util.function.Supplier;

import com.tcn.cosmoslibrary.common.lib.CosmosChunkPos;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class EPacketEmptyTank  {

	private CosmosChunkPos pos;
	
	public EPacketEmptyTank(FriendlyByteBuf buf) {
		this.pos = CosmosChunkPos.convertTo(buf.readBlockPos());
	}
	
	public EPacketEmptyTank(CosmosChunkPos pos) {
		this.pos = pos;
	}
	
	public static void encode(EPacketEmptyTank packet, FriendlyByteBuf buf) {
		buf.writeBlockPos(CosmosChunkPos.convertFrom(packet.pos));
	}
	
	public static void handle(final EPacketEmptyTank packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			Pocket pocket = PocketRegistryManager.getPocketFromChunkPosition(packet.pos);
			
			if (pocket.exists()) {
				pocket.emptyFluidTank();
				DimensionalPockets.CONSOLE.debug("[Packet Delivery Success] <emptytank> Fluid Tank Emptied for Pocket: { " + pocket.getChunkPos() + "} ");
			}
			
			PocketRegistryManager.saveData();
		});
		
		ctx.setPacketHandled(true);
	}
}
