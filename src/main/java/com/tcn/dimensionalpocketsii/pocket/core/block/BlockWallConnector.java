package com.tcn.dimensionalpocketsii.pocket.core.block;

import com.tcn.cosmoslibrary.common.block.CosmosBlockUnbreakable;
import com.tcn.dimensionalpocketsii.core.management.CoreConfigurationManager;
import com.tcn.dimensionalpocketsii.core.management.CoreModBusManager;
import com.tcn.dimensionalpocketsii.pocket.core.tileentity.TileEntityConnector;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class BlockWallConnector extends CosmosBlockUnbreakable {
	
	public static final IntegerProperty MODE = IntegerProperty.create("mode", 0, 3);
	public static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 3);
	
	public BlockWallConnector(Block.Properties prop) {
		super(prop);
		
		this.registerDefaultState(this.defaultBlockState().setValue(MODE, 0).setValue(TYPE, 0));
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader worldIn) {
		return CoreModBusManager.CONNECTOR_TILE_TYPE.create();
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public void attack(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		TileEntity tileEntity = world.getBlockEntity(pos);
		
		if (tileEntity instanceof TileEntityConnector) {
			((TileEntityConnector) tileEntity).attack(state, world, pos, player);
		}
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
		TileEntity tileEntity = worldIn.getBlockEntity(pos);
		
		if (tileEntity instanceof TileEntityConnector) {
			return ((TileEntityConnector) tileEntity).use(state, worldIn, pos, playerIn, handIn, hit);
		}
		return ActionResultType.PASS;
	}
	
	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(MODE, TYPE);
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {	
		TileEntity tile_in = worldIn.getBlockEntity(currentPos);
		
		if (tile_in instanceof TileEntityConnector) {
			TileEntityConnector tile = (TileEntityConnector) worldIn.getBlockEntity(currentPos);

			return stateIn.setValue(MODE, tile.getSide(Direction.UP).getIndex()).setValue(TYPE, tile.getConnectionType().getIndex());
		} else {
			return this.defaultBlockState();
		}
	}

	public BlockState updateState(BlockState state, BlockPos posIn, World worldIn) {
		if (!worldIn.isClientSide) {
			TileEntity entity = worldIn.getBlockEntity(posIn);
			
			if (entity instanceof TileEntityConnector) {
				TileEntityConnector connector = (TileEntityConnector) entity;
				
				return this.defaultBlockState().setValue(MODE, connector.getSide(Direction.UP).getIndex()).setValue(TYPE, connector.getConnectionType().getIndex());
			} else {
				return this.defaultBlockState();
			}
		} else {
			return this.defaultBlockState();
		}
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
