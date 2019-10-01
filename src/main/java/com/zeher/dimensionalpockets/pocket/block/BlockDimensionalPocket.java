package com.zeher.dimensionalpockets.pocket.block;

import com.zeher.dimensionalpockets.pocket.client.tileentity.TileEntityDimensionalPocket;
import com.zeher.zeherlib.core.block.ModBlock;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDimensionalPocket extends ModBlock implements ITileEntityProvider {

	public BlockDimensionalPocket(String name, Material arg0, String tool, int harvest, int hardness, int resistance, CreativeTabs tab) {
		super(name, arg0, tool, harvest, hardness, resistance, tab);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityDimensionalPocket();
	}

	@Override
	public boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}
	
	@Override
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
		TileEntity tileEntity = world.getTileEntity(pos);
		
		if (tileEntity instanceof TileEntityDimensionalPocket) {
			((TileEntityDimensionalPocket) tileEntity).onBlockClicked(world, pos, player);
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity tileEntity = world.getTileEntity(pos);
		
		if (tileEntity instanceof TileEntityDimensionalPocket) {
			((TileEntityDimensionalPocket) tileEntity).onBlockActivated(world, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
		}
		return false;
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		TileEntity tileEntity = world.getTileEntity(pos);
		
		if (tileEntity instanceof TileEntityDimensionalPocket) {
			((TileEntityDimensionalPocket) tileEntity).onBlockAdded(world, pos, state);
		}
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		
		if (tileEntity instanceof TileEntityDimensionalPocket) {
			((TileEntityDimensionalPocket) tileEntity).onBlockPlacedBy(worldIn, pos, state, placer, stack);
		}
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		
		if (tileEntity instanceof TileEntityDimensionalPocket) {
			((TileEntityDimensionalPocket) tileEntity).onBlockDestroyedByPlayer(worldIn, pos, state);
		}
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		TileEntity tileEntity = world.getTileEntity(pos);
		
		if (tileEntity instanceof TileEntityDimensionalPocket) {
			((TileEntityDimensionalPocket) tileEntity).onNeighborChange(world, pos, neighbor);
		}
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return (layer == BlockRenderLayer.CUTOUT_MIPPED || layer == BlockRenderLayer.TRANSLUCENT);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		return false;
	}
}
