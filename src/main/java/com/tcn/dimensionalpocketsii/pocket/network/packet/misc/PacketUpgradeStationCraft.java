package com.tcn.dimensionalpocketsii.pocket.network.packet.misc;

import java.util.function.Supplier;

import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.core.management.DimensionManager;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleUpgradeStation;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;

public class PacketUpgradeStationCraft  {
	
	private BlockPos pos;
	
	public PacketUpgradeStationCraft(FriendlyByteBuf buf) {
		this.pos = buf.readBlockPos();
	}
	
	public PacketUpgradeStationCraft(BlockPos pos) {
		this.pos = pos;
	}
	
	public static void encode(PacketUpgradeStationCraft packet, FriendlyByteBuf buf) {
		buf.writeBlockPos(packet.pos);
	}
	
	public static void handle(final PacketUpgradeStationCraft packet, Supplier<NetworkEvent.Context> context) {
		NetworkEvent.Context ctx = context.get();
		
		ctx.enqueueWork(() -> {
			ServerLevel world = ServerLifecycleHooks.getCurrentServer().getLevel(DimensionManager.POCKET_WORLD);
			BlockEntity tile = world.getBlockEntity(packet.pos);
			
			if (tile instanceof BlockEntityModuleUpgradeStation) {
				BlockEntityModuleUpgradeStation tileEntity = (BlockEntityModuleUpgradeStation) tile;
				
				tileEntity.craftItem();
				DimensionalPockets.CONSOLE.debug("[Packet Delivery Success] {upgradestation} <craft> Item crafted.");
			} else {
				DimensionalPockets.CONSOLE.debugWarn("[Packet Delivery Failure] {upgradestation} <craft> Block Entity not equal to expected.");
			}
		});
		
		ctx.setPacketHandled(true);
	}
}
