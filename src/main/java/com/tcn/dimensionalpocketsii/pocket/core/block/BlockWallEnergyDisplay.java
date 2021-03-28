package com.tcn.dimensionalpocketsii.pocket.core.block;

import java.util.Random;

import com.tcn.cosmoslibrary.common.block.CosmosBlockUnbreakable;
import com.tcn.cosmoslibrary.common.chat.CosmosChatUtil;
import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper;
import com.tcn.cosmoslibrary.common.math.ChunkPos;
import com.tcn.cosmoslibrary.common.util.CosmosUtil;
import com.tcn.dimensionalpocketsii.core.management.CoreConfigurationManager;
import com.tcn.dimensionalpocketsii.core.management.CoreModBusManager;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class BlockWallEnergyDisplay extends CosmosBlockUnbreakable {
	public static final IntegerProperty ENERGY = IntegerProperty.create("energy", 0, 28);
	
	public BlockWallEnergyDisplay(Block.Properties prop) {
		super(prop);
		
		this.registerDefaultState(this.defaultBlockState().setValue(ENERGY, 0));
	}
	
	@Override
	public void tick(BlockState stateIn, ServerWorld worldIn, BlockPos posIn, Random randIn) {
		worldIn.setBlockAndUpdate(posIn, this.updateState(stateIn, posIn, worldIn));
		worldIn.blockUpdated(posIn, this);
		worldIn.sendBlockUpdated(posIn, stateIn, this.updateState(stateIn, posIn, worldIn), 3);
	}
	
	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
		if (CosmosUtil.getStackItem(playerIn) instanceof BlockItem) {
			return ActionResultType.FAIL;
		}
		
		if(!worldIn.isClientSide) {
			ChunkPos chunkPos = ChunkPos.scaleToChunkPos(pos);
			Pocket pocketIn = PocketRegistryManager.getPocketFromChunkPosition(chunkPos);
			
			if (playerIn.isShiftKeyDown()) {
				if(pocketIn.exists()) {
					if (CosmosUtil.holdingWrench(playerIn)) {
						if (pocketIn.checkIfOwner(playerIn)) {
							worldIn.setBlockAndUpdate(pos, CoreModBusManager.BLOCK_WALL.defaultBlockState());
							
							if (!playerIn.isCreative()) {
								CosmosUtil.addItem(playerIn, CoreModBusManager.MODULE_ENERGY_DISPLAY, 1);
							}
							
							return ActionResultType.SUCCESS;
						} else {
							CosmosChatUtil.sendServerPlayerMessage(playerIn, CosmosCompHelper.getErrorText("dimensionalpocketsii.pocket.status.no_access"));
							return ActionResultType.FAIL;
						}
					} 
					
					else if (CosmosUtil.handEmpty(playerIn)) {
						pocketIn.shift(playerIn, EnumShiftDirection.LEAVE, null, null);
						return ActionResultType.SUCCESS;
					}
				}
			}
		}
		return ActionResultType.SUCCESS;
	}
	
	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(ENERGY);
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {	
		Pocket pocket = PocketRegistryManager.getPocketFromChunkPosition(ChunkPos.scaleToChunkPos(currentPos));
		
		if (pocket.exists()) {
			return this.defaultBlockState().setValue(ENERGY, pocket.getStoredLevelScaled(28));
		}
		return this.defaultBlockState();
	}

	public BlockState updateState(BlockState state, BlockPos posIn, World worldIn) {
		if (!worldIn.isClientSide) {
			Pocket pocket = PocketRegistryManager.getPocketFromChunkPosition(ChunkPos.scaleToChunkPos(posIn));
			
			if (pocket.exists()) {
				return this.defaultBlockState().setValue(ENERGY, pocket.getStoredLevelScaled(28));
			}
		}
		return this.defaultBlockState();
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