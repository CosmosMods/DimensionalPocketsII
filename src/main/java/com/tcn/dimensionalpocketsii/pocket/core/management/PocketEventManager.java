package com.tcn.dimensionalpocketsii.pocket.core.management;

import java.util.UUID;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.tcn.cosmoslibrary.client.impl.util.TextHelper;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.core.management.CoreConfigurationManager;
import com.tcn.dimensionalpocketsii.core.management.CoreDimensionManager;
import com.tcn.dimensionalpocketsii.core.management.ModBusManager;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWall;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallEdge;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
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
		Entity entity = event.getEntity();
		
		if (entity != null) {
			World world = entity.getEntityWorld();
			RegistryKey<World> dimension = world.getDimensionKey();
			
			if (world != null) {
				if (entity instanceof PlayerEntity) {
					if (dimension.equals(CoreDimensionManager.POCKET_WORLD)) {
						event.setCanceled(true);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onBlockPlacedEvent(BlockEvent.EntityPlaceEvent event) {
		if (event.getEntity() instanceof ServerPlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntity();
			
			if (event.getPlacedBlock().getBlock() instanceof BlockWall 
					|| event.getPlacedBlock().getBlock() instanceof BlockWallEdge
					|| event.getPlacedBlock().getBlock() == ModBusManager.BLOCK_DIMENSIONAL_CORE) {
				
				if (!player.isCreative()) {
					event.setCanceled(true);
					
					player.inventory.markDirty();
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onCommandEvent(CommandEvent event) {
		ParseResults<CommandSource> results = event.getParseResults();
		CommandContextBuilder<CommandSource> context = results.getContext();
		CommandSource source = context.getSource();
		String command = results.getReader().getString();
		
		Entity entity = source.getEntity();
		
		if (entity != null) {
			World world = entity.getEntityWorld();
			RegistryKey<World> dimension = world.getDimensionKey();
			
			if (world != null) {
				if (dimension.equals(CoreDimensionManager.POCKET_WORLD)) {
					if (entity instanceof PlayerEntity) {
						if (command.contains("tp")) {
							if (((PlayerEntity) entity).isCreative()) {
								event.setCanceled(false);
							}  else if (!CoreConfigurationManager.getInstance().getCanTeleport()) {
								entity.sendMessage(new StringTextComponent(TextHelper.RED + "You cannot use this command inside a Pocket."), UUID.randomUUID());
								event.setCanceled(true);
							}
						} 
						
						else if (command.contains("kill")) {
							entity.sendMessage(new StringTextComponent(TextHelper.RED + "You cannot use this command inside a Pocket."), UUID.randomUUID());
							event.setCanceled(true);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onLivingFall(LivingFallEvent event) {
		Entity entity = event.getEntity();
		
		if (entity != null) {
			World world = entity.getEntityWorld();
			RegistryKey<World> dimension = world.getDimensionKey();
			
			if (world != null) {
				if (dimension.equals(CoreDimensionManager.POCKET_WORLD)) {
					event.setDistance(0.0F);
					event.setCanceled(true);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent event) {
		Entity entity = event.getEntityLiving();
		
		if (entity != null) {
			World world = entity.getEntityWorld();
			RegistryKey<World> dimension = world.getDimensionKey();
			
			if (world != null) {
				if (dimension.equals(CoreDimensionManager.POCKET_WORLD)) {
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
			}
		}
	}
	
	@SubscribeEvent
	public void onDestroyBlock(LivingDestroyBlockEvent event) {
		Entity entity = event.getEntity();
		
		if (entity != null) {
			World world = entity.getEntityWorld();
			RegistryKey<World> dimension = world.getDimensionKey();
			
			if (world != null) {
				if (dimension.equals(CoreDimensionManager.POCKET_WORLD)) {
					if (event.getState().getBlock() instanceof BlockWall) {
						event.setCanceled(true);
						
						world.notifyBlockUpdate(event.getPos(), event.getState(), event.getState(), 3);
						world.notifyNeighborsOfStateChange(event.getPos(), event.getState().getBlock()); //.notifyBlockUpdate(event.getPos(), event.getState(), event.getState(), 3);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		Entity entity = event.getEntityLiving();
		
		if (entity != null) {
			World world = entity.getEntityWorld();
			RegistryKey<World> dimension = world.getDimensionKey();
			
			if (world != null) {
				if (dimension.equals(CoreDimensionManager.POCKET_WORLD)) {
					if (entity instanceof ServerPlayerEntity) {
						if(entity.getEntityWorld().getDifficulty() == Difficulty.PEACEFUL) {
							((ServerPlayerEntity) entity).setHealth(((PlayerEntity) entity).getMaxHealth());
							event.setCanceled(true);
						} else {
							((ServerPlayerEntity) entity).func_242111_a(World.OVERWORLD, null, 0.0F, true, true);
							event.setCanceled(false);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent event) {
		PlayerEntity entity = event.getPlayer();
		
		if (entity != null) {
			World world = entity.getEntityWorld();
			BlockPos pos = event.getPos();
			BlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			RegistryKey<World> dimension = world.getDimensionKey();
			
			if (world != null) {
				if (dimension.equals(CoreDimensionManager.POCKET_WORLD)) {
					if (!CoreConfigurationManager.getInstance().getCanDestroyWalls()) {
						if (block.equals(ModBusManager.BLOCK_WALL) 
								|| block.equals(ModBusManager.BLOCK_WALL_EDGE)
									|| block.equals(ModBusManager.BLOCK_WALL_CONNECTOR)) {
										//|| event.getWorld().getBlockState(event.getPos()).getBlock() == BlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_ENERGY_DISPLAY
											//|| event.getWorld().getBlockState(event.getPos()).getBlock() == BlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_FLUID_DISPLAY) {
							
							for (Direction c: Direction.values()) {
								BlockPos offset_pos = pos.offset(c);
								BlockState offset_state = world.getBlockState(offset_pos);

								if (!world.isAirBlock(offset_pos)) {
									world.notifyBlockUpdate(offset_pos, offset_state, offset_state, 3);
								}
							}
							
							event.setCanceled(true);
						}
					}
				}
			}
		}
	}
}