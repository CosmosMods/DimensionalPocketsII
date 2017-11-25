package com.zeher.dimensionalpockets.core.tileentity;

import com.zeher.dimensionalpockets.core.pocket.Pocket;
import com.zeher.trzcore.core.tileentity.TRZTileEntityAbstract;

import net.minecraft.entity.player.EntityPlayer;


public abstract class TileEntityDim extends TRZTileEntityAbstract {

	public boolean isUsableByPlayer(EntityPlayer player) {
		return world.getTileEntity(pos) == this && player.getDistanceSq(pos) <= 64.0D;
	}

	public abstract Pocket getPocket();
}