package com.zeher.dimpockets.pocket.core.block;

import com.zeher.dimpockets.pocket.core.tileentity.PocketTileEntity;
import com.zeher.zeherlib.api.compat.core.impl.BlockRemovableNBT;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockPocket extends BlockRemovableNBT {
	
	public static final IntegerProperty NORTH = IntegerProperty.create("north", 0, 3);
	public static final IntegerProperty EAST = IntegerProperty.create("east", 0, 3);
	public static final IntegerProperty SOUTH = IntegerProperty.create("south", 0, 3);
	public static final IntegerProperty WEST = IntegerProperty.create("west", 0, 3);
	public static final IntegerProperty UP = IntegerProperty.create("up", 0, 3);
	public static final IntegerProperty DOWN = IntegerProperty.create("down", 0, 3);
	public static final BooleanProperty LOCKED = BooleanProperty.create("locked");

	public BlockPocket(Block.Properties prop) {
		super(prop);
		
		this.setDefaultState(this.getDefaultState()
				.with(NORTH, 0).with(EAST, 0)
				.with(SOUTH, 0).with(WEST, 0)
				.with(UP, 0).with(DOWN, 0)
				.with(LOCKED, false));
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader worldIn) {
		return new PocketTileEntity();
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		TileEntity tileEntity = world.getTileEntity(pos);
		
		if (tileEntity instanceof PocketTileEntity) {
			((PocketTileEntity) tileEntity).onBlockClicked(state, world, pos, player);
		}
	}

	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		
		if (tileEntity instanceof PocketTileEntity) {
			((PocketTileEntity) tileEntity).onBlockActivated(state, worldIn, pos, playerIn, handIn, hit);
		}
		return false;
	}
	
	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		
		if (tileEntity instanceof PocketTileEntity) {
			((PocketTileEntity) tileEntity).onBlockAdded(state, worldIn, pos, oldState, isMoving);
		}
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		
		if (tileEntity instanceof PocketTileEntity) {
			((PocketTileEntity) tileEntity).onBlockPlacedBy(worldIn, pos, state, placer, stack);
		}
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		
		if (tileEntity instanceof PocketTileEntity) {
			((PocketTileEntity) tileEntity).onBlockHarvested(worldIn, pos, state, player);
		}
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
	TileEntity tileEntity = worldIn.getTileEntity(pos);
		
		if (tileEntity instanceof PocketTileEntity) {
			((PocketTileEntity) tileEntity).neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		}
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public boolean canRenderInLayer(BlockState state, BlockRenderLayer layer) {
		return (layer == BlockRenderLayer.CUTOUT_MIPPED || layer == BlockRenderLayer.TRANSLUCENT);
	}

	@OnlyIn(Dist.CLIENT)
	public static boolean shouldSideBeRendered(BlockState adjacentState, IBlockReader blockState, BlockPos blockAccess, Direction pos) {
		return false;
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, WEST, SOUTH, UP, DOWN, LOCKED);
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {	
		TileEntity tile_in = worldIn.getTileEntity(currentPos);
		
		if (tile_in instanceof PocketTileEntity) {
			PocketTileEntity tile = (PocketTileEntity) worldIn.getTileEntity(currentPos);

			return stateIn.with(NORTH, tile.getSide(Direction.NORTH).getIndex())
					.with(EAST, tile.getSide(Direction.EAST).getIndex())
					.with(SOUTH, tile.getSide(Direction.SOUTH).getIndex())
					.with(WEST, tile.getSide(Direction.WEST).getIndex())
					.with(UP, tile.getSide(Direction.UP).getIndex())
					.with(DOWN, tile.getSide(Direction.DOWN).getIndex())
					.with(LOCKED, tile.getLockState());
		} else {
			return this.getDefaultState();
		}
	}
}