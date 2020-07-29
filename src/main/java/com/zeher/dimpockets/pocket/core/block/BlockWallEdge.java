package com.zeher.dimpockets.pocket.core.block;

import javax.annotation.Nonnull;

import com.zeher.dimpockets.core.manager.BusSubscriberMod;
import com.zeher.dimpockets.core.manager.DimConfigManager;
import com.zeher.dimpockets.core.manager.ModDimensionManager;
import com.zeher.dimpockets.pocket.core.Pocket;
import com.zeher.dimpockets.pocket.core.dimshift.ShifterUtil;
import com.zeher.dimpockets.pocket.core.manager.PocketRegistryManager;
import com.zeher.zeherlib.api.client.util.TextHelper;
import com.zeher.zeherlib.mod.block.ModBlockConnectedUnbreakable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
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
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
		if (playerIn.dimension == ModDimensionManager.POCKET_DIMENSION.getDimensionType() && playerIn.isSneaking() && playerIn.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
			if (worldIn.isRemote) {
				return false;
			}
			
			Pocket pocket = PocketRegistryManager.getPocket(new BlockPos(pos.getX() >> 4, pos.getY() >> 4, pos.getZ() >> 4));
			
			if(pocket != null) {
				pocket.shiftFrom(playerIn);
			} else {
				ShifterUtil.sendPlayerToBedWithMessage(playerIn, worldIn, TextHelper.ITALIC + TextHelper.RED + "Pocket is Null. Sending you to your bed!");
			}
		}
		return false;
	}
	
	@Override
	public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
		return DimConfigManager.CAN_DESTROY_WALLS_IN_CREATIVE;
    }
	
	@Override
	protected boolean canConnect(@Nonnull BlockState orig, @Nonnull BlockState conn) {
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
		return false;
	}
}