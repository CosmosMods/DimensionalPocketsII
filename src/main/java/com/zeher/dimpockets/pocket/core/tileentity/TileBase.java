package com.zeher.dimpockets.pocket.core.tileentity;

import com.zeher.dimpockets.pocket.core.Pocket;
import com.zeher.zeherlib.mod.tile.ModTileEntityAbstract;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntityType;

public abstract class TileBase extends ModTileEntityAbstract {

	public TileBase(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	public boolean isUsableByPlayer(PlayerEntity player) {
		return world.getTileEntity(pos) == this && player.getDistanceSq(player) <= 64.0D;
	}

	public abstract Pocket getPocket();
}