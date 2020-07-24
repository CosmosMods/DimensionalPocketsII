package com.zeher.dimpockets.pocket.core.manager;

import com.zeher.dimpockets.DimReference;
import com.zeher.dimpockets.DimensionalPockets;
import com.zeher.dimpockets.core.manager.ModBlockManager;
import com.zeher.dimpockets.core.manager.ModConfigManager;
import com.zeher.dimpockets.pocket.core.block.BlockWall;
import com.zeher.zeherlib.api.client.util.TextHelper;
import com.zeher.zeherlib.mod.util.ModUtil;

import net.minecraft.block.Block;
import net.minecraft.command.CommandKill;
import net.minecraft.command.CommandTP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID)
public class PocketEventManager {
	
	@SubscribeEvent
	public void onEnderTeleport(EnderTeleportEvent event) {
		//Disable ender-pearl functionality
		if (event.getEntityLiving().world.provider.getDimension() == DimReference.CONSTANT.POCKET_DIMENSION_ID) {
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onCommandEvent(CommandEvent event) {
		if (event.getCommand() instanceof CommandTP) {
			Entity player = event.getSender().getCommandSenderEntity();
			
			if (player instanceof EntityPlayer) {
				if (((EntityPlayer) player).isCreative()) {
					event.setCanceled(false);
				} else if (!ModConfigManager.CAN_TELEPORT_TO_DIM) {
						ModUtil.sendPlayerMessage((EntityPlayer) player, TextHelper.RED + "You cannot use this command inside a Pocket.");
						event.setCanceled(true);
				}
			}
		}
		
		else if (event.getCommand() instanceof CommandKill) {
			Entity player = event.getSender().getCommandSenderEntity();
			ModUtil.sendPlayerMessage((EntityPlayer) player, TextHelper.RED + "You cannot use this command inside a Pocket.");
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onLivingFall(LivingFallEvent event) {
		//Negate fall damage.
		if (event.getEntityLiving().world.provider.getDimension() == DimReference.CONSTANT.POCKET_DIMENSION_ID) {
			event.setDistance(0.0F);
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent event) {
		Entity entity = event.getEntityLiving();
		DamageSource damage = event.getSource();
		String type = damage.damageType;
		
		if (entity instanceof EntityPlayer) {
			if (entity.getEntityWorld().getDifficulty() == EnumDifficulty.PEACEFUL) {
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
		if (entity instanceof EntityPlayer) {
			if(entity.getEntityWorld().getDifficulty() == EnumDifficulty.PEACEFUL) {
				((EntityPlayer) entity).setHealth(((EntityPlayer) entity).getMaxHealth());
				event.setCanceled(true);
			} else {
				((EntityPlayer) entity).setSpawnDimension(0);
				event.setCanceled(false);
			}
		}
	}
	
	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent event) {
		Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
		
		if (!ModConfigManager.CAN_DESTROY_WALLS_IN_CREATIVE
				&& block == ModBlockManager.BLOCK_DIMENSIONAL_POCKET_WALL 
				|| block == ModBlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_EDGE
				|| block == ModBlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_CONNECTOR
				|| block == ModBlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_ENERGY_DISPLAY
				|| block == ModBlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_FLUID_DISPLAY) {
			event.setCanceled(true);
		}
	}
}