package com.zeher.zeherlib.mod.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class ModFluid extends Fluid {

	public ModFluid(String fluidName, ResourceLocation still, ResourceLocation flowing) {
		super(fluidName, still, flowing);
		this.setUnlocalizedName(fluidName);
	}
}