package com.zeher.dimpockets.pocket.core.block;

import java.util.Random;

import com.zeher.dimpockets.DimReference;
import com.zeher.dimpockets.core.manager.ModConfigManager;
import com.zeher.dimpockets.pocket.core.tileentity.TileConnector;
import com.zeher.zeherlib.api.compat.core.interfaces.EnumConnectionType;
import com.zeher.zeherlib.mod.block.ModBlockContainerUnbreakable;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockConnector extends ModBlockContainerUnbreakable implements ITileEntityProvider {
	
	public static final PropertyInteger MODE = PropertyInteger.create("mode", 0, 3);
	public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 3);
	
	public BlockConnector(String name, Material material) {
		super(name, material);
		this.setLightLevel(1F);
		
		this.setDefaultState(this.blockState.getBaseState().withProperty(MODE, 0).withProperty(TYPE, 0));
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileConnector();
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return (worldIn.provider.getDimension() == DimReference.CONSTANT.POCKET_DIMENSION_ID) && ModConfigManager.CAN_DESTROY_WALLS_IN_CREATIVE;
	}
	
	@Override
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
		TileEntity tileEntity = world.getTileEntity(pos);
		
		if (tileEntity instanceof TileConnector) {
			((TileConnector) tileEntity).onBlockClicked(world, pos, player);
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity tileEntity = world.getTileEntity(pos);
		
		if (tileEntity instanceof TileConnector) {
			((TileConnector) tileEntity).onBlockActivated(world, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
		}
		return false;
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		TileEntity tileEntity = world.getTileEntity(pos);
		
		if (tileEntity instanceof TileConnector) {
			((TileConnector) tileEntity).onBlockAdded(world, pos, state);
		}
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		
		if (tileEntity instanceof TileConnector) {
			((TileConnector) tileEntity).onBlockPlacedBy(worldIn, pos, state, placer, stack);
		}
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		
		if (tileEntity instanceof TileConnector) {
			((TileConnector) tileEntity).onBlockDestroyedByPlayer(worldIn, pos, state);
		}
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		TileEntity tileEntity = world.getTileEntity(pos);
		
		if (tileEntity instanceof TileConnector) {
			((TileConnector) tileEntity).onNeighborChange(world, pos, neighbor);
		}
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { MODE, TYPE });
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		TileConnector tile = (TileConnector) worldIn.getTileEntity(pos);
		
		return state.withProperty(MODE, tile.getSide(EnumFacing.UP).getIndex()).withProperty(TYPE, tile.getType().getIndex());
	}
}