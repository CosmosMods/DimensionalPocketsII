package com.zeher.zeherlib.api.compat.core.impl;

import com.zeher.zeherlib.api.compat.util.CompatUtil;
import com.zeher.zeherlib.mod.block.ModBlockContainer;
import com.zeher.zeherlib.mod.util.ModUtil;

import cofh.redstoneflux.api.IEnergyHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Class used to implement shift-right click removal with a wrench. 
 * NBT supported for {@link IInventory} and {@link IEnergyHandler}
 * @author josh1
 *
 */
public class BlockContainerRemovableNBT extends ModBlockContainer {
	
	public BlockContainerRemovableNBT(String name, Material material, String tool, int harvest, int hardness, int resistance, CreativeTabs tab) {
		super(name, material, tool, harvest, hardness, resistance, tab);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hit_x, float hit_y, float hit_z) {
		player.swingArm(EnumHand.MAIN_HAND);
		
		world.notifyBlockUpdate(pos, state, state, 3);
		TileEntity tile = world.getTileEntity(pos);
		
		if (ModUtil.isHoldingHammer(player) && player.isSneaking() && !world.isRemote) {
			CompatUtil.generateStack(world, pos);
			
			return true;
		}	
		return true;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		super.breakBlock(worldIn, pos, state);
	}	
}