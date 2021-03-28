package com.tcn.dimensionalpocketsii.pocket.network.packet.block;

import java.util.function.Supplier;

import com.tcn.dimensionalpocketsii.DimensionalPockets;
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

public class PacketSideGuide  {
	
	private BlockPos pos;
	private RegistryKey<World> dimension;
	
	public PacketSideGuide(PacketBuffer buf) {
		this.pos = buf.readBlockPos();
		ResourceLocation location = buf.readResourceLocation();
		this.dimension = RegistryKey.create(Registry.DIMENSION_REGISTRY, location);
	}
	
	public PacketSideGuide(BlockPos pos, RegistryKey<World> dimensionIn) {
		this.pos = pos;
		this.dimension = dimensionIn;
	}
	
	public static void encode(PacketSideGuide packet, PacketBuffer buf) {
		buf.writeBlockPos(packet.pos);
		buf.writeResourceLocation(packet.dimension.location());
	}
	
	public static void handle(final PacketSideGuide packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
			ServerWorld world = server.getLevel(packet.dimension);
			TileEntity tile = world.getBlockEntity(packet.pos);
			
			if (tile instanceof TileEntityPocket) {
				TileEntityPocket tile_pocket = (TileEntityPocket) tile;
			
				tile_pocket.toggleSideGuide();
				
				tile_pocket.sendUpdates(true);
			} else {
				DimensionalPockets.LOGGER.warn("[FAIL] TileEntity not instanceof!");
			}
			
		});
		
		ctx.setPacketHandled(true);
	}
}
