package com.zeher.dimensionalpockets.pocket.block;

import java.util.Random;

import javax.annotation.Nonnull;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.core.handler.BlockHandler;
import com.zeher.dimensionalpockets.pocket.Pocket;
import com.zeher.dimensionalpockets.pocket.PocketRegistry;
import com.zeher.zeherlib.api.client.util.TextHelper;
import com.zeher.zeherlib.mod.block.ModBlockConnectedGlass;
import com.zeher.zeherlib.mod.util.ModUtil;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDimensionalGlass extends ModBlockConnectedGlass {

	public static final PropertyBool DOWN = PropertyBool.create("down");
	public static final PropertyBool UP = PropertyBool.create("up");
	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool WEST = PropertyBool.create("west");
	public static final PropertyBool EAST = PropertyBool.create("east");
	
	public BlockDimensionalGlass(String name, CreativeTabs tab) {
		super(name, "", -1, 10, 6000001, tab);
		this.setLightLevel(1F);
		this.setBlockUnbreakable();
		this.setHardness(-1.0F);
		this.setResistance(6000001.0F);
		this.setBlockUnbreakable();
		
		this.setDefaultState(this.blockState.getBaseState()
				.withProperty(DOWN, Boolean.FALSE)
                .withProperty(EAST, Boolean.FALSE)
                .withProperty(NORTH, Boolean.FALSE)
                .withProperty(SOUTH, Boolean.FALSE)
                .withProperty(UP, Boolean.FALSE)
                .withProperty(WEST, Boolean.FALSE));

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
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return (worldIn.provider.getDimension() == DimensionalPockets.DIMENSION_ID) && DimensionalPockets.CAN_DESTROY_WALLS_IN_CREATIVE;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.equals(null)) {
			return false;
		}
		
		if (playerIn.dimension == DimensionalPockets.DIMENSION_ID) {
			if (ModUtil.isHoldingHammer(playerIn) && playerIn.isSneaking()) {
				worldIn.setBlockState(pos, BlockHandler.BLOCK_DIMENSIONAL_POCKET_WALL_CONNECTOR.getDefaultState().withProperty(BlockDimensionalPocketWallConnector.MODE, 0));
			}
			
			if (ModUtil.isHoldingHammer(playerIn) && !playerIn.isSneaking()) {
				worldIn.setBlockState(pos, BlockHandler.BLOCK_DIMENSIONAL_POCKET_WALL.getDefaultState());
				return true;
			}
			
			if (!ModUtil.isHoldingHammer(playerIn) && playerIn.isSneaking()) {
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
			} else if (playerIn.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
				return false;
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
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.withProperty(DOWN,  this.canSideConnect(world, pos, EnumFacing.DOWN))
					.withProperty(EAST,  this.canSideConnect(world, pos, EnumFacing.EAST))
					.withProperty(NORTH, this.canSideConnect(world, pos, EnumFacing.NORTH))
					.withProperty(SOUTH, this.canSideConnect(world, pos, EnumFacing.SOUTH))
					.withProperty(UP,    this.canSideConnect(world, pos, EnumFacing.UP))
					.withProperty(WEST,  this.canSideConnect(world, pos, EnumFacing.WEST));
	}
	
	@Override
	@Nonnull
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { DOWN, UP, NORTH, SOUTH, WEST, EAST });
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	private boolean canSideConnect(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		final IBlockState orig = world.getBlockState(pos);
		final IBlockState conn = world.getBlockState(pos.offset(facing));
		
		return orig != null && conn != null && this.canConnect(orig, conn);
	}
	
	protected boolean canConnect(@Nonnull IBlockState orig, @Nonnull IBlockState conn) {
		//return conn.getBlock().equals(BlockHandler.REGISTRATION.DECORATION.BLOCK_GLASS_BLACK);
		return orig.getBlock() == conn.getBlock();
	}
	
	@Nonnull
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        Block block = iblockstate.getBlock();
        
        if (block instanceof ModBlockConnectedGlass) {
            return false;
        } else {
        	return true;
        }
    }
}