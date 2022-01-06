package com.tcn.dimensionalpocketsii.core.item.tool;

import java.util.List;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.common.lib.ComponentHelper;
import com.tcn.cosmoslibrary.energy.item.CosmosEnergyHoe;
import com.tcn.cosmoslibrary.energy.item.CosmosEnergyItem;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class DimensionalHoe extends CosmosEnergyHoe {
	
	public boolean enhanced;
	
	public DimensionalHoe(Tier itemTier, int attackDamageIn, float attackSpeedIn, boolean enhanced, Properties builderIn, CosmosEnergyItem.Properties energyProperties) {
		super(itemTier, attackDamageIn, attackSpeedIn, builderIn, energyProperties);
		
		this.enhanced = enhanced;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if (!ComponentHelper.isShiftKeyDown(Minecraft.getInstance())) {
			if (this.enhanced) {
				tooltip.add(ComponentHelper.getTooltipInfo("dimensionalpocketsii.item.info.hoe_enhanced"));
			} else {
				tooltip.add(ComponentHelper.getTooltipInfo("dimensionalpocketsii.item.info.hoe"));
			}
			
			if (ComponentHelper.displayShiftForDetail) {
				tooltip.add(ComponentHelper.shiftForMoreDetails());
			} 
			
		} else {
			
			tooltip.add(ComponentHelper.getTooltipOne("dimensionalpocketsii.item.info.tool_charge"));
			
			if (this.enhanced) {
				tooltip.add(ComponentHelper.getTooltipThree("dimensionalpocketsii.item.info.tool_usage_enhanced"));
				tooltip.add(ComponentHelper.getTooltipFour("dimensionalpocketsii.item.info.tool_energy_enhanced"));
			} else {
				tooltip.add(ComponentHelper.getTooltipThree("dimensionalpocketsii.item.info.tool_usage"));
				tooltip.add(ComponentHelper.getTooltipFour("dimensionalpocketsii.item.info.tool_energy"));
			}
			
			tooltip.add(ComponentHelper.getTooltipLimit("dimensionalpocketsii.item.info.tool_limitation"));
			
			tooltip.add(ComponentHelper.shiftForLessDetails());
		}
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}
}
