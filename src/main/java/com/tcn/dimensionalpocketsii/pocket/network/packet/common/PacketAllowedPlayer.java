package com.tcn.dimensionalpocketsii.pocket.network.packet.common;

import java.util.function.Supplier;

import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleConnector;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityPocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;

public class PacketAllowedPlayer  {
	
	private BlockPos pos;
	private String player_name;
	private boolean add;
	private ResourceKey<Level> dimension;
	
	public PacketAllowedPlayer(FriendlyByteBuf buf) {
		this.pos = buf.readBlockPos();
		this.player_name = buf.readComponent().getString();
		this.add = buf.readBoolean();
		ResourceLocation location = buf.readResourceLocation();
		this.dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, location);
	}
	
	public PacketAllowedPlayer(BlockPos pos, String playerNameIn, boolean addIn, ResourceKey<Level> dimension) {
		this.pos = pos;
		this.player_name = playerNameIn;
		this.add = addIn;
		this.dimension = dimension;
	}
	
	public static void encode(PacketAllowedPlayer packet, FriendlyByteBuf buf) {
		buf.writeBlockPos(packet.pos);
		buf.writeComponent(new TextComponent(packet.player_name));
		buf.writeBoolean(packet.add);
		buf.writeResourceLocation(packet.dimension.location());
	}
	
	public static void handle(final PacketAllowedPlayer packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
			ServerLevel world = server.getLevel(packet.dimension);
			BlockEntity tile = world.getBlockEntity(packet.pos);
			String player_name = packet.player_name;
			boolean add = packet.add;
			
			if (!player_name.isEmpty()) {
				if (tile instanceof BlockEntityModuleConnector) {
					BlockEntityModuleConnector tile_connector = (BlockEntityModuleConnector) tile;
					
					Pocket pocket = tile_connector.getPocket();
					
					if (pocket.exists()) {
						if (add) {
							pocket.addAllowedPlayerNBT(player_name);
							tile_connector.sendUpdates(true);
							
							sendConsoleMessage(false, true, pocket, player_name);
						} else {
							pocket.removeAllowedPlayerNBT(player_name);
							tile_connector.sendUpdates(true);

							sendConsoleMessage(true, true, pocket, player_name);
						}
					}
				} else if (tile instanceof BlockEntityPocket) {
					BlockEntityPocket tile_pocket = (BlockEntityPocket) tile;
					
					Pocket pocket = tile_pocket.getPocket();
					
					if (pocket.exists()) {
						if (add) {
							pocket.addAllowedPlayerNBT(player_name);
							tile_pocket.sendUpdates(true);

							sendConsoleMessage(false, false, pocket, player_name);
						} else {
							pocket.removeAllowedPlayerNBT(player_name);
							tile_pocket.sendUpdates(true);

							sendConsoleMessage(true, false, pocket, player_name);
						}
					}
				} else {
					DimensionalPockets.CONSOLE.debugWarn("[Packet Delivery Failure] <allowedplayer> Block Entity not equal to expected.");
				}
			}
			
			PocketRegistryManager.saveData();
		});
		
		ctx.setPacketHandled(true);
	}
	
	public static void sendConsoleMessage(boolean removed, boolean connector, Pocket pocket, String added) {
		if (removed) {
			DimensionalPockets.CONSOLE.debug("[Packet Delivery Success] " + (connector ? "{connector}" : "{pocket}") + " <allowedplayer> Allowed Player removed for Pocket: { " + pocket.getChunkPos() + "} Removed by: { " + pocket.getOwnerName() + " } Removed Player: { " + added + " }");
		} else {
			DimensionalPockets.CONSOLE.debug("[Packet Delivery Success]"  + (connector ? "{connector}" : "{pocket}") + " <allowedplayer> Allowed Player added for Pocket: { " + pocket.getChunkPos() + "} Added by: { " + pocket.getOwnerName() + " } Added Player: { " + added + " }");
		}
	}
}
