package com.tcn.cosmoslibrary.impl;

import com.tcn.cosmoslibrary.impl.enums.EnumSideState;
import com.tcn.cosmoslibrary.impl.interfaces.tile.ISidedTile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

/**
 * Reference implementation of {@link ISidedTile}. This is only meant to be an example for how {@link ISidedTile} is used.
 * @author TheRealZeher
 */
public abstract class SidedTileEntity extends TileEntity implements ISidedTile {
		
	public SidedTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
		// TODO Auto-generated constructor stub
	}

	public EnumSideState[] SIDE_STATE_ARRAY = EnumSideState.getStandardArray();

	@Override 
	public EnumSideState getSide(Direction facing) {
		return SIDE_STATE_ARRAY[facing.getIndex()];
	}
	
	@Override
	public void setSide(Direction facing, EnumSideState side_state, boolean update) {
		SIDE_STATE_ARRAY[facing.getIndex()] = side_state;
		
		this.sendUpdates(update);
	}
	
	@Override
	public EnumSideState[] getSideArray() {
		return this.SIDE_STATE_ARRAY;
	}

	@Override
	public void setSideArray(EnumSideState[] new_array, boolean update) {
		SIDE_STATE_ARRAY = new_array;
		
		this.sendUpdates(update);
	}

	@Override
	public void cycleSide(Direction facing, boolean update) {
		EnumSideState state = SIDE_STATE_ARRAY[facing.getIndex()];
		state = state.getNextState();
		
		this.sendUpdates(update);
	}

	@Override
	public boolean canConnect(Direction facing) {
		EnumSideState state = SIDE_STATE_ARRAY[facing.getIndex()];
		
		if (state.equals(EnumSideState.DISABLED)) {
			return false;
		}
		return true;
	}

	@Override
	public void sendUpdates(boolean update) {
		this.world.notifyBlockUpdate(this.getPos(), world.getBlockState(this.getPos()), world.getBlockState(this.getPos()), 3);
		this.markDirty();
	}
	
}