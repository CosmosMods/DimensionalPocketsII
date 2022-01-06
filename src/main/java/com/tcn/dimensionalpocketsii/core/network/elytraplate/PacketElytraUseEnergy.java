package com.tcn.dimensionalpocketsii.core.network.elytraplate;

import java.util.UUID;
import java.util.function.Supplier;

import com.tcn.dimensionalpocketsii.core.item.armour.DimensionalElytraplate;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;

public class PacketElytraUseEnergy {
	
	private UUID playerUUID;
	private int index;
	private int energyUse;
	
	public PacketElytraUseEnergy(FriendlyByteBuf buf) {
		this.playerUUID = buf.readUUID();
		this.index = buf.readInt();
		this.energyUse = buf.readInt();
	}
	
	public PacketElytraUseEnergy(UUID id, int index, int energyUseIn) {
		this.playerUUID = id;
		this.index = index;
		this.energyUse = energyUseIn;
	}
	
	public static void encode(PacketElytraUseEnergy packet, FriendlyByteBuf buf) {
		buf.writeUUID(packet.playerUUID);
		buf.writeInt(packet.index);
		buf.writeInt(packet.energyUse);
	}
	
	public static void handle(final PacketElytraUseEnergy packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(packet.playerUUID);
			
			ItemStack itemStack = player.getInventory().armor.get(packet.index);
			Item item = itemStack.getItem();
			
			if (item instanceof DimensionalElytraplate) {
				DimensionalElytraplate elytraplate = (DimensionalElytraplate) item;
				
				elytraplate.extractEnergy(itemStack, packet.energyUse, false);
			}
		});
		
		ctx.setPacketHandled(true);
	}
}
