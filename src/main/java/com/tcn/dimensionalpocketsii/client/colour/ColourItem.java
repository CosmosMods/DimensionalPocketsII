package com.tcn.dimensionalpocketsii.client.colour;

import com.tcn.cosmoslibrary.common.item.CosmosArmourItemColourable;
import com.tcn.cosmoslibrary.common.item.CosmosArmourItemElytra;
import com.tcn.cosmoslibrary.common.lib.ComponentColour;
import com.tcn.dimensionalpocketsii.core.management.ModBusManager;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ColourItem implements ItemColor {

	@Override
	public int getColor(ItemStack stack, int itemLayerIn) {
		Item item = stack.getItem();
		
		if (item.equals(ModBusManager.BLOCK_ITEM_POCKET)) {
			if (stack.hasTag()) {
				CompoundTag stack_tag = stack.getTag();
				
				if (stack_tag.contains("nbt_data")) {
					CompoundTag nbt_data = stack_tag.getCompound("nbt_data");
					
					if (nbt_data.contains("colour")) {
						int colour = nbt_data.getInt("colour");
						
						if (colour == ComponentColour.POCKET_PURPLE.dec()) {
							return ComponentColour.POCKET_PURPLE_LIGHT.dec();
						}
						
						return colour;
					}
				}
			}
		} else if (item.equals(ModBusManager.DIMENSIONAL_SHIFTER) || item.equals(ModBusManager.DIMENSIONAL_SHIFTER_ENHANCED)) {
			if (stack.hasTag()) {
				CompoundTag stack_tag = stack.getTag();
				
				if (stack_tag.contains("nbt_data")) {
					CompoundTag compound_tag = stack_tag.getCompound("nbt_data");
					
					if (compound_tag.contains("colour")) {
						int decimal = compound_tag.getInt("colour");
						
						if (itemLayerIn == 0) {
							if (decimal == ComponentColour.POCKET_PURPLE.dec()) {
								return ComponentColour.POCKET_PURPLE_LIGHT.dec();
							} else {
								return decimal;
							}
						} else {
							return ComponentColour.WHITE.dec();
						}
					}
				}
			}
		} else if (item instanceof CosmosArmourItemElytra) {
			if (stack.hasTag()) {
				CompoundTag stack_tag = stack.getTag();
				
				if (stack_tag.contains("nbt_data")) {
					CompoundTag compound_tag = stack_tag.getCompound("nbt_data");
					
					int colour = ComponentColour.POCKET_PURPLE_LIGHT.dec();
					int wing_colour = ComponentColour.LIGHT_GRAY.dec();
					
					if (compound_tag.contains("colour")) {
						colour = compound_tag.getInt("colour");
					}
					
					if (compound_tag.contains("wing_colour")) {
						wing_colour = compound_tag.getInt("wing_colour");
					}
					
					if (itemLayerIn == 0) {
						return colour;
					} else if (itemLayerIn == 1) {
						return wing_colour;
					} else {
						return ComponentColour.WHITE.dec();
					}
				}
			} 

			if (itemLayerIn == 1) {
				return ComponentColour.LIGHT_GRAY.dec();
			}
		}
		
		else if (item instanceof CosmosArmourItemColourable) {
			if (stack.hasTag()) {
				CompoundTag stack_tag = stack.getTag();
				
				if (stack_tag.contains("nbt_data")) {
					CompoundTag compound_tag = stack_tag.getCompound("nbt_data");
					
					int colour = ComponentColour.POCKET_PURPLE_LIGHT.dec();
					
					if (compound_tag.contains("colour")) {
						colour = compound_tag.getInt("colour");
					}
					
					if (itemLayerIn == 0) {
						return colour;
					}else {
						return ComponentColour.WHITE.dec();
					}
				}
			} 
		}
		
		if (itemLayerIn == 0) {
			return ComponentColour.POCKET_PURPLE_LIGHT.dec();
		} else {
			return ComponentColour.WHITE.dec();
		}
	}
}