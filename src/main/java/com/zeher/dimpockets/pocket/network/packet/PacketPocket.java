package com.zeher.dimpockets.pocket.network.packet;

import javax.annotation.Nullable;

import com.zeher.dimpockets.DimReference;
import com.zeher.dimpockets.core.log.ModLogger;
import com.zeher.dimpockets.pocket.core.tileentity.TileConnector;
import com.zeher.dimpockets.pocket.core.tileentity.TilePocket;
import com.zeher.zeherlib.api.compat.core.interfaces.EnumSideState;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketPocket implements IMessage {

	private static BlockPos pos;
	
	private static int dimension;
	
	private static String function;
	private static String name;
	private static String player_name;
	
	private static boolean lock;
	private static boolean sides;
	private static boolean add;
	
	private static EnumFacing facing;

	public PacketPocket() { }
	
	public PacketPocket(BlockPos pos, int meta) {
		this.function = "empty";
		this.pos = pos;
	}
	
	public PacketPocket(BlockPos pos, boolean sides, @Nullable int meta) {
		this.function = "sides";
		this.pos = pos;
		this.sides = sides;
	}
	
	public PacketPocket(BlockPos pos, String creator) {
		this.function = "creator";
		this.pos = pos;
		this.name = creator;
	}
	
	public PacketPocket(BlockPos pos, boolean lock) {
		this.function = "lock";
		this.pos = pos;
		this.lock = lock;
	}
	
	public PacketPocket(BlockPos pos, boolean add, String player_name) {
		this.function = "player";
		this.pos = pos;
		this.add = add;
		this.player_name = player_name;
	}

	public PacketPocket(BlockPos pos, EnumFacing facing) {
		this.function = "side_state";
		this.pos = pos;
		this.facing = facing;
	}
	
	public PacketPocket(BlockPos pos, int dimension, EnumFacing facing) {
		this.function = "side_stateIn";
		this.pos = pos;
		this.facing = facing;
		this.dimension = dimension;
	}

	@Override
	public void fromBytes(ByteBuf buf) { }

	@Override
	public void toBytes(ByteBuf buf) { }

	public static class Handler implements IMessageHandler<PacketPocket, IMessage> {
		@Override
		public IMessage onMessage(final PacketPocket message, final MessageContext ctx) {
			TileEntity tile_offset = ctx.getServerHandler().player.world.getTileEntity(pos);
			
			if (function.equals("side_stateIn")) {
				TileEntity pocket = FMLCommonHandler.instance().getMinecraftServerInstance().getServer().getWorld(dimension).getTileEntity(pos);

				if (pocket != null) {
					if (pocket instanceof TilePocket) {
						((TilePocket) pocket).cycleSide(facing);
						((TilePocket) pocket).sendUpdates();
		
						ModLogger.info("[SUCCESS] <Pocket / Connector>: setSide() { " + pos + ", " + facing.getName() + " }", PacketPocket.class);
					} else {
						ModLogger.warning("[FAIL] <Pocket / Connector>: Tile not instanceof DimPocket!", PacketPocket.class);
					}
				} else {
					ModLogger.warning("[FAIL] <Pocket / Connector>: Tile null!", PacketPocket.class);
				}
			}
			
			else if (tile_offset != null) {
				if (tile_offset instanceof TilePocket) {
					
					if (function.equals("side_state")) {
						((TilePocket) tile_offset).cycleSide(facing);
						((TilePocket) tile_offset).sendUpdates();
	
						ModLogger.info("[SUCCESS] <Pocket / Block>: setSide() { " + pos + ", " + facing.getName() + " }", PacketPocket.class);
					} 
					
					else if (function.equals("lock")) {
						((TilePocket) tile_offset).setLockState(lock);
						((TilePocket) tile_offset).sendUpdates();
						
						ModLogger.info("[SUCCESS] <Pocket / Block>: setLockState() { " + pos + ", " + lock + " }", PacketPocket.class);
					}
					
					else if (function.equals("creator")) {
						((TilePocket) tile_offset).getPocket().setCreator(name);
						
						ModLogger.info("[SUCCESS] <Pocket / Block>: setCreator() { " + pos + ", " + name + " }", PacketPocket.class);
					}
					
					else if (function.equals("sides")) {
						((TilePocket) tile_offset).setSidesState(sides);
						((TilePocket) tile_offset).sendUpdates();

						ModLogger.info("[SUCCESS] <Pocket / Block>: setSidesState() { " + pos + ", " + sides + " }", PacketPocket.class);
					} 
					
					else if (function.equals("player")) {
						if (add) {
							((TilePocket) tile_offset).getPocket().addToPlayerMap(player_name);
							
							ModLogger.info("[SUCCESS] <Pocket / Block>: addPlayerToMap() { " + pos + ", " + player_name + " }", PacketPocket.class);
						} else {
							((TilePocket) tile_offset).getPocket().removeFromPlayerMap(player_name);
							
							ModLogger.info("[SUCCESS] <Pocket / Block>: removeFromPlayerMap() { " + pos + ", " + player_name + " }", PacketPocket.class);
						}
					} 
					
					else if (function.equals("empty")) {
						((TilePocket) tile_offset).drain(((TilePocket) tile_offset).getTank().getFluid(), true);
						((TilePocket) tile_offset).sendUpdates();
						
						ModLogger.info("[SUCCESS] <Pocket / Block>: drain() { " + pos + " }", PacketPocket.class);
					} else {
						ModLogger.warning("[FAIL] <Pocket / Block>: Function parameter not recognised! Contact the Mod Developer!", PacketPocket.class);
					}
				} else {
					ModLogger.warning("[FAIL] <Pocket / Block>: Tile not instanceof DimPocket! Contact the Mod Developer!", PacketPocket.class);
				}
			} else {
				ModLogger.warning("[FAIL] <Pocket / Block>: Tile null! Contact the Mod Developer!", PacketPocket.class);
			}
			
			return null;
		}
	}
}