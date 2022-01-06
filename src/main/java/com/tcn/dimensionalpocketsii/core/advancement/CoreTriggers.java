package com.tcn.dimensionalpocketsii.core.advancement;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class CoreTriggers {

	public static final UseShifterTrigger USE_SHIFTER_TRIGGER = new UseShifterTrigger();
	
	public static void triggerUseShifter(ServerPlayer playerIn, ItemStack stack) {
		USE_SHIFTER_TRIGGER.trigger(playerIn, stack);
	}
}
