package com.zhr.dimensionalpocketsii.pocket.core.block;

import javax.annotation.Nonnull;

import com.zhr.cosmoslibrary.impl._client.util.TextHelper;
import com.zhr.cosmoslibrary.impl.block.ModBlockConnectedUnbreakable;
import com.zhr.dimensionalpocketsii.core.management.ModConfigurationManager;
import com.zhr.dimensionalpocketsii.core.management.bus.BusSubscriberMod;
import com.zhr.dimensionalpocketsii.pocket.core.Pocket;
import com.zhr.dimensionalpocketsii.pocket.core.management.PocketDimensionManager;
import com.zhr.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.zhr.dimensionalpocketsii.pocket.core.shift.ShifterCore;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.DebugPacketSender;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockWallEdge extends ModBlockConnectedUnbreakable {

	public BlockWallEdge(Block.Properties prop) {
		super(prop);
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
		if (playerIn.world.getDimensionKey().equals(PocketDimensionManager.POCKET_WORLD) && playerIn.isSneaking() && playerIn.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
			if (worldIn.isRemote) {
				return ActionResultType.FAIL;
			}
			
			Pocket pocket = PocketRegistryManager.getPocket(new BlockPos(pos.getX() >> 4, pos.getY() >> 4, pos.getZ() >> 4));
			
			if(pocket != null) {
				pocket.shiftFrom(playerIn);
			} else {
				ShifterCore.sendPlayerToBedWithMessage((ServerPlayerEntity)playerIn, worldIn, TextHelper.ITALIC + TextHelper.RED + "Pocket is Null!");
			}
		}
		return ActionResultType.FAIL;
	}
	
	@Override
	public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
		return ModConfigurationManager.getInstance().getCanDestroyWalls();
    }
	
	@Override
	protected boolean canConnect(@Nonnull BlockState orig, @Nonnull BlockState conn) {
		if (ModConfigurationManager.getInstance().getConnectedTexturesInsidePocket()) {
			if (conn.getBlock().equals(Blocks.AIR)) {
				return false;
			} else if (orig.getBlock().equals(conn.getBlock())) {
				return true;
			} else if (conn.getBlock().equals(BusSubscriberMod.BLOCK_DIMENSIONAL_POCKET_WALL)) {
				return true;
			}
			/**
			else if (conn.getBlock().equals(BlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_CONNECTOR)) {
				return true;
			} else if (conn.getBlock().equals(BlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_ENERGY_DISPLAY)) {
				return true;
			} else if (conn.getBlock().equals(BlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_FLUID_DISPLAY)) {
				return true;
			}*/
		}
		return false;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
		return true;
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		DebugPacketSender.func_218806_a(worldIn, pos);
	}
}