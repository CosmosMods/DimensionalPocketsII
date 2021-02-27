package com.zhr.dimensionalpocketsii.pocket.core.block;

import com.zhr.cosmoslibrary.impl._client.util.TextHelper;
import com.zhr.cosmoslibrary.impl.block.ModBlockUnbreakable;
import com.zhr.cosmoslibrary.impl.interfaces.IItemGroupNone;
import com.zhr.cosmoslibrary.impl.util.ModUtil;
import com.zhr.dimensionalpocketsii.pocket.core.Pocket;
import com.zhr.dimensionalpocketsii.pocket.core.management.PocketDimensionManager;
import com.zhr.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.zhr.dimensionalpocketsii.pocket.core.shift.ShifterCore;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.DebugPacketSender;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockWall extends ModBlockUnbreakable implements IItemGroupNone {
	
	public BlockWall(Block.Properties prop) {
		super(prop);
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
		if (playerIn.getEntityWorld().getDimensionKey().equals(PocketDimensionManager.POCKET_WORLD)) {
			if (!playerIn.isSneaking()) {
				if (ModUtil.isHoldingHammer(playerIn)) {
					playerIn.swingArm(Hand.MAIN_HAND);
					
					if (pos.getY() != 0 && pos.getY() != 15) {
						//worldIn.setBlockState(pos, BlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_ENERGY_DISPLAY.getDefaultState());
						return ActionResultType.SUCCESS;
					}
				}
			} else {
				if (ModUtil.isHoldingHammer(playerIn)) {
					playerIn.swingArm(Hand.MAIN_HAND);
					
					//worldIn.setBlockState(pos, BlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_CONNECTOR.getDefaultState());
					return ActionResultType.SUCCESS;
				}
				
				if (playerIn.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
					if (worldIn.isRemote) {
						return ActionResultType.FAIL;
					}
					
					Pocket pocket = PocketRegistryManager.getPocket(new BlockPos(pos.getX() >> 4, pos.getY() >> 4, pos.getZ() >> 4));
					
					if(pocket != null) {
						pocket.shiftFrom(playerIn);
					} else {
						ShifterCore.sendPlayerToBedWithMessage((ServerPlayerEntity)playerIn, worldIn, TextHelper.ITALIC + TextHelper.RED + "Pocket is Null!");
					}
					
					return ActionResultType.SUCCESS;
				}
			}
		}
		return ActionResultType.FAIL;
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