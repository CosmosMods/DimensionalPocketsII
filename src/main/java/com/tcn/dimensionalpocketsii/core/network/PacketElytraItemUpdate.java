package com.tcn.dimensionalpocketsii.core.network;

import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class PacketElytraItemUpdate  {
	
	private UUID playerUUID;
	private int index;
	private CompoundNBT stack_tag;
	
	public PacketElytraItemUpdate(PacketBuffer buf) {
		this.playerUUID = buf.readUUID();
		this.index = buf.readInt();
		this.stack_tag = buf.readNbt();
	}
	
	public PacketElytraItemUpdate(UUID id, int index, CompoundNBT stack_tag) {
		this.playerUUID = id;
		this.index = index;
		this.stack_tag = stack_tag;
	}
	
	public static void encode(PacketElytraItemUpdate packet, PacketBuffer buf) {
		buf.writeUUID(packet.playerUUID);
		buf.writeInt(packet.index);
		buf.writeNbt(packet.stack_tag);
	}
	
	public static void handle(final PacketElytraItemUpdate packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			ServerPlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(packet.playerUUID);
			
			player.inventory.armor.get(packet.index).setTag(packet.stack_tag);
			
			System.out.println("Elytraplate updated successfully.");
		});
		
		ctx.setPacketHandled(true);
	}
}
