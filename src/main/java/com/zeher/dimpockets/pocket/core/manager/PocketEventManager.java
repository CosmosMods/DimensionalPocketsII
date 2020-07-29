package com.zeher.dimpockets.pocket.core.manager;

import com.zeher.dimpockets.DimensionalPockets;
import com.zeher.dimpockets.core.manager.BusSubscriberMod;
import com.zeher.dimpockets.core.manager.DimConfigManager;
import com.zeher.dimpockets.core.manager.ModDimensionManager;
import com.zeher.dimpockets.pocket.core.block.BlockWall;
import com.zeher.dimpockets.pocket.core.block.BlockWallEdge;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID)
public class PocketEventManager {
	
	@SubscribeEvent
	public void onEnderTeleport(EnderTeleportEvent event) {
		//Disable ender-pearl functionality
		if (event.getEntityLiving().world.getDimension().getType() == ModDimensionManager.POCKET_DIMENSION.getDimensionType()) {
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onBlockPlacedEvent(BlockEvent.EntityPlaceEvent event) {
		if (event.getEntity() instanceof ServerPlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntity();
			
			if (event.getPlacedBlock().getBlock() instanceof BlockWall 
					|| event.getPlacedBlock().getBlock() instanceof BlockWallEdge
					|| event.getPlacedBlock().getBlock() == BusSubscriberMod.BLOCK_DIMENSIONAL_CORE) {
				
				if (!player.isCreative()) {
					event.setCanceled(true);
					
					player.inventory.markDirty();
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onCommandEvent(CommandEvent event) {
		/**
		if (event.getParseResults() instanceof TeleportCommand) {
			Entity player = event.getSender().getCommandSenderEntity();
			
			if (player instanceof PlayerEntity) {
				if (((PlayerEntity) player).isCreative()) {
					event.setCanceled(false);
				}
				
				else if (!DimConfigManager.CAN_TELEPORT_TO_DIM) {
						//ModUtil.sendPlayerMessage((PlayerEntity) player, TextHelper.RED + "You cannot use this command inside a Pocket.");
						event.setCanceled(true);
				}
			}
		}
		
		else if (event.getCommand() instanceof KillCommand) {
			Entity player = event.getSender().getCommandSenderEntity();
			//ModUtil.sendPlayerMessage((PlayerEntity) player, TextHelper.RED + "You cannot use this command inside a Pocket.");
			event.setCanceled(true);
		}*/
	}
	
	@SubscribeEvent
	public void onLivingFall(LivingFallEvent event) {
		//Negate fall damage.
		if (event.getEntityLiving().world.getDimension().getType() == ModDimensionManager.POCKET_DIMENSION.getDimensionType()) {
			event.setDistance(0.0F);
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent event) {
		Entity entity = event.getEntityLiving();
		
		if (entity instanceof PlayerEntity) {
			if (entity.getEntityWorld().getDifficulty() == Difficulty.PEACEFUL) {
				event.setCanceled(true);
			} else {
				event.setCanceled(false);
			}
		} else {
			event.setCanceled(false);
		}
	}
	
	@SubscribeEvent
	public void onDestroyBlock(LivingDestroyBlockEvent event) {
		//Disable the breaking of Pocket Wall Blocks
		if (event.getState().getBlock() instanceof BlockWall) {
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		Entity entity = event.getEntityLiving();
		
		//Disable death for players IN PEACEFUL
		if (entity instanceof PlayerEntity) {
			if(entity.getEntityWorld().getDifficulty() == Difficulty.PEACEFUL) {
				((PlayerEntity) entity).setHealth(((PlayerEntity) entity).getMaxHealth());
				event.setCanceled(true);
			} else {
				((PlayerEntity) entity).setSpawnDimenion(DimensionType.OVERWORLD);
				event.setCanceled(false);
			}
		}
	}
	
	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent event) {
		if (!DimConfigManager.CAN_DESTROY_WALLS_IN_CREATIVE && event.getWorld().getBlockState(event.getPos()).getBlock() == BusSubscriberMod.BLOCK_DIMENSIONAL_POCKET_WALL 
				|| event.getWorld().getBlockState(event.getPos()).getBlock() == BusSubscriberMod.BLOCK_DIMENSIONAL_POCKET_WALL_EDGE) {
					//|| event.getWorld().getBlockState(event.getPos()).getBlock() == BlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_CONNECTOR
						//|| event.getWorld().getBlockState(event.getPos()).getBlock() == BlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_ENERGY_DISPLAY
							//|| event.getWorld().getBlockState(event.getPos()).getBlock() == BlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_FLUID_DISPLAY) {
			event.setCanceled(false);
		}
	}
}