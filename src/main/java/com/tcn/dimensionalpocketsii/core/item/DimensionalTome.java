package com.tcn.dimensionalpocketsii.core.item;

import java.util.List;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenTome;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DimensionalTome extends Item {

	public DimensionalTome(Item.Properties properties) {
		super(properties);
	}

	@OnlyIn(Dist.CLIENT)
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		Minecraft.getInstance().setScreen(new ScreenTome(true));
		
		return ActionResult.pass(playerIn.getItemInHand(handIn));
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		
		if (!CosmosCompHelper.isShiftKeyDown(Minecraft.getInstance())) {
			tooltip.add(CosmosCompHelper.getTooltipInfo("dimensionalpocketsii.info.tome_info"));
			
			if (CosmosCompHelper.displayShiftForDetail) {
				tooltip.add(CosmosCompHelper.shiftForMoreDetails());
			}
		} else {
			tooltip.add(CosmosCompHelper.getTooltipOne("dimensionalpocketsii.info.tome_shift_one"));
			tooltip.add(CosmosCompHelper.getTooltipTwo("dimensionalpocketsii.info.tome_shift_two"));
			
			tooltip.add(CosmosCompHelper.shiftForLessDetails());
		}
	}
}
