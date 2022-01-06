package com.tcn.dimensionalpocketsii.pocket.network.packet.elytraplate;

import java.util.function.Supplier;

import com.tcn.cosmoslibrary.common.lib.CosmosChunkPos;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.network.NetworkEvent;

public class EPacketAllowedPlayer  {
	
	private CosmosChunkPos pos;
	private String player_name;
	private boolean add;
	
	public EPacketAllowedPlayer(FriendlyByteBuf buf) {
		this.pos = CosmosChunkPos.convertTo(buf.readBlockPos());
		this.player_name = buf.readComponent().getString();
		this.add = buf.readBoolean();
	}
	
	public EPacketAllowedPlayer(CosmosChunkPos pos, String playerNameIn, boolean addIn) {
		this.pos = pos;
		this.player_name = playerNameIn;
		this.add = addIn;
	}
	
	public static void encode(EPacketAllowedPlayer packet, FriendlyByteBuf buf) {
		buf.writeBlockPos(CosmosChunkPos.convertFrom(packet.pos));
		buf.writeComponent(new TextComponent(packet.player_name));
		buf.writeBoolean(packet.add);
	}
	
	public static void handle(final EPacketAllowedPlayer packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			String player_name = packet.player_name;
			boolean add = packet.add;
			
			Pocket pocket = PocketRegistryManager.getPocketFromChunkPosition(packet.pos);
			
			if (!player_name.isEmpty()) {
				if (pocket.exists()) {
					if (add) {
						pocket.addAllowedPlayerNBT(player_name);
						
						sendConsoleMessage(false, true, pocket, player_name);
					} else {
						pocket.removeAllowedPlayerNBT(player_name);

						sendConsoleMessage(true, true, pocket, player_name);
					}
				}
			} 
			
			PocketRegistryManager.saveData();
		});
		
		ctx.setPacketHandled(true);
	}
	
	public static void sendConsoleMessage(boolean removed, boolean connector, Pocket pocket, String added) {
		if (removed) {
			DimensionalPockets.CONSOLE.debug("[Packet Delivery Success] " + "{elytraplate}" + " <allowedplayer> Allowed Player removed for Pocket: { " + pocket.getChunkPos() + "} Removed by: { " + pocket.getOwnerName() + " } Removed Player: { " + added + " }");
		} else {
			DimensionalPockets.CONSOLE.debug("[Packet Delivery Success]"  + "{elytraplate}" + " <allowedplayer> Allowed Player added for Pocket: { " + pocket.getChunkPos() + "} Added by: { " + pocket.getOwnerName() + " } Added Player: { " + added + " }");
		}
	}
}
