package com.zeher.zehercraft.transport.core.block;

import java.util.List;

import javax.annotation.Nullable;

import com.zeher.zehercraft.ZCReference;
import com.zeher.zehercraft.transport.core.tile.TileEntityEnergyChannelTransparentSurge;
import com.zeher.zeherlib.api.azrf.BlockContainerRemovable;
import com.zeher.zeherlib.api.azrf.EnumChannelSideState;
import com.zeher.zeherlib.api.azrf.ISidedChannelTile;
import com.zeher.zeherlib.mod.util.ModUtil;

import cofh.redstoneflux.api.IEnergyHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEnergyChannelTransparentSurge extends BlockContainerRemovable {
	
	public static final PropertyBool DOWN = PropertyBool.create("down");
	public static final PropertyBool UP = PropertyBool.create("up");
	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool WEST = PropertyBool.create("west");
	public static final PropertyBool EAST = PropertyBool.create("east");
	
	protected static final AxisAlignedBB[] BOUNDING_BOXES = ZCReference.RESOURCE.TRANSPORT.BOUNDING_BOXES_STANDARD_SURGE;
	protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.2D, 0.2D, 0.2D, 0.8D, 0.8D, 0.8D);

	protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.2D, 0.2D, 0.2D, 0.8D, 0.8D, 1.0D);
	protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.2D, 0.2D, 0.0D, 0.8D, 0.8D, 0.8D);

	protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.2D, 0.2D, 0.2D, 1.0D, 0.8D, 0.8D);
	protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.2D, 0.2D, 0.8D, 0.8D, 0.8D);

	protected static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.2D, 0.2D, 0.2D, 0.8D, 1.0D, 0.8D);
	protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.2D, 0.0D, 0.2D, 0.8D, 0.8D, 0.8D);
	
	public BlockEnergyChannelTransparentSurge(String name, Material material, String tool, int harvest, int hardness, int resistance, CreativeTabs tab) {
		super(name, material, tool, harvest, hardness, resistance, tab);
		
		this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, false).withProperty(SOUTH, false).withProperty(EAST, false)
				.withProperty(WEST, false).withProperty(WEST, false).withProperty(UP, false).withProperty(DOWN, false));
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityEnergyChannelTransparentSurge();
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

	@Override
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
		TileEntity tile = world.getTileEntity(pos);
		
		if (tile instanceof TileEntityEnergyChannelTransparentSurge) {
			System.out.println(((TileEntityEnergyChannelTransparentSurge) tile).getSideArray());
			
			System.out.println(((TileEntityEnergyChannelTransparentSurge) tile).storage.getMaxEnergyStored() + " | " + ((TileEntityEnergyChannelTransparentSurge) tile).storage.getEnergyStored() + " | " + ((TileEntityEnergyChannelTransparentSurge) tile).getLastFacing());
			
		}
		//OPEN GUI
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hit_x, float hit_y, float hit_z) {
		super.onBlockActivated(world, pos, state, player, hand, facing, hit_x, hit_y, hit_z);
		
		if (ModUtil.isHoldingHammer(player) && !player.isSneaking()) {
			TileEntity tile = world.getTileEntity(pos);
			
			if (tile instanceof TileEntityEnergyChannelTransparentSurge && tile != null) {
				((TileEntityEnergyChannelTransparentSurge) tile).cycleSide(facing);
			}
		}
		
		return false;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB box, List<AxisAlignedBB> colliding_boxes, @Nullable Entity entity, boolean bool){
		if (!bool) {
			state = state.getActualState(world, pos);
		}
		
		this.addCollisionBoxToList(pos, box, colliding_boxes, BASE_AABB);
		
		if (state.getValue(NORTH).booleanValue()) {
			this.addCollisionBoxToList(pos, box, colliding_boxes, NORTH_AABB);
		}
		
		if (state.getValue(SOUTH).booleanValue()) {
			this.addCollisionBoxToList(pos, box, colliding_boxes, SOUTH_AABB);
		}
		
		if (state.getValue(EAST).booleanValue()) {
			this.addCollisionBoxToList(pos, box, colliding_boxes, EAST_AABB);
		}
		
		if (state.getValue(WEST).booleanValue()) {
			this.addCollisionBoxToList(pos, box, colliding_boxes, WEST_AABB);
		}
		
		if (state.getValue(UP).booleanValue()) {
			this.addCollisionBoxToList(pos, box, colliding_boxes, UP_AABB);
		}
		
		if (state.getValue(DOWN).booleanValue()) {
			this.addCollisionBoxToList(pos, box, colliding_boxes, DOWN_AABB);
		}
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { NORTH, SOUTH, EAST, WEST, UP, DOWN });
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		state = this.getActualState(state, source, pos);
		return BOUNDING_BOXES[this.getBoundingBoxIDx(state)];
	}
	
	public static int getBoundingBoxIDx(IBlockState state) {
		int i = 0;

		if (state.getValue(NORTH).booleanValue()) {
			i |= 1 << 2;
		}

		if (state.getValue(EAST).booleanValue()) {
			i |= 1 << 5;
		}

		if (state.getValue(SOUTH).booleanValue()) {
			i |= 1 << 3;
		}

		if (state.getValue(WEST).booleanValue()) {
			i |= 1 << 4;
		}
		if (state.getValue(UP).booleanValue()) {
			i |= 1 << 1;
		}
		if (state.getValue(DOWN).booleanValue()) {
			i |= 1 << 0;
		}
		
		return i;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity tile_this = world.getTileEntity(pos);
		
		EnumChannelSideState[] side_array = EnumChannelSideState.getStandardArray();
		
		if (tile_this != null && tile_this instanceof ISidedChannelTile) {
			side_array = ((ISidedChannelTile) tile_this).getSideArray();
		}
		
		/** Order is: [D-U-N-S-W-E] **/
		boolean[] bool_array = new boolean[] { false, false, false, false, false, false };
		
		for (int i = 0; i < side_array.length; i++) {
			if (side_array[i].equals(EnumChannelSideState.DISABLED)) {
				bool_array[i] = false;
			} else if (side_array[i].equals(EnumChannelSideState.INTERFACE_INPUT) || side_array[i].equals(EnumChannelSideState.INTERFACE_OUTPUT) || side_array[i].equals(EnumChannelSideState.INTERFACE_NORMAL)) {
				bool_array[i] = true;
			} else {
				bool_array[i] = this.canConnectToBlock(world, pos, EnumFacing.getFront(i));
			}
		}
		
		return state.withProperty(DOWN, bool_array[0]).withProperty(UP, bool_array[1])
				.withProperty(NORTH, bool_array[2]).withProperty(SOUTH, bool_array[3])
				.withProperty(WEST, bool_array[4]).withProperty(EAST, bool_array[5]);
	}
	
	private boolean canConnectToBlock(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		TileEntity tile_other = world.getTileEntity(pos.offset(facing));
		
		if (tile_other instanceof IEnergyHandler) {
			return ((IEnergyHandler) tile_other).canConnectEnergy(facing.getOpposite());
		}
		
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
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
}