package com.tcn.dimensionalpocketsii.pocket.client;

import com.tcn.cosmoslibrary.impl.colour.EnumMinecraftColour;
import com.tcn.dimensionalpocketsii.core.management.ModBusManager;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class PocketItemColour implements IItemColor {

	@Override
	public int getColor(ItemStack stack, int tintindex) {
		if (stack.getItem().equals(ModBusManager.BLOCKITEM_POCKET)) {
			if (stack.hasTag()) {
				CompoundNBT stack_tag = stack.getTag();
				
				if (stack_tag.contains("nbt_data")) {
				CompoundNBT nbt_data = stack_tag.getCompound("nbt_data");
					
					if (nbt_data.contains("colour")) {
						int colour = nbt_data.getInt("colour");
						
						if (colour == EnumMinecraftColour.POCKET_PURPLE.getDecimal()) {
							return EnumMinecraftColour.POCKET_PURPLE_LIGHT.getDecimal();
						}
						
						return colour;
					}
				}
			}
		}
		
		return EnumMinecraftColour.POCKET_PURPLE_LIGHT.getDecimal();
	}
}