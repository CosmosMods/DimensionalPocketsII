package com.zeher.dimpockets.pocket.core.block;

import java.util.Random;

import com.zeher.dimpockets.DimReference;
import com.zeher.dimpockets.core.manager.ModBlockManager;
import com.zeher.dimpockets.core.manager.ModConfigManager;
import com.zeher.dimpockets.pocket.core.Pocket;
import com.zeher.dimpockets.pocket.core.manager.PocketRegistryManager;
import com.zeher.zeherlib.api.client.util.TextHelper;
import com.zeher.zeherlib.api.compat.core.impl.UnlistedStringProperty;
import com.zeher.zeherlib.mod.block.ModBlockUnbreakable;
import com.zeher.zeherlib.mod.util.ModUtil;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFluidDisplay extends ModBlockUnbreakable {
	
	public static final IUnlistedProperty<Integer> FluidLevel = new Properties.PropertyAdapter<Integer>(PropertyInteger.create("fluidLevel", 0, 16));
	public static final IUnlistedProperty<String> FluidName = new UnlistedStringProperty("fluidName");
	public static final IUnlistedProperty<Boolean> CullFluidTop = new Properties.PropertyAdapter<Boolean>(PropertyBool.create("cullFluidTop"));
	
	public BlockFluidDisplay(String name, Material material) {
		super(name, material);
		this.setLightLevel(1F);
		this.setTickRandomly(true);
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
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return (worldIn.provider.getDimension() == DimReference.CONSTANT.POCKET_DIMENSION_ID) && ModConfigManager.CAN_DESTROY_WALLS_IN_CREATIVE;
	}
	
	@Override
    public boolean requiresUpdates() {
        return true;
    }
	
	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) { } 

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.dimension == DimReference.CONSTANT.POCKET_DIMENSION_ID) {
			TileEntity tile = worldIn.getTileEntity(pos);
			
			Pocket pocket_ = PocketRegistryManager.getPocket(new BlockPos(pos.getX() >> 4, pos.getY() >> 4, pos.getZ() >> 4));
			
			if (!playerIn.isSneaking()) {
				if (ModUtil.isHoldingHammer(playerIn)) {
					playerIn.swingArm(EnumHand.MAIN_HAND);
					
					worldIn.setBlockState(pos, ModBlockManager.BLOCK_DIMENSIONAL_POCKET_WALL.getDefaultState());
					return true;
				}
			} else {
				if (!ModUtil.isHoldingHammer(playerIn) && playerIn.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
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
			}
		}
		return false;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[] { FluidLevel, FluidName, CullFluidTop };
		IProperty[] listedProperties = new IProperty[] {};
		return new ExtendedBlockState(this, listedProperties, unlistedProperties);
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		IExtendedBlockState extendedState = (IExtendedBlockState) state;
		Pocket pocket = PocketRegistryManager.getPocket(new BlockPos(pos.getX() >> 4, pos.getY() >> 4, pos.getZ() >> 4));
		
		if (pocket != null) {
			Fluid tankFluid = pocket.getCurrentStoredFluid();
			int fluidLevel = pocket.getFluidFillLevel();
			return extendedState.withProperty(FluidName, (tankFluid != null) ? tankFluid.getName() : "").withProperty(FluidLevel, fluidLevel).withProperty(CullFluidTop, false);
		}
		return extendedState.withProperty(FluidName, "").withProperty(FluidLevel, 0).withProperty(CullFluidTop, false);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
}