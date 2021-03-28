package com.tcn.dimensionalpocketsii.pocket.core.item.block;

import java.util.List;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper;
import com.tcn.cosmoslibrary.common.comp.CosmosColour;
import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper.Value;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class ItemBlockPocket extends BlockItem {

	public ItemBlockPocket(Block block, Item.Properties prop) {
		super(block, prop);
	}
	
	@Override
	public void appendHoverText(ItemStack stackIn, @Nullable World worldIn, List<ITextComponent> toolTipIn, ITooltipFlag flagIn) {
		super.appendHoverText(stackIn, worldIn, toolTipIn, flagIn);
		
		if (!CosmosCompHelper.isShiftKeyDown(Minecraft.getInstance())) {
			toolTipIn.add(CosmosCompHelper.getTooltipInfo("dimensionalpocketsii.info.block.pocket"));
			
			if (CosmosCompHelper.displayShiftForDetail) {
				toolTipIn.add(CosmosCompHelper.shiftForMoreDetails());
			}
		} else {
			toolTipIn.add(CosmosCompHelper.getTooltipOne("dimensionalpocketsii.info.block.pocket_shift_one"));
			toolTipIn.add(CosmosCompHelper.getTooltipTwo("dimensionalpocketsii.info.block.pocket_shift_two"));
			toolTipIn.add(CosmosCompHelper.getTooltipThree("dimensionalpocketsii.info.block.pocket_shift_three"));
			toolTipIn.add(CosmosCompHelper.shiftForLessDetails());
		}
		
		if (stackIn.hasTag()) {
			if (stackIn.hasTag()) {
				CompoundNBT tag = stackIn.getTag();
				if (tag.contains("nbt_data")) {
					CompoundNBT nbt_data = tag.getCompound("nbt_data");
					
					if (nbt_data.contains("chunk_set")) {
						CompoundNBT chunk_set = nbt_data.getCompound("chunk_set");
						
						int x = chunk_set.getInt("X");
						int z = chunk_set.getInt("Z");
						
						
						
						toolTipIn.add(CosmosCompHelper.locComp(CosmosColour.GRAY, false, "dimensionalpocketsii.info.pocket.pocket").append(CosmosCompHelper.locComp(Value.LIGHT_GRAY + "[ " + Value.BRIGHT_BLUE + x + Value.LIGHT_GRAY + ", " + Value.BRIGHT_BLUE + z + Value.LIGHT_GRAY + " ]")));
					}
				}
			}
		}
	}

	@Override
	public boolean isDamageable(ItemStack stackIn) {
		return false;
	}
}