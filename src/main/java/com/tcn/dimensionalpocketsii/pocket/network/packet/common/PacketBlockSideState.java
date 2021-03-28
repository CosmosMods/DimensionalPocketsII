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
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class PacketBlockSideState  {
	
	private BlockPos pos;
	private int index;
	private RegistryKey<World> dimension;
	
	public PacketBlockSideState(PacketBuffer buf) {
		this.pos = buf.readBlockPos();
		this.index = buf.readInt();
		ResourceLocation location = buf.readResourceLocation();
		this.dimension = RegistryKey.create(Registry.DIMENSION_REGISTRY, location);
	}
	
	public PacketBlockSideState(BlockPos pos, Direction dir, RegistryKey<World> dimension) {
		this.pos = pos;
		this.index = dir.get3DDataValue();
		this.dimension = dimension;
	}
	
	public static void encode(PacketBlockSideState packet, PacketBuffer buf) {
		buf.writeBlockPos(packet.pos);
		buf.writeInt(packet.index);
		buf.writeResourceLocation(packet.dimension.location());
	}
	
	public static void handle(final PacketBlockSideState packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
			ServerWorld world = server.getLevel(packet.dimension);
			TileEntity tile = world.getBlockEntity(packet.pos);
			
			if (tile instanceof TileEntityConnector) {
				TileEntityConnector tile_connector = (TileEntityConnector) tile;
				
				Pocket pocket = tile_connector.getPocket();
				
				if (pocket.exists()) {
					pocket.cycleSide(Direction.from3DDataValue(packet.index), true);
					
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
					pocket.cycleSide(Direction.from3DDataValue(packet.index), true);
					
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
