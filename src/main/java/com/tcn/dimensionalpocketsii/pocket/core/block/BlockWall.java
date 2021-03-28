package com.tcn.dimensionalpocketsii.pocket.core.block;

import com.tcn.cosmoslibrary.common.block.CosmosBlockUnbreakable;
import com.tcn.cosmoslibrary.common.chat.CosmosChatUtil;
import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper;
import com.tcn.cosmoslibrary.common.math.ChunkPos;
import com.tcn.cosmoslibrary.common.util.CosmosUtil;
import com.tcn.dimensionalpocketsii.core.management.CoreConfigurationManager;
import com.tcn.dimensionalpocketsii.core.management.CoreDimensionManager;
import com.tcn.dimensionalpocketsii.core.management.CoreModBusManager;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;
import com.tcn.dimensionalpocketsii.pocket.core.util.PocketUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockWall extends CosmosBlockUnbreakable {
	
	//public static final IntegerProperty COLOUR = IntegerProperty.create("colour", 0, 1000000000);
	
	public BlockWall(Block.Properties prop) {
		super(prop);
	}
	
	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
		if (!worldIn.isClientSide) {
			if (PocketUtil.isDimensionEqual(worldIn, CoreDimensionManager.POCKET_WORLD)) {
				Pocket pocket = PocketRegistryManager.getPocketFromChunkPosition(ChunkPos.scaleToChunkPos(pos));
				
				if (pocket.exists()) {
					if (!playerIn.isShiftKeyDown()) {
						
					} else {
						if (!playerIn.getItemInHand(handIn).isEmpty()) {
							if (CosmosUtil.handItem(playerIn, CoreModBusManager.MODULE_CONNECTOR)) {
								if (pos.getY() != 1 && pos.getY() != pocket.getInternalHeight()) {
									worldIn.setBlockAndUpdate(pos, CoreModBusManager.BLOCK_WALL_CONNECTOR.defaultBlockState());
									
									if (!playerIn.isCreative()) {
										playerIn.inventory.getSelected().shrink(1);
									}
									
									return ActionResultType.FAIL;
								}
							}
							
							else if (CosmosUtil.handItem(playerIn, CoreModBusManager.MODULE_CHARGER)) {
								if (pos.getY() != 1 && pos.getY() != pocket.getInternalHeight()) {
									worldIn.setBlockAndUpdate(pos, CoreModBusManager.BLOCK_WALL_CHARGER.defaultBlockState());

									if (!playerIn.isCreative()) {
										playerIn.inventory.getSelected().shrink(1);
									}
									
									return ActionResultType.FAIL;
								}
							}
							
							else if (CosmosUtil.handItem(playerIn, CoreModBusManager.MODULE_CRAFTER)) {
								if (pos.getY() != 1 && pos.getY() != pocket.getInternalHeight()) {
									worldIn.setBlockAndUpdate(pos, CoreModBusManager.BLOCK_WALL_CRAFTER.defaultBlockState());

									if (!playerIn.isCreative()) {
										playerIn.inventory.getSelected().shrink(1);
									}
									
									return ActionResultType.FAIL;
								}
							}

							else if (CosmosUtil.handItem(playerIn, CoreModBusManager.MODULE_ENERGY_DISPLAY)) {
								if (pos.getY() != 1 && pos.getY() != pocket.getInternalHeight()) {
									worldIn.setBlockAndUpdate(pos, CoreModBusManager.BLOCK_WALL_ENERGY_DISPLAY.defaultBlockState());

									if (!playerIn.isCreative()) {
										playerIn.inventory.getSelected().shrink(1);
									}
									
									return ActionResultType.FAIL;
								}
							}
							
						} else {
							pocket.shift(playerIn, EnumShiftDirection.LEAVE, null, null);
							return ActionResultType.SUCCESS;
						}
					}
				} else {
					CosmosChatUtil.sendServerPlayerMessage(playerIn, CosmosCompHelper.getErrorText("dimensionalpocketsii.pocket.status.action.null"));
					return ActionResultType.FAIL;
				}
			}
		}
		
		if (playerIn.inventory.getSelected().getItem() instanceof BlockItem) {
			return ActionResultType.FAIL;
		}
		return ActionResultType.SUCCESS;
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
		return true;
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		//DebugPacketSender.func_218806_a(worldIn, pos);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		if (CoreConfigurationManager.getInstance().getCanDestroyWalls()) {
			return this.defaultBlockState();
		}
		
		return Blocks.AIR.defaultBlockState();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        if (CoreConfigurationManager.getInstance().getCanDestroyWalls()) {
        	return this.getBlock().getCloneItemStack(world, pos, state);
        }
        
        return ItemStack.EMPTY;
    }
}