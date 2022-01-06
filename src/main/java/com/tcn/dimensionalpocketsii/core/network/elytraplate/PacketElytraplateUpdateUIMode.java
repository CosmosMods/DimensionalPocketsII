package com.tcn.dimensionalpocketsii.core.network.elytraplate;

import java.util.UUID;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.common.enums.EnumUIMode;
import com.tcn.dimensionalpocketsii.core.item.armour.DimensionalElytraplate;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

public class PacketElytraplateUpdateUIMode  {
	
	private UUID playerUUID;
	private int index;
	private EnumUIMode mode;
	
	public PacketElytraplateUpdateUIMode(FriendlyByteBuf buf) {
		this.playerUUID = buf.readUUID();
		this.index = buf.readInt();
		int index = buf.readInt();
		
		if (index > -1) {
			this.mode = EnumUIMode.getStateFromIndex(index);
		}
	}
	
	public PacketElytraplateUpdateUIMode(UUID id, int indexIn, @Nullable EnumUIMode modeIn) {
		this.playerUUID = id;
		this.index = indexIn;
		this.mode = modeIn;
	}
	
	public static void encode(PacketElytraplateUpdateUIMode packet, FriendlyByteBuf buf) {
		buf.writeUUID(packet.playerUUID);
		buf.writeInt(packet.index);
		
		if (packet.mode != null) {
			buf.writeInt(packet.mode.getIndex());
		} else {
			buf.writeInt(-1);
		}
	}
	
	public static void handle(final PacketElytraplateUpdateUIMode packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(packet.playerUUID);

			ItemStack stack = player.getInventory().armor.get(packet.index);
			
			if (stack != null) {
				if (packet.mode != null) {
					DimensionalElytraplate.setUIMode(stack, packet.mode);
				}
			}
		});
		
		ctx.setPacketHandled(true);
	}
}
