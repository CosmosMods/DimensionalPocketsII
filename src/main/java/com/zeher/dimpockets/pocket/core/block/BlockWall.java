package com.zeher.dimpockets.pocket.core.block;

import javax.annotation.Nonnull;

import com.zeher.dimpockets.DimReference;
import com.zeher.dimpockets.core.manager.ModBlockManager;
import com.zeher.dimpockets.core.manager.ModConfigManager;
import com.zeher.dimpockets.pocket.core.Pocket;
import com.zeher.dimpockets.pocket.core.manager.PocketRegistryManager;
import com.zeher.zeherlib.api.client.util.TextHelper;
import com.zeher.zeherlib.api.compat.core.interfaces.EnumConnectionType;
import com.zeher.zeherlib.mod.block.ModBlockConnectedUnbreakable;
import com.zeher.zeherlib.mod.util.ModUtil;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class BlockWall extends ModBlockConnectedUnbreakable {
	
	public BlockWall(String name, Material material) {
		super(name, material);
		this.setLightLevel(1F);
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return (worldIn.provider.getDimension() == DimReference.CONSTANT.POCKET_DIMENSION_ID) && ModConfigManager.CAN_DESTROY_WALLS_IN_CREATIVE;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.dimension == DimReference.CONSTANT.POCKET_DIMENSION_ID) {
			if (!playerIn.isSneaking()) {
				if (ModUtil.isHoldingHammer(playerIn)) {
					playerIn.swingArm(EnumHand.MAIN_HAND);
					
					if (pos.getY() != 0 && pos.getY() != 15) {
						worldIn.setBlockState(pos, ModBlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_ENERGY_DISPLAY.getDefaultState());
						return true;
					}
				}
			} else {
				if (ModUtil.isHoldingHammer(playerIn)) {
					playerIn.swingArm(EnumHand.MAIN_HAND);
					
					worldIn.setBlockState(pos, ModBlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_CONNECTOR.getDefaultState());
					
					Pocket pocket = PocketRegistryManager.getPocket(new BlockPos(pos.getX() >> 4, pos.getY() >> 4, pos.getZ() >> 4));
					
					if (pocket != null) {
						pocket.updateInConnectorMap(pos, EnumConnectionType.getStandardValue());
					}
					return true;
				}
				
				else if (!ModUtil.isHoldingHammer(playerIn) && playerIn.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
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
					} else {
						if(pocket.getSourceBlockDim() != DimReference.CONSTANT.POCKET_DIMENSION_ID) {
							playerIn.setSneaking(false);
						}
						
						pocket.shiftFromPocket(playerIn);
						
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	protected boolean canConnect(@Nonnull IBlockState orig, @Nonnull IBlockState conn) {
		if (ModConfigManager.CONNECTED_TEXTURES_INSIDE_POCKET) {
			if (orig.getBlock().equals(conn.getBlock())) {
				return true;
			} 
		}
		return false;
	}
}