package com.zeher.zeherlib.api.compat.core.impl;

import com.zeher.zeherlib.api.compat.core.interfaces.EnumSideState;
import com.zeher.zeherlib.api.compat.core.interfaces.ISidedTile;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

/**
 * Reference implementation of {@link ISidedTile}. This is only meant to be an example for how {@link ISidedTile} is used.
 * @author TheRealZeher
 */
public class SidedTileEntity extends TileEntity implements ISidedTile {
		
	public EnumSideState[] SIDE_STATE_ARRAY = EnumSideState.getStandardArray();

	@Override 
	public EnumSideState getSide(EnumFacing facing) {
		return SIDE_STATE_ARRAY[facing.getIndex()];
	}
	
	@Override
	public void setSide(EnumFacing facing, EnumSideState side_state) {
		SIDE_STATE_ARRAY[facing.getIndex()] = side_state;
		
		this.sendUpdates();
	}
	
	@Override
	public EnumSideState[] getSideArray() {
		return this.SIDE_STATE_ARRAY;
	}

	@Override
	public void setSideArray(EnumSideState[] new_array) {
		SIDE_STATE_ARRAY = new_array;
		
		this.sendUpdates();
	}

	@Override
	public void cycleSide(EnumFacing facing) {
		EnumSideState state = SIDE_STATE_ARRAY[facing.getIndex()];
		state = state.getNextState();
		
		this.sendUpdates();
	}

	@Override
	public boolean canConnect(EnumFacing facing) {
		EnumSideState state = SIDE_STATE_ARRAY[facing.getIndex()];
		
		if (state.equals(EnumSideState.DISABLED)) {
			return false;
		}
		return true;
	}

	@Override
	public void sendUpdates() {
		this.world.markBlockRangeForRenderUpdate(this.getPos(), this.getPos());
		this.world.notifyBlockUpdate(this.getPos(), world.getBlockState(this.getPos()), world.getBlockState(this.getPos()), 3);
		this.world.scheduleBlockUpdate(this.getPos(), this.getBlockType(), 0, 0);		
		this.markDirty();
	}
	
}