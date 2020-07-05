package com.zeher.zehercraft.processing.core.block;

import com.zeher.zehercraft.ZCReference;
import com.zeher.zehercraft.core.handler.GuiHandler;
import com.zeher.zehercraft.processing.core.tile.TileEntityCharger;
import com.zeher.zeherlib.api.azrf.BlockContainerRemovableNBT;
import com.zeher.zeherlib.mod.util.ModUtil;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCharger extends BlockContainerRemovableNBT {
	
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyInteger ENERGY = PropertyInteger.create("energy", 0, 14);
	
	public BlockCharger(String name, Material material, String tool, int harvest, int hardness, int resistance, CreativeTabs tab) {
		super(name, material, tool, harvest, hardness, resistance, tab);
		
		this.setDefaultState(this.blockState.getBaseState().withProperty(ENERGY, 0).withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) { }

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hit_x, float hit_y, float hit_z) {
		super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hit_x, hit_y, hit_z);
		
		if (playerIn.getHeldItem(hand).isEmpty() && !worldIn.isRemote && !ModUtil.isHoldingHammer(playerIn)) {
			GuiHandler.openGui(playerIn, ZCReference.RESOURCE.PROCESSING.CHARGER_INDEX, worldIn, pos);
		}
		
		return true;
	}
	
	@Override
	public boolean requiresUpdates() {
		return true;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityCharger();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.05D, 0.0D, 0.05D, 0.95D, 0.88D, 0.95D);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { ENERGY, FACING });
	}
	
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntityCharger tile = (TileEntityCharger) world.getTileEntity(pos);
		
		return state.withProperty(ENERGY, tile.getEnergyScaled(14)).withProperty(FACING, state.getValue(FACING));
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		this.setDefaultFacing(world, pos, state);
	}
	
	private void setDefaultFacing(World world, BlockPos pos, IBlockState state) {
		if (!world.isRemote) {
			IBlockState state_north = world.getBlockState(pos.north());
			IBlockState state_south = world.getBlockState(pos.south());
			IBlockState state_west = world.getBlockState(pos.west());
			IBlockState state_east = world.getBlockState(pos.east());
			
			EnumFacing facing = state.getValue(FACING);
			
			if (facing == EnumFacing.NORTH && state_north.isFullBlock() && !state_south.isFullBlock()) {
				facing = EnumFacing.SOUTH;
			} else if (facing == EnumFacing.SOUTH && state_south.isFullBlock() && !state_north.isFullBlock()) {
				facing = EnumFacing.NORTH;
			} else if (facing == EnumFacing.WEST && state_west.isFullBlock() && !state_east.isFullBlock()) {
				facing = EnumFacing.EAST;
			} else if (facing == EnumFacing.EAST && state_east.isFullBlock() && !state_west.isFullBlock()) {
				facing = EnumFacing.WEST;
			}
			
			world.setBlockState(pos, state.withProperty(FACING, facing), 2);
		}
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hit_x, float hit_y, float hit_z, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
	}
	
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}
	
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}
}