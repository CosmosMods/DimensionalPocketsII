package com.tcn.cosmoslibrary.impl.registry.object;

import com.tcn.cosmoslibrary.impl.nbt.UtilNBT.Const;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.registries.ForgeRegistries;

public class ObjectFluidTankCustom {
	
	private static final String NBT_FLUID_KEY = "fluid_tank_object";
	private static final String NBT_FILL_LEVEL_KEY = "fill_level";
	private static final String NBT_FLUID_CAPACITY_KEY = "capacity";
	private static final String NBT_FLUID_VOLUME_KEY = "volume";
	
	private FluidTank fluid_tank;
	
	private int fill_level;
	
	@SuppressWarnings("unused")
	private ObjectFluidTankCustom() { }
	
	public ObjectFluidTankCustom(FluidTank fluid_tank, int fill_level) {
		this.fluid_tank = fluid_tank;
		this.fill_level = fill_level;
	}
	
	public FluidTank getFluidTank() {
		return this.fluid_tank;
	}
	
	public void setFluidTank(FluidTank player_name) {
		this.fluid_tank = player_name;
	}
	
	public int getFillLevel() {
		return this.fill_level;
	}
	
	public void setFillLevel(int fill_level) {
		this.fill_level = fill_level;
	}
	
	public static ObjectFluidTankCustom readFromNBT(CompoundNBT compound) {
		int capacity = compound.getInt(NBT_FLUID_CAPACITY_KEY);
		int volume = compound.getInt(NBT_FLUID_VOLUME_KEY);
		int fill_level = compound.getInt(NBT_FILL_LEVEL_KEY);
		
		FluidTank tank = new FluidTank(capacity);
		
		CompoundNBT fluid = compound.getCompound(NBT_FLUID_KEY);
		String namespace = fluid.getString(Const.NBT_NAMESPACE_KEY);
		String path = fluid.getString(Const.NBT_PATH_KEY);
		
		ResourceLocation fluidName = new ResourceLocation(namespace, path);
		tank.setFluid(new FluidStack(ForgeRegistries.FLUIDS.getValue(fluidName), volume));
		
		return new ObjectFluidTankCustom(tank, fill_level);
	}
	
	public void writeToNBT(CompoundNBT compound) {
		ResourceLocation fluid_name = fluid_tank.getFluid().getFluid().getRegistryName();
		
		CompoundNBT fluid = new CompoundNBT();
		
		fluid.putString(Const.NBT_NAMESPACE_KEY, fluid_name.getNamespace());
		fluid.putString(Const.NBT_PATH_KEY, fluid_name.getPath());
		
		compound.putInt(NBT_FILL_LEVEL_KEY, this.getFillLevel());
		compound.putInt(NBT_FLUID_CAPACITY_KEY, this.getFluidTank().getCapacity());
		compound.putInt(NBT_FLUID_VOLUME_KEY, this.getFluidTank().getFluidAmount());
		compound.put(NBT_FLUID_KEY, fluid);
	}
}
