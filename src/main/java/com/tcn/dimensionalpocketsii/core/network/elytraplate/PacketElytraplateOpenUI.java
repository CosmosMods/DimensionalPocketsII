package com.tcn.dimensionalpocketsii.core.network.elytraplate;

import java.util.UUID;
import java.util.function.Supplier;

import com.tcn.cosmoslibrary.common.lib.CosmosChunkPos;
import com.tcn.dimensionalpocketsii.client.container.MenuProviderElytraplateScreen;
import com.tcn.dimensionalpocketsii.client.container.MenuProviderElytraplateSettings;
import com.tcn.dimensionalpocketsii.core.item.armour.DimensionalElytraplate;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.server.ServerLifecycleHooks;

public class PacketElytraplateOpenUI {
	
	private UUID playerUUID;
	private int index;
	private boolean screen;
	
	public PacketElytraplateOpenUI(FriendlyByteBuf buf) {
		this.playerUUID = buf.readUUID();
		this.index = buf.readInt();
		this.screen = buf.readBoolean();
	}
	
	public PacketElytraplateOpenUI(UUID id, int index, boolean screenIn) {
		this.playerUUID = id;
		this.index = index;
		this.screen = screenIn;
	}
	
	public static void encode(PacketElytraplateOpenUI packet, FriendlyByteBuf buf) {
		buf.writeUUID(packet.playerUUID);
		buf.writeInt(packet.index);
		buf.writeBoolean(packet.screen);
	}
	
	public static void handle(final PacketElytraplateOpenUI packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(packet.playerUUID);
			
			ItemStack stack = player.getInventory().armor.get(packet.index);

			if (stack.hasTag()) {
				CompoundTag compound = stack.getTag();
				
				if (compound.contains("nbt_data")) {
					CompoundTag nbtData = compound.getCompound("nbt_data");
					
					if (nbtData.contains("chunk_pos")) {
						CompoundTag chunkPos = nbtData.getCompound("chunk_pos");

						int x = chunkPos.getInt("x");
						int z = chunkPos.getInt("z");
						CosmosChunkPos chunk = new CosmosChunkPos(x, z);
						
						Pocket pocket_ = PocketRegistryManager.getPocketFromChunkPosition(chunk);
						
						CompoundTag compoundA = new CompoundTag();
						pocket_.writeToNBT(compoundA);
						
						if (pocket_ != null) {
							if (stack.getItem() instanceof DimensionalElytraplate) {
								if (packet.screen) {
									NetworkHooks.openGui(player, new MenuProviderElytraplateScreen(), (packetBuffer) -> {
										packetBuffer.writeNbt(compoundA);
										packetBuffer.writeItemStack(stack, false);
										packetBuffer.writeBoolean(packet.screen);
									});
								} else {
									NetworkHooks.openGui(player, new MenuProviderElytraplateSettings(), (packetBuffer) -> {
										packetBuffer.writeNbt(compoundA);
										packetBuffer.writeItemStack(stack, false);
										packetBuffer.writeBoolean(packet.screen);
									});
								}
							}
						}
					}
				}
			}
			
		});
		
		ctx.setPacketHandled(true);
	}
}