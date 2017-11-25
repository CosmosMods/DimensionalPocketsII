package com.zeher.dimensionalpockets.core.block;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.core.pocket.Pocket;
import com.zeher.dimensionalpockets.core.pocket.PocketRegistry;
import com.zeher.trzcore.api.TRZTextUtil;
import com.zeher.trzcore.core.block.TRZBlock;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDimensionalPocketWall extends TRZBlock {

	public BlockDimensionalPocketWall(String name, Material arg0, String tool, int harvest, int hardness, int resistance, CreativeTabs tab) {
		super(name, arg0, tool, harvest, hardness, resistance, tab);
		this.setLightLevel(1F);
		this.setBlockUnbreakable();
	}

	@Override
	public boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}

	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return DimensionalPockets.can_destroy_walls_in_creative;
	}

	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
		// Do nothing...
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) {
		return ItemStack.EMPTY;
	}

	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return (worldIn.provider.getDimension() == DimensionalPockets.dimension_id) && DimensionalPockets.can_destroy_walls_in_creative;
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.equals(null)) {
			return false;
		}

		if (!playerIn.isSneaking()) {
			return false;
		}
		
		if(!playerIn.getHeldItem(hand).isEmpty()) {
			return false;
		}

		if (playerIn.dimension == DimensionalPockets.dimension_id && playerIn.isSneaking()) {
			if (worldIn.isRemote) {
				return false;
			}
			
			Pocket pocket = PocketRegistry.getPocket(new BlockPos(pos.getX() >> 4, pos.getY() >> 4, pos.getZ() >> 4));
			if(pocket == null) {
				TextComponentString comp = new TextComponentString(TRZTextUtil.RED + "Pocket is null. Teleport disabled.");
				playerIn.sendMessage(comp);
				return false;
			}
			if(pocket.getBlockDim() != DimensionalPockets.dimension_id) {
				playerIn.setSneaking(false);
			}
			
			pocket.shiftFrom(playerIn);
			return true;
		}
		
		return true;
	}

}
