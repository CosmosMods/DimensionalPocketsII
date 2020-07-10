package com.zeher.dimensionalpockets.network.core.packet;

import com.zeher.dimensionalpockets.core.util.DimLogger;
import com.zeher.dimensionalpockets.pocket.tileentity.TileEntityDimensionalPocket;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketLock implements IMessage {

	private static boolean lock;
	private static BlockPos pos;

	public PacketLock() { }

	public PacketLock(BlockPos pos, boolean lock) {
		this.lock = lock;
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) { }

	@Override
	public void toBytes(ByteBuf buf) { }

	public static class Handler implements IMessageHandler<PacketLock, IMessage> {
		@Override
		public IMessage onMessage(final PacketLock message, final MessageContext ctx) {
			TileEntity tile_offset = ctx.getServerHandler().player.world.getTileEntity(pos);
			
			DimLogger.info("Lock Packet Sent");
			
			if (!(tile_offset == null)) {
				if (tile_offset instanceof TileEntityDimensionalPocket) {
					((TileEntityDimensionalPocket) tile_offset).setLockState(lock);
				} else {
					DimLogger.warning("Tile is not instanceof DimPocket");
				}
			} else {
				DimLogger.warning("Tile is null");
			}

			return null;
		}
	}
}
