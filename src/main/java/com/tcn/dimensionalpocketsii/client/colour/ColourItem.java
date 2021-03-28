package com.tcn.dimensionalpocketsii.client.colour;

import com.tcn.cosmoslibrary.common.comp.CosmosColour;
import com.tcn.dimensionalpocketsii.core.management.CoreModBusManager;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ColourItem implements IItemColor {

	@Override
	public int getColor(ItemStack stack, int itemLayerIn) {
		Item item = stack.getItem();
		
		if (item.equals(CoreModBusManager.BLOCKITEM_POCKET)) {
			if (stack.hasTag()) {
				CompoundNBT stack_tag = stack.getTag();
				
				if (stack_tag.contains("nbt_data")) {
				CompoundNBT nbt_data = stack_tag.getCompound("nbt_data");
					
					if (nbt_data.contains("colour")) {
						int colour = nbt_data.getInt("colour");
						
						if (colour == CosmosColour.POCKET_PURPLE.dec()) {
							return CosmosColour.POCKET_PURPLE_LIGHT.dec();
						}
						
						return colour;
					}
				}
			}
		} else if (item.equals(CoreModBusManager.DIMENSIONAL_SHIFTER) || item.equals(CoreModBusManager.DIMENSIONAL_SHIFTER_ENHANCED)) {
			if (stack.hasTag()) {
				CompoundNBT stack_tag = stack.getTag();
				
				if (stack_tag.contains("nbt_data")) {
					CompoundNBT compound_tag = stack_tag.getCompound("nbt_data");
					
					if (compound_tag.contains("colour")) {
						int decimal = compound_tag.getInt("colour");
						
						if (itemLayerIn == 0) {
							if (decimal == CosmosColour.POCKET_PURPLE.dec()) {
								return CosmosColour.POCKET_PURPLE_LIGHT.dec();
							} else {
								return decimal;
							}
						} else {
							return CosmosColour.WHITE.dec();
						}
					}
				}
			} else if (itemLayerIn == 0) {
				return CosmosColour.POCKET_PURPLE_LIGHT.dec();
			} else {
				return CosmosColour.WHITE.dec();
			}
		}
		
		else if (item.equals(CoreModBusManager.DIMENSIONAL_ELYTRAPLATE_SCREEN) || item.equals(CoreModBusManager.DIMENSIONAL_ELYTRAPLATE_SHIFT)) {
			if (stack.hasTag()) {
				CompoundNBT stack_tag = stack.getTag();
				
				if (stack_tag.contains("nbt_data")) {
					CompoundNBT compound_tag = stack_tag.getCompound("nbt_data");
					
					if (compound_tag.contains("colour")) {
						int decimal = compound_tag.getInt("colour");
						
						if (itemLayerIn == 0) {
							return decimal;
						} else {
							return CosmosColour.WHITE.dec();
						}
					}
				} else {
					if (itemLayerIn == 0) {
						return CosmosColour.POCKET_PURPLE_LIGHT.dec();
					} else {
						return CosmosColour.WHITE.dec();
					}
				}
			} else {
				if (itemLayerIn == 0) {
					return CosmosColour.POCKET_PURPLE_LIGHT.dec();
				} else {
					return CosmosColour.WHITE.dec();
				}
			}
		} else {
			if (itemLayerIn == 0) {
				return CosmosColour.POCKET_PURPLE_LIGHT.dec();
			} else {
				return CosmosColour.WHITE.dec();
			}
		}
		return CosmosColour.POCKET_PURPLE_LIGHT.dec();
	}
}