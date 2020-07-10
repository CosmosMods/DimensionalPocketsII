package com.zeher.dimensionalpockets.pocket.event;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.core.handler.BlockHandler;
import com.zeher.dimensionalpockets.pocket.block.BlockDimensionalPocketWall;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID)
public class PocketEventHandler {
	
	@SubscribeEvent
	public void onEnderTeleport(EnderTeleportEvent event) {
		//Disable ender-pearl functionality
		if (event.getEntityLiving().world.provider.getDimension() == DimensionalPockets.DIMENSION_ID) {
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onLivingFall(LivingFallEvent event) {
		//Negate fall damage.
		if (event.getEntityLiving().world.provider.getDimension() == DimensionalPockets.DIMENSION_ID) {
			event.setDistance(0.0F);
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent event) {
		//Negate fall damage mk2
		if (event.getSource() == DamageSource.FALL) {
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onDestroyBlock(LivingDestroyBlockEvent event) {
		//Disable the breaking of Pocket Wall Blocks
		if (event.getState().getBlock() instanceof BlockDimensionalPocketWall) {
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		//Disable death for players
		if (event.getEntityLiving() instanceof EntityPlayer) {
			event.setCanceled(true);
		}
	}
	
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