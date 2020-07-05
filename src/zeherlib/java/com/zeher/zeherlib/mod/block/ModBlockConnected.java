package com.zeher.zeherlib.mod.block;

import javax.annotation.Nonnull;
import javax.swing.Icon;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlockConnected extends ModBlock {
	
	public static final PropertyBool DOWN = PropertyBool.create("down");
	public static final PropertyBool UP = PropertyBool.create("up");
	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool WEST = PropertyBool.create("west");
	public static final PropertyBool EAST = PropertyBool.create("east");
	
	public ModBlockConnected(String name, Material material, String tool, int harvest, int hardness, int resistance, CreativeTabs tab) {
		super(name, material, tool, harvest, hardness, hardness, tab);
		this.setDefaultState(this.blockState.getBaseState()
				 .withProperty(DOWN, Boolean.FALSE)
                 .withProperty(EAST, Boolean.FALSE)
                 .withProperty(NORTH, Boolean.FALSE)
                 .withProperty(SOUTH, Boolean.FALSE)
                 .withProperty(UP, Boolean.FALSE)
                 .withProperty(WEST, Boolean.FALSE));
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.withProperty(DOWN,  this.canSideConnect(world, pos, EnumFacing.DOWN))
					.withProperty(EAST,  this.canSideConnect(world, pos, EnumFacing.EAST))
					.withProperty(NORTH, this.canSideConnect(world, pos, EnumFacing.NORTH))
					.withProperty(SOUTH, this.canSideConnect(world, pos, EnumFacing.SOUTH))
					.withProperty(UP,    this.canSideConnect(world, pos, EnumFacing.UP))
					.withProperty(WEST,  this.canSideConnect(world, pos, EnumFacing.WEST));
	}
	
	@Override
	@Nonnull
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { DOWN, UP, NORTH, SOUTH, WEST, EAST });
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	private boolean canSideConnect(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		final IBlockState orig = world.getBlockState(pos);
		final IBlockState conn = world.getBlockState(pos.offset(facing));
		
		return orig != null && conn != null && this.canConnect(orig, conn);
	}
	
	protected boolean canConnect(@Nonnull IBlockState orig, @Nonnull IBlockState conn) {
		//return conn.getBlock().equals(BlockHandler.REGISTRATION.DECORATION.BLOCK_GLASS_BLACK);
		return orig.getBlock() == conn.getBlock();
	}
}