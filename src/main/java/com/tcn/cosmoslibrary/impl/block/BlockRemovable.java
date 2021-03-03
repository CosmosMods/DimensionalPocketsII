package com.tcn.cosmoslibrary.impl.block;

import com.tcn.cosmoslibrary.impl.util.CompatUtil;
import com.tcn.cosmoslibrary.impl.util.CosmosUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class BlockRemovable extends CosmosBlock {

	public BlockRemovable(Block.Properties builder, String name) {//, Material material, String tool, int harvest, int hardness, int resistance, CreativeTabs tab) {
		super(builder);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		player.swingArm(Hand.MAIN_HAND);
		
		worldIn.notifyBlockUpdate(pos, state, state, 3);
		//TileEntity tile = worldIn.getTileEntity(pos);
		
		if (CosmosUtil.isHoldingHammer(player) && player.isSneaking() && !worldIn.isRemote) {
			spawnAsEntity(worldIn, pos, CompatUtil.generateItemStackFromTile(this));
			worldIn.removeBlock(pos, false);
			
			return ActionResultType.SUCCESS;
		}	
		return ActionResultType.FAIL;
	}
}