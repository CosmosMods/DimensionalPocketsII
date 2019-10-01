package com.zeher.dimensionalpockets.network.core.packet;

import com.zeher.dimensionalpockets.core.util.DimLogger;
import com.zeher.dimensionalpockets.pocket.client.tileentity.TileEntityDimensionalPocket;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketCreatorClient implements IMessage {

	private static String name;
	private static BlockPos pos;

	public PacketCreatorClient() { }

	public PacketCreatorClient(String name, BlockPos pos) {
		this.name = name;
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) { }

	@Override
	public void toBytes(ByteBuf buf) { }

	public static class Handler implements IMessageHandler<PacketCreatorClient, IMessage> {
		
		@SideOnly(Side.CLIENT)
		@Override
		public IMessage onMessage(final PacketCreatorClient message, final MessageContext ctx) {
			DimLogger.info("Client_Creator Packet sent. ID: [" + name + "] [" + ctx.side + "]");
			
			//NBTTagCompound tag = new NBTTagCompound();
			//tag.setString("name", name);
			//SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity(pos, 0, tag);
			
			//ctx.getClientHandler().handleUpdateTileEntity(packet);
			
			World world = Minecraft.getMinecraft().world;
			TileEntity world_tile = world.getTileEntity(pos);
			
			if (world.isRemote) {
				((TileEntityDimensionalPocket) world_tile).getPocket().setCreator(name);
			}
			if (world_tile != null) {
				if (world_tile instanceof TileEntityDimensionalPocket) {
					if (!(name.isEmpty())) {
						((TileEntityDimensionalPocket) world_tile).getPocket().setCreator(name);
					}
				}
			}
			return null;
		}
	}
}
