package com.tcn.dimensionalpocketsii.pocket.core.block;

import com.tcn.cosmoslibrary.impl.block.CosmosBlockUnbreakable;
import com.tcn.dimensionalpocketsii.core.management.CoreConfigurationManager;
import com.tcn.dimensionalpocketsii.pocket.core.tileentity.TileEntityConnector;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class BlockWallConnector extends CosmosBlockUnbreakable {
	
	public static final IntegerProperty MODE = IntegerProperty.create("mode", 0, 3);
	public static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 3);
	
	public BlockWallConnector(Block.Properties prop) {
		super(prop);
		
		this.setDefaultState(this.getDefaultState().with(MODE, 0).with(TYPE, 0));
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader worldIn) {
		return new TileEntityConnector();
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		TileEntity tileEntity = world.getTileEntity(pos);
		
		if (tileEntity instanceof TileEntityConnector) {
			((TileEntityConnector) tileEntity).onBlockClicked(state, world, pos, player);
		}
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		
		if (tileEntity instanceof TileEntityConnector) {
			return ((TileEntityConnector) tileEntity).onBlockActivated(state, worldIn, pos, playerIn, handIn, hit);
		}
		return ActionResultType.PASS;
	}
	
	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		
		if (tileEntity instanceof TileEntityConnector) {
			((TileEntityConnector) tileEntity).onBlockAdded(state, worldIn, pos, oldState, isMoving);
		}
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		
		if (tileEntity instanceof TileEntityConnector) {
			((TileEntityConnector) tileEntity).onBlockPlacedBy(worldIn, pos, state, placer, stack);
		}
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		
		if (tileEntity instanceof TileEntityConnector) {
			((TileEntityConnector) tileEntity).onBlockHarvested(worldIn, pos, state, player);
		}
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
	TileEntity tileEntity = worldIn.getTileEntity(pos);
		
		if (tileEntity instanceof TileEntityConnector) {
			((TileEntityConnector) tileEntity).neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		}
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(MODE, TYPE);
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {	
		TileEntity tile_in = worldIn.getTileEntity(currentPos);
		
		if (tile_in instanceof TileEntityConnector) {
			TileEntityConnector tile = (TileEntityConnector) worldIn.getTileEntity(currentPos);

			return stateIn.with(MODE, tile.getSide(Direction.UP).getIndex()).with(TYPE, tile.getConnectionType().getIndex());
		} else {
			return this.getDefaultState();
		}
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		if (CoreConfigurationManager.getInstance().getCanDestroyWalls()) {
			return this.getDefaultState();
		}
		return Blocks.AIR.getDefaultState();
	}
}
