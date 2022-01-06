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

public class PacketTrapPlayers  {
	
	private BlockPos pos;
	private ResourceKey<Level> dimension;
	private boolean trap;
	
	public PacketTrapPlayers(FriendlyByteBuf buf) {
		this.pos = buf.readBlockPos();
		ResourceLocation location = buf.readResourceLocation();
		this.dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, location);
		this.trap = buf.readBoolean();
	}
	
	public PacketTrapPlayers(BlockPos pos, ResourceKey<Level> dimension, boolean lock) {
		this.pos = pos;
		this.trap = lock;
		this.dimension = dimension;
	}
	
	public static void encode(PacketTrapPlayers packet, FriendlyByteBuf buf) {
		buf.writeBlockPos(packet.pos);
		buf.writeResourceLocation(packet.dimension.location());
		buf.writeBoolean(packet.trap);
	}
	
	public static void handle(final PacketTrapPlayers packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
			ServerLevel world = server.getLevel(packet.dimension);
			BlockEntity tile = world.getBlockEntity(packet.pos);
			boolean value = packet.trap;
			
			if (tile instanceof BlockEntityModuleConnector) {
				BlockEntityModuleConnector tile_connector = (BlockEntityModuleConnector) tile;
				Pocket pocket = tile_connector.getPocket();
				
				if (pocket.exists()) {
					pocket.setTrapState(value);
					tile_connector.sendUpdates(true);
					
					ServerLevel source_world = server.getLevel(pocket.getSourceBlockDimension());
					BlockEntity tile_pocket = source_world.getBlockEntity(pocket.getSourceBlockPos());
					
					if (tile_pocket instanceof BlockEntityPocket) {
						((BlockEntityPocket) tile_pocket).sendUpdates(true);
					}
					DimensionalPockets.CONSOLE.debug("[Packet Delivery Success] {connector} <trapplayers> Trap Players setting set to: { " + value +  " } for Pocket: { " + pocket.getChunkPos() + "} ");
				}
			} else if (tile instanceof BlockEntityPocket) {
				BlockEntityPocket tile_pocket = (BlockEntityPocket) tile;
				Pocket pocket = tile_pocket.getPocket();
				
				if (pocket.exists()) {
					pocket.setTrapState(value);
				}
				DimensionalPockets.CONSOLE.debug("[Packet Delivery Success] {pocket} <trapplayers> Trap Players setting set to: { " + value +  " } for Pocket: { " + pocket.getChunkPos() + "} ");
				
				tile_pocket.sendUpdates(true);
			} else {
				DimensionalPockets.CONSOLE.debugWarn("[Packet Delivery Failure] <trapplayers> Block Entity not equal to expected.");
			}
			
			PocketRegistryManager.saveData();
		});
		
		ctx.setPacketHandled(true);
	}
}
