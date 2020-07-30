package com.zeher.dimpockets.pocket.core.tileentity;

import com.zeher.dimpockets.core.manager.TileEntityManager;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class PocketTileEntity extends AbstractPocketTileEntity {
	
	public PocketTileEntity() {
		super(TileEntityManager.POCKET);
	}

	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("tile.dimensionalpocketsii.pocket");
	}

}