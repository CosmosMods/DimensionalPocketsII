package com.tcn.dimensionalpocketsii.pocket.core.item.block;

import java.util.List;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class ItemBlockCrafter extends BlockItem {

	public ItemBlockCrafter(Block block, Item.Properties prop) {
		super(block, prop);
	}
	
	@Override
	public void appendHoverText(ItemStack stackIn, @Nullable World worldIn, List<ITextComponent> toolTipIn, ITooltipFlag flagIn) {
		super.appendHoverText(stackIn, worldIn, toolTipIn, flagIn);
		
		if (!CosmosCompHelper.isShiftKeyDown(Minecraft.getInstance())) {
			toolTipIn.add(CosmosCompHelper.getTooltipInfo("dimensionalpocketsii.info.block.module.crafter"));
		
			if (CosmosCompHelper.displayShiftForDetail) {
				toolTipIn.add(CosmosCompHelper.shiftForMoreDetails());
			}
		} else {
			toolTipIn.add(CosmosCompHelper.getTooltipOne("dimensionalpocketsii.info.block.module.crafter_shift_one"));
			toolTipIn.add(CosmosCompHelper.shiftForLessDetails());
		}
	}

	@Override
	public boolean isDamageable(ItemStack stackIn) {
		return false;
	}
}