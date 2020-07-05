package com.zeher.zeherlib.mod.block;

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
 * Basic ItemBlock for giving a Block a description.
 */
public class ModItemBlock extends ItemBlock {
	
	public String info;
	public String shift_desc_one;
	public String shift_desc_two;

	public ModItemBlock(Block block, String info, String shift_desc_one, String shift_desc_two) {
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
		
		if (!shift_desc_one.isEmpty() && !shift_desc_two.isEmpty()) {
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
		} else {
			tooltip.add(TextHelper.getInfoText(this.info));
		}
	}
}