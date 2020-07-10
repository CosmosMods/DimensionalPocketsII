package com.zeher.dimensionalpockets.core.util;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public enum EnumPocketSideState implements IStringSerializable {

	NONE(0, "none"),
	GENERAL(1, "general"),
	ENERGY(2, "energy"),
	FLUID(3, "fluid"),
	ITEM(4, "item");
	
	private final int index;
	private final String name;
	
	public static final EnumPocketSideState[] VALUES = new EnumPocketSideState[3];
	
	private EnumPocketSideState (int index_in, String name_in) {
		this.index = index_in;
		this.name = name_in;
	}
	
	public static EnumPocketSideState getNext(EnumPocketSideState state) {
		switch (state) {
		case NONE:
			return GENERAL;
		case GENERAL:
			return ENERGY;
		case FLUID:
			return ITEM;
		case ITEM:
			return NONE;
		default:
				throw new IllegalStateException("Unable to find next side of EnumPocketSideState.class");
		}
	}
	
	public static EnumPocketSideState getDefaultValue() {
		return NONE;
	}

	@Override
	public String getName() {
		return this.toString();
	}
	
	@Override
	public String toString() {
        return this.name;
    }
	
	public int getIndex() {
		return this.index;
	}
	
	public static EnumPocketSideState getSideFromIndex(int index_in) {
		switch (index_in) {
			case 0 :
				return NONE;
			case 1:
				return GENERAL;
			case 2: 
				return ENERGY;
			case 3:
				return FLUID;
			case 4:
				return ITEM;
			default:
				throw new IllegalStateException("No Enum exists with that index: " + "[ " + index_in + " ]");
		}
	}
}