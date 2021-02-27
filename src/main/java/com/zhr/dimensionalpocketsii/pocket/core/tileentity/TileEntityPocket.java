package com.zhr.dimensionalpocketsii.pocket.core.tileentity;

import com.zhr.dimensionalpocketsii.core.management.bus.BusSubscriberMod;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class TileEntityPocket extends TileEntityPocketBase {
	
	public TileEntityPocket() {
		super(BusSubscriberMod.POCKET_TYPE);
	}

	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("tile.dimensionalpocketsii.pocket");
	}

}