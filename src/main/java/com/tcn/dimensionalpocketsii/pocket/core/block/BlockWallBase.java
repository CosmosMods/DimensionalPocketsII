package com.tcn.dimensionalpocketsii.pocket.core.block;

import com.tcn.cosmoslibrary.common.block.CosmosBlockUnbreakable;
import com.tcn.cosmoslibrary.common.chat.CosmosChatUtil;
import com.tcn.cosmoslibrary.common.lib.ComponentHelper;
import com.tcn.cosmoslibrary.common.lib.CosmosChunkPos;
import com.tcn.cosmoslibrary.common.util.CosmosUtil;
import com.tcn.dimensionalpocketsii.core.management.ConfigurationManager;
import com.tcn.dimensionalpocketsii.core.management.DimensionManager;
import com.tcn.dimensionalpocketsii.core.management.ObjectManager;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.block.entity.BlockEntityModuleAnvil;
import com.tcn.dimensionalpocketsii.pocket.core.block.entity.BlockEntityModuleArmourWorkbench;
import com.tcn.dimensionalpocketsii.pocket.core.block.entity.BlockEntityModuleBlastFurnace;
import com.tcn.dimensionalpocketsii.pocket.core.block.entity.BlockEntityModuleCharger;
import com.tcn.dimensionalpocketsii.pocket.core.block.entity.BlockEntityModuleFurnace;
import com.tcn.dimensionalpocketsii.pocket.core.block.entity.BlockEntityModuleGenerator;
import com.tcn.dimensionalpocketsii.pocket.core.block.entity.BlockEntityModuleSmithingTable;
import com.tcn.dimensionalpocketsii.pocket.core.block.entity.BlockEntityModuleUpgradeStation;
import com.tcn.dimensionalpocketsii.pocket.core.registry.StorageManager;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;
import com.tcn.dimensionalpocketsii.pocket.core.util.PocketUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BlockWallBase extends CosmosBlockUnbreakable {
	
	public BlockWallBase(Block.Properties prop) {
		super(prop);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level levelIn, BlockPos pos, Player playerIn, InteractionHand handIn, BlockHitResult hit) {
		if (CosmosUtil.getStackItem(playerIn) instanceof BlockItem) {
			return InteractionResult.FAIL;
		}
		
		if (PocketUtil.isDimensionEqual(levelIn, DimensionManager.POCKET_WORLD)) {
			Pocket pocket = StorageManager.getPocketFromChunkPosition(levelIn, CosmosChunkPos.scaleToChunkPos(pos));
			
			if (pocket.exists()) {
				if (!playerIn.isShiftKeyDown()) {
					return InteractionResult.FAIL;
				} else {
					if (!playerIn.getItemInHand(handIn).isEmpty()) {
						if (CosmosUtil.handItem(playerIn, ObjectManager.module_focus)) {
							if (pos.getY() == 1) {
								if (pocket.checkIfOwner(playerIn)) {
									if (!levelIn.isClientSide) {
										levelIn.setBlockAndUpdate(pos, ObjectManager.block_dimensional_focus.defaultBlockState());
										
										if (!playerIn.isCreative()) {
											playerIn.getInventory().getSelected().shrink(1);
										}
									}
									
									return InteractionResult.SUCCESS;
								} else {
									CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.not_owner"));
									return InteractionResult.FAIL;
								}
							}
						}

						else if (CosmosUtil.handItem(playerIn, ObjectManager.module_crafter)) {
							if (pocket.checkIfOwner(playerIn)) {
								if (!levelIn.isClientSide) {
									levelIn.setBlockAndUpdate(pos, ObjectManager.block_wall_crafter.defaultBlockState());
	
									if (!playerIn.isCreative()) {
										playerIn.getInventory().getSelected().shrink(1);
									}
								}
								
								return InteractionResult.SUCCESS;
							} else {
								CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.not_owner"));
								return InteractionResult.FAIL;
							}
						}
						
						else if (CosmosUtil.handItem(playerIn, ObjectManager.module_connector)) {
							if (pocket.checkIfOwner(playerIn)) {
								if (!levelIn.isClientSide) {
									levelIn.setBlockAndUpdate(pos, ObjectManager.block_wall_connector.defaultBlockState());
									
									if (!playerIn.isCreative()) {
										playerIn.getInventory().getSelected().shrink(1);
									}
								}
								
								if (!levelIn.isClientSide) {
									pocket.addUpdateable(pos);
								}
								
								return InteractionResult.SUCCESS;
							} else {
								CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.not_owner"));
								return InteractionResult.FAIL;
							}
						}
						
						else if (CosmosUtil.handItem(playerIn, ObjectManager.module_creative_energy)) {
							if (pocket.checkIfOwner(playerIn)) {
								if (!levelIn.isClientSide) {
									levelIn.setBlockAndUpdate(pos, ObjectManager.block_wall_creative_energy.defaultBlockState());
									
									if (!playerIn.isCreative()) {
										playerIn.getInventory().getSelected().shrink(1);
									}
								}
								
								return InteractionResult.SUCCESS;
							} else {
								CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.not_owner"));
								return InteractionResult.FAIL;
							}
						}

						else if (CosmosUtil.handItem(playerIn, ObjectManager.module_creative_fluid)) {
							if (pocket.checkIfOwner(playerIn)) {
								if (!levelIn.isClientSide) {
									levelIn.setBlockAndUpdate(pos, ObjectManager.block_wall_creative_fluid.defaultBlockState());
									
									if (!playerIn.isCreative()) {
										playerIn.getInventory().getSelected().shrink(1);
									}
								}
									
								return InteractionResult.SUCCESS;
							} else {
								CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.not_owner"));
								return InteractionResult.FAIL;
							}
						}
						
						if (pos.getY() != 1 && pos.getY() != pocket.getInternalHeight()) {
							if (CosmosUtil.handItem(playerIn, ObjectManager.module_charger)) {
								if (!levelIn.isClientSide) {
									if (pocket.checkIfOwner(playerIn)) {
										levelIn.setBlockAndUpdate(pos, ObjectManager.block_wall_charger.defaultBlockState());
	
										BlockEntity entity = levelIn.getBlockEntity(pos);
										
										if (entity instanceof BlockEntityModuleCharger) {
											((BlockEntityModuleCharger) levelIn.getBlockEntity(pos)).loadFromItemStack(CosmosUtil.getStack(playerIn));
										}
	
										if (!playerIn.isCreative()) {
											playerIn.getInventory().getSelected().shrink(1);
										}
										pocket.addUpdateable(pos);
									}
									
									return InteractionResult.SUCCESS;
								} else {
									CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.not_owner"));
									return InteractionResult.FAIL;
								}
							}

							else if (CosmosUtil.handItem(playerIn, ObjectManager.module_smithing_table)) {
								if (pocket.checkIfOwner(playerIn)) {
									if (!levelIn.isClientSide) {
										levelIn.setBlockAndUpdate(pos, ObjectManager.block_wall_smithing_table.defaultBlockState());
	
										BlockEntity entity = levelIn.getBlockEntity(pos);
										
										if (entity instanceof BlockEntityModuleSmithingTable) {
											((BlockEntityModuleSmithingTable) levelIn.getBlockEntity(pos)).loadFromItemStack(CosmosUtil.getStack(playerIn));
										}
	
										if (!playerIn.isCreative()) {
											playerIn.getInventory().getSelected().shrink(1);
										}
									}
									
									return InteractionResult.SUCCESS;
								} else {
									CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.not_owner"));
									return InteractionResult.FAIL;
								}
							}

							else if (CosmosUtil.handItem(playerIn, ObjectManager.module_upgrade_station)) {
								if (pocket.checkIfOwner(playerIn)) {
									if (!levelIn.isClientSide) {
										levelIn.setBlockAndUpdate(pos, ObjectManager.block_wall_upgrade_station.defaultBlockState());
										
										BlockEntity entity = levelIn.getBlockEntity(pos);
										
										if (entity instanceof BlockEntityModuleUpgradeStation) {
											((BlockEntityModuleUpgradeStation) levelIn.getBlockEntity(pos)).loadFromItemStack(CosmosUtil.getStack(playerIn));
										}
	
										if (!playerIn.isCreative()) {
											playerIn.getInventory().getSelected().shrink(1);
										}
									}
									
									return InteractionResult.SUCCESS;
								} else {
									CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.not_owner"));
									return InteractionResult.FAIL;
								}
							}
							
							else if (CosmosUtil.handItem(playerIn, ObjectManager.module_furnace)) {
								if (pocket.checkIfOwner(playerIn)) {
									if (!levelIn.isClientSide) {
										levelIn.setBlockAndUpdate(pos, ObjectManager.block_wall_furnace.defaultBlockState());
										
										BlockEntity entity = levelIn.getBlockEntity(pos);
										
										if (entity instanceof BlockEntityModuleFurnace) {
											((BlockEntityModuleFurnace) levelIn.getBlockEntity(pos)).loadFromItemStack(CosmosUtil.getStack(playerIn));
										}
	
										if (!playerIn.isCreative()) {
											playerIn.getInventory().getSelected().shrink(1);
										}
									}
									
									return InteractionResult.SUCCESS;
								} else {
									CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.not_owner"));
									return InteractionResult.FAIL;
								}
							}
							
							else if (CosmosUtil.handItem(playerIn, ObjectManager.module_blast_furnace)) {
								if (pocket.checkIfOwner(playerIn)) {
									if (!levelIn.isClientSide) {
										levelIn.setBlockAndUpdate(pos, ObjectManager.block_wall_blast_furnace.defaultBlockState());
										
										BlockEntity entity = levelIn.getBlockEntity(pos);
										
										if (entity instanceof BlockEntityModuleBlastFurnace) {
											((BlockEntityModuleBlastFurnace) levelIn.getBlockEntity(pos)).loadFromItemStack(CosmosUtil.getStack(playerIn));
										}
	
										if (!playerIn.isCreative()) {
											playerIn.getInventory().getSelected().shrink(1);
										}
									}
									
									return InteractionResult.SUCCESS;
								} else {
									CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.not_owner"));
									return InteractionResult.FAIL;
								}
							}

							else if (CosmosUtil.handItem(playerIn, ObjectManager.module_energy_display)) {
								if (pocket.checkIfOwner(playerIn)) {
									if (!levelIn.isClientSide) {
										levelIn.setBlockAndUpdate(pos, ((BlockWallEnergyDisplay) ObjectManager.block_wall_energy_display).updateState(state, pos, levelIn));
	
										if (!playerIn.isCreative()) {
											playerIn.getInventory().getSelected().shrink(1);
										}
	
										pocket.addUpdateable(pos);
									}
									
									return InteractionResult.SUCCESS;
								} else {
									CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.not_owner"));
									return InteractionResult.FAIL;
								}
							}

							else if (CosmosUtil.handItem(playerIn, ObjectManager.module_fluid_display)) {
								if (pocket.checkIfOwner(playerIn)) {
									if (!levelIn.isClientSide) {
										levelIn.setBlockAndUpdate(pos, ObjectManager.block_wall_fluid_display.defaultBlockState());

										if (!playerIn.isCreative()) {
											playerIn.getInventory().getSelected().shrink(1);
										}
	
										pocket.addUpdateable(pos);
									}
									
									return InteractionResult.SUCCESS;
								} else {
									CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.not_owner"));
									return InteractionResult.FAIL;
								}
							}

							else if (CosmosUtil.handItem(playerIn, ObjectManager.module_armour_workbench)) {
								if (pocket.checkIfOwner(playerIn)) {
									if (!levelIn.isClientSide) {
										levelIn.setBlockAndUpdate(pos, ObjectManager.block_wall_armour_workbench.defaultBlockState());
	
										BlockEntity entity = levelIn.getBlockEntity(pos);
										
										if (entity instanceof BlockEntityModuleArmourWorkbench) {
											((BlockEntityModuleArmourWorkbench) levelIn.getBlockEntity(pos)).loadFromItemStack(CosmosUtil.getStack(playerIn));
										}
	
										if (!playerIn.isCreative()) {
											playerIn.getInventory().getSelected().shrink(1);
										}
									}
									
									return InteractionResult.SUCCESS;
								} else {
									CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.not_owner"));
									return InteractionResult.FAIL;
								}
							}

							else if (CosmosUtil.handItem(playerIn, ObjectManager.module_generator)) {
								if (pocket.checkIfOwner(playerIn)) {
									if (!levelIn.isClientSide) {
										levelIn.setBlockAndUpdate(pos, ObjectManager.block_wall_generator.defaultBlockState());
	
										BlockEntity entity = levelIn.getBlockEntity(pos);
										
										if (entity instanceof BlockEntityModuleGenerator) {
											((BlockEntityModuleGenerator) levelIn.getBlockEntity(pos)).loadFromItemStack(CosmosUtil.getStack(playerIn));
										}
	
										if (!playerIn.isCreative()) {
											playerIn.getInventory().getSelected().shrink(1);
										}
									}
									
									return InteractionResult.SUCCESS;
								} else {
									CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.not_owner"));
									return InteractionResult.FAIL;
								}
							}
							
							else if (CosmosUtil.handItem(playerIn, ObjectManager.module_anvil)) {
								if (pocket.checkIfOwner(playerIn)) {
									if (!levelIn.isClientSide) {
										levelIn.setBlockAndUpdate(pos, ObjectManager.block_wall_anvil.defaultBlockState());
	
										BlockEntity entity = levelIn.getBlockEntity(pos);
										
										if (entity instanceof BlockEntityModuleAnvil) {
											((BlockEntityModuleAnvil) levelIn.getBlockEntity(pos)).loadFromItemStack(CosmosUtil.getStack(playerIn));
										}
	
										if (!playerIn.isCreative()) {
											playerIn.getInventory().getSelected().shrink(1);
										}
									}
									
									return InteractionResult.SUCCESS;
								} else {
									CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.not_owner"));
									return InteractionResult.FAIL;
								}
							}

						}
					} else {
						if (CosmosUtil.handEmpty(playerIn)) {
							pocket.shift(playerIn, EnumShiftDirection.LEAVE, null, null, null);
							return InteractionResult.SUCCESS;
						}
					}
				}
			} else {
				CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.null"));
				return InteractionResult.FAIL;
			}
		}
	
		return InteractionResult.FAIL;
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return true;
	}

	@Override
	public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		//DebugPacketSender.func_218806_a(worldIn, pos);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		if (ConfigurationManager.getInstance().getCanDestroyWalls()) {
			return this.defaultBlockState();
		}
		
		return Blocks.AIR.defaultBlockState();
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter blockReader, BlockPos posIn, BlockState stateIn) {
		if (ConfigurationManager.getInstance().getCanDestroyWalls()) {
			return new ItemStack(this);
		} else {
			return ItemStack.EMPTY;
		}
    }
}