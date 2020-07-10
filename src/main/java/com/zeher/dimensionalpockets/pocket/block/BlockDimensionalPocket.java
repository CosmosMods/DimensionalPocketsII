package com.zeher.dimensionalpockets.pocket.block;

import com.zeher.dimensionalpockets.pocket.tileentity.TileEntityDimensionalPocket;
import com.zeher.zeherlib.api.compat.core.impl.BlockContainerRemovableNBT;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
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

public class BlockDimensionalPocket extends BlockContainerRemovableNBT implements ITileEntityProvider {
	
	public static final PropertyInteger NORTH = PropertyInteger.create("north", 0, 3);
	public static final PropertyInteger EAST = PropertyInteger.create("east", 0, 3);
	public static final PropertyInteger SOUTH = PropertyInteger.create("south", 0, 3);
	public static final PropertyInteger WEST = PropertyInteger.create("west", 0, 3);
	public static final PropertyInteger UP = PropertyInteger.create("up", 0, 3);
	public static final PropertyInteger DOWN = PropertyInteger.create("down", 0, 3);
	public static final PropertyBool LOCKED = PropertyBool.create("locked");

	public BlockDimensionalPocket(String name, Material arg0, String tool, int harvest, int hardness, int resistance, CreativeTabs tab) {
		super(name, arg0, tool, harvest, hardness, resistance, tab);
		
		this.setBlockUnbreakable();
		
		this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, 0).withProperty(EAST, 0)
				.withProperty(SOUTH, 0).withProperty(WEST, 0).withProperty(UP, 0).withProperty(DOWN, 0).withProperty(LOCKED, false));
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
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
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
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { NORTH, EAST, WEST, SOUTH, UP, DOWN, LOCKED });
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		TileEntity tile_in = worldIn.getTileEntity(pos);
		
		if (tile_in instanceof TileEntityDimensionalPocket) {
			TileEntityDimensionalPocket tile = (TileEntityDimensionalPocket) worldIn.getTileEntity(pos);

			return state.withProperty(NORTH, tile.getSide(EnumFacing.NORTH).getIndex())
					.withProperty(EAST, tile.getSide(EnumFacing.EAST).getIndex())
					.withProperty(SOUTH, tile.getSide(EnumFacing.SOUTH).getIndex())
					.withProperty(WEST, tile.getSide(EnumFacing.WEST).getIndex())
					.withProperty(UP, tile.getSide(EnumFacing.UP).getIndex())
					.withProperty(DOWN, tile.getSide(EnumFacing.DOWN).getIndex())
					.withProperty(LOCKED, tile.getLockState());
		} else {
			return this.getDefaultState();
		}
	}
	
}