package com.tcn.dimensionalpocketsii.pocket.core.block;

import com.tcn.cosmoslibrary.common.block.CosmosBlockUnbreakable;
import com.tcn.cosmoslibrary.common.chat.CosmosChatUtil;
import com.tcn.cosmoslibrary.common.lib.ComponentHelper;
import com.tcn.cosmoslibrary.common.lib.CosmosChunkPos;
import com.tcn.cosmoslibrary.common.util.CosmosUtil;
import com.tcn.dimensionalpocketsii.core.management.ConfigurationManager;
import com.tcn.dimensionalpocketsii.core.management.DimensionManager;
import com.tcn.dimensionalpocketsii.core.management.ModBusManager;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleArmourWorkbench;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleCharger;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleFurnace;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleGenerator;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleUpgradeStation;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
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
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand handIn, BlockHitResult hit) {
		if (!worldIn.isClientSide) {
			if (PocketUtil.isDimensionEqual(worldIn, DimensionManager.POCKET_WORLD)) {
				Pocket pocket = PocketRegistryManager.getPocketFromChunkPosition(CosmosChunkPos.scaleToChunkPos(pos));
				
				if (pocket.exists()) {
					if (!playerIn.isShiftKeyDown()) {
						
					} else {
						if (!playerIn.getItemInHand(handIn).isEmpty()) {
							
							if (CosmosUtil.handItem(playerIn, ModBusManager.MODULE_FOCUS)) {
								if (pos.getY() == 1) {
									if (pocket.checkIfOwner(playerIn)) {
										worldIn.setBlockAndUpdate(pos, ModBusManager.BLOCK_FOCUS.defaultBlockState());
										
										if (!playerIn.isCreative()) {
											playerIn.getInventory().getSelected().shrink(1);
										}
										
										return InteractionResult.SUCCESS;
										
									} else {
										CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.not_owner"));
										return InteractionResult.FAIL;
									}
								}
							}

							if (CosmosUtil.handItem(playerIn, ModBusManager.MODULE_CRAFTER)) {
								if (pocket.checkIfOwner(playerIn)) {
									worldIn.setBlockAndUpdate(pos, ModBusManager.BLOCK_WALL_CRAFTER.defaultBlockState());

									if (!playerIn.isCreative()) {
										playerIn.getInventory().getSelected().shrink(1);
									}
									
									return InteractionResult.SUCCESS;
									
								} else {
									CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.not_owner"));
									return InteractionResult.FAIL;
								}
							}
							
							else if (CosmosUtil.handItem(playerIn, ModBusManager.MODULE_CONNECTOR)) {
								if (pocket.checkIfOwner(playerIn)) {
									worldIn.setBlockAndUpdate(pos, ModBusManager.BLOCK_WALL_CONNECTOR.defaultBlockState());
									
									if (!playerIn.isCreative()) {
										playerIn.getInventory().getSelected().shrink(1);
									}
									
									return InteractionResult.PASS;
								} else {
									CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.not_owner"));
									return InteractionResult.FAIL;
								}
							}
							
							if (pos.getY() != 1 && pos.getY() != pocket.getInternalHeight()) {
								if (CosmosUtil.handItem(playerIn, ModBusManager.MODULE_CHARGER)) {
									if (pocket.checkIfOwner(playerIn)) {
										worldIn.setBlockAndUpdate(pos, ModBusManager.BLOCK_WALL_CHARGER.defaultBlockState());

										BlockEntity entity = worldIn.getBlockEntity(pos);
										
										if (entity instanceof BlockEntityModuleCharger) {
											((BlockEntityModuleCharger) worldIn.getBlockEntity(pos)).loadFromItemStack(CosmosUtil.getStack(playerIn));
										}

										if (!playerIn.isCreative()) {
											playerIn.getInventory().getSelected().shrink(1);
										}
										
										return InteractionResult.SUCCESS;
									} else {
										CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.not_owner"));
										return InteractionResult.FAIL;
									}
								}
								
								else if (CosmosUtil.handItem(playerIn, ModBusManager.MODULE_UPGRADE_STATION)) {
									if (pocket.checkIfOwner(playerIn)) {
										worldIn.setBlockAndUpdate(pos, ModBusManager.BLOCK_WALL_UPGRADE_STATION.defaultBlockState());
										
										BlockEntity entity = worldIn.getBlockEntity(pos);
										
										if (entity instanceof BlockEntityModuleUpgradeStation) {
											((BlockEntityModuleUpgradeStation) worldIn.getBlockEntity(pos)).loadFromItemStack(CosmosUtil.getStack(playerIn));
										}

										if (!playerIn.isCreative()) {
											playerIn.getInventory().getSelected().shrink(1);
										}

										return InteractionResult.SUCCESS;
									} else {
										CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.not_owner"));
										return InteractionResult.FAIL;
									}
								}
								
								else if (CosmosUtil.handItem(playerIn, ModBusManager.MODULE_FURNACE)) {
									if (pocket.checkIfOwner(playerIn)) {
										worldIn.setBlockAndUpdate(pos, ModBusManager.BLOCK_WALL_FURNACE.defaultBlockState());
										
										BlockEntity entity = worldIn.getBlockEntity(pos);
										
										if (entity instanceof BlockEntityModuleFurnace) {
											((BlockEntityModuleFurnace) worldIn.getBlockEntity(pos)).loadFromItemStack(CosmosUtil.getStack(playerIn));
										}

										if (!playerIn.isCreative()) {
											playerIn.getInventory().getSelected().shrink(1);
										}
	
										return InteractionResult.SUCCESS;
									} else {
										CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.not_owner"));
										return InteractionResult.FAIL;
									}
								}
	
								else if (CosmosUtil.handItem(playerIn, ModBusManager.MODULE_ENERGY_DISPLAY)) {
									if (pocket.checkIfOwner(playerIn)) {
										worldIn.setBlockAndUpdate(pos, ((BlockWallEnergyDisplay) ModBusManager.BLOCK_WALL_ENERGY_DISPLAY).updateState(state, pos, worldIn));
	
										if (!playerIn.isCreative()) {
											playerIn.getInventory().getSelected().shrink(1);
										}
										
										return InteractionResult.SUCCESS;
									} else {
										CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.not_owner"));
										return InteractionResult.FAIL;
									}
								}
	
								else if (CosmosUtil.handItem(playerIn, ModBusManager.MODULE_FLUID_DISPLAY)) {
									if (pocket.checkIfOwner(playerIn)) {
										worldIn.setBlockAndUpdate(pos, ModBusManager.BLOCK_WALL_FLUID_DISPLAY.defaultBlockState());
	
										if (!playerIn.isCreative()) {
											playerIn.getInventory().getSelected().shrink(1);
										}
										
										return InteractionResult.SUCCESS;
									} else {
										CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.not_owner"));
										return InteractionResult.FAIL;
									}
								}
	
								else if (CosmosUtil.handItem(playerIn, ModBusManager.MODULE_ARMOUR_WORKBENCH)) {
									if (pocket.checkIfOwner(playerIn)) {
										worldIn.setBlockAndUpdate(pos, ModBusManager.BLOCK_WALL_ARMOUR_WORKBENCH.defaultBlockState());
	
										BlockEntity entity = worldIn.getBlockEntity(pos);
										
										if (entity instanceof BlockEntityModuleArmourWorkbench) {
											((BlockEntityModuleArmourWorkbench) worldIn.getBlockEntity(pos)).loadFromItemStack(CosmosUtil.getStack(playerIn));
										}

										if (!playerIn.isCreative()) {
											playerIn.getInventory().getSelected().shrink(1);
										}
	
										return InteractionResult.SUCCESS;
									} else {
										CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.not_owner"));
										return InteractionResult.FAIL;
									}
								}
	
								else if (CosmosUtil.handItem(playerIn, ModBusManager.MODULE_GENERATOR)) {
									if (pocket.checkIfOwner(playerIn)) {
										worldIn.setBlockAndUpdate(pos, ModBusManager.BLOCK_WALL_GENERATOR.defaultBlockState());
	
										BlockEntity entity = worldIn.getBlockEntity(pos);
										
										if (entity instanceof BlockEntityModuleGenerator) {
											((BlockEntityModuleGenerator) worldIn.getBlockEntity(pos)).loadFromItemStack(CosmosUtil.getStack(playerIn));
										}

										if (!playerIn.isCreative()) {
											playerIn.getInventory().getSelected().shrink(1);
										}
										
										return InteractionResult.SUCCESS;
									} else {
										CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.not_owner"));
										return InteractionResult.FAIL;
									}
								}
							}
						} else {
							pocket.shift(playerIn, EnumShiftDirection.LEAVE, null, null, null);
							return InteractionResult.SUCCESS;
						}
					}
				} else {
					CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.null"));
					return InteractionResult.FAIL;
				}
			}
		}
		
		if (playerIn.getInventory().getSelected().getItem() instanceof BlockItem) {
			return InteractionResult.FAIL;
		}
		return InteractionResult.SUCCESS;
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