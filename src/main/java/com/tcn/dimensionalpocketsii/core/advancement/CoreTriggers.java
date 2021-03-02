package com.tcn.dimensionalpocketsii.core.advancement;

import com.tcn.dimensionalpocketsii.DimensionalPockets;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CoreTriggers {

	public static final UseShifterTrigger USE_SHIFTER_TRIGGER = new UseShifterTrigger(new ResourceLocation(DimensionalPockets.MOD_ID, "use_shifter"));
	
	public static void triggerUseShifter(ServerPlayerEntity playerIn, ItemStack stack) {
		USE_SHIFTER_TRIGGER.trigger(playerIn, stack);
	}
	
}
