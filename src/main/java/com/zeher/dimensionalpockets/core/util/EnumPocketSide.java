package com.zeher.dimensionalpockets.core.util;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public enum EnumPocketSide implements IStringSerializable {

	NONE(0, "none", "gui.enum_pocket_side.none", 0xFF0000),
	INPUT(1, "input", "gui.enum_pocket_side.input", 0x2ECCFA),
	OUTPUT(2, "output", "gui.enum_pocket_side.output", 0x00FF00);
	
	private final int index;
	private final String name;
	private final String gui_name;
	private final int colour;
	
	public static final EnumPocketSide[] VALUES = new EnumPocketSide[3];
	
	private EnumPocketSide (int index_in, String name_in, String gui_name, int colour) {
		this.index = index_in;
		this.name = name_in;
		this.gui_name = gui_name;
		this.colour = colour;
	}
	
	public static EnumPocketSide getNext(EnumPocketSide state) {
		switch (state) {
		case OUTPUT:
			return NONE;
		case INPUT:
			return OUTPUT;
		case NONE:
			return INPUT;
		default:
            throw new IllegalStateException("Unable to find next side of " + state);
		}
	}
	
	public EnumPocketSide getNext() {
		switch (this) {
		case OUTPUT:
			return NONE;
		case INPUT:
			return OUTPUT;
		case NONE:
			return INPUT;
		default:
            throw new IllegalStateException("Unable to find next side of " + this);
		}
	}
	
	public static EnumPocketSide[] getDefaultArray() {
		return new EnumPocketSide[] { NONE, NONE, NONE, NONE, NONE, NONE };
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
	
	public String getGuiNameFormatted() {
		return I18n.format(gui_name).toString();
	}
	
	public String getGuiName() {
		return this.gui_name;
	}
	
	public int getGuiColour() {
		return this.colour;
	}
	
	public static EnumPocketSide getSideFromIndex(int index_in) {
		switch (index_in) {
			case 0 :
				return NONE;
			case 1:
				return INPUT;
			case 2: 
				return OUTPUT;
			default:
				throw new IllegalStateException("No Enum exists with that index: " + "[ " + index_in + " ]");
		}
	}
}