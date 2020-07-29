package com.zeher.zeherlib.mod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ModBlockConnectedGlass extends ModBlockConnected {

	public ModBlockConnectedGlass(Block.Properties properties) {
		super(properties);
	}
	
	@OnlyIn(Dist.CLIENT)
    public boolean doesSideBlockRendering(BlockState state, IEnviromentBlockReader world, BlockPos pos, Direction face) {
		BlockState iblockstate = world.getBlockState(pos.offset(face));
        Block block = iblockstate.getBlock();
        
        if (block instanceof ModBlockConnectedGlass) {
            return false;
        } else {
        	return true;
        }
    }
}
