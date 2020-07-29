package com.zeher.zeherlib.api.compat.core.interfaces;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.Direction;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface IFluidStorage {
	public int capacity = 0;
	
	public int getFluidCapacity();
	
	public int fill(FluidStack resource, boolean doDrain);
	public FluidStack drain(FluidStack resource, boolean doDrain);
	public FluidStack drain(int maxDrain, boolean doDrain);
	
	public boolean canFill(Direction from, Fluid fluid);
	public boolean canDrain(Direction from, Fluid fluid);
	
	public FluidTank getTank();
}
