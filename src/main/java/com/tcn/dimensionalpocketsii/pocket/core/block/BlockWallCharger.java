package com.tcn.dimensionalpocketsii.pocket.core.block;

import com.tcn.cosmoslibrary.impl.block.CosmosBlockUnbreakable;
import com.tcn.dimensionalpocketsii.pocket.core.tileentity.TileEntityCharger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockWallCharger extends CosmosBlockUnbreakable {
	
	public BlockWallCharger(Block.Properties prop) {
		super(prop);
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader worldIn) {
		return new TileEntityCharger();
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		TileEntity tileEntity = world.getTileEntity(pos);
		
		if (tileEntity instanceof TileEntityCharger) {
			((TileEntityCharger) tileEntity).onBlockClicked(state, world, pos, player);
		}
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		
		if (tileEntity instanceof TileEntityCharger) {
			return ((TileEntityCharger) tileEntity).onBlockActivated(state, worldIn, pos, playerIn, handIn, hit);
		}
		return ActionResultType.PASS;
	}
	
	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		
		if (tileEntity instanceof TileEntityCharger) {
			((TileEntityCharger) tileEntity).onBlockAdded(state, worldIn, pos, oldState, isMoving);
		}
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		
		if (tileEntity instanceof TileEntityCharger) {
			((TileEntityCharger) tileEntity).onBlockPlacedBy(worldIn, pos, state, placer, stack);
		}
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		
		if (tileEntity instanceof TileEntityCharger) {
			((TileEntityCharger) tileEntity).onBlockHarvested(worldIn, pos, state, player);
		}
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
	TileEntity tileEntity = worldIn.getTileEntity(pos);
		
		if (tileEntity instanceof TileEntityCharger) {
			((TileEntityCharger) tileEntity).neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		}
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

}