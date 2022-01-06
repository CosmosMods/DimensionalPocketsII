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
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

public class PacketHostileSpawnState  {
	
	private BlockPos pos;
	private ResourceKey<Level> dimension;
	private boolean value;
	
	public PacketHostileSpawnState(FriendlyByteBuf buf) {
		this.pos = buf.readBlockPos();
		ResourceLocation location = buf.readResourceLocation();
		this.dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, location);
		this.value = buf.readBoolean();
	}
	
	public PacketHostileSpawnState(BlockPos posIn, ResourceKey<Level> dimensionIn, boolean valueIn) {
		this.pos = posIn;
		this.value = valueIn;
		this.dimension = dimensionIn;
	}
	
	public static void encode(PacketHostileSpawnState packet, FriendlyByteBuf buf) {
		buf.writeBlockPos(packet.pos);
		buf.writeResourceLocation(packet.dimension.location());
		buf.writeBoolean(packet.value);
	}
	
	public static void handle(final PacketHostileSpawnState packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
			ServerLevel world = server.getLevel(packet.dimension);
			BlockEntity tile = world.getBlockEntity(packet.pos);
			boolean value = packet.value;
			
			if (tile instanceof BlockEntityModuleConnector) {
				BlockEntityModuleConnector tile_connector = (BlockEntityModuleConnector) tile;
				Pocket pocket = tile_connector.getPocket();
				
				if (pocket.exists()) {
					pocket.setHostileSpawnState(value);
					tile_connector.sendUpdates(true);
					
					ServerLevel source_world = server.getLevel(pocket.getSourceBlockDimension());
					BlockEntity tile_pocket = source_world.getBlockEntity(pocket.getSourceBlockPos());
					
					if (tile_pocket instanceof BlockEntityPocket) {
						((BlockEntityPocket) tile_pocket).sendUpdates(true);
					}
					
					
					DimensionalPockets.CONSOLE.debug("[Packet Delivery Success] {connector} <hostilespawnstate> Hostile Spawn setting set to: { " + value +  " } for Pocket: { " + pocket.getChunkPos() + "} ");
				}
			} else if (tile instanceof BlockEntityPocket) {
				BlockEntityPocket tile_pocket = (BlockEntityPocket) tile;
				Pocket pocket = tile_pocket.getPocket();
				
				if (pocket.exists()) {
					pocket.setHostileSpawnState(value);
					tile_pocket.sendUpdates(true);
					DimensionalPockets.CONSOLE.debug("[Packet Delivery Success] {pocket} <hostilespawnstate> Hostile Spawn setting set to: { " + value +  " } for Pocket: { " + pocket.getChunkPos() + "} ");
				}
			} else {
				DimensionalPockets.CONSOLE.debugWarn("[Packet Delivery Failure] <hostilespawnstate> Block Entity not equal to expected.");
			}
			
			PocketRegistryManager.saveData();
		});
		
		ctx.setPacketHandled(true);
	}
}
