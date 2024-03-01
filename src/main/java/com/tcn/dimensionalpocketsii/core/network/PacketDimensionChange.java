package com.tcn.dimensionalpocketsii.core.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;
import com.tcn.dimensionalpocketsii.pocket.core.shift.Shifter;
import com.tcn.dimensionalpocketsii.pocket.core.shift.ShifterCore;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

public class PacketDimensionChange  {
	
	private UUID playerUUID;
	private ResourceKey<Level> dimension;
	private BlockPos block_pos;
	private EnumShiftDirection direction;
	private float yaw;
	private float pitch;
	private boolean playVanillaSound;
	private boolean sendMessage;
	private boolean safeSpawn;
	
	public PacketDimensionChange(FriendlyByteBuf buf) {
		playerUUID = buf.readUUID();
		
		ResourceLocation location = buf.readResourceLocation();
		dimension = ResourceKey.create(Registries.DIMENSION, location);
		
		block_pos = buf.readBlockPos();
		
		direction = EnumShiftDirection.getDirectionFromIndex(buf.readInt());
		
		yaw = buf.readFloat();
		pitch = buf.readFloat();
		playVanillaSound = buf.readBoolean();
		sendMessage = buf.readBoolean();
		safeSpawn = buf.readBoolean();
	}
	
	public PacketDimensionChange(UUID id, ResourceKey<Level> location, EnumShiftDirection direction, BlockPos pos, float yaw, float pitch, boolean playVanillaSound, boolean sendMessage, boolean safeSpawn) {
		this.playerUUID = id;
		this.dimension = location;
		this.block_pos = pos;
		this.direction = direction;
		
		this.yaw = yaw;
		this.pitch = pitch;
		this.playVanillaSound = playVanillaSound;
		this.sendMessage = sendMessage;
		this.safeSpawn = safeSpawn;
	}
	
	public static void encode(PacketDimensionChange packet, FriendlyByteBuf buf) {
		buf.writeUUID(packet.playerUUID);
		buf.writeResourceLocation(packet.dimension.location());
		buf.writeBlockPos(packet.block_pos);
		buf.writeInt(packet.direction.getIndex());
		buf.writeFloat(packet.yaw);
		buf.writeFloat(packet.pitch);
		buf.writeBoolean(packet.playVanillaSound);
		buf.writeBoolean(packet.sendMessage);
		buf.writeBoolean(packet.safeSpawn);
	}
	
	public static void handle(final PacketDimensionChange packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(packet.playerUUID);
			
			Shifter shifter = Shifter.createTeleporter(packet.dimension, packet.direction, packet.block_pos, packet.yaw, packet.pitch, packet.playVanillaSound, packet.sendMessage, packet.safeSpawn);
			ShifterCore.shiftPlayerToDimension(player, shifter);
		});
		
		ctx.setPacketHandled(true);
	}
}
