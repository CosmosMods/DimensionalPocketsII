package com.zeher.zehercraft.transport.core.tile;

import com.zeher.zehercraft.ZCReference;
import com.zeher.zehercraft.transport.core.util.TransportUtil;
import com.zeher.zeherlib.api.azrf.EnumChannelSideState;
import com.zeher.zeherlib.api.azrf.ChannelType;
import com.zeher.zeherlib.api.azrf.EnumChannelSideState;
import com.zeher.zeherlib.api.azrf.IChannel;
import com.zeher.zeherlib.api.azrf.ISidedTile;

import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import cofh.redstoneflux.impl.EnergyStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TileEntityEnergyChannelTransparentSurge extends TileEntity implements IChannel.Energy, ITickable, IEnergyReceiver, IEnergyProvider, ChannelType.IChannelEnergy {

	private EnumChannelSideState[] SIDE_STATE_ARRAY = EnumChannelSideState.getStandardArray();
	
	public int capacity = ZCReference.RESOURCE.TRANSPORT.ENERGY_CAPACITY_SURGE;
	public int max_transfer = ZCReference.RESOURCE.TRANSPORT.ENERGY_MAX_TRANSFER_SURGE;
	
	public EnergyStorage storage = new EnergyStorage(capacity, max_transfer);
	
	public EnumFacing last_facing;
	public int last_rf_rate;

	@Override 
	public EnumChannelSideState getSide(EnumFacing facing) {
		return this.SIDE_STATE_ARRAY[facing.getIndex()];
	}
	
	@Override
	public void setSide(EnumFacing facing, EnumChannelSideState side_state) {
		this.SIDE_STATE_ARRAY[facing.getIndex()] = side_state;
		
		this.updateRenders();
	}
	
	@Override
	public EnumChannelSideState[] getSideArray() {
		return this.SIDE_STATE_ARRAY;
	}

	@Override
	public void setSideArray(EnumChannelSideState[] new_array) {
		this.SIDE_STATE_ARRAY = new_array;
		
		this.updateRenders();
	}

	@Override
	public void cycleSide(EnumFacing facing) {
		EnumChannelSideState next = this.SIDE_STATE_ARRAY[facing.getIndex()].getNextState();
		
		this.setSide(facing, next);
		
		this.updateRenders();
	}

	@Override
	public boolean canConnect(EnumFacing facing) {
		EnumChannelSideState state = SIDE_STATE_ARRAY[facing.getIndex()];
		
		if (state.equals(EnumChannelSideState.DISABLED)) {
			return false;
		}
		return true;
	}

	@Override
	public void updateRenders() {
		Minecraft.getMinecraft().renderGlobal.markBlockRangeForRenderUpdate(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
		world.markBlockRangeForRenderUpdate(pos, pos);
		this.markDirty();
	}
	
	@Override
	public EnumFacing getLastFacing() {
		return this.last_facing;
	}
	
	@Override
	public void setLastFacing(EnumFacing facing) {
		this.last_facing = facing;
	}
	
	@Override
	public int getLastRFRate() {
		return this.last_rf_rate;
	}

	@Override
	public void setLastRFRate(int value) { 
		this.last_rf_rate = value;
	}

	@Override
	public void update() {
		TileEntity tile = this.world.getTileEntity(this.pos);
		
		for (EnumFacing c : EnumFacing.VALUES) {
			TileEntity tile_other = world.getTileEntity(this.pos.offset(c));
			
			if (tile_other instanceof IEnergyReceiver) {
				if (this.hasEnergy() && ((IEnergyReceiver) tile_other).canConnectEnergy(c.getOpposite())) {
					if (tile_other instanceof ChannelType.IChannelEnergy && tile_other instanceof ISidedTile) {
						if (!((ISidedTile) tile_other).getSide(c.getOpposite()).equals(EnumChannelSideState.DISABLED) && !((ISidedTile) tile_other).getSide(c.getOpposite()).equals(EnumChannelSideState.INTERFACE_OUTPUT)) {
							if (this.last_facing != null) {
								if (!c.equals(last_facing.getOpposite())) {
									this.extractEnergy(c, ((IEnergyReceiver) tile_other).receiveEnergy(c.getOpposite(), this.getLastRFRate(), false), false);
								}
							}	
						}
					} else if (tile_other instanceof ISidedTile) {
						if (!((ISidedTile) tile_other).getSide(c.getOpposite()).equals(EnumChannelSideState.DISABLED) && !((ISidedTile) tile_other).getSide(c.getOpposite()).equals(EnumChannelSideState.INTERFACE_OUTPUT)) {
							
							this.extractEnergy(c, ((IEnergyReceiver) tile_other).receiveEnergy(c.getOpposite(), this.getLastRFRate(), false), false);
						}
					} else {
						this.extractEnergy(c, ((IEnergyReceiver) tile_other).receiveEnergy(c.getOpposite(), this.getLastRFRate(), false), false);
					}
				}
			}
		}
	}

	@Override
	public int getEnergyStored(EnumFacing from) {
		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return storage.getMaxEnergyStored();
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		if (this.getSide(from) == EnumChannelSideState.DISABLED) {
			return false;
		}
		return true;
	}

	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		return storage.extractEnergy(maxExtract, simulate);
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		if (this.getEnergyStored(from.getOpposite()) < this.getMaxEnergyStored(from.getOpposite())) {
			if (this.getSide(from.getOpposite()).equals(EnumChannelSideState.DISABLED) || this.getSide(from.getOpposite()).equals(EnumChannelSideState.INTERFACE_OUTPUT)) {
				return 0;
			}
			
			this.last_facing = from.getOpposite();
			
			this.setLastRFRate(0);
			this.setLastRFRate(maxReceive);
			
			return storage.receiveEnergy(maxReceive, simulate);
		}
		
		return 0;
	}
	
	public boolean hasEnergy() {
		return this.getEnergyStored(EnumFacing.DOWN) > 0;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.storage.readFromNBT(compound);
		
		this.last_facing = EnumFacing.getFront(compound.getInteger("last_facing"));
		
		this.SIDE_STATE_ARRAY = new EnumChannelSideState[] { EnumChannelSideState.getStateFromIndex(compound.getInteger("down")),
				EnumChannelSideState.getStateFromIndex(compound.getInteger("up")),
				EnumChannelSideState.getStateFromIndex(compound.getInteger("north")),
				EnumChannelSideState.getStateFromIndex(compound.getInteger("south")),
				EnumChannelSideState.getStateFromIndex(compound.getInteger("west")),
				EnumChannelSideState.getStateFromIndex(compound.getInteger("east"))};
		
		super.readFromNBT(compound);

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		this.storage.writeToNBT(compound);
		
		if (this.last_facing != null) {
			compound.setInteger("last_facing", this.last_facing.getIndex());
		}
		
		compound.setInteger("down", this.SIDE_STATE_ARRAY[0].getIndex());
		compound.setInteger("up", this.SIDE_STATE_ARRAY[1].getIndex());
		compound.setInteger("north", this.SIDE_STATE_ARRAY[2].getIndex());
		compound.setInteger("south", this.SIDE_STATE_ARRAY[3].getIndex());
		compound.setInteger("west", this.SIDE_STATE_ARRAY[4].getIndex());
		compound.setInteger("east", this.SIDE_STATE_ARRAY[5].getIndex());
		
		return super.writeToNBT(compound);
	}

	@Override
	public EnumChannelSideState getStateForConnection(EnumFacing facing) {
		return TransportUtil.getStateForConnectionSurge(facing, getPos(), getWorld(), this);
	}
}