package com.tcn.dimensionalpocketsii.pocket.core.tileentity;

import com.tcn.dimensionalpocketsii.core.management.ModBusManager;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class TileEntityCharger extends TileEntityChargerBase {

	public TileEntityCharger() {
		super(ModBusManager.CHARGER_TYPE);
	}
	
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("tile.dimensionalpocketsii.charger");
	}
}
