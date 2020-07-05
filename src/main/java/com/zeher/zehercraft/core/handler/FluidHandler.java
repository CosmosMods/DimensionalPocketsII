package com.zeher.zehercraft.core.handler;

import com.zeher.zehercraft.ZCReference;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidHandler {
	
	public static final Fluid FLUID_COOLANT = new Fluid("coolant", ZCReference.RESOURCE.BASE.FLUID_COOLANT_STILL, ZCReference.RESOURCE.BASE.FLUID_COOLANT_FLOWING).setDensity(2000).setViscosity(500).setTemperature(-500);
	public static final Fluid FLUID_ENERGIZED_REDSTONE = new Fluid("energized_redstone", ZCReference.RESOURCE.BASE.FLUID_ENERGIZED_REDSTONE_STILL, ZCReference.RESOURCE.BASE.FLUID_ENERGIZED_REDSTONE_FLOWING).setDensity(2500).setViscosity(4000).setTemperature(1500);
	public static final Fluid FLUIS_GLOWSTONE_INFUSED_MAGMA = new Fluid("glowstone_infused_magma", ZCReference.RESOURCE.BASE.FLUID_GLOWSTONE_INFUSED_MAGMA_STILL, ZCReference.RESOURCE.BASE.FLUID_GLOWSTONE_INFUSED_MAGMA_FLOWING).setDensity(4000).setViscosity(6000).setTemperature(2500);
	
	public static void preInitialization() {
		registerFluid(FLUID_COOLANT);
		registerFluid(FLUID_ENERGIZED_REDSTONE);
		registerFluid(FLUIS_GLOWSTONE_INFUSED_MAGMA);
	}
	
	public static void registerFluid(Fluid fluid) {
		FluidRegistry.registerFluid(fluid);
		FluidRegistry.addBucketForFluid(fluid);
	}
}