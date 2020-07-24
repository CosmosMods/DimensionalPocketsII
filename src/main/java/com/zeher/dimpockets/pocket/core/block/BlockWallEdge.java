package com.zeher.dimpockets.pocket.core.block;

import java.util.Random;

import javax.annotation.Nonnull;

import com.zeher.dimpockets.DimReference;
import com.zeher.dimpockets.core.manager.ModBlockManager;
import com.zeher.dimpockets.core.manager.ModConfigManager;
import com.zeher.dimpockets.pocket.core.Pocket;
import com.zeher.dimpockets.pocket.core.manager.PocketRegistryManager;
import com.zeher.zeherlib.api.client.util.TextHelper;
import com.zeher.zeherlib.mod.block.ModBlockConnectedUnbreakable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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

public class BlockWallEdge extends ModBlockConnectedUnbreakable {

	public BlockWallEdge(String name, Material material) {
		super(name, material);
		this.setLightLevel(1F);
	}
	
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return (worldIn.provider.getDimension() == DimReference.CONSTANT.POCKET_DIMENSION_ID) && ModConfigManager.CAN_DESTROY_WALLS_IN_CREATIVE;
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.dimension == DimReference.CONSTANT.POCKET_DIMENSION_ID && playerIn.isSneaking() && playerIn.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
			if (worldIn.isRemote) {
				return false;
			}
			
			Pocket pocket = PocketRegistryManager.getPocket(new BlockPos(pos.getX() >> 4, pos.getY() >> 4, pos.getZ() >> 4));
			
			if(pocket == null) {
				if (!worldIn.isRemote)  {
					TextComponentString comp = new TextComponentString(TextHelper.RED + "Pocket is null. Teleport disabled.");
					playerIn.sendMessage(comp);
					return false;
				}
			}
			if(pocket.getSourceBlockDim() != DimReference.CONSTANT.POCKET_DIMENSION_ID) {
				playerIn.setSneaking(false);
			}
			
			pocket.shiftFromPocket(playerIn);
			return true;
		}
		
		return false;
	}
	
	@Override
	protected boolean canConnect(@Nonnull IBlockState orig, @Nonnull IBlockState conn) {
		if (conn.getBlock().equals(Blocks.AIR)) {
			return false;
		} else if (orig.getBlock().equals(conn.getBlock())) {
			return true;
		} else if (conn.getBlock().equals(ModBlockManager.BLOCK_DIMENSIONAL_POCKET_WALL)) {
			return true;
		} else if (conn.getBlock().equals(ModBlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_CONNECTOR)) {
			return true;
		} else if (conn.getBlock().equals(ModBlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_ENERGY_DISPLAY)) {
			return true;
		} else if (conn.getBlock().equals(ModBlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_FLUID_DISPLAY)) {
			return true;
		}
		return false;
	}
}