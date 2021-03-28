package com.tcn.dimensionalpocketsii.core.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.tcn.cosmoslibrary.common.math.ChunkPos;
import com.tcn.dimensionalpocketsii.core.management.CoreDimensionManager;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class PacketElytraShift  {
	
	private UUID playerUUID;
	private RegistryKey<World> dimension;
	private ChunkPos chunk_pos;
	
	public PacketElytraShift(PacketBuffer buf) {
		playerUUID = buf.readUUID();
		
		ResourceLocation location = buf.readResourceLocation();
		dimension = RegistryKey.create(Registry.DIMENSION_REGISTRY, location);
		
		chunk_pos = ChunkPos.convertTo(buf.readBlockPos());
	}
	
	public PacketElytraShift(UUID id, RegistryKey<World> location, ChunkPos pos) {
		this.playerUUID = id;
		this.dimension = location;
		this.chunk_pos = pos;
	}
	
	public static void encode(PacketElytraShift packet, PacketBuffer buf) {
		buf.writeUUID(packet.playerUUID);
		buf.writeResourceLocation(packet.dimension.location());
		buf.writeBlockPos(ChunkPos.convertFrom(packet.chunk_pos));
	}
	
	public static void handle(final PacketElytraShift packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			ServerPlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(packet.playerUUID);
			
			Pocket pocket = PocketRegistryManager.getPocketFromChunkPosition(packet.chunk_pos);
			
			if (pocket.exists()) {
				if (packet.dimension.equals(CoreDimensionManager.POCKET_WORLD)) {
					pocket.shift(player, EnumShiftDirection.LEAVE, null, null);
				} else {
					pocket.shift(player, EnumShiftDirection.ENTER, null, null);
				}
			}
		});
		
		ctx.setPacketHandled(true);
	}
}
