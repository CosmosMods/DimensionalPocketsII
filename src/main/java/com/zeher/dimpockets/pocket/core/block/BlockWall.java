package com.zeher.dimpockets.pocket.core.block;

import com.zeher.dimpockets.core.manager.ModDimensionManager;
import com.zeher.dimpockets.pocket.core.Pocket;
import com.zeher.dimpockets.pocket.core.dimshift.ShifterUtil;
import com.zeher.dimpockets.pocket.core.manager.PocketRegistryManager;
import com.zeher.zeherlib.api.client.util.TextHelper;
import com.zeher.zeherlib.api.compat.core.impl.IItemGroupNone;
import com.zeher.zeherlib.mod.block.ModBlockUnbreakable;
import com.zeher.zeherlib.mod.util.ModUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class BlockWall extends ModBlockUnbreakable implements IItemGroupNone {
	
	public BlockWall(Block.Properties prop) {
		super(prop);
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
		if (playerIn.dimension == ModDimensionManager.POCKET_DIMENSION.getDimensionType()) {
			if (!playerIn.isSneaking()) {
				if (ModUtil.isHoldingHammer(playerIn)) {
					playerIn.swingArm(Hand.MAIN_HAND);
					
					if (pos.getY() != 0 && pos.getY() != 15) {
						//worldIn.setBlockState(pos, BlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_ENERGY_DISPLAY.getDefaultState());
						return true;
					}
				}
			} else {
				if (ModUtil.isHoldingHammer(playerIn)) {
					playerIn.swingArm(Hand.MAIN_HAND);
					
					//worldIn.setBlockState(pos, BlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_CONNECTOR.getDefaultState());
					return true;
				}
				
				if (playerIn.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
					if (worldIn.isRemote) {
						return false;
					}
					
					Pocket pocket = PocketRegistryManager.getPocket(new BlockPos(pos.getX() >> 4, pos.getY() >> 4, pos.getZ() >> 4));
					
					if(pocket != null) {
						pocket.shiftFrom(playerIn);
					} else {
						ShifterUtil.sendPlayerToBedWithMessage(playerIn, worldIn, TextHelper.ITALIC + TextHelper.RED + "Pocket is Null. Sending you to your bed!");
					}
					
					return true;
				}
			}
		}
		return false;
	}
}