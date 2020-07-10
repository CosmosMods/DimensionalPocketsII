package com.zeher.dimensionalpockets.network.core.packet;

import com.zeher.dimensionalpockets.core.handler.NetworkHandler;
import com.zeher.dimensionalpockets.core.util.DimLogger;
import com.zeher.dimensionalpockets.pocket.tileentity.TileEntityDimensionalPocket;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketCreatorServer implements IMessage {

	private static String name;
	private static BlockPos pos;

	public PacketCreatorServer() { }

	public PacketCreatorServer(String name, BlockPos pos) {
		this.name = name;
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		
	}

	public static class Handler implements IMessageHandler<PacketCreatorServer, IMessage> {

		@Override
		public IMessage onMessage(final PacketCreatorServer message, final MessageContext ctx) {
			TileEntity tile_offset = ctx.getServerHandler().player.world.getTileEntity(pos);

			DimLogger.info("Server_Creator Packet sent. ID: [" + name + "]");
			
			if (!(tile_offset == null)) {
				if (tile_offset instanceof TileEntityDimensionalPocket) {
					if (name != null) {
						((TileEntityDimensionalPocket) tile_offset).getPocket().setCreator(name);
						//NetworkHandler.sendCreatorPacketToClient(name, pos, tile);
					}
				}
			}
			return null;
		}
	}
}
