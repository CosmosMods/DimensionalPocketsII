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

public class PacketEmptyTank  {
	
	private BlockPos pos;
	private ResourceKey<Level> dimension;
	
	public PacketEmptyTank(FriendlyByteBuf buf) {
		this.pos = buf.readBlockPos();
		ResourceLocation location = buf.readResourceLocation();
		this.dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, location);
	}
	
	public PacketEmptyTank(BlockPos pos, ResourceKey<Level> dimension) {
		this.pos = pos;
		this.dimension = dimension;
	}
	
	public static void encode(PacketEmptyTank packet, FriendlyByteBuf buf) {
		buf.writeBlockPos(packet.pos);
		buf.writeResourceLocation(packet.dimension.location());
	}
	
	public static void handle(final PacketEmptyTank packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
			ServerLevel world = server.getLevel(packet.dimension);
			BlockEntity tile = world.getBlockEntity(packet.pos);
			
			if (tile instanceof BlockEntityModuleConnector) {
				BlockEntityModuleConnector tile_connector = (BlockEntityModuleConnector) tile;
				Pocket pocket = tile_connector.getPocket();
				
				if (pocket.exists()) {
					pocket.emptyFluidTank();
					DimensionalPockets.CONSOLE.debug("[Packet Delivery Success] <emptytank> Fluid Tank Emptied for Pocket: { " + pocket.getChunkPos() + "} ");
				}
			} else if (tile instanceof BlockEntityPocket){
				BlockEntityPocket tile_pocket = (BlockEntityPocket) tile;
				Pocket pocket = tile_pocket.getPocket();
				
				if (pocket.exists()) {
					pocket.emptyFluidTank();
					DimensionalPockets.CONSOLE.debug("[Packet Delivery Success] <emptytank> Fluid Tank Emptied for Pocket: { " + pocket.getChunkPos() + "} ");
				}
			} else {
				DimensionalPockets.CONSOLE.debugWarn("[Packet Delivery Failure] <emptytank> Block Entity not equal to expected.");
			}
			
			PocketRegistryManager.saveData();
		});
		
		ctx.setPacketHandled(true);
	}
}
