package com.zeher.zeherlib.api.azrf;

import java.util.List;

import javax.annotation.Nullable;

import com.zeher.zeherlib.api.client.util.TextHelper;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * ItemBlock class used to apply information to a block.
 * Can display NBT information when a block has such information.
 * Otherwise it just displays the given [info], [shift_desc_one] & [shift_desc_two] information on the tool-tip.
 */
public class AZRFItemBlock extends ItemBlock {
	
	public String info;
	public String shift_desc_one;
	public String shift_desc_two;

	public AZRFItemBlock(Block block, String info, String shift_desc_one, String shift_desc_two) {
		super(block);
		
		this.setRegistryName(block.getRegistryName());
		
		this.info = info;
		this.shift_desc_one = shift_desc_one;
		this.shift_desc_two = shift_desc_two;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		if (!TextHelper.isShiftKeyDown()) {
			tooltip.add(TextHelper.getInfoText(this.info));
			
			if (TextHelper.displayShiftForDetail) {
				tooltip.add(TextHelper.shiftForMoreDetails());
			}
		} else {
			tooltip.add(TextHelper.getDescOneText(shift_desc_one));
			tooltip.add(TextHelper.getDescTwoText(shift_desc_two));
			
			tooltip.add(TextHelper.shiftForLessDetails());
		}
		
		if ( stack.hasTagCompound()) {
			if (!TextHelper.isControlKeyDown()) {
				tooltip.add(TextHelper.ctrlForMoreDetails());
			} else {
				if (stack.hasTagCompound()) {
					NBTTagCompound tag = stack.getTagCompound();
					if (tag.hasKey("nbt_data")) {
						
						NBTTagCompound compound_tag = tag.getCompoundTag("nbt_data");
						
						tooltip.add(TextHelper.GRAY + "~ [ Data Tag: { nbt_data } ]:");
						
						//if(compound_tag.hasKey("energy")) {
							int energy = compound_tag.getInteger("energy");
							
							tooltip.add(TextHelper.LIGHT_BLUE + "   > " + TextHelper.LIGHT_GRAY + "[ " + TextHelper.LIGHT_RED + "Energy " + TextHelper.GRAY + "(energy)" + TextHelper.LIGHT_GRAY + " ]");
							
							tooltip.add(TextHelper.RED + "     - " + TextHelper.LIGHT_GRAY + "["+ energy + "] " + TextHelper.PURPLE + "RF");
							
						//}
						
						if (compound_tag.hasKey("Items")) {
							int size = compound_tag.getInteger("size");
	
							NonNullList<ItemStack> list_ = NonNullList.<ItemStack>withSize(size, ItemStack.EMPTY);
							ItemStackHelper.loadAllItems(tag.getCompoundTag("nbt_data"), list_);
							
							tooltip.add(TextHelper.GRAY + "   > [ Items: { Items } ]: ");
							
							if (list_.size() > 6) {
								for (int j = 0; j < 6; j++) {
									if (list_.get(j)!= null){
										if (list_.get(j).getItem() != Item.getItemFromBlock(Blocks.AIR)) {
											tooltip.add(TextHelper.GRAY + "     - ( " + "Slot " + j + ": " + list_.get(j).getCount() + "x " + I18n.format(list_.get(j).getItem().getItemStackDisplayName(list_.get(j))) + " )");
										}
									}
								}
								tooltip.add(TextHelper.GRAY + "     - ( & " + (list_.size() - 5) + " stack(s) more... )");
							} else {
								for (int j = 0; j < list_.size(); j++) {
									if (list_.get(j)!= null){
										if (list_.get(j).getItem() != Item.getItemFromBlock(Blocks.AIR)) {
											tooltip.add(TextHelper.GRAY + "     - ( " + "Slot " + j + ": " + list_.get(j).getCount() + "x " + I18n.format(list_.get(j).getItem().getItemStackDisplayName(list_.get(j))) + " )");
										}
									}
								}
							}
						}
						
						if (compound_tag.hasKey("sides")) {
							NBTTagCompound compound_tag_sides = compound_tag.getCompoundTag("sides");
							
							String[] strings = new String[] {"", "", "", "", "", ""};
							String[] text_colours = new String[] {"", "", "", "", "", ""};
							
							for (EnumFacing c : EnumFacing.VALUES) {
								int index = compound_tag_sides.getInteger("index_" + c.getIndex());
								strings[c.getIndex()] = EnumSideState.getStateFromIndex(index).getDisplayName();
								text_colours[c.getIndex()] = EnumSideState.getStateFromIndex(index).getTextColour();
							}
							
							tooltip.add(TextHelper.LIGHT_BLUE + "   > " + TextHelper.LIGHT_GRAY + "[ " + TextHelper.TEAL + "ISidedTile " + TextHelper.GRAY + "(sides)" + TextHelper.LIGHT_GRAY + " ]");
							
							tooltip.add(TextHelper.RED + "     - " + TextHelper.LIGHT_GRAY + "[U] " + TextHelper.GRAY + "= " + text_colours[1] + strings[1]);
							tooltip.add(TextHelper.RED + "     - " + TextHelper.LIGHT_GRAY + "[D] " + TextHelper.GRAY + "= " + text_colours[0] + strings[0]);
							tooltip.add(TextHelper.RED + "     - " + TextHelper.LIGHT_GRAY + "[N] " + TextHelper.GRAY + "= " + text_colours[2] + strings[2]);
							tooltip.add(TextHelper.RED + "     - " + TextHelper.LIGHT_GRAY + "[S] " + TextHelper.GRAY + "= " + text_colours[3] + strings[3]);
							tooltip.add(TextHelper.RED + "     - " + TextHelper.LIGHT_GRAY + "[E] " + TextHelper.GRAY + "= " + text_colours[5] + strings[5]);
							tooltip.add(TextHelper.RED + "     - " + TextHelper.LIGHT_GRAY + "[W] " + TextHelper.GRAY + "= " + text_colours[4] + strings[4]);
							
						}
					}
					tooltip.add(TextHelper.ctrlForLessDetails());
				}
			}
		}
	}
}