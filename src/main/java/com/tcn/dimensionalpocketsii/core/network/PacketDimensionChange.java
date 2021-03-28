package com.tcn.dimensionalpocketsii.core.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;
import com.tcn.dimensionalpocketsii.pocket.core.shift.Shifter;
import com.tcn.dimensionalpocketsii.pocket.core.shift.ShifterCore;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class PacketDimensionChange  {
	
	private UUID playerUUID;
	private RegistryKey<World> dimension;
	private BlockPos block_pos;
	private EnumShiftDirection direction;
	private float yaw;
	private float pitch;
	private boolean playVanillaSound;
	private boolean sendMessage;
	
	public PacketDimensionChange(PacketBuffer buf) {
		playerUUID = buf.readUUID();
		
		ResourceLocation location = buf.readResourceLocation();
		dimension = RegistryKey.create(Registry.DIMENSION_REGISTRY, location);
		
		block_pos = buf.readBlockPos();
		
		direction = EnumShiftDirection.getDirectionFromIndex(buf.readInt());
		
		yaw = buf.readFloat();
		pitch = buf.readFloat();
		playVanillaSound = buf.readBoolean();
		sendMessage = buf.readBoolean();
	}
	
	public PacketDimensionChange(UUID id, RegistryKey<World> location, EnumShiftDirection direction, BlockPos pos, float yaw, float pitch, boolean playVanillaSound, boolean sendMessage) {
		this.playerUUID = id;
		this.dimension = location;
		this.block_pos = pos;
		this.direction = direction;
		
		this.yaw = yaw;
		this.pitch = pitch;
		this.playVanillaSound = playVanillaSound;
		this.sendMessage = sendMessage;
	}
	
	public static void encode(PacketDimensionChange packet, PacketBuffer buf) {
		buf.writeUUID(packet.playerUUID);
		buf.writeResourceLocation(packet.dimension.location());
		buf.writeBlockPos(packet.block_pos);
		buf.writeInt(packet.direction.getIndex());
		buf.writeFloat(packet.yaw);
		buf.writeFloat(packet.pitch);
		buf.writeBoolean(packet.playVanillaSound);
		buf.writeBoolean(packet.sendMessage);
	}
	
	public static void handle(final PacketDimensionChange packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			ServerPlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(packet.playerUUID);
			
			Shifter shifter = Shifter.createTeleporter(packet.dimension, packet.direction, packet.block_pos, packet.yaw, packet.pitch, packet.playVanillaSound, packet.sendMessage);
			ShifterCore.shiftPlayerToDimension(player, shifter);
		});
		
		ctx.setPacketHandled(true);
	}
}
