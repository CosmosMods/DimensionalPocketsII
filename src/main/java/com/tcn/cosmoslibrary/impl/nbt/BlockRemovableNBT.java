package com.tcn.cosmoslibrary.impl.nbt;

import com.tcn.cosmoslibrary.impl.block.CosmosBlock;
import com.tcn.cosmoslibrary.impl.util.CompatUtil;
import com.tcn.cosmoslibrary.impl.util.CosmosUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ActionResultType;
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
public class BlockRemovableNBT extends CosmosBlock {
	
	public BlockRemovableNBT(Block.Properties builder) {
		super(builder);
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		player.swingArm(Hand.MAIN_HAND);
		
		worldIn.notifyBlockUpdate(pos, state, state, 3);
		
		if (CosmosUtil.isHoldingHammer(player) && player.isSneaking() && !worldIn.isRemote) {
			CompatUtil.generateStack(worldIn, pos);
			
			return ActionResultType.SUCCESS;
		}	
		return ActionResultType.FAIL;
	}
	
}