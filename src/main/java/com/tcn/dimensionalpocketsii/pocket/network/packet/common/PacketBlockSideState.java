package com.tcn.dimensionalpocketsii.pocket.network.packet.common;

import java.util.function.Supplier;

import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleConnector;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityPocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

public class PacketBlockSideState  {
	
	private BlockPos pos;
	private int index;
	private ResourceKey<Level> dimension;
	
	public PacketBlockSideState(FriendlyByteBuf buf) {
		this.pos = buf.readBlockPos();
		this.index = buf.readInt();
		ResourceLocation location = buf.readResourceLocation();
		this.dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, location);
	}
	
	public PacketBlockSideState(BlockPos pos, Direction dir, ResourceKey<Level> dimension) {
		this.pos = pos;
		this.index = dir.get3DDataValue();
		this.dimension = dimension;
	}
	
	public static void encode(PacketBlockSideState packet, FriendlyByteBuf buf) {
		buf.writeBlockPos(packet.pos);
		buf.writeInt(packet.index);
		buf.writeResourceLocation(packet.dimension.location());
	}
	
	public static void handle(final PacketBlockSideState packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
			ServerLevel world = server.getLevel(packet.dimension);
			BlockEntity tile = world.getBlockEntity(packet.pos);
			
			if (tile instanceof BlockEntityModuleConnector) {
				BlockEntityModuleConnector tile_connector = (BlockEntityModuleConnector) tile;
				Pocket pocket = tile_connector.getPocket();
				
				if (pocket.exists()) {
					pocket.cycleSide(Direction.from3DDataValue(packet.index), true);
					
					ServerLevel source_world = server.getLevel(pocket.getSourceBlockDimension());
					BlockEntity tile_pocket = source_world.getBlockEntity(pocket.getSourceBlockPos());
					
					if (tile_pocket instanceof BlockEntityPocket) {
						((BlockEntityPocket) tile_pocket).sendUpdates(true);
					}
					DimensionalPockets.CONSOLE.debug("[Packet Delivery Success] {connector} <pocketsidestate> Pocket Side State cycled.");
				}
			} else if (tile instanceof BlockEntityPocket) {
				BlockEntityPocket tile_pocket = (BlockEntityPocket) tile;
				Pocket pocket = tile_pocket.getPocket();
				
				if (pocket.exists()) {
					pocket.cycleSide(Direction.from3DDataValue(packet.index), true);
					
					tile_pocket.sendUpdates(true);

					DimensionalPockets.CONSOLE.debug("[Packet Delivery Success] {pocket} <pocketsidestate> Pocket Side State cycled.");
				}
			} else {
				DimensionalPockets.CONSOLE.debugWarn("[Packet Delivery Failure] <pocketsidestate> Block Entity not equal to expected.");
			}
			
			PocketRegistryManager.saveData();
		});
		
		ctx.setPacketHandled(true);
	}
}
