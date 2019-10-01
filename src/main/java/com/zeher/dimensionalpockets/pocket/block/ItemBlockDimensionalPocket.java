package com.zeher.dimensionalpockets.pocket.block;

import java.util.List;

import javax.annotation.Nullable;

import com.zeher.zeherlib.api.util.TextUtil;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockDimensionalPocket extends ItemBlock {

	public ItemBlockDimensionalPocket(Block block) {
		super(block);
		this.setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (!TextUtil.isShiftKeyDown()) {
			tooltip.add(TextUtil.getInfoText("Creates a pocket dimension!"));
			if (TextUtil.displayShiftForDetail) {
				tooltip.add(TextUtil.shiftForDetails());
			}
		} else {
			tooltip.add(TextUtil.getInfoText("Once placed shift-right click to enter the pocket."));
			tooltip.add(TextUtil.getInfoText("To exit the pocket, simply shift-right click on any wall."));
		}
	}

}
