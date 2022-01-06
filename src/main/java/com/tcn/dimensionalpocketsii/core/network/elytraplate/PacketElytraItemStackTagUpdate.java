package com.tcn.dimensionalpocketsii.core.network.elytraplate;

import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;

public class PacketElytraItemStackTagUpdate  {
	
	private UUID playerUUID;
	private int index;
	private CompoundTag stack_tag;
	
	public PacketElytraItemStackTagUpdate(FriendlyByteBuf buf) {
		this.playerUUID = buf.readUUID();
		this.index = buf.readInt();
		this.stack_tag = buf.readNbt();
	}
	
	public PacketElytraItemStackTagUpdate(UUID id, int index, CompoundTag stack_tag) {
		this.playerUUID = id;
		this.index = index;
		this.stack_tag = stack_tag;
	}
	
	public static void encode(PacketElytraItemStackTagUpdate packet, FriendlyByteBuf buf) {
		buf.writeUUID(packet.playerUUID);
		buf.writeInt(packet.index);
		buf.writeNbt(packet.stack_tag);
	}
	
	public static void handle(final PacketElytraItemStackTagUpdate packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(packet.playerUUID);
			
			player.getInventory().armor.get(packet.index).setTag(packet.stack_tag);
			
			//System.out.println("Elytraplate updated successfully.");
		});
		
		ctx.setPacketHandled(true);
	}
}
