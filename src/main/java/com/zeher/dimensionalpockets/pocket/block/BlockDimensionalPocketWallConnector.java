package com.zeher.dimensionalpockets.pocket.block;

import java.util.Random;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.core.handler.BlockHandler;
import com.zeher.dimensionalpockets.core.handler.ItemHandler;
import com.zeher.dimensionalpockets.pocket.Pocket;
import com.zeher.dimensionalpockets.pocket.PocketRegistry;
import com.zeher.dimensionalpockets.pocket.tileentity.TileEntityDimensionalPocketWallConnector;
import com.zeher.zeherlib.api.client.util.TextHelper;
import com.zeher.zeherlib.api.compat.core.interfaces.EnumConnectionType;
import com.zeher.zeherlib.mod.block.ModBlockContainer;
import com.zeher.zeherlib.mod.util.ModUtil;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public class BlockDimensionalPocketWallConnector extends ModBlockContainer implements ITileEntityProvider {

	public static final int CONNECTOR_META = 1;

	public static final PropertyInteger MODE = PropertyInteger.create("mode", 0, 3);
	public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 3);
	
	public BlockDimensionalPocketWallConnector(String name, Material arg0, String tool, int harvest, CreativeTabs tab) {
		super(name, arg0, tool, harvest, -1, 6000001, tab);
		this.setLightLevel(1F);
		this.setHardness(-1.0F);
		this.setResistance(6000001.0F);
		this.setBlockUnbreakable();
		
		this.setDefaultState(this.blockState.getBaseState().withProperty(MODE, 0).withProperty(TYPE, 0));
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityDimensionalPocketWallConnector();
	}

	@Override
	public boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return DimensionalPockets.CAN_DESTROY_WALLS_IN_CREATIVE;
	}

	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion) { }

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return (worldIn.provider.getDimension() == DimensionalPockets.DIMENSION_ID) && DimensionalPockets.CAN_DESTROY_WALLS_IN_CREATIVE;
	}
	
	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		if (playerIn.dimension == DimensionalPockets.DIMENSION_ID) {
			TileEntity tile = worldIn.getTileEntity(pos);
			
			if (tile instanceof TileEntityDimensionalPocketWallConnector) {
				if (ModUtil.isHoldingHammer(playerIn) && playerIn.isSneaking()) {
					if(((TileEntityDimensionalPocketWallConnector) tile).getPocket().getCreator() != null) {
						String creator = ((TileEntityDimensionalPocketWallConnector) tile).getPocket().getCreator();
						
						if (creator != null) {
							if (playerIn.getName().equals(creator)) {
								if (!(worldIn.isRemote)) {
									FMLNetworkHandler.openGui(playerIn, DimensionalPockets.INSTANCE, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());
								}
							} else {
								if (!(worldIn.isRemote)) {
									TextComponentString comp = new TextComponentString(TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.access_set.name"));
									playerIn.sendMessage(comp);
								}
							}
						}
					} else {
						TextComponentString comp = new TextComponentString(TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.creator_null.name"));
						playerIn.sendMessage(comp);
					}
				}
			}
		}
	} 

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.dimension == DimensionalPockets.DIMENSION_ID) {
			TileEntity tile = worldIn.getTileEntity(pos);
			
			Pocket pocket_ = PocketRegistry.getPocket(new BlockPos(pos.getX() >> 4, pos.getY() >> 4, pos.getZ() >> 4));
			
			if (!playerIn.isSneaking()) {
				if (tile instanceof TileEntityDimensionalPocketWallConnector) {
					if (ModUtil.isHoldingHammer(playerIn)) {
						((TileEntityDimensionalPocketWallConnector) tile).cycleSide(EnumFacing.UP);
						
						if(!worldIn.isRemote) {
							TextComponentString comp = new TextComponentString(TextHelper.LIGHT_GRAY + TextHelper.BOLD + I18n.format("pocket.status.cycle_side.name") + ((TileEntityDimensionalPocketWallConnector) tile).getSide(facing).getTextColour() + TextHelper.BOLD + " [" + ((TileEntityDimensionalPocketWallConnector) tile).getSide(facing).getDisplayName() + "]");
							playerIn.sendMessage(comp);
						}
					} else {
						((TileEntityDimensionalPocketWallConnector) tile).fill(new FluidStack(FluidRegistry.WATER, 1000), true);
					}
				} 
			} else {
				if (ModUtil.isHoldingHammer(playerIn)) {	
					worldIn.setBlockState(pos, BlockHandler.BLOCK_DIMENSIONAL_POCKET_WALL.getDefaultState());
					return false;
				} 
				
				else if (!ModUtil.isHoldingHammer(playerIn) && playerIn.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
					if (worldIn.isRemote) {
						return false;
					}
					
					Pocket pocket = PocketRegistry.getPocket(new BlockPos(pos.getX() >> 4, pos.getY() >> 4, pos.getZ() >> 4));
					
					if(pocket == null) {
						if (!worldIn.isRemote)  {
							TextComponentString comp = new TextComponentString(TextHelper.RED + "Pocket is null. Teleport disabled.");
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
			}
		}
		return false;
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return ItemStack.EMPTY.getItem();
    }
	
	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
		return DimensionalPockets.CAN_DESTROY_WALLS_IN_CREATIVE;
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
		TileEntityDimensionalPocketWallConnector tile = (TileEntityDimensionalPocketWallConnector) worldIn.getTileEntity(pos);
		
		return state.withProperty(MODE, tile.getSide(EnumFacing.UP).getIndex()).withProperty(TYPE, tile.getType().getIndex());
	}
}