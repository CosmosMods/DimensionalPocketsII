package com.tcn.cosmoslibrary.impl.interfaces;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.Direction;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface IFluidStorage {
	public int capacity = 0;
	
	public int getFluidCapacity();
	
	public int fill(FluidStack resource, FluidAction doDrain);
	public FluidStack drain(FluidStack resource, FluidAction doDrain);
	public FluidStack drain(int maxDrain, FluidAction doDrain);
	
	public boolean canFill(Direction from, Fluid fluid);
	public boolean canDrain(Direction from, Fluid fluid);
	
	public FluidTank getTank();
}
