package com.zeher.dimensionalpockets.network.core.packet;

import com.zeher.dimensionalpockets.core.util.DimLogger;
import com.zeher.dimensionalpockets.pocket.tileentity.TileEntityDimensionalPocket;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketPlayer implements IMessage {

	private static String name;
	private static boolean add;
	private static BlockPos pos;

	public PacketPlayer() { }

	public PacketPlayer(String name, BlockPos pos, boolean add) {
		this.name = name;
		this.add = add;
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) { }

	@Override
	public void toBytes(ByteBuf buf) { }

	public static class Handler implements IMessageHandler<PacketPlayer, IMessage> {
		@Override
		public IMessage onMessage(final PacketPlayer message, final MessageContext ctx) {
			TileEntity tile_offset = ctx.getServerHandler().player.world.getTileEntity(pos);

			//DimLogger.info("Player Packet sent. ID: [" + name + "] [" + add + "]");
			
			if (!(tile_offset == null)) {
				if (tile_offset instanceof TileEntityDimensionalPocket) {
					if (add) {
						((TileEntityDimensionalPocket) tile_offset).getPocket().addAllowedPlayer(name);
					} else {
						((TileEntityDimensionalPocket) tile_offset).getPocket().removeAllowedPlayer(name);
					}
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
