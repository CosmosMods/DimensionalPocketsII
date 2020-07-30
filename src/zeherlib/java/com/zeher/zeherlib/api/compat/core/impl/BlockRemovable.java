package com.zeher.zeherlib.api.compat.core.impl;

import com.zeher.zeherlib.api.compat.util.CompatUtil;
import com.zeher.zeherlib.mod.block.ModBlock;
import com.zeher.zeherlib.mod.util.ModUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class BlockRemovable extends ModBlock {

	public BlockRemovable(Block.Properties builder, String name) {//, Material material, String tool, int harvest, int hardness, int resistance, CreativeTabs tab) {
		super(builder);
	}

	@SuppressWarnings("static-access")
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		player.swingArm(Hand.MAIN_HAND);
		
		worldIn.notifyBlockUpdate(pos, state, state, 3);
		//TileEntity tile = worldIn.getTileEntity(pos);
		
		if (ModUtil.isHoldingHammer(player) && player.isSneaking() && !worldIn.isRemote) {
			this.spawnAsEntity(worldIn, pos, CompatUtil.generateItemStackFromTile(this));
			worldIn.removeBlock(pos, false);
			
			return true;
		}	
		return true;
	}
}