package com.zeher.zeherlib.mod.block;

import net.minecraft.block.Block;
import net.minecraft.block.ContainerBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class ModBlockContainer extends ContainerBlock {

	public ModBlockContainer(Block.Properties builder) { 
		super(builder);
	}
	
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return null;
	}
}