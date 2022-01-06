package com.tcn.dimensionalpocketsii.core.network.elytraplate;

import java.util.UUID;
import java.util.function.Supplier;

import com.tcn.dimensionalpocketsii.core.item.armour.DimensionalElytraplate;
import com.tcn.dimensionalpocketsii.core.item.armour.ElytraSettings;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

public class PacketElytraSettingsChange  {
	
	private UUID playerUUID;
	private int index;
	private ElytraSettings setting;
	boolean value;
	
	public PacketElytraSettingsChange(FriendlyByteBuf buf) {
		this.playerUUID = buf.readUUID();
		this.index = buf.readInt();
		this.setting = ElytraSettings.getStateFromIndex(buf.readInt());
		this.value = buf.readBoolean();
	}
	
	public PacketElytraSettingsChange(UUID id, int index, ElytraSettings settingIn, boolean valueIn) {
		this.playerUUID = id;
		this.index = index;
		this.setting = settingIn;
		this.value = valueIn;
	}
	
	public static void encode(PacketElytraSettingsChange packet, FriendlyByteBuf buf) {
		buf.writeUUID(packet.playerUUID);
		buf.writeInt(packet.index);
		buf.writeInt(packet.setting.getIndex());
		buf.writeBoolean(packet.value);
	}
	
	public static void handle(final PacketElytraSettingsChange packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(packet.playerUUID);
			
			ItemStack stack = player.getInventory().armor.get(packet.index);
			
			DimensionalElytraplate.addOrUpdateElytraSetting(stack, packet.setting, packet.value);
		});
		
		ctx.setPacketHandled(true);
	}
}
