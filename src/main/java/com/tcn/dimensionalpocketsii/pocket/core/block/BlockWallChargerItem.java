package com.tcn.dimensionalpocketsii.pocket.core.block;

import java.util.List;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.impl.colour.ChatColour;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class BlockWallChargerItem extends BlockItem {

	public String info = "Used to charge Dimensional Items.";
	public String shift_desc_one = "To create: right click on a wall block inside your Pocket.";
	public String shift_desc_two = "To remove: right click on the Charger";
	public String shift_desc_three = "To use: right click the item that requires charging on the Charger. To retreive, right click with an empty hand.";

	public BlockWallChargerItem(Block block, Item.Properties prop) {
		super(block, prop);
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		if (!ChatColour.isShiftKeyDown(Minecraft.getInstance())) {
			tooltip.add(new StringTextComponent(ChatColour.getInfoText(this.info)));
			
			if (ChatColour.displayShiftForDetail) {
				tooltip.add(new StringTextComponent(ChatColour.shiftForMoreDetails()));
			}
		} else {
			tooltip.add(new StringTextComponent(ChatColour.getDescOneText(shift_desc_one)));
			tooltip.add(new StringTextComponent(ChatColour.getDescTwoText(shift_desc_two)));
			tooltip.add(new StringTextComponent(ChatColour.getDescThreeText(shift_desc_three)));
			tooltip.add(new StringTextComponent(ChatColour.shiftForLessDetails()));
		}
	}

	@Override
	public boolean isDamageable(DamageSource damageSource) {
		return false;
	}
}