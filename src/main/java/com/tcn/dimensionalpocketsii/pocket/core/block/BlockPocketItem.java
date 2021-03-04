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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class BlockPocketItem extends BlockItem {

	public String info = "Creates a pocket dimension!";
	public String shift_desc_one = "Once placed shift-right click to enter the pocket.";
	public String shift_desc_two = "To exit the pocket, simply shift-right click on any wall.";
	public String shift_desc_three = "To change colours, simply right-click your Pocket Block, or Connector with your choice of dye!";

	public BlockPocketItem(Block block, Item.Properties prop) {
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
		
		if (stack.hasTag()) {
			if (stack.hasTag()) {
				CompoundNBT tag = stack.getTag();
				if (tag.contains("nbt_data")) {
					CompoundNBT nbt_data = tag.getCompound("nbt_data");
					
					if (nbt_data.contains("chunk_set")) {
						CompoundNBT chunk_set = nbt_data.getCompound("chunk_set");
						
						int x = chunk_set.getInt("X");
						int z = chunk_set.getInt("Z");
						
						tooltip.add(new StringTextComponent(ChatColour.GRAY + "Linked to Pocket: " + ChatColour.LIGHT_GRAY + "[ " + ChatColour.BRIGHT_BLUE + x + ChatColour.LIGHT_GRAY + ", " + ChatColour.BRIGHT_BLUE + z + ChatColour.LIGHT_GRAY + " ]"));
					}
				}
			}
		}
	}

	@Override
	public boolean isDamageable(DamageSource damageSource) {
		return false;
	}
}