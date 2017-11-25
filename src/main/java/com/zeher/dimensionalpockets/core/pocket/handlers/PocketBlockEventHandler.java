package com.zeher.dimensionalpockets.core.pocket.handlers;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.core.handlers.BlockHandler;

import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PocketBlockEventHandler {

	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent event) {
		if (!DimensionalPockets.can_destroy_walls_in_creative && event.getWorld().getBlockState(event.getPos()).getBlock() == BlockHandler.block_dimensional_pocket_wall) {
			event.setCanceled(true);
		}
	}
}
