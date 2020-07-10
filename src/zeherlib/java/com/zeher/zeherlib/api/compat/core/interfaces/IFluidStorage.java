package com.zeher.zeherlib.api.compat.core.interfaces;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public interface IFluidStorage {
	public int capacity = 0;
	
	public int getFluidCapacity();
	
	public int fill(FluidStack resource, boolean doDrain);
	public FluidStack drain(FluidStack resource, boolean doDrain);
	public FluidStack drain(int maxDrain, boolean doDrain);
	
	public boolean canFill(EnumFacing from, Fluid fluid);
	public boolean canDrain(EnumFacing from, Fluid fluid);
	
	public FluidTankInfo[] getTankInfo(EnumFacing from);
	public IFluidTankProperties[] getTankProperties();
	public FluidTank getTank();
}
