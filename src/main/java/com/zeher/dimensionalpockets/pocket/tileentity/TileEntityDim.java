package com.zeher.dimensionalpockets.pocket.tileentity;

import com.zeher.dimensionalpockets.pocket.Pocket;
import com.zeher.zeherlib.mod.tile.ModTileEntityAbstract;

import net.minecraft.entity.player.EntityPlayer;

public abstract class TileEntityDim extends ModTileEntityAbstract {

	public boolean isUsableByPlayer(EntityPlayer player) {
		return world.getTileEntity(pos) == this && player.getDistanceSq(pos) <= 64.0D;
	}

	public abstract Pocket getPocket();
}