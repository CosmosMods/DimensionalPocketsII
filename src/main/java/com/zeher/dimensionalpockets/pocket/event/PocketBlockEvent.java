package com.zeher.dimensionalpockets.pocket.event;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.core.handler.BlockHandler;

import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID)
public class PocketBlockEvent {

	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent event) {
		if (!DimensionalPockets.CAN_DESTROY_WALLS_IN_CREATIVE && event.getWorld().getBlockState(event.getPos()).getBlock() == BlockHandler.BLOCK_DIMENSIONAL_POCKET_WALL || event.getWorld().getBlockState(event.getPos()).getBlock() == BlockHandler.BLOCK_DIMENSIONAL_POCKET_WALL_EDGE) {
			event.setCanceled(true);
		}
		
		if(event.getWorld().getBlockState(event.getPos()).getBlock() == BlockHandler.BLOCK_DIMENSIONAL_POCKET_WALL) {
			event.setCanceled(true);
		}
	}
}
