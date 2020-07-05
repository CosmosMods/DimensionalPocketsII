package com.zeher.zehercraft.processing.core.block;

import com.zeher.zehercraft.ZCReference;
import com.zeher.zehercraft.core.handler.GuiHandler;
import com.zeher.zehercraft.processing.core.tile.TileEntitySynthesiser;
import com.zeher.zeherlib.mod.block.ModBlockContainer;
import com.zeher.zeherlib.mod.util.ModUtil;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSynthesiser extends ModBlockContainer {
	
	public static final PropertyInteger ENERGY = PropertyInteger.create("energy", 0, 14);
	
	public BlockSynthesiser(String name, Material material, String tool, int harvest, int hardness, int resistance, CreativeTabs tab) {
		super(name, material, tool, harvest, hardness, resistance, tab);
		
		this.setDefaultState(this.blockState.getBaseState().withProperty(ENERGY, 0));
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
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		TileEntitySynthesiser tile = (TileEntitySynthesiser) worldIn.getTileEntity(pos);
		if (!tile.getStackInSlot(0).isEmpty()) {
			playerIn.inventory.addItemStackToInventory(tile.getStackInSlot(0));
			tile.setInventorySlotContents(0, ItemStack.EMPTY);
			worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.2F, 2F, false);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntitySynthesiser tile = (TileEntitySynthesiser) worldIn.getTileEntity(pos);
		if ((!playerIn.getHeldItem(hand).isEmpty()) && (tile.getStackInSlot(0).isEmpty())) {
			ItemStack stack = playerIn.getHeldItem(hand);

			worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.2F, 0F, false);
			
			tile.setInventorySlotContents(0, stack.copy());
			stack.shrink(1);
		} else if (playerIn.getHeldItem(hand).isEmpty() && playerIn.isSneaking() && !worldIn.isRemote && !ModUtil.isHoldingHammer(playerIn)) {
			GuiHandler.openGui(playerIn, ZCReference.RESOURCE.PROCESSING.SYNTHESISER_INDEX, worldIn, pos);
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
		return new TileEntitySynthesiser();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.05D, 0.0D, 0.05D, 0.95D, 1D, 0.95D);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { ENERGY });
	}
	
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntitySynthesiser tile = (TileEntitySynthesiser) world.getTileEntity(pos);
		
		return state.withProperty(ENERGY, tile.getEnergyStored(EnumFacing.DOWN) / 10000);
	}
}