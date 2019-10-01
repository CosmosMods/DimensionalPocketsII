package com.zeher.dimensionalpockets.pocket.block;

import java.util.Random;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.pocket.Pocket;
import com.zeher.dimensionalpockets.pocket.PocketRegistry;
import com.zeher.zeherlib.api.util.TextUtil;
import com.zeher.zeherlib.core.block.BlockConnected;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDimensionalPocketWallEdge extends BlockConnected {

	public BlockDimensionalPocketWallEdge(String name, Material arg0, String tool, int harvest, CreativeTabs tab) {
		super(name, arg0, tool, harvest, -1, 6000001, tab);
		this.setLightLevel(1F);
		this.setBlockUnbreakable();
		this.setHardness(-1.0F);
		this.setResistance(6000001.0F);
	}

	@Override
	public boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}

	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return DimensionalPockets.CAN_DESTROY_WALLS_IN_CREATIVE;
	}

	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion) { }

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) {
		return ItemStack.EMPTY;
	}

	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return (worldIn.provider.getDimension() == DimensionalPockets.DIMENSION_ID) && DimensionalPockets.CAN_DESTROY_WALLS_IN_CREATIVE;
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.equals(null)) {
			return false;
		}

		if (!playerIn.isSneaking()) {
			return false;
		}
		
		if((!playerIn.getHeldItem(EnumHand.MAIN_HAND).isEmpty())) {
			return false;
		}
		
		if (playerIn.dimension == DimensionalPockets.DIMENSION_ID && playerIn.isSneaking()) {
			if (worldIn.isRemote) {
				return false;
			}
			
			Pocket pocket = PocketRegistry.getPocket(new BlockPos(pos.getX() >> 4, pos.getY() >> 4, pos.getZ() >> 4));
			
			if(pocket == null) {
				if (!worldIn.isRemote)  {
					TextComponentString comp = new TextComponentString(TextUtil.RED + "Pocket is null. Teleport disabled.");
					playerIn.sendMessage(comp);
					return false;
				}
			}
			if(pocket.getBlockDim() != DimensionalPockets.DIMENSION_ID) {
				playerIn.setSneaking(false);
			}
			
			pocket.shiftFrom(playerIn);
			return true;
		}
		
		return true;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }
	
	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
		return false;
    }
}
