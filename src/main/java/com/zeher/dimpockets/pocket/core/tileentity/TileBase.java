package com.zeher.dimpockets.pocket.core.tileentity;

import com.zeher.dimpockets.pocket.core.Pocket;
import com.zeher.zeherlib.mod.tile.ModTileEntityAbstract;

import net.minecraft.entity.player.EntityPlayer;

public abstract class TileBase extends ModTileEntityAbstract {

	public boolean isUsableByPlayer(EntityPlayer player) {
		return world.getTileEntity(pos) == this && player.getDistanceSq(pos) <= 64.0D;
	}

	public abstract Pocket getPocket();
}