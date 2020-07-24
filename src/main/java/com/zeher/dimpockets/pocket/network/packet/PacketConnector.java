package com.zeher.dimpockets.pocket.network.packet;

import com.zeher.dimpockets.core.log.ModLogger;
import com.zeher.dimpockets.pocket.core.tileentity.TileConnector;
import com.zeher.dimpockets.pocket.core.tileentity.TilePocket;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketConnector implements IMessage {

	private static BlockPos pos;
	private static String function;
	private static EnumFacing facing;
	private static boolean lock;

	public PacketConnector() { }
	
	public PacketConnector(BlockPos pos, boolean lock) {
		this.function = "lock";
		this.pos = pos;
		this.lock = lock;
	}
	
	public PacketConnector(BlockPos pos, EnumFacing facing) {
		this.function = "side_state";
		this.pos = pos;
		this.facing = facing;
	}

	public PacketConnector(BlockPos pos, String function) {
		this.pos = pos;
		this.function = function;
	}

	@Override
	public void fromBytes(ByteBuf buf) { }

	@Override
	public void toBytes(ByteBuf buf) { }

	public static class Handler implements IMessageHandler<PacketConnector, IMessage> {
		@Override
		public IMessage onMessage(final PacketConnector message, final MessageContext ctx) {
			TileEntity tile_offset = ctx.getServerHandler().player.world.getTileEntity(pos);
			
			if (tile_offset != null) {
				if (tile_offset instanceof TileConnector) {
					
					if (function.equals("empty")) {
						((TileConnector) tile_offset).drain(((TileConnector) tile_offset).getTank().getFluid(), true);
						((TileConnector) tile_offset).sendUpdates();
						
						ModLogger.info("[SUCCESS] drain() { " + pos + " }", PacketConnector.class);
					} 
					
					else if (function.equals("lock")) {
						((TileConnector) tile_offset).setLockState(lock);
						((TileConnector) tile_offset).sendUpdates();
						
						ModLogger.info("[SUCCESS] <Connector>: setLockState() { " + pos + ", " + lock + " }", PacketConnector.class);
					}
					
					else if (function.equals("cycle")) {
						((TileConnector) tile_offset).cycleType();
						((TileConnector) tile_offset).sendUpdates();
						
						ModLogger.info("[SUCCESS] <Connector>: cycleType() { " + pos + " }", PacketConnector.class);
					}
					
					else if (function.equals("side_state")) {
						((TileConnector) tile_offset).cycleSide(facing);
						((TileConnector) tile_offset).sendUpdates();
						
						ModLogger.info("[SUCCESS] <Connector>: cycleSide() { " + pos + ", " + facing.getName() +  " }", PacketConnector.class);
					} else {
						ModLogger.warning("[FAIL] <Connector>: Function parameter not recognised! Contact the Mod Developer!", PacketConnector.class);
					}
				} else {
					ModLogger.warning("[FAIL] <Connector>: Tile is not instanceof WallConnector! Contact the Mod Developer!", PacketConnector.class);
				}
			} else {
				ModLogger.warning("[FAIL] <Connector>: Tile is null! Contact the Mod Developer!", PacketConnector.class);
			}
			return null;
		}
	}
}