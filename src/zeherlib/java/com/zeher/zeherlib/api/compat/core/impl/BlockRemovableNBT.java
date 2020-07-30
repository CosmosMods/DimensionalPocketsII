package com.zeher.zeherlib.api.compat.core.impl;

import com.zeher.zeherlib.api.compat.util.CompatUtil;
import com.zeher.zeherlib.mod.block.ModBlock;
import com.zeher.zeherlib.mod.util.ModUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

/**
 * Class used to implement shift-right click removal with a wrench. 
 * NBT supported for {@link IInventory} and {@link IEnergyHandler}
 * @author TheRealZeher
 *
 */
public class BlockRemovableNBT extends ModBlock {
	
	public BlockRemovableNBT(Block.Properties builder) {
		super(builder);
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		player.swingArm(Hand.MAIN_HAND);
		
		worldIn.notifyBlockUpdate(pos, state, state, 3);
		
		if (ModUtil.isHoldingHammer(player) && player.isSneaking() && !worldIn.isRemote) {
			CompatUtil.generateStack(worldIn, pos);
			
			return true;
		}	
		return true;
	}
	
}