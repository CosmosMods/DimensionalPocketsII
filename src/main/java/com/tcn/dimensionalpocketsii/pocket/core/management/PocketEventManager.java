package com.tcn.dimensionalpocketsii.pocket.core.management;

import java.util.UUID;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.tcn.cosmoslibrary.common.chat.CosmosChatUtil;
import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper;
import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper.Value;
import com.tcn.cosmoslibrary.common.math.ChunkPos;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.core.management.CoreConfigurationManager;
import com.tcn.dimensionalpocketsii.core.management.CoreDimensionManager;
import com.tcn.dimensionalpocketsii.core.management.CoreModBusManager;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWall;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID, bus = EventBusSubscriber.Bus.FORGE)
public class PocketEventManager {
	
	@SubscribeEvent
	public static void onEnderTeleportEvent(EnderTeleportEvent event) {
		Entity entity = event.getEntity();
		
		if (entity != null) {
			World world = entity.level;
			RegistryKey<World> dimension = world.dimension();
			
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
	public static void onCommandEvent(CommandEvent event) {
		ParseResults<CommandSource> results = event.getParseResults();
		CommandContextBuilder<CommandSource> context = results.getContext();
		CommandSource source = context.getSource();
		String command = results.getReader().getString();
		
		Entity entity = source.getEntity();
		
		if (entity != null) {
			World world = entity.level;
			RegistryKey<World> dimension = world.dimension();
			
			if (world != null) {
				if (dimension.equals(CoreDimensionManager.POCKET_WORLD)) {
					if (entity instanceof PlayerEntity) {
						PlayerEntity playerIn = (PlayerEntity) entity;
						
						if (CoreConfigurationManager.getInstance().getCancelCommands()) {
							if (command.contains("tp")) {
								playerIn.sendMessage(CosmosCompHelper.getErrorText("dimensionalpocketsii.error.command.usage").append(" " + Value.ORANGE + "[ " + command + " ]"), UUID.randomUUID());
								event.setCanceled(true);
							} else if (command.contains("kill")) {
								playerIn.sendMessage(CosmosCompHelper.getErrorText("dimensionalpocketsii.error.command.usage").append(" " + Value.ORANGE + "[ " + command + " ]"), UUID.randomUUID());
								event.setCanceled(true);
							} else if (command.contains("setblock")) {
								playerIn.sendMessage(CosmosCompHelper.getErrorText("dimensionalpocketsii.error.command.usage").append(" " + Value.ORANGE + "[ " + command + " ]"), UUID.randomUUID());
								event.setCanceled(true);
							}
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onLivingFallEvent(LivingFallEvent event) {
		Entity entity = event.getEntity();
		
		if (entity != null) {
			if (entity instanceof PlayerEntity) {
				World world = entity.level;
				RegistryKey<World> dimension = world.dimension();
				
				if (world != null) {
					if (dimension.equals(CoreDimensionManager.POCKET_WORLD)) {
						event.setDistance(0.0F);
						event.setCanceled(true);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onLivingHurtEvent(LivingHurtEvent event) {
		Entity entity = event.getEntityLiving();
		
		if (entity != null) {
			if (entity instanceof PlayerEntity) {
				World world = entity.level;
				RegistryKey<World> dimension = world.dimension();
				
				if (world != null) {
					if (dimension.equals(CoreDimensionManager.POCKET_WORLD)) {
						if (entity instanceof PlayerEntity) {
							if (entity.level.getDifficulty() == Difficulty.PEACEFUL) {
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
	}
	
	@SubscribeEvent
	public static void onLivingDestroyBlockEvent(LivingDestroyBlockEvent event) {
		Entity entity = event.getEntity();
		
		if (entity != null) {
			World world = entity.level;
			RegistryKey<World> dimension = world.dimension();
			
			if (world != null) {
				if (dimension.equals(CoreDimensionManager.POCKET_WORLD)) {
					if (event.getState().getBlock() instanceof BlockWall) {
						event.setCanceled(true);
						
						world.sendBlockUpdated(event.getPos(), event.getState(), event.getState(), 3);
						//world.notifyNeighborsOfStateChange(event.getPos(), event.getState().getBlock()); //.notifyBlockUpdate(event.getPos(), event.getState(), event.getState(), 3);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onLivingDeathEvent(LivingDeathEvent event) {
		Entity entity = event.getEntityLiving();
		
		if (entity != null) {
			World world = entity.level;
			RegistryKey<World> dimension = world.dimension();
			
			if (world != null) {
				if (dimension.equals(CoreDimensionManager.POCKET_WORLD)) {
					if (entity instanceof ServerPlayerEntity) {
						if(entity.level.getDifficulty() == Difficulty.PEACEFUL) {
							((ServerPlayerEntity) entity).setHealth(((PlayerEntity) entity).getMaxHealth());
							event.setCanceled(true);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		EntityType<?> entity_type = entity.getType();
		World world = entity.level;
		RegistryKey<World> dimension = world.dimension();
		EntityClassification clazz = entity_type.getCategory();
		BlockPos pos = entity.blockPosition();
		ChunkPos chunkPos = ChunkPos.scaleToChunkPos(pos);
		
		if (dimension.equals(CoreDimensionManager.POCKET_WORLD)) {
			Pocket pocket = PocketRegistryManager.getPocketFromChunkPosition(chunkPos);
			
			if (pocket != null) {
				boolean spawns = pocket.getHostileSpawnStateValue();
				
				if (!spawns) {
					if (clazz.equals(EntityClassification.MONSTER)) {
						event.setCanceled(true);
					}
				}
			} else {
				if (entity_type.equals(EntityType.ELDER_GUARDIAN)) {
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityMountEvent(EntityMountEvent event) {
		Entity entity = event.getEntity();
		RegistryKey<World> dimension = entity.level.dimension();
		
		if (entity instanceof PlayerEntity) {
			if (dimension.equals(CoreDimensionManager.POCKET_WORLD)) {
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public static void onPlayerInteractRightClickItem(PlayerInteractEvent.RightClickItem event) {
		Entity entity = event.getEntity();
		RegistryKey<World> dimension = entity.level.dimension();
		ItemStack stack = event.getItemStack();
		Item item = stack.getItem();
		ResourceLocation registry_name = item.getRegistryName();
		
		if (!CoreConfigurationManager.getInstance().getCanUseItems()) {
			if (entity instanceof PlayerEntity) {
				PlayerEntity playerIn = (PlayerEntity) entity;
				
				if (dimension.equals(CoreDimensionManager.POCKET_WORLD)) {
					for (int i = 0; i < CoreConfigurationManager.getInstance().getDisallowedItems().size(); i++) {
						Object obj = CoreConfigurationManager.getInstance().getDisallowedItems().get(i);
						
						if (obj instanceof String) {
							String string = (String) obj;
							
							if (string.contains(":")) {
								String[] split = string.split(":");
								
								ResourceLocation string_location = new ResourceLocation(split[0], split[1]);
								
								if (registry_name.equals(string_location)) {
									CosmosChatUtil.sendServerPlayerMessage(playerIn, CosmosCompHelper.getErrorText("dimensionalpocketsii.error.item.usage"));
									
									event.setCanceled(true);
								}
							}
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onLivingEntityUseItemEvent(LivingEntityUseItemEvent event) {
		Entity entity = event.getEntity();
		RegistryKey<World> dimension = entity.level.dimension();
		ItemStack stack = event.getItem();
		Item item = stack.getItem();
		ResourceLocation registry_name = item.getRegistryName();
		
		if (!CoreConfigurationManager.getInstance().getCanUseItems()) {
			if (entity instanceof PlayerEntity) {
				PlayerEntity playerIn = (PlayerEntity) entity;
				
				if (dimension.equals(CoreDimensionManager.POCKET_WORLD)) {
					for (int i = 0; i < CoreConfigurationManager.getInstance().getDisallowedItems().size(); i++) {
						Object obj = CoreConfigurationManager.getInstance().getDisallowedItems().get(i);
						
						if (obj instanceof String) {
							String string = (String) obj;
							
							if (string.contains(":")) {
								String[] split = string.split(":");
								
								ResourceLocation string_location = new ResourceLocation(split[0], split[1]);
								
								if (registry_name.equals(string_location)) {
									CosmosChatUtil.sendServerPlayerMessage(playerIn, CosmosCompHelper.getErrorText("dimensionalpocketsii.error.item.usage"));
									
									event.setCanceled(true);
								}
							}
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onBlockEntityPlaceEvent(BlockEvent.EntityPlaceEvent event) {
		Entity entity = event.getEntity();
		RegistryKey<World> dimension = entity.level.dimension();
		BlockState state = event.getPlacedBlock();
		Block block = state.getBlock();
		ResourceLocation registry_name = block.getRegistryName();
		
		if (!CoreConfigurationManager.getInstance().getCanPlaceStructures()) {
			if (entity instanceof PlayerEntity) {
				PlayerEntity playerIn = (PlayerEntity) entity;
				
				if (dimension.equals(CoreDimensionManager.POCKET_WORLD)) {
					for (int i = 0; i < CoreConfigurationManager.getInstance().getDisallowedBlocks().size(); i++) {
						Object obj = CoreConfigurationManager.getInstance().getDisallowedBlocks().get(i);
						
						if (obj instanceof String) {
							String string = (String) obj;
							
							if (string.contains(":")) {
								String[] split = string.split(":");
								ResourceLocation string_location = new ResourceLocation(split[0], split[1]);
								
								if (registry_name.equals(string_location)) {
									CosmosChatUtil.sendServerPlayerMessage(playerIn, CosmosCompHelper.getErrorText("dimensionalpocketsii.error.block.usage"));
									
									event.setCanceled(true);
								}
							}
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onBlockBreakEvent(BlockEvent.BreakEvent event) {
		PlayerEntity entity = event.getPlayer();
		
		if (entity != null) {
			World world = entity.level;
			BlockPos pos = event.getPos();
			BlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			RegistryKey<World> dimension = world.dimension();
			
			if (world != null) {
				if (dimension.equals(CoreDimensionManager.POCKET_WORLD)) {
					if (!CoreConfigurationManager.getInstance().getCanDestroyWalls()) {
						if (block.equals(CoreModBusManager.BLOCK_WALL) 
							|| block.equals(CoreModBusManager.BLOCK_WALL_EDGE)
								|| block.equals(CoreModBusManager.BLOCK_WALL_CONNECTOR)
									|| block.equals(CoreModBusManager.BLOCK_WALL_CHARGER)
										|| block.equals(CoreModBusManager.BLOCK_WALL_ENERGY_DISPLAY)
											|| block.equals(Blocks.BEDROCK)) {
										
							for (Direction c: Direction.values()) {
								BlockPos offset_pos = pos.offset(c.getNormal());
								BlockState offset_state = world.getBlockState(offset_pos);

								if (!world.isEmptyBlock(offset_pos)) {
									world.sendBlockUpdated(offset_pos, offset_state, offset_state, 3);
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