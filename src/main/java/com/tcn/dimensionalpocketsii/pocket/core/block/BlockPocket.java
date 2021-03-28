package com.tcn.dimensionalpocketsii.pocket.core.block;

import com.tcn.cosmoslibrary.common.nbt.BlockRemovableNBT;
import com.tcn.dimensionalpocketsii.core.management.CoreModBusManager;
import com.tcn.dimensionalpocketsii.pocket.core.tileentity.TileEntityPocket;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class BlockPocket extends BlockRemovableNBT {
	
	public static final IntegerProperty NORTH = IntegerProperty.create("north", 0, 3);
	public static final IntegerProperty EAST = IntegerProperty.create("east", 0, 3);
	public static final IntegerProperty SOUTH = IntegerProperty.create("south", 0, 3);
	public static final IntegerProperty WEST = IntegerProperty.create("west", 0, 3);
	public static final IntegerProperty UP = IntegerProperty.create("up", 0, 3);
	public static final IntegerProperty DOWN = IntegerProperty.create("down", 0, 3);
	public static final BooleanProperty LOCKED = BooleanProperty.create("locked");
	public static final BooleanProperty SIDE_GUIDE = BooleanProperty.create("side_guide");

	public BlockPocket(Block.Properties prop) {
		super(prop);
		
		this.registerDefaultState(this.defaultBlockState()
				.setValue(NORTH, 0).setValue(EAST, 0)
				.setValue(SOUTH, 0).setValue(WEST, 0)
				.setValue(UP, 0).setValue(DOWN, 0)
				.setValue(LOCKED, false).setValue(SIDE_GUIDE, false));
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader worldIn) {
		return CoreModBusManager.POCKET_TILE_TYPE.create();
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public void attack(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		TileEntity tileEntity = world.getBlockEntity(pos);
		
		if (tileEntity instanceof TileEntityPocket) {
			((TileEntityPocket) tileEntity).attack(state, world, pos, player);
		}
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
		TileEntity tileEntity = worldIn.getBlockEntity(pos);
		
		if (tileEntity instanceof TileEntityPocket) {
			return ((TileEntityPocket) tileEntity).use(state, worldIn, pos, playerIn, handIn, hit);
		}
		return ActionResultType.FAIL;
	}
	
	@Override
	public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		TileEntity tileEntity = worldIn.getBlockEntity(pos);
		
		if (tileEntity instanceof TileEntityPocket) {
			((TileEntityPocket) tileEntity).onPlace(state, worldIn, pos, oldState, isMoving);
		}
	}

	@Override
	public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		TileEntity tileEntity = worldIn.getBlockEntity(pos);
		
		if (tileEntity instanceof TileEntityPocket) {
			((TileEntityPocket) tileEntity).setPlacedBy(worldIn, pos, state, placer, stack);
		}
	}

	@Override
	public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		TileEntity tileEntity = worldIn.getBlockEntity(pos);
		
		if (tileEntity instanceof TileEntityPocket) {
			((TileEntityPocket) tileEntity).playerWillDestroy(worldIn, pos, state, player);
		}
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
	TileEntity tileEntity = worldIn.getBlockEntity(pos);
		
		if (tileEntity instanceof TileEntityPocket) {
			((TileEntityPocket) tileEntity).neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		}
	}

	public BlockState updateState(BlockState state, BlockPos posIn, World worldIn) {
		if (!worldIn.isClientSide) {
			TileEntity entity = worldIn.getBlockEntity(posIn);
			
			if (entity instanceof TileEntityPocket) {
				TileEntityPocket tile = (TileEntityPocket) entity;
				
				return this.defaultBlockState().setValue(NORTH, tile.getSide(Direction.NORTH).getIndex())
						.setValue(EAST, tile.getSide(Direction.EAST).getIndex())
						.setValue(SOUTH, tile.getSide(Direction.SOUTH).getIndex())
						.setValue(WEST, tile.getSide(Direction.WEST).getIndex())
						.setValue(UP, tile.getSide(Direction.UP).getIndex())
						.setValue(DOWN, tile.getSide(Direction.DOWN).getIndex())
						.setValue(LOCKED, tile.getLockState())
						.setValue(SIDE_GUIDE, tile.getSideGuideValue());
			} else {
				return this.defaultBlockState();
			}
		} else {
			return this.defaultBlockState();
		}
	}
	
	@Override
	public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
		return false;
    }
	
	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, WEST, SOUTH, UP, DOWN, LOCKED, SIDE_GUIDE);
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {	
		TileEntity tile_in = worldIn.getBlockEntity(currentPos);
		
		if (tile_in instanceof TileEntityPocket) {
			TileEntityPocket tile = (TileEntityPocket) worldIn.getBlockEntity(currentPos);

			return stateIn.setValue(NORTH, tile.getSide(Direction.NORTH).getIndex())
					.setValue(EAST, tile.getSide(Direction.EAST).getIndex())
					.setValue(SOUTH, tile.getSide(Direction.SOUTH).getIndex())
					.setValue(WEST, tile.getSide(Direction.WEST).getIndex())
					.setValue(UP, tile.getSide(Direction.UP).getIndex())
					.setValue(DOWN, tile.getSide(Direction.DOWN).getIndex())
					.setValue(LOCKED, tile.getLockState())
					.setValue(SIDE_GUIDE, tile.getSideGuideValue());
		} else {
			return this.defaultBlockState();
		}
	}
}