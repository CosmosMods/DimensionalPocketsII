package com.tcn.dimensionalpocketsii.pocket.core.block;

import java.util.List;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.client.impl.util.TextHelper;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class ItemBlockPocket extends BlockItem {

	public String info;
	public String shift_desc_one;
	public String shift_desc_two;

	public ItemBlockPocket(Block block, Item.Properties prop, String info, String shift_desc_one, String shift_desc_two) {
		super(block, prop);
		
		this.info = info;
		this.shift_desc_one = shift_desc_one;
		this.shift_desc_two = shift_desc_two;
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
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
		
		if (stack.hasTag()) {
			if (stack.hasTag()) {
				CompoundNBT tag = stack.getTag();
				if (tag.contains("nbt_data")) {
					CompoundNBT nbt_data = tag.getCompound("nbt_data");
					
					if (nbt_data.contains("chunk_set")) {
						CompoundNBT chunk_set = nbt_data.getCompound("chunk_set");
						
						int x = chunk_set.getInt("X");
						int z = chunk_set.getInt("Z");
						
						tooltip.add(new StringTextComponent(TextHelper.GRAY + "Linked to Pocket: " + TextHelper.LIGHT_GRAY + "[ " + TextHelper.BRIGHT_BLUE + x + TextHelper.LIGHT_GRAY + ", " + TextHelper.BRIGHT_BLUE + z + TextHelper.LIGHT_GRAY + " ]"));
					}
				}
			}
		}
	}
}