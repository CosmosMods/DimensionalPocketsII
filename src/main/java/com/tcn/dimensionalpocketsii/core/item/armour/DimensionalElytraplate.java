package com.tcn.dimensionalpocketsii.core.item.armour;

import java.util.List;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper;
import com.tcn.cosmoslibrary.common.item.CosmosElytraArmourItem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class DimensionalElytraplate extends CosmosElytraArmourItem {

	public DimensionalElytraplate(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builderIn, Item item, boolean damageableIn) {
		super(materialIn, slot, builderIn, item, damageableIn);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		
		if (!CosmosCompHelper.isShiftKeyDown(Minecraft.getInstance())) {
			tooltip.add(CosmosCompHelper.getTooltipInfo("dimensionalpocketsii.info.elytraplate_info"));
			
			if (CosmosCompHelper.displayShiftForDetail) {
				tooltip.add(CosmosCompHelper.shiftForMoreDetails());
			}
		} else {
			tooltip.add(CosmosCompHelper.getTooltipOne("dimensionalpocketsii.info.elytraplate_shift_one"));
			tooltip.add(CosmosCompHelper.getTooltipTwo("dimensionalpocketsii.info.elytraplate_shift_two"));
			tooltip.add(CosmosCompHelper.shiftForLessDetails());
		}
	}
	
	@Override
	public boolean isDamageable(ItemStack stack) {
		return false;
	}

}