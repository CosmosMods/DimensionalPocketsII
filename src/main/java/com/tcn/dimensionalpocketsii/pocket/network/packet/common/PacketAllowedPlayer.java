package com.tcn.dimensionalpocketsii.pocket.network.packet.common;

import java.util.function.Supplier;

import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.tcn.dimensionalpocketsii.pocket.core.tileentity.TileEntityConnector;
import com.tcn.dimensionalpocketsii.pocket.core.tileentity.TileEntityPocket;

import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class PacketAllowedPlayer  {
	
	private BlockPos pos;
	private String player_name;
	private boolean add;
	private RegistryKey<World> dimension;
	
	public PacketAllowedPlayer(PacketBuffer buf) {
		this.pos = buf.readBlockPos();
		this.player_name = buf.readComponent().getString();
		this.add = buf.readBoolean();
		ResourceLocation location = buf.readResourceLocation();
		this.dimension = RegistryKey.create(Registry.DIMENSION_REGISTRY, location);
	}
	
	public PacketAllowedPlayer(BlockPos pos, String playerNameIn, boolean addIn, RegistryKey<World> dimension) {
		this.pos = pos;
		this.player_name = playerNameIn;
		this.add = addIn;
		this.dimension = dimension;
	}
	
	public static void encode(PacketAllowedPlayer packet, PacketBuffer buf) {
		buf.writeBlockPos(packet.pos);
		buf.writeComponent(new StringTextComponent(packet.player_name));
		buf.writeBoolean(packet.add);
		buf.writeResourceLocation(packet.dimension.location());
	}
	
	public static void handle(final PacketAllowedPlayer packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
			ServerWorld world = server.getLevel(packet.dimension);
			TileEntity tile = world.getBlockEntity(packet.pos);
			String player_name = packet.player_name;
			boolean add = packet.add;
			
			if (!player_name.isEmpty()) {
				if (tile instanceof TileEntityConnector) {
					TileEntityConnector tile_connector = (TileEntityConnector) tile;
					
					Pocket pocket = tile_connector.getPocket();
					
					if (pocket.exists()) {
						if (add) {
							pocket.addAllowedPlayerNBT(player_name);
							tile_connector.sendUpdates(true);
							
							DimensionalPockets.LOGGER.warn("[SUCCESS] Allowed Player Added! [" + player_name + "]");
						} else {
							pocket.removeAllowedPlayerNBT(player_name);
							tile_connector.sendUpdates(true);
							
							DimensionalPockets.LOGGER.warn("[SUCCESS] Allowed Player Removed! [" + player_name + "]");
						}
					}
				} else if (tile instanceof TileEntityPocket){
					TileEntityPocket tile_pocket = (TileEntityPocket) tile;
					
					Pocket pocket = tile_pocket.getPocket();
					
					if (pocket.exists()) {
						if (add) {
							pocket.addAllowedPlayerNBT(player_name);
							tile_pocket.sendUpdates(true);
							
							DimensionalPockets.LOGGER.warn("[SUCCESS] Allowed Player Added! [" + player_name + "]");
						} else {
							pocket.removeAllowedPlayerNBT(player_name);
							tile_pocket.sendUpdates(true);
							
							DimensionalPockets.LOGGER.warn("[SUCCESS] Allowed Player Removed! [" + player_name + "]");
						}
					}
				} else {
					DimensionalPockets.LOGGER.warn("[FAIL] TileEntity not instanceof!");
				}
			}
			
			PocketRegistryManager.saveData();
		});
		
		ctx.setPacketHandled(true);
	}
}
