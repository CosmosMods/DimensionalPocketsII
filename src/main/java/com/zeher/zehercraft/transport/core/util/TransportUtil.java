package com.zeher.zehercraft.transport.core.util;

import com.zeher.zehercraft.transport.core.tile.TileEntityEnergyChannel;
import com.zeher.zehercraft.transport.core.tile.TileEntityEnergyChannelSurge;
import com.zeher.zehercraft.transport.core.tile.TileEntityEnergyChannelTransparent;
import com.zeher.zehercraft.transport.core.tile.TileEntityEnergyChannelTransparentSurge;
import com.zeher.zeherlib.api.azrf.EnumChannelSideState;
import com.zeher.zeherlib.api.azrf.EnumChannelSideState;
import com.zeher.zeherlib.api.azrf.ISidedChannelTile;
import com.zeher.zeherlib.api.azrf.ISidedTile;

import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TransportUtil {
	
	public static EnumChannelSideState getStateForConnection(EnumFacing facing, BlockPos pos, World world, ISidedChannelTile tile) {
		TileEntity tile_offset = world.getTileEntity(pos.offset(facing));
		
		if (tile.getSide(facing).equals(EnumChannelSideState.DISABLED)) {
			return EnumChannelSideState.DISABLED;
		} else if (tile.getSide(facing).equals(EnumChannelSideState.INTERFACE_NORMAL)) { 
			return EnumChannelSideState.INTERFACE_NORMAL;
		} else if (tile.getSide(facing).equals(EnumChannelSideState.INTERFACE_INPUT)) { 
			return EnumChannelSideState.INTERFACE_INPUT;
		} else if (tile.getSide(facing).equals(EnumChannelSideState.INTERFACE_OUTPUT)) { 
			return EnumChannelSideState.INTERFACE_OUTPUT;
		} else if (tile_offset != null) {
			if (tile_offset instanceof TileEntityEnergyChannel) {
				if (((TileEntityEnergyChannel) tile_offset).getSide(facing.getOpposite()).equals(EnumChannelSideState.DISABLED)) {
					return EnumChannelSideState.NO_CONN;
				} else {
					return EnumChannelSideState.CABLE;
				}
			} else if (tile_offset instanceof TileEntityEnergyChannelTransparent) {
				if (((TileEntityEnergyChannelTransparent) tile_offset).getSide(facing.getOpposite()).equals(EnumChannelSideState.DISABLED)) {
					return EnumChannelSideState.NO_CONN;
				} else {
					return EnumChannelSideState.CABLE;
				}
			} else if (tile_offset instanceof TileEntityEnergyChannelSurge) {
				if (((TileEntityEnergyChannelSurge) tile_offset).getSide(facing.getOpposite()).equals(EnumChannelSideState.DISABLED)) {
					return EnumChannelSideState.NO_CONN;
				} else {
					return EnumChannelSideState.CABLE_OTHER;
				}
			}  else if (tile_offset instanceof TileEntityEnergyChannelTransparentSurge) {
				if (((TileEntityEnergyChannelTransparentSurge) tile_offset).getSide(facing.getOpposite()).equals(EnumChannelSideState.DISABLED)) {
					return EnumChannelSideState.NO_CONN;
				} else {
					return EnumChannelSideState.CABLE_OTHER;
				}
			} else if (tile_offset instanceof IEnergyReceiver) {
				if (((IEnergyReceiver) tile_offset).canConnectEnergy(facing.getOpposite())) {
					if (tile.getSide(facing).equals(EnumChannelSideState.NO_CONN)) {
						return EnumChannelSideState.INTERFACE_NORMAL;
					} else if (tile.getSide(facing).equals(EnumChannelSideState.INTERFACE_NORMAL)) {
						return EnumChannelSideState.INTERFACE_NORMAL;
					} else if (tile.getSide(facing).equals(EnumChannelSideState.INTERFACE_OUTPUT)) {
						return EnumChannelSideState.INTERFACE_OUTPUT;
					} else if (tile.getSide(facing).equals(EnumChannelSideState.INTERFACE_INPUT)) {
						return EnumChannelSideState.INTERFACE_INPUT;
					} else {
						return EnumChannelSideState.NO_CONN;
					}
				}
			} else if (tile_offset instanceof IEnergyProvider) {
				if (((IEnergyProvider) tile_offset).canConnectEnergy(facing.getOpposite())) {
					if (tile.getSide(facing).equals(EnumChannelSideState.NO_CONN)) {
						return EnumChannelSideState.INTERFACE_NORMAL;
					} else if (tile.getSide(facing).equals(EnumChannelSideState.INTERFACE_NORMAL)) {
						return EnumChannelSideState.INTERFACE_NORMAL;
					} else if (tile.getSide(facing).equals(EnumChannelSideState.INTERFACE_OUTPUT)) {
						return EnumChannelSideState.INTERFACE_OUTPUT;
					} else if (tile.getSide(facing).equals(EnumChannelSideState.INTERFACE_INPUT)) {
						return EnumChannelSideState.INTERFACE_INPUT;
					} else {
						return EnumChannelSideState.NO_CONN;
					}
				}
			}
		}
		return EnumChannelSideState.NO_CONN;
	}
	
	public static EnumChannelSideState getStateForConnectionSurge(EnumFacing facing, BlockPos pos, World world, ISidedChannelTile tile) {
		TileEntity tile_offset = world.getTileEntity(pos.offset(facing));
		
		if (tile.getSide(facing).equals(EnumChannelSideState.DISABLED)) {
			return EnumChannelSideState.DISABLED;
		} else if (tile.getSide(facing).equals(EnumChannelSideState.INTERFACE_NORMAL)) { 
			return EnumChannelSideState.INTERFACE_NORMAL;
		} else if (tile.getSide(facing).equals(EnumChannelSideState.INTERFACE_INPUT)) { 
			return EnumChannelSideState.INTERFACE_INPUT;
		} else if (tile.getSide(facing).equals(EnumChannelSideState.INTERFACE_OUTPUT)) { 
			return EnumChannelSideState.INTERFACE_OUTPUT;
		} else if (tile_offset != null) {
			if (tile_offset instanceof TileEntityEnergyChannel) {
				if (((TileEntityEnergyChannel) tile_offset).getSide(facing.getOpposite()).equals(EnumChannelSideState.DISABLED)) {
					return EnumChannelSideState.NO_CONN;
				} else {
					return EnumChannelSideState.CABLE_OTHER;
				}
			} else if (tile_offset instanceof TileEntityEnergyChannelTransparent) {
				if (((TileEntityEnergyChannelTransparent) tile_offset).getSide(facing.getOpposite()).equals(EnumChannelSideState.DISABLED)) {
					return EnumChannelSideState.NO_CONN;
				} else {
					return EnumChannelSideState.CABLE_OTHER;
				}
			} else if (tile_offset instanceof TileEntityEnergyChannelSurge) {
				if (((TileEntityEnergyChannelSurge) tile_offset).getSide(facing.getOpposite()).equals(EnumChannelSideState.DISABLED)) {
					return EnumChannelSideState.NO_CONN;
				} else {
					return EnumChannelSideState.CABLE;
				}
			}  else if (tile_offset instanceof TileEntityEnergyChannelTransparentSurge) {
				if (((TileEntityEnergyChannelTransparentSurge) tile_offset).getSide(facing.getOpposite()).equals(EnumChannelSideState.DISABLED)) {
					return EnumChannelSideState.NO_CONN;
				} else {
					return EnumChannelSideState.CABLE;
				}
			} else if (tile_offset instanceof IEnergyReceiver) {
				if (((IEnergyReceiver) tile_offset).canConnectEnergy(facing.getOpposite())) {
					if (tile.getSide(facing).equals(EnumChannelSideState.NO_CONN)) {
						return EnumChannelSideState.INTERFACE_NORMAL;
					} else if (tile.getSide(facing).equals(EnumChannelSideState.INTERFACE_NORMAL)) {
						return EnumChannelSideState.INTERFACE_NORMAL;
					} else if (tile.getSide(facing).equals(EnumChannelSideState.INTERFACE_OUTPUT)) {
						return EnumChannelSideState.INTERFACE_OUTPUT;
					} else if (tile.getSide(facing).equals(EnumChannelSideState.INTERFACE_INPUT)) {
						return EnumChannelSideState.INTERFACE_INPUT;
					} else {
						return EnumChannelSideState.NO_CONN;
					}
				}
			} else if (tile_offset instanceof IEnergyProvider) {
				if (((IEnergyProvider) tile_offset).canConnectEnergy(facing.getOpposite())) {
					if (tile.getSide(facing).equals(EnumChannelSideState.NO_CONN)) {
						return EnumChannelSideState.INTERFACE_NORMAL;
					} else if (tile.getSide(facing).equals(EnumChannelSideState.INTERFACE_NORMAL)) {
						return EnumChannelSideState.INTERFACE_NORMAL;
					} else if (tile.getSide(facing).equals(EnumChannelSideState.INTERFACE_OUTPUT)) {
						return EnumChannelSideState.INTERFACE_OUTPUT;
					} else if (tile.getSide(facing).equals(EnumChannelSideState.INTERFACE_INPUT)) {
						return EnumChannelSideState.INTERFACE_INPUT;
					} else {
						return EnumChannelSideState.NO_CONN;
					}
				}
			}
		}
		return EnumChannelSideState.NO_CONN;
	}
}