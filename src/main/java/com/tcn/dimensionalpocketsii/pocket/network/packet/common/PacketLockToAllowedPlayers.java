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
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;

public class PacketLockToAllowedPlayers  {
	
	private BlockPos pos;
	private ResourceKey<Level> dimension;
	private boolean lock;
	
	public PacketLockToAllowedPlayers(FriendlyByteBuf buf) {
		this.pos = buf.readBlockPos();
		ResourceLocation location = buf.readResourceLocation();
		this.dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, location);
		this.lock = buf.readBoolean();
	}
	
	public PacketLockToAllowedPlayers(BlockPos pos, ResourceKey<Level> dimension, boolean lock) {
		this.pos = pos;
		this.lock = lock;
		this.dimension = dimension;
	}
	
	public static void encode(PacketLockToAllowedPlayers packet, FriendlyByteBuf buf) {
		buf.writeBlockPos(packet.pos);
		buf.writeResourceLocation(packet.dimension.location());
		buf.writeBoolean(packet.lock);
	}
	
	public static void handle(final PacketLockToAllowedPlayers packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
			ServerLevel world = server.getLevel(packet.dimension);
			BlockEntity tile = world.getBlockEntity(packet.pos);
			boolean value = packet.lock;
			
			if (tile instanceof BlockEntityModuleConnector) {
				BlockEntityModuleConnector tile_connector = (BlockEntityModuleConnector) tile;
				Pocket pocket = tile_connector.getPocket();
				
				if (pocket.exists()) {
					pocket.setAllowedPlayerState(value);
					tile_connector.sendUpdates(true);
					
					ServerLevel source_world = server.getLevel(pocket.getSourceBlockDimension());
					BlockEntity tile_pocket = source_world.getBlockEntity(pocket.getSourceBlockPos());
					
					if (tile_pocket instanceof BlockEntityPocket) {
						((BlockEntityPocket) tile_pocket).sendUpdates(true);
					}
					DimensionalPockets.CONSOLE.debug("[Packet Delivery Success] {connector} <lockallowedplayers> Lock Allowed Players setting set to: { " + value +  " } for Pocket: { " + pocket.getChunkPos() + "} ");
				}
			} else if (tile instanceof BlockEntityPocket) {
				BlockEntityPocket tile_pocket = (BlockEntityPocket) tile;
				Pocket pocket = tile_pocket.getPocket();
				
				if (pocket.exists()) {
					pocket.setAllowedPlayerState(value);
					tile_pocket.sendUpdates(true);
					DimensionalPockets.CONSOLE.debug("[Packet Delivery Success] {pocket} <lockallowedplayers> Lock Allowed Players setting set to: { " + value +  " } for Pocket: { " + pocket.getChunkPos() + "} ");
				}
			} else {
				DimensionalPockets.CONSOLE.debugWarn("[Packet Delivery Failure] <lockallowedplayers> Block Entity not equal to expected.");
			}
			
			PocketRegistryManager.saveData();
		});
		
		ctx.setPacketHandled(true);
	}
}
