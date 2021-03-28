package com.tcn.dimensionalpocketsii.core.item;

import java.util.List;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class DimensionalBow extends BowItem {

	public DimensionalBow(Properties builder) {
		super(builder);
	}
	
	@Override
	public void appendHoverText(ItemStack stackIn, @Nullable World worldIn, List<ITextComponent> toolTipIn, ITooltipFlag flagIn) {
		super.appendHoverText(stackIn, worldIn, toolTipIn, flagIn);
		
		if (!CosmosCompHelper.isShiftKeyDown(Minecraft.getInstance())) {
			toolTipIn.add(CosmosCompHelper.getTooltipInfo("dimensionalpocketsii.info.shifter_info"));
			
			if (CosmosCompHelper.displayShiftForDetail) {
				toolTipIn.add(CosmosCompHelper.shiftForMoreDetails());
			} 
			
		} else {
			toolTipIn.add(CosmosCompHelper.getTooltipOne("dimensionalpocketsii.info.shifter_shift_one"));
			toolTipIn.add(CosmosCompHelper.getTooltipTwo("dimensionalpocketsii.info.shifter_shift_two"));
			toolTipIn.add(CosmosCompHelper.getTooltipThree("dimensionalpocketsii.info.shifter_shift_three"));
			toolTipIn.add(CosmosCompHelper.getTooltipLimit("dimensionalpocketsii.info.shifter_limitation"));
			
			toolTipIn.add(CosmosCompHelper.shiftForLessDetails());
		}
	}

	@Override
	public int getUseDuration(ItemStack stackIn) {
		return 30000;
	}

	@Override
	public int getDefaultProjectileRange() {
		return 40;
	}
}
