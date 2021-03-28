package com.tcn.dimensionalpocketsii.pocket.network.packet.common;

import java.util.function.Supplier;

import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.tcn.dimensionalpocketsii.pocket.core.tileentity.TileEntityConnector;
import com.tcn.dimensionalpocketsii.pocket.core.tileentity.TileEntityPocket;

import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class PacketLockToAllowedPlayers  {
	
	private BlockPos pos;
	private RegistryKey<World> dimension;
	private boolean lock;
	
	public PacketLockToAllowedPlayers(PacketBuffer buf) {
		this.pos = buf.readBlockPos();
		ResourceLocation location = buf.readResourceLocation();
		this.dimension = RegistryKey.create(Registry.DIMENSION_REGISTRY, location);
		this.lock = buf.readBoolean();
	}
	
	public PacketLockToAllowedPlayers(BlockPos pos, RegistryKey<World> dimension, boolean lock) {
		this.pos = pos;
		this.lock = lock;
		this.dimension = dimension;
	}
	
	public static void encode(PacketLockToAllowedPlayers packet, PacketBuffer buf) {
		buf.writeBlockPos(packet.pos);
		buf.writeResourceLocation(packet.dimension.location());
		buf.writeBoolean(packet.lock);
	}
	
	public static void handle(final PacketLockToAllowedPlayers packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
			ServerWorld world = server.getLevel(packet.dimension);
			TileEntity tile = world.getBlockEntity(packet.pos);
			
			if (tile instanceof TileEntityConnector) {
				TileEntityConnector tile_connector = (TileEntityConnector) tile;
				Pocket pocket = tile_connector.getPocket();
				
				if (pocket.exists()) {
					pocket.setAllowedPlayerState(packet.lock);
					tile_connector.sendUpdates(true);
					
					ServerWorld source_world = server.getLevel(pocket.getSourceBlockDimension());
					TileEntity tile_pocket = source_world.getBlockEntity(pocket.getSourceBlockPos());
					
					if (tile_pocket instanceof TileEntityPocket) {
						((TileEntityPocket) tile_pocket).sendUpdates(true);
					}
					
				}
			} else if (tile instanceof TileEntityPocket) {
				TileEntityPocket tile_pocket = (TileEntityPocket) tile;
				Pocket pocket = tile_pocket.getPocket();
				
				if (pocket.exists()) {
					pocket.setAllowedPlayerState(packet.lock);
					tile_pocket.sendUpdates(true);
				}
			} else {
				DimensionalPockets.LOGGER.warn("[FAIL] TileEntity not instanceof!");
			}
			
			PocketRegistryManager.saveData();
		});
		
		ctx.setPacketHandled(true);
	}
}
