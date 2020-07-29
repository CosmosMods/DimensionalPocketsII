package com.zeher.zeherlib.mod.block;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

public class ModBlockConnectedUnbreakable extends ModBlockUnbreakable {
	
	public static final BooleanProperty DOWN = BooleanProperty.create("down");
	public static final BooleanProperty UP = BooleanProperty.create("up");
	public static final BooleanProperty NORTH = BooleanProperty.create("north");
	public static final BooleanProperty SOUTH = BooleanProperty.create("south");
	public static final BooleanProperty WEST = BooleanProperty.create("west");
	public static final BooleanProperty EAST = BooleanProperty.create("east");
	
	public ModBlockConnectedUnbreakable(Block.Properties properties) {
		super(properties);
		this.setDefaultState(this.getDefaultState()
				 .with(DOWN, Boolean.FALSE)
                 .with(EAST, Boolean.FALSE)
                 .with(NORTH, Boolean.FALSE)
                 .with(SOUTH, Boolean.FALSE)
                 .with(UP, Boolean.FALSE)
                 .with(WEST, Boolean.FALSE));
	}
	
	@Override
	public int tickRate(IWorldReader worldIn) {
		return 10;
	}
	
	@Override
	public boolean ticksRandomly(BlockState state) {
		return true;
	}

	private boolean canSideConnect(IWorld world, BlockPos pos, Direction facing) {
		final BlockState orig = world.getBlockState(pos);
		final BlockState conn = world.getBlockState(pos.offset(facing));
		
		return orig != null && conn != null && this.canConnect(orig, conn);
	}
	
	protected boolean canConnect(@Nonnull BlockState orig, @Nonnull BlockState conn) {
		return orig.getBlock() == conn.getBlock();
	}
	
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos pos, BlockPos facingPos) {
		return stateIn.with(DOWN,  this.canSideConnect(worldIn, pos, Direction.DOWN))
				.with(EAST,  this.canSideConnect(worldIn, pos, Direction.EAST))
				.with(NORTH, this.canSideConnect(worldIn, pos, Direction.NORTH))
				.with(SOUTH, this.canSideConnect(worldIn, pos, Direction.SOUTH))
				.with(UP,    this.canSideConnect(worldIn, pos, Direction.UP))
				.with(WEST,  this.canSideConnect(worldIn, pos, Direction.WEST));
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(DOWN, EAST, NORTH, SOUTH, UP, WEST);
	}
}