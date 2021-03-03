package com.tcn.cosmoslibrary.impl.block;

import java.util.List;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.client.impl.util.TextHelper;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Basic ItemBlock for giving a Block a description.
 */
public class CosmosItemBlock extends BlockItem {
	
	public String info;
	public String shift_desc_one;
	public String shift_desc_two;

	public CosmosItemBlock(Block block, Item.Properties properties, String info, String shift_desc_one, String shift_desc_two) {
		super(block, properties);
		
		this.info = info;
		this.shift_desc_one = shift_desc_one;
		this.shift_desc_two = shift_desc_two;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		if (!shift_desc_one.isEmpty() && !shift_desc_two.isEmpty()) {
			if (!TextHelper.isShiftKeyDown(Minecraft.getInstance())) {
				tooltip.add(new StringTextComponent(TextHelper.getInfoText(this.info)));
				
				if (TextHelper.displayShiftForDetail) {
					tooltip.add(new StringTextComponent(TextHelper.shiftForMoreDetails()));
				}
			} else {
				tooltip.add(new StringTextComponent(TextHelper.getDescOneText(shift_desc_one)));
				tooltip.add(new StringTextComponent(TextHelper.getDescTwoText(shift_desc_two)));
				
				tooltip.add(new StringTextComponent(TextHelper.shiftForLessDetails()));
			}
		} else {
			tooltip.add(new StringTextComponent(TextHelper.getInfoText(this.info)));
		}
	}
}