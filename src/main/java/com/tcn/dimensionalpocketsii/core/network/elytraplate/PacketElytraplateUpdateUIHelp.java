package com.tcn.dimensionalpocketsii.core.network.elytraplate;

import java.util.UUID;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.common.enums.EnumUIHelp;
import com.tcn.dimensionalpocketsii.core.item.armour.DimensionalElytraplate;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

public class PacketElytraplateUpdateUIHelp  {
	
	private UUID playerUUID;
	private int index;
	private EnumUIHelp mode;
	
	public PacketElytraplateUpdateUIHelp(FriendlyByteBuf buf) {
		this.playerUUID = buf.readUUID();
		this.index = buf.readInt();
		int index = buf.readInt();
		
		if (index > -1) {
			this.mode = EnumUIHelp.getStateFromIndex(index);
		}
	}
	
	public PacketElytraplateUpdateUIHelp(UUID id, int indexIn, @Nullable EnumUIHelp modeIn) {
		this.playerUUID = id;
		this.index = indexIn;
		this.mode = modeIn;
	}
	
	public static void encode(PacketElytraplateUpdateUIHelp packet, FriendlyByteBuf buf) {
		buf.writeUUID(packet.playerUUID);
		buf.writeInt(packet.index);
		
		if (packet.mode != null) {
			buf.writeInt(packet.mode.getIndex());
		} else {
			buf.writeInt(-1);
		}
	}
	
	public static void handle(final PacketElytraplateUpdateUIHelp packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(packet.playerUUID);

			ItemStack stack = player.getInventory().armor.get(packet.index);
			
			if (stack != null) {
				if (packet.mode != null) {
					DimensionalElytraplate.setUIHelp(stack, packet.mode);
				}
			}
		});
		
		ctx.setPacketHandled(true);
	}
}
