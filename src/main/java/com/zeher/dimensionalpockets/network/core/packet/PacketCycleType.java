package com.zeher.dimensionalpockets.network.core.packet;

import com.zeher.dimensionalpockets.core.util.DimLogger;
import com.zeher.dimensionalpockets.pocket.tileentity.TileEntityDimensionalPocketWallConnector;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketCycleType implements IMessage {
	
	private static BlockPos pos;

	public PacketCycleType() { }

	public PacketCycleType(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) { }

	@Override
	public void toBytes(ByteBuf buf) { }

	public static class Handler implements IMessageHandler<PacketCycleType, IMessage> {
		@Override
		public IMessage onMessage(final PacketCycleType message, final MessageContext ctx) {
			TileEntity tile_offset = ctx.getServerHandler().player.world.getTileEntity(pos);
			
			DimLogger.info("Cycle Packet Sent");
			
			if (!(tile_offset == null)) {
				if (tile_offset instanceof TileEntityDimensionalPocketWallConnector) {
					((TileEntityDimensionalPocketWallConnector) tile_offset).cycleType();
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
