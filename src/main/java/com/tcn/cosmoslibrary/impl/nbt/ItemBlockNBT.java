package com.tcn.cosmoslibrary.impl.nbt;

import java.util.List;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.client.impl.util.TextHelper;
import com.tcn.cosmoslibrary.impl.enums.EnumSideState;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

/**
 * ItemBlock class used to apply information to a block.
 * Can display NBT information when a block has such information.
 * Otherwise it just displays the given [info], [shift_desc_one] & [shift_desc_two] information on the tool-tip.
 */
public class ItemBlockNBT extends BlockItem {
	
	public String info;
	public String shift_desc_one;
	public String shift_desc_two;
	public String shift_desc_three;
	public String limitation;

	public ItemBlockNBT(Block block, Item.Properties builder, String info, String shift_desc_one) {
		super(block, builder);
		
		this.info = info;
		this.shift_desc_one = shift_desc_one;
	}
	
	public ItemBlockNBT(Block block, Item.Properties builder, String info, String shift_desc_one, String shift_desc_two) {
		super(block, builder);
		
		this.info = info;
		this.shift_desc_one = shift_desc_one;
		this.shift_desc_two = shift_desc_two;
	}
	
	public ItemBlockNBT(Block block, Item.Properties builder, String info, String shift_desc_one, String shift_desc_two, String limitation) {
		super(block, builder);
		
		this.info = info;
		this.shift_desc_one = shift_desc_one;
		this.shift_desc_two = shift_desc_two;
		this.limitation = limitation;
	}
	
	public ItemBlockNBT(Block block, Item.Properties builder, String info, String shift_desc_one, String shift_desc_two, String shift_desc_three, String limitation) {
		super(block, builder);
		
		this.info = info;
		this.shift_desc_one = shift_desc_one;
		this.shift_desc_two = shift_desc_two;
		this.shift_desc_three = shift_desc_three;
		this.limitation = limitation;
	}
	
	@SuppressWarnings("deprecation")
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
			
			if (this.shift_desc_two != null) {
				tooltip.add(new StringTextComponent(TextHelper.getDescTwoText(shift_desc_two)));
			}
			
			if (this.shift_desc_three != null) {
				tooltip.add(new StringTextComponent(TextHelper.getDescThreeText(shift_desc_three)));
			}
			
			if (this.limitation != null) {
				tooltip.add(new StringTextComponent(TextHelper.getLimitationText(limitation)));
			}
			tooltip.add(new StringTextComponent(TextHelper.shiftForLessDetails()));
		}
		
		if (stack.hasTag()) {
			if (!TextHelper.isControlKeyDown(Minecraft.getInstance())) {
				tooltip.add(new StringTextComponent(TextHelper.ctrlForMoreDetails()));
			} else {
				if (stack.hasTag()) {
					CompoundNBT tag = stack.getTag();
					if (tag.contains("nbt_data")) {
						
						CompoundNBT compound_tag = tag.getCompound("nbt_data");
						
						//tooltip.add(new StringTextComponent(TextHelper.GRAY + "~ [ Data Tag: { nbt_data } ]:"));
						
						//if(compound_tag.hasKey("energy")) {
							int energy = compound_tag.getInt("energy");
							
							tooltip.add(new StringTextComponent(TextHelper.GRAY + "Energy Stored: " + TextHelper.LIGHT_GRAY + "["+ energy + "] " + TextHelper.PURPLE + "RF"));
							
						//}
						
						if (compound_tag.contains("Items")) {
							int size = compound_tag.getInt("size");
	
							NonNullList<ItemStack> list_ = NonNullList.<ItemStack>withSize(size, ItemStack.EMPTY);
							ItemStackHelper.loadAllItems(tag.getCompound("nbt_data"), list_);
							
							tooltip.add(new StringTextComponent(TextHelper.GRAY + "   > [ Items: { Items } ]: "));
							
							if (list_.size() > 6) {
								for (int j = 0; j < 6; j++) {
									if (list_.get(j)!= null){
										if (list_.get(j).getItem() != Item.getItemFromBlock(Blocks.AIR)) {
											tooltip.add(new StringTextComponent(TextHelper.GRAY + "     - ( " + "Slot " + j + ": " + list_.get(j).getCount() + "x " + I18n.format(list_.get(j).getItem().getDisplayName(list_.get(j)).toString()) + " )"));
										}
									}
								}
								tooltip.add(new StringTextComponent(TextHelper.GRAY + "     - ( & " + (list_.size() - 5) + " stack(s) more... )"));
							} else {
								for (int j = 0; j < list_.size(); j++) {
									if (list_.get(j)!= null){
										if (list_.get(j).getItem() != Item.getItemFromBlock(Blocks.AIR)) {
											tooltip.add(new StringTextComponent(TextHelper.GRAY + "     - ( " + "Slot " + j + ": " + list_.get(j).getCount() + "x " + I18n.format(list_.get(j).getItem().getDisplayName(list_.get(j)).toString()) + " )"));
										}
									}
								}
							}
						}
						
						if (compound_tag.contains("sides")) {
							CompoundNBT compound_tag_sides = compound_tag.getCompound("sides");
							
							String[] strings = new String[] {"", "", "", "", "", ""};
							String[] text_colours = new String[] {"", "", "", "", "", ""};
							
							for (Direction c : Direction.values()) {
								int index = compound_tag_sides.getInt("index_" + c.getIndex());
								strings[c.getIndex()] = EnumSideState.getStateFromIndex(index).getDisplayName();
								text_colours[c.getIndex()] = EnumSideState.getStateFromIndex(index).getTextColour();
							}
							
							tooltip.add(new StringTextComponent(TextHelper.LIGHT_BLUE + "   > " + TextHelper.LIGHT_GRAY + "[ " + TextHelper.TEAL + "ISidedTile " + TextHelper.GRAY + "(sides)" + TextHelper.LIGHT_GRAY + " ]"));
							
							tooltip.add(new StringTextComponent(TextHelper.RED + "     - " + TextHelper.LIGHT_GRAY + "[U] " + TextHelper.GRAY + "= " + text_colours[1] + strings[1]));
							tooltip.add(new StringTextComponent(TextHelper.RED + "     - " + TextHelper.LIGHT_GRAY + "[D] " + TextHelper.GRAY + "= " + text_colours[0] + strings[0]));
							tooltip.add(new StringTextComponent(TextHelper.RED + "     - " + TextHelper.LIGHT_GRAY + "[N] " + TextHelper.GRAY + "= " + text_colours[2] + strings[2]));
							tooltip.add(new StringTextComponent(TextHelper.RED + "     - " + TextHelper.LIGHT_GRAY + "[S] " + TextHelper.GRAY + "= " + text_colours[3] + strings[3]));
							tooltip.add(new StringTextComponent(TextHelper.RED + "     - " + TextHelper.LIGHT_GRAY + "[E] " + TextHelper.GRAY + "= " + text_colours[5] + strings[5]));
							tooltip.add(new StringTextComponent(TextHelper.RED + "     - " + TextHelper.LIGHT_GRAY + "[W] " + TextHelper.GRAY + "= " + text_colours[4] + strings[4]));
							
						}
					}
					tooltip.add(new StringTextComponent(TextHelper.ctrlForLessDetails()));
				}
			}
		}
	}
}