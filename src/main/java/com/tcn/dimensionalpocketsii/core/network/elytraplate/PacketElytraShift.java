package com.tcn.dimensionalpocketsii.core.network.elytraplate;

import java.util.UUID;
import java.util.function.Supplier;

import com.tcn.cosmoslibrary.common.lib.CosmosChunkPos;
import com.tcn.dimensionalpocketsii.core.management.DimensionManager;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.registry.StorageManager;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

public class PacketElytraShift  {
	
	private UUID playerUUID;
	private ResourceKey<Level> dimension;
	private CosmosChunkPos chunk_pos;
	
	public PacketElytraShift(FriendlyByteBuf buf) {
		playerUUID = buf.readUUID();
		
		ResourceLocation location = buf.readResourceLocation();
		dimension = ResourceKey.create(Registries.DIMENSION, location);
		
		chunk_pos = CosmosChunkPos.convertTo(buf.readBlockPos());
	}
	
	public PacketElytraShift(UUID id, ResourceKey<Level> location, CosmosChunkPos pos) {
		this.playerUUID = id;
		this.dimension = location;
		this.chunk_pos = pos;
	}
	
	public static void encode(PacketElytraShift packet, FriendlyByteBuf buf) {
		buf.writeUUID(packet.playerUUID);
		buf.writeResourceLocation(packet.dimension.location());
		buf.writeBlockPos(CosmosChunkPos.convertFrom(packet.chunk_pos));
	}
	
	public static void handle(final PacketElytraShift packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(packet.playerUUID);
			
			Pocket pocket = StorageManager.getPocketFromChunkPosition(null, packet.chunk_pos);
			
			if (pocket.exists()) {
				if (packet.dimension.equals(DimensionManager.POCKET_WORLD)) {
					pocket.shift(player, EnumShiftDirection.LEAVE, null, null, null);
				} else {
					pocket.shift(player, EnumShiftDirection.ENTER, null, null, null);
				}
			}
		});
		
		ctx.setPacketHandled(true);
	}
}
