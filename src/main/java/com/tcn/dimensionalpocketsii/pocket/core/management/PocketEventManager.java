package com.tcn.dimensionalpocketsii.pocket.core.management;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.tcn.cosmoslibrary.common.chat.CosmosChatUtil;
import com.tcn.cosmoslibrary.common.event.PortalEvent;
import com.tcn.cosmoslibrary.common.lib.ComponentColour;
import com.tcn.cosmoslibrary.common.lib.ComponentHelper;
import com.tcn.cosmoslibrary.common.lib.ComponentHelper.Value;
import com.tcn.cosmoslibrary.common.lib.CosmosChunkPos;
import com.tcn.cosmoslibrary.system.primative.ObjectHolder2;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.core.management.ConfigurationManager;
import com.tcn.dimensionalpocketsii.core.management.DimensionManager;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.block.AbstractBlockPocket;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockFocus;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockPocket;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockPocketEnhanced;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallBase;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallDoor;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallEdge;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallModule;
import com.tcn.dimensionalpocketsii.pocket.core.block.ItemBlockPocket;
import com.tcn.dimensionalpocketsii.pocket.core.block.ItemBlockPocketEnhanced;
import com.tcn.dimensionalpocketsii.pocket.core.block.entity.BlockEntityFocus;
import com.tcn.dimensionalpocketsii.pocket.core.event.PocketBlockEvent;
import com.tcn.dimensionalpocketsii.pocket.core.event.PocketEvent;
import com.tcn.dimensionalpocketsii.pocket.core.gson.PocketChunkInfo;
import com.tcn.dimensionalpocketsii.pocket.core.registry.StorageManager;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;
import com.tcn.dimensionalpocketsii.pocket.network.packet.misc.PacketFocusTeleport;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.server.ServerLifecycleHooks;

/**
 * 
 * @author TheCosmicNebula_, VsnGamer
 *
 * #OPEN
 * This means that any code contained in this class can be copied as per the MIT Licence of the Original Author.
 * 
 */

@SuppressWarnings({ "resource", "deprecation" })
@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID, bus = EventBusSubscriber.Bus.FORGE)
public class PocketEventManager {

