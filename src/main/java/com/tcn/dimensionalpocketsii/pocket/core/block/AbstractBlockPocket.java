package com.tcn.dimensionalpocketsii.pocket.core.block;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.common.nbt.CosmosBlockRemovableNBT;
import com.tcn.dimensionalpocketsii.pocket.core.block.entity.AbstractBlockEntityPocket;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public abstract class AbstractBlockPocket extends CosmosBlockRemovableNBT implements EntityBlock {
	
	public static final IntegerProperty NORTH = IntegerProperty.create("north", 0, 3);
	public static final IntegerProperty EAST = IntegerProperty.create("east", 0, 3);
	public static final IntegerProperty SOUTH = IntegerProperty.create("south", 0, 3);
	public static final IntegerProperty WEST = IntegerProperty.create("west", 0, 3);
	public static final IntegerProperty UP = IntegerProperty.create("up", 0, 3);
	public static final IntegerProperty DOWN = IntegerProperty.create("down", 0, 3);
	public static final BooleanProperty LOCKED = BooleanProperty.create("locked");
	public static final BooleanProperty SIDE_GUIDE = BooleanProperty.create("side_guide");
	
	private BlockEntityType<? extends AbstractBlockEntityPocket> blockEntityType;
	private boolean isSingleChunk;

	public AbstractBlockPocket(Block.Properties prop, BlockEntityType<? extends AbstractBlockEntityPocket> blockEntityTypeIn, boolean isSingleChunkIn) {
		super(prop);
		
		this.registerDefaultState(this.defaultBlockState()
				.setValue(NORTH, 0).setValue(EAST, 0)
				.setValue(SOUTH, 0).setValue(WEST, 0)
				.setValue(UP, 0).setValue(DOWN, 0)
				.setValue(LOCKED, false).setValue(SIDE_GUIDE, false));
		
		this.blockEntityType = blockEntityTypeIn;
		this.isSingleChunk = isSingleChunkIn;
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level levelIn, BlockState stateIn, BlockEntityType<T> entityTypeIn) {
		return createTicker(levelIn, entityTypeIn, this.blockEntityType);
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> createTicker(Level levelIn, BlockEntityType<T> entityTypeIn, BlockEntityType<? extends AbstractBlockEntityPocket> entityIn) {
		return createTickerHelper(entityTypeIn, entityIn, AbstractBlockEntityPocket::tick);
	}
	
	@Override
	public void attack(BlockState state, Level world, BlockPos pos, Player player) {
		BlockEntity tileEntity = world.getBlockEntity(pos);
		
		if (tileEntity instanceof AbstractBlockEntityPocket) {
			((AbstractBlockEntityPocket) tileEntity).attack(state, world, pos, player);
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand handIn, BlockHitResult hit) {
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		
		if (tileEntity instanceof AbstractBlockEntityPocket) {
			return ((AbstractBlockEntityPocket) tileEntity).use(state, worldIn, pos, playerIn, handIn, hit);
		}
		return InteractionResult.FAIL;
	}
	
	@Override
	public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		
		if (tileEntity instanceof AbstractBlockEntityPocket) {
			((AbstractBlockEntityPocket) tileEntity).onPlace(state, worldIn, pos, oldState, isMoving);
		}
	}

	@Override
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		
		if (tileEntity instanceof AbstractBlockEntityPocket) {
			((AbstractBlockEntityPocket) tileEntity).setPlacedBy(worldIn, pos, state, placer, stack);
		}
	}

	@Override
	public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		
		if (tileEntity instanceof AbstractBlockEntityPocket) {
			((AbstractBlockEntityPocket) tileEntity).playerWillDestroy(worldIn, pos, state, player);
		}
	}

	@Override
	public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
	BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		
		if (tileEntity instanceof AbstractBlockEntityPocket) {
			((AbstractBlockEntityPocket) tileEntity).neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		}
	}

	public BlockState updateState(BlockState state, BlockPos posIn, Level worldIn) {
		if (!worldIn.isClientSide) {
			BlockEntity entity = worldIn.getBlockEntity(posIn);
			
			if (entity instanceof AbstractBlockEntityPocket) {
				AbstractBlockEntityPocket tile = (AbstractBlockEntityPocket) entity;
				
				if (tile.getPocket() != null) {
					return this.defaultBlockState()
							.setValue(NORTH, tile.getSide(Direction.NORTH).getIndex())
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
		} else {
			return this.defaultBlockState();
		}
	}
	
	@Override
	public boolean canEntityDestroy(BlockState state, BlockGetter world, BlockPos pos, Entity entity) {
		return false;
    }
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, WEST, SOUTH, UP, DOWN, LOCKED, SIDE_GUIDE);
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		BlockEntity tile_in = worldIn.getBlockEntity(currentPos);
		
		if (tile_in instanceof AbstractBlockEntityPocket) {
			AbstractBlockEntityPocket tile = (AbstractBlockEntityPocket) worldIn.getBlockEntity(currentPos);

			if (tile.getPocket() != null) {
				return this.defaultBlockState()
						.setValue(NORTH, tile.getSide(Direction.NORTH).getIndex())
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
	public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
        return false;
    }
	
	public boolean getIsSingleChunk() {
		return this.isSingleChunk;
	}
}