package com.zeher.dimensionalpockets.core.block;

import com.zeher.dimensionalpockets.core.tileentity.TileEntityDimensionalPocket;
import com.zeher.trzcore.api.interfaces.IBlockInteract;
import com.zeher.trzcore.api.interfaces.IBlockNotifier;
import com.zeher.trzcore.core.block.TRZBlock;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDimensionalPocket extends TRZBlock implements ITileEntityProvider {

	public BlockDimensionalPocket(String name, Material arg0, String tool, int harvest, int hardness, int resistance,
			CreativeTabs tab) {
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
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof IBlockInteract) {
			return ((IBlockInteract) tileEntity).onBlockActivated(world, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
		}
		return false;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof IBlockNotifier) {
			((IBlockNotifier) tileEntity).onBlockPlacedBy(worldIn, pos, state, placer, stack);
		}
    }
	
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof IBlockNotifier) {
			((IBlockNotifier) tileEntity).onBlockDestroyedByPlayer(worldIn, pos, state);
		}
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof IBlockNotifier) {
			((IBlockNotifier) tileEntity).onNeighborChange(world, pos, neighbor);
		}
	}
}