	@OnlyIn(Dist.CLIENT)
	private static boolean lastSneaking;
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onInputEvent(InputEvent event){
		LocalPlayer player = Minecraft.getInstance().player;
		
		if (player == null || player.isSpectator() || !player.isAlive() || player.input == null) {
			return;
		}
		
		boolean sneaking = player.input.shiftKeyDown;
		
		if (lastSneaking != sneaking) {
			lastSneaking = sneaking;
			
			if (sneaking) {
				tryTeleport(player, Direction.DOWN);
			}
		}
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onJumpEvent(LivingEvent.LivingJumpEvent event) {
		Entity entity = event.getEntity();
		
		if (entity instanceof Player && entity.level().isClientSide) {
			tryTeleport((LocalPlayer) entity, Direction.UP);
		}
	}

	@OnlyIn(Dist.CLIENT)
    private static void tryTeleport(LocalPlayer player, Direction facing) {
        BlockGetter world = player.getCommandSenderWorld();

        BlockPos fromPos = getOriginFocus(player);
        if (fromPos == null) {
        	return;
        }

        BlockPos.MutableBlockPos toPos = fromPos.mutable();
        
        BlockEntity entity = world.getBlockEntity(fromPos);

        while (true) {
            toPos.setY(toPos.getY() + facing.getStepY());
            
            if (world.isOutsideBuildHeight(toPos) || Math.abs(toPos.getY() - fromPos.getY()) > ConfigurationManager.getInstance().getFocusJumpRange()) {
                break;
            }
            
            BlockFocus focus = FocusJumpHandler.getElevator(world.getBlockState(toPos));
            BlockEntity toEntity = world.getBlockEntity(toPos);
           
            if (focus != null && FocusJumpHandler.isBlocked(world, toPos)) {
            	if (entity instanceof BlockEntityFocus) {
            		BlockEntityFocus fromFocusEntity = (BlockEntityFocus) entity;
            		
            		if (toEntity instanceof BlockEntityFocus) {
            			BlockEntityFocus toFocusEntity = (BlockEntityFocus) toEntity;
            			Pocket pocket = toFocusEntity.getPocket();
            			
            			if (pocket != null && toPos.getY() == toFocusEntity.getPocket().getInternalHeight()) {
            				return;
            			}
            			
            			if (fromFocusEntity.getJumpEnabled() && toFocusEntity.getJumpEnabled()) {
            				PocketNetworkManager.INSTANCE.sendToServer(new PacketFocusTeleport(fromPos, toPos));
 		                    break;
            			}
            		}
	            }
            }
        }
    }

    /**
     * Checks if a player(lower part) is in or has an elevator up to 2 blocks below
     *
     * @param player the player trying to teleport
     * @return the position of the first valid elevator or null if it doesn't exist
     */
	@OnlyIn(Dist.CLIENT)
    private static BlockPos getOriginFocus(LocalPlayer player) {
        Level world = player.getCommandSenderWorld();
        BlockPos pos = new BlockPos(player.blockPosition());

        // Check the player's feet and the 2 blocks under it
        for (int i = 0; i < 3; i++) {
            if (FocusJumpHandler.getElevator(world.getBlockState(pos)) != null && FocusJumpHandler.isBlocked(world, pos)) {
                return pos;
            }
            pos = pos.below();
        }

        // Focus doesn't exist or it's invalid
        return null;
    }
	
	@SubscribeEvent
	public static void onPocketBlockPlacedEvent(final PocketBlockEvent.PlacePocketBlock event) { }

	@SubscribeEvent
	public static void onPocketBlockPickupEvent(final PocketBlockEvent.PickupPocketBlock event) { }
	
	@SubscribeEvent
	public static void onPocketGeneratedEvent(final PocketEvent.GeneratePocketEvent event) { }
	
	@SubscribeEvent
	public static void onPortalTravelEvent(final PortalEvent.PortalTravel event) {
		Entity entity = event.getEntity();
		Level level = entity.level();
		BlockPos pos = event.getDestPos();
		CosmosChunkPos chunkPos = CosmosChunkPos.scaleToChunkPos(pos);
		ResourceLocation destDimension = event.getDestDimension();
		ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, destDimension);
		
		if (dimension.equals(DimensionManager.POCKET_WORLD)) {
			if (entity instanceof ServerPlayer) {
				ServerPlayer playerIn = (ServerPlayer) entity;
				
				Pocket pocket = StorageManager.getPocketFromChunkPosition(level, chunkPos);
				if (!pocket.checkIfPlayerCanShift(playerIn, EnumShiftDirection.GENERIC)) {
					if (!entity.level().isClientSide) {
						DimensionalPockets.CONSOLE.debug("[Event Cancellation] <cosmosportals:portaltravel> { Player: '" + playerIn.getDisplayName().getString() + "', UUID: '" + playerIn.getUUID() + "' } tried to use a Portal to Pocket: { " + pocket.getDominantChunkPos() + " } that has been locked by the Owner. This event has been cancelled.");
					}
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onContainerLinkEvent(final PortalEvent.LinkContainer event) {
		Entity entity = event.getEntity();
		Level level = entity.level();
		BlockPos pos = event.getEntityPos();
		CosmosChunkPos chunkPos = CosmosChunkPos.scaleToChunkPos(pos);
		ResourceLocation destDimension = event.getDestDimension();
		ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, destDimension);

		if (dimension.equals(DimensionManager.POCKET_WORLD)) {
			Pocket pocket = StorageManager.getPocketFromChunkPosition(level, chunkPos);
			
			if (entity instanceof Player) {
				Player playerIn = (Player) entity;
				
				if (!pocket.checkIfOwner(playerIn)) {
					if (!entity.level().isClientSide) {
						DimensionalPockets.CONSOLE.debug("[Event Cancellation] <cosmosportals:containerlink> { Player: '" + playerIn.getDisplayName().getString() + "', UUID: '" + playerIn.getUUID() + "' } tried to create a Dimension Container inside Pocket: { " + pocket.getDominantChunkPos() + " } which they don't own. This event has been cancelled.");
					}
					event.setCanceled(true);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onEnderTeleportEvent(EntityTeleportEvent.EnderEntity event) {
		Entity entity = event.getEntity();
		
		if (entity != null) {
			Level world = entity.level();
			ResourceKey<Level> dimension = world.dimension();
			
			if (world != null) {
				if (entity instanceof Player) {
					Player playerIn = (Player) entity;
					
					if (dimension.equals(DimensionManager.POCKET_WORLD)) {
						if (!world.isClientSide) {
							DimensionalPockets.CONSOLE.debug("[Event Cancellation] <enderteleport> { Player: '" + playerIn.getDisplayName().getString() + "', UUID: '" + playerIn.getUUID() + "' } tried to ender teleport. This event has been cancelled.");
						}
						event.setCanceled(true);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEnderPearlTeleportEvent(EntityTeleportEvent.EnderPearl event) {
		Entity entity = event.getEntity();
		
		if (entity != null) {
			Level world = entity.level();
			ResourceKey<Level> dimension = world.dimension();
			
			if (world != null) {
				if (entity instanceof Player) {
					Player playerIn = (Player) entity;
					
					if (!ConfigurationManager.getInstance().getCanUseItems()) {
						if (dimension.equals(DimensionManager.POCKET_WORLD)) {
							if (!world.isClientSide) {
								DimensionalPockets.CONSOLE.debug("[Event Cancellation] <enderpearlteleport> { Player: '" + playerIn.getDisplayName().getString() + "', UUID: '" + playerIn.getUUID() + "' } tried to use an Ender Pearl. This event has been cancelled.");
							}
							event.setCanceled(true);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onCommandEvent(CommandEvent event) {
		ParseResults<CommandSourceStack> results = event.getParseResults();
		CommandContextBuilder<CommandSourceStack> context = results.getContext();
		CommandSourceStack source = context.getSource();
		String command = results.getReader().getString();
		Entity entity = source.getEntity();
		
		if (entity != null) {
			Level world = entity.level();
			ResourceKey<Level> dimension = world.dimension();
			
			if (world != null) {
				if (dimension.equals(DimensionManager.POCKET_WORLD)) {
					if (entity instanceof ServerPlayer) {
						boolean cancelled = false;
						ServerPlayer playerIn = (ServerPlayer) entity;
					
						MinecraftServer serverIn = ServerLifecycleHooks.getCurrentServer();
						
						if (serverIn != null) {
							
							if (dimension.equals(DimensionManager.POCKET_WORLD)) {
								for (int i = 0; i < ConfigurationManager.getInstance().getBlockedCommands().size(); i++) {
									Object obj = ConfigurationManager.getInstance().getBlockedCommands().get(i);
									
									if (obj instanceof String) {
										String string = (String) obj;
										
										if (command.contains(string/* + " "*/)) {
											if (serverIn.isSingleplayer()) {
												System.out.println("SINGLE");
												if (!ConfigurationManager.getInstance().getCanUseCommands()) {
													cancelled = true;
												}
											} else {
												System.out.println("SERVER");
												if (playerIn.hasPermissions(ConfigurationManager.getInstance().getOPLevel())) {
													cancelled = false;
												}
											}
										}
									}
								}
							}
						}
						
						if (cancelled) {
							event.setCanceled(true);
							
							if (!world.isClientSide) {
								playerIn.sendSystemMessage(ComponentHelper.getErrorText("dimensionalpocketsii.error.command.usage").append(" " + Value.YELLOW + " /" + command));
								DimensionalPockets.CONSOLE.debug("[Event Cancellation] <usecommand> { Player: '" + playerIn.getDisplayName().getString() + "', UUID: '" + playerIn.getUUID() + "' } tried to use command: { " + command  + " }. This event has been cancelled.");
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
			if (entity instanceof Player) {
				Level world = entity.level();
				ResourceKey<Level> dimension = world.dimension();
				
				if (world != null) {
					if (dimension.equals(DimensionManager.POCKET_WORLD)) {
						event.setDistance(0.0F);
						event.setCanceled(true);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onLivingHurtEvent(LivingHurtEvent event) {
		Entity entity = event.getEntity();
		
		if (entity != null) {
			if (entity instanceof Player) {
				Level world = entity.level();
				ResourceKey<Level> dimension = world.dimension();
				
				if (world != null) {
					if (dimension.equals(DimensionManager.POCKET_WORLD)) {
						if (entity instanceof Player) {
							if (entity.level().getDifficulty() == Difficulty.PEACEFUL) {
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
			Level world = entity.level();
			ResourceKey<Level> dimension = world.dimension();
			
			if (world != null) {
				if (dimension.equals(DimensionManager.POCKET_WORLD)) {
					if (event.getState().getBlock() instanceof BlockWallBase) {
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
		Entity entity = event.getEntity();
		
		if (entity != null) {
			Level world = entity.level();
			ResourceKey<Level> dimension = world.dimension();
			
			if (world != null) {
				if (dimension.equals(DimensionManager.POCKET_WORLD)) {
					if (entity instanceof ServerPlayer) {
						if(entity.level().getDifficulty() == Difficulty.PEACEFUL) {
							((ServerPlayer) entity).setHealth(((Player) entity).getMaxHealth());
							event.setCanceled(true);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onEntityJoinLevelEvent(EntityJoinLevelEvent event) {
		Entity entity = event.getEntity();
		EntityType<?> entity_type = entity.getType();
		Level world = entity.level();
		ResourceKey<Level> dimension = world.dimension();
		MobCategory clazz = entity_type.getCategory();
		BlockPos pos = entity.blockPosition();
		CosmosChunkPos chunkPos = CosmosChunkPos.scaleToChunkPos(pos);
		
		if (ConfigurationManager.getInstance().getStopHostileSpawns()) {
			if (!(entity instanceof Player)) {
				if (dimension.equals(DimensionManager.POCKET_WORLD)) {
					if (!world.isClientSide) {
						Pocket pocket = StorageManager.getPocketFromChunkPosition(world, chunkPos);
						
						if (pocket != null) {
							boolean spawns = pocket.getHostileSpawnStateValue();
							
							if (!spawns) {
								if (clazz.equals(MobCategory.MONSTER)) {
									event.setCanceled(true);
								}
							}
						}
						
						if (entity_type.equals(EntityType.ELDER_GUARDIAN)) {
							if (!world.isClientSide) {
								DimensionalPockets.CONSOLE.debug("[Event Cancellation] <entityjoin> An Elder Guardian was attempted to spawn. This event has been cancelled.");
							}
							event.setCanceled(true);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityMountEvent(EntityMountEvent event) {
		Entity entity = event.getEntityMounting();
		Entity mounted = event.getEntityBeingMounted();
		ResourceKey<Level> dimension = entity.level().dimension();
		
		if (entity instanceof Player) {
			Player playerIn = (Player) entity;
			
			if (dimension.equals(DimensionManager.POCKET_WORLD)) {
				if (mounted.getType().equals(EntityType.MINECART) || mounted.getType().equals(EntityType.BOAT)) {
					if (!entity.level().isClientSide) {
						DimensionalPockets.CONSOLE.debug("[Event Cancellation] <entitymount> { Player: '" + playerIn.getDisplayName().getString() + "', UUID: '" + playerIn.getUUID() + "' } tried to mount entity: { " + mounted + " } This event has been cancelled.");
					}
					event.setCanceled(true);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onPlayerInteractRightClickItem(PlayerInteractEvent.RightClickItem event) {
		Entity entity = event.getEntity();
		ResourceKey<Level> dimension = entity.level().dimension();
		ItemStack stack = event.getItemStack();
		Item item = stack.getItem();
		
		ResourceLocation registry_name = new ResourceLocation(BuiltInRegistries.ITEM.getKey(item).getNamespace(), BuiltInRegistries.ITEM.getKey(item).getPath());
		
		if (!ConfigurationManager.getInstance().getCanUseItems()) {
			if (entity instanceof Player) {
				Player playerIn = (Player) entity;
				
				if (dimension.equals(DimensionManager.POCKET_WORLD)) {					
					for (int i = 0; i < ConfigurationManager.getInstance().getBlockedItems().size(); i++) {
						Object obj = ConfigurationManager.getInstance().getBlockedItems().get(i);
						
						if (obj instanceof String) {
							String string = (String) obj;
							
							if (string.contains(":")) {
								String[] split = string.split(":");
								
								ResourceLocation string_location = new ResourceLocation(split[0], split[1]);
								
								if (registry_name.equals(string_location)) {									
									if (!entity.level().isClientSide) {
										CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.error.item.usage"));
										DimensionalPockets.CONSOLE.debug("[Event Cancellation] <rightclickitem> { Player: '" + playerIn.getDisplayName().getString() + "', UUID: '" + playerIn.getUUID() + "' } tried to right click a blacklised Item. This event has been cancelled.");
									}
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
		ResourceKey<Level> dimension = entity.level().dimension();
		ItemStack stack = event.getItem();
		Item item = stack.getItem();

		ResourceLocation registry_name = new ResourceLocation(BuiltInRegistries.ITEM.getKey(item).getNamespace(), BuiltInRegistries.ITEM.getKey(item).getPath());
		
		if (!ConfigurationManager.getInstance().getCanUseItems()) {
			if (entity instanceof Player) {
				Player playerIn = (Player) entity;
				
				if (dimension.equals(DimensionManager.POCKET_WORLD)) {
					for (int i = 0; i < ConfigurationManager.getInstance().getBlockedItems().size(); i++) {
						Object obj = ConfigurationManager.getInstance().getBlockedItems().get(i);
						
						if (obj instanceof String) {
							String string = (String) obj;
							
							if (string.contains(":")) {
								String[] split = string.split(":");
								
								ResourceLocation string_location = new ResourceLocation(split[0], split[1]);
								
								if (registry_name.equals(string_location)) {
									if (!entity.level().isClientSide) {
										CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.error.item.usage"));
										DimensionalPockets.CONSOLE.debug("[Event Cancellation] <useitem> { Player: '" + playerIn.getDisplayName().getString() + "', UUID: '" + playerIn.getUUID() + "' } tried to use a blacklised Item. This event has been cancelled.");
									}
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
		ResourceKey<Level> dimension = entity.level().dimension();
		BlockState state = event.getPlacedBlock();
		Block block = state.getBlock();

		ResourceLocation registry_name = new ResourceLocation(BuiltInRegistries.ITEM.getKey(block.asItem()).getNamespace(), BuiltInRegistries.ITEM.getKey(block.asItem()).getPath());
		
		if (entity instanceof Player) {
			Player playerIn = (Player) entity;
			
			if (!ConfigurationManager.getInstance().getCanPlaceStructures()) {
				if (dimension.equals(DimensionManager.POCKET_WORLD)) {
					for (int i = 0; i < ConfigurationManager.getInstance().getBlockedStructures().size(); i++) {
						Object obj = ConfigurationManager.getInstance().getBlockedStructures().get(i);
						
						if (obj instanceof String) {
							String string = (String) obj;
							
							if (string.contains(":")) {
								String[] split = string.split(":");
								ResourceLocation string_location = new ResourceLocation(split[0], split[1]);
								
								if (registry_name.equals(string_location)) {
									if (!entity.level().isClientSide) {
										CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.error.block.usage"));
										DimensionalPockets.CONSOLE.debug("[Event Cancellation] <placeblock> Player: { " + playerIn.getDisplayName().getContents() + " } tried to right click a blacklised Item. This event has been cancelled.");
									}
									event.setCanceled(true);
								}
							}
						}
					}
				}
			}
			
			if (block instanceof BlockPocket || block instanceof BlockPocketEnhanced) {
				ItemStack mainStack = playerIn.getMainHandItem();
				ItemStack offHandStack = playerIn.getOffhandItem();
				
				if (mainStack.getItem() instanceof ItemBlockPocket || offHandStack.getItem() instanceof ItemBlockPocketEnhanced) {
					if (mainStack.hasTag()) {
						CompoundTag compound = mainStack.getTag();
						
						if (compound.contains("nbt_data")) {
							CompoundTag nbt_data = compound.getCompound("nbt_data");
							CompoundTag chunk_set = nbt_data.getCompound("chunk_set");
							int x = chunk_set.getInt("X");
							int z = chunk_set.getInt("Z");
							
							CosmosChunkPos pos = CosmosChunkPos.scaleToChunkPos(event.getPos());
							CosmosChunkPos testPos = new CosmosChunkPos(x, z);
							
							if (pos.equals(testPos)) {
								event.setCanceled(true);
							}
						}
					}
				}
				
				if (offHandStack.getItem() instanceof ItemBlockPocket || offHandStack.getItem() instanceof ItemBlockPocketEnhanced) {
					if (offHandStack.hasTag()) {
						CompoundTag compound = offHandStack.getTag();
						
						if (compound.contains("nbt_data")) {
							CompoundTag nbt_data = compound.getCompound("nbt_data");
							CompoundTag chunk_set = nbt_data.getCompound("chunk_set");
							int x = chunk_set.getInt("X");
							int z = chunk_set.getInt("Z");
							
							CosmosChunkPos pos = CosmosChunkPos.scaleToChunkPos(event.getPos());
							CosmosChunkPos testPos = new CosmosChunkPos(x, z);
							
							if (pos.equals(testPos)) {
								event.setCanceled(true);
							}
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onBlockBreakEvent(BlockEvent.BreakEvent event) {
		Player entity = event.getPlayer();
		
		if (entity != null) {
			Level world = entity.level();
			BlockPos pos = event.getPos();
			BlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			ResourceKey<Level> dimension = world.dimension();
			
			if (world != null) {
				if (dimension.equals(DimensionManager.POCKET_WORLD)) {
					if (!ConfigurationManager.getInstance().getCanDestroyWalls()) {
						if (block instanceof BlockWallBase || block instanceof BlockWallEdge || block instanceof BlockWallModule 
								|| block.equals(Blocks.BEDROCK) || block instanceof BlockFocus || block instanceof BlockWallDoor) {
											
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
	
	@SubscribeEvent
	public static void onEntityPlaceBlockEvent(BlockEvent.EntityPlaceEvent event) {
		BlockState state = event.getPlacedBlock();
		Block block = state.getBlock();

		if (block instanceof AbstractBlockPocket) {
			AbstractBlockPocket blockPocket = (AbstractBlockPocket) block;
			
			Entity entity = event.getEntity();
			BlockPos pos = event.getPos();
			
			if (entity != null && entity instanceof Player) {
				Player player = (Player) entity;
				Level level = player.level();
				
				if (level != null) {
					ResourceKey<Level> dimension = level.dimension();
					ObjectHolder2<PocketChunkInfo, Pocket> info = StorageManager.getChunkInfoForPocket(dimension, pos);
					
					if (info != null) {
						PocketChunkInfo chunkInfo = info.getFirst();
						
						if (blockPocket.getIsSingleChunk() != chunkInfo.isSingleChunk()) {
							event.setCanceled(true);
							
							if (info.getSecond().getOwnerName().equals(entity.getDisplayName().getString())) {
								entity.sendSystemMessage(ComponentHelper.style(ComponentColour.ORANGE, "You cannot place a Pocket here, as there was a different one here before."));
							}
						}
					}
				}
			}
		}
	}
}