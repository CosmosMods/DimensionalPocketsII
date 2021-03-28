package com.tcn.dimensionalpocketsii.core.integration;

import com.tcn.dimensionalpocketsii.DimensionalPockets;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class CoreJeiPlugin implements IModPlugin {
	
	public CoreJeiPlugin() { }

	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(DimensionalPockets.MOD_ID, "integration_jei");
	}
	
	@Override
	public void registerRecipes(IRecipeRegistration registry) { }

}
