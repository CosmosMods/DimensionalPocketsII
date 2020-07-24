package com.zeher.dimpockets.pocket.core.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.zeher.dimpockets.DimReference;
import com.zeher.dimpockets.DimensionalPockets;
import com.zeher.dimpockets.core.manager.ModBlockManager;
import com.zeher.dimpockets.pocket.core.Pocket;
import com.zeher.dimpockets.pocket.core.manager.PocketRegistryManager;
import com.zeher.zeherlib.api.client.util.TextHelper;
import com.zeher.zeherlib.api.compat.client.interfaces.IClientUpdatedTile;
import com.zeher.zeherlib.api.compat.core.interfaces.EnumChannelSideState;
import com.zeher.zeherlib.api.compat.core.interfaces.EnumConnectionType;
import com.zeher.zeherlib.api.compat.core.interfaces.EnumSideState;
import com.zeher.zeherlib.api.compat.core.interfaces.IChannelType;
import com.zeher.zeherlib.api.compat.core.interfaces.IConnectionTypeTile;
import com.zeher.zeherlib.api.compat.core.interfaces.IFluidStorage;
import com.zeher.zeherlib.api.compat.core.interfaces.ISidedChannelTile;
import com.zeher.zeherlib.api.compat.core.interfaces.ISidedTile;
import com.zeher.zeherlib.api.core.interfaces.block.IBlockInteract;
import com.zeher.zeherlib.api.core.interfaces.block.IBlockNotifier;
import com.zeher.zeherlib.mod.util.ModUtil;

import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public class TileConnector extends TileBase implements IBlockNotifier, IBlockInteract, IInventory, ISidedInventory, ITickable, ISidedTile, IEnergyReceiver, IEnergyProvider, IFluidHandler, IFluidStorage, IClientUpdatedTile.Storage, IClientUpdatedTile.FluidTile, IConnectionTypeTile {
	
	private EnumSideState[] SIDE_STATE_ARRAY = EnumSideState.getStandardArray();
	private EnumConnectionType TYPE = EnumConnectionType.getStandardValue();
	
	private NonNullList<ItemStack> INVENTORY_STACKS = NonNullList.<ItemStack>withSize(56, ItemStack.EMPTY);
	
	@Override
	public Pocket getPocket() {
		return PocketRegistryManager.getPocket(new BlockPos(this.getPos().getX() >> 4, this.getPos().getY() >> 4, this.getPos().getZ() >> 4));
	}

	public void updateRenderers() {
		Minecraft.getMinecraft().renderGlobal.markBlockRangeForRenderUpdate(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
		this.world.markBlockRangeForRenderUpdate(this.getPos(), this.getPos());
		this.markDirty();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("index", this.getSide(EnumFacing.DOWN).getIndex());
		compound.setInteger("type", this.TYPE.getIndex());
		
		super.writeToNBT(compound);
		
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		EnumSideState state = EnumSideState.getStateFromIndex(compound.getInteger("index"));
		this.SIDE_STATE_ARRAY = new EnumSideState[] { state, state, state, state, state, state };
		this.setType(EnumConnectionType.getStateFromIndex(compound.getInteger("type")));
		
		super.readFromNBT(compound);
	}

	@Override
	public void update() {
		TileEntity tile = this.world.getTileEntity(this.getPos());
		
		for (EnumFacing c : EnumFacing.VALUES) {
			TileEntity tile_other = this.world.getTileEntity(this.getPos().offset(c));
			
			if (this.getType().equals(EnumConnectionType.ENERGY) && this.getSide(c).equals(EnumSideState.INTERFACE_OUTPUT)) {
				if (tile_other instanceof IEnergyReceiver && !(tile_other instanceof TileConnector)) {
					
					if (this.hasEnergy() && ((IEnergyReceiver) tile_other).canConnectEnergy(c.getOpposite())) {
						if (tile_other instanceof IChannelType.IChannelEnergy && tile_other instanceof ISidedChannelTile) {
							if (!((ISidedChannelTile) tile_other).getSide(c.getOpposite()).equals(EnumChannelSideState.DISABLED) && !((ISidedChannelTile) tile_other).getSide(c.getOpposite()).equals(EnumChannelSideState.INTERFACE_OUTPUT)) {
								this.extractEnergy(c, ((IEnergyReceiver) tile_other).receiveEnergy(c.getOpposite(), this.getMaxExtract(), false), false);
							}	
						} else if (tile_other instanceof ISidedTile) {
							if (!((ISidedTile) tile_other).getSide(c.getOpposite()).equals(EnumSideState.DISABLED) && !((ISidedTile) tile_other).getSide(c.getOpposite()).equals(EnumSideState.INTERFACE_OUTPUT)) {
								this.extractEnergy(c, ((IEnergyReceiver) tile_other).receiveEnergy(c.getOpposite(), this.getMaxExtract(), false), false);
							}
						} else {
							this.extractEnergy(c, ((IEnergyReceiver) tile_other).receiveEnergy(c.getOpposite(), this.getMaxExtract(), false), false);
						}
					}
				}
			}
		}
		
		if (this.getPocket().isGenerated()) {
			this.setSurroundingStacks();
			this.setStacks();
		}
		
		this.checkFluidSlots();
	}
	

	@Override
	public EnumConnectionType getType() {
		return this.TYPE;
	}

	@Override
	public void setType(EnumConnectionType type) {
		this.TYPE = type;
		this.sendUpdates();
		
		this.getPocket().updateInConnectorMap(this.getPos(), this.getType());
	}
	
	@Override
	public void cycleType() {
		EnumConnectionType next_state = TYPE.getNextState();
		this.TYPE = next_state;
		this.sendUpdates();
		
		this.getPocket().updateInConnectorMap(this.getPos(), this.getType());
	}
	
	@Override 
	public EnumSideState getSide(EnumFacing facing) {
		return SIDE_STATE_ARRAY[facing.getIndex()];
	}
	
	@Override
	public void setSide(EnumFacing facing, EnumSideState side_state) {
		for (int i = 0; i < this.SIDE_STATE_ARRAY.length; i++) {
			SIDE_STATE_ARRAY[i] = side_state;
		}
		this.sendUpdates();
	}
	
	@Override
	public EnumSideState[] getSideArray() {
		return this.SIDE_STATE_ARRAY;
	}

	@Override
	public void setSideArray(EnumSideState[] new_array) {
		SIDE_STATE_ARRAY = new_array;
		
		this.sendUpdates();
	}

	@Override
	public void cycleSide(EnumFacing facing) {
		EnumSideState state = SIDE_STATE_ARRAY[facing.getIndex()];
		
		EnumSideState state2 = state.getNextState();
		
		for (int i = 0; i < this.SIDE_STATE_ARRAY.length; i++) {
			SIDE_STATE_ARRAY[i] = state2;
		}
		
		this.sendUpdates();
	}

	@Override
	public boolean canConnect(EnumFacing facing) {
		EnumSideState state = SIDE_STATE_ARRAY[facing.getIndex()];
		
		if (state.equals(EnumSideState.DISABLED)) {
			return false;
		}
		return true;
	}

	@Override
	public void sendUpdates() {
		this.world.markBlockRangeForRenderUpdate(this.getPos(), this.getPos());
		this.world.notifyBlockUpdate(this.getPos(), world.getBlockState(this.getPos()), world.getBlockState(this.getPos()), 3);
		this.world.scheduleBlockUpdate(this.getPos(), this.getBlockType(), 0, 0);		
		this.markDirty();
	}
	
	/**
	 * - TODO IEnergy Start
	 */
	@Override
	public int getEnergyStored(EnumFacing from) {
		return this.getPocket().getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return this.getPocket().getMaxEnergyStored();
	}
	
	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		if (this.getType() != EnumConnectionType.ENERGY) {
			return false;
		}
		if (this.getSide(from).equals(EnumSideState.DISABLED)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		if (this.getSide(EnumFacing.UP).equals(EnumSideState.INTERFACE_INPUT)) {
			this.markDirty();
			return this.getPocket().receiveEnergy(maxReceive, simulate);	
		}
		return 0;
	}
	
	public void setEnergy(int set) {
		this.markDirty();
		this.getPocket().setEnergyStored(set);
	}

	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		if (this.getSide(EnumFacing.UP).equals(EnumSideState.INTERFACE_OUTPUT)){
			this.markDirty();
			return this.getPocket().extractEnergy(maxExtract, simulate);
		}
		return 0;
	}
	
	@Override
	public boolean hasEnergy() {
		return this.getPocket().hasStored();
	}
	
	public int getMaxExtract() {
		return this.getPocket().getMaxExtract();
	}
	
	public int getMaxReceive() {
		return this.getPocket().getMaxReceive();
	}
	
	/**
	 * - TODO IFluid Start
	 */

	@Override
	public int getFluidLevelScaled(int one) {
		return this.getPocket().getFluidLevelScaled(one);
	}
	
	@Override
	public Fluid getCurrentStoredFluid() {
		this.markDirty();
		return this.getPocket().getCurrentStoredFluid();
	}

	@Override
	public boolean isFluidEmpty() {
		return this.getPocket().isFluidTankEmpty();
	}

	@Override
	public int getCurrentFluidAmount() {
		this.markDirty();
		return this.getPocket().getCurrentFluidAmount();
	}
	
	public String getCurrentStoredFluidName() {
		return this.getPocket().getCurrentStoredFluidName();
	}

	public int fill(FluidStack resource, boolean doFill) {
		this.markDirty();
		return this.getPocket().fill(resource, doFill);
	}

	public FluidStack drain(FluidStack resource, boolean doDrain) {
		this.markDirty();
		return this.getPocket().drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		this.markDirty();
		return this.getPocket().drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		return true;
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		return this.getPocket().getFluidTankInfo(from);
	}

	@Override
	public IFluidTankProperties[] getTankProperties() {
		return this.getPocket().getFluidTankProperties();
	}

	@Override
	public FluidTank getTank() {
		return this.getPocket().getFluidTank();
	}

	@Override
	public int getFluidCapacity() {
		return this.getPocket().getFluidTankCapacity();
	}
	
	/**
	 * Set the data once it has been received. [NBT > TE]
	 */
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		this.readFromNBT(tag);
	}
	
	/**
	 * Retrieve the data to be stored. [TE > NBT]
	 */
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);
		return tag;
	}
	
	/**
	 * Actually sends the data to the server. [NBT > SER]
	 */
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.getPos(), 3, this.getUpdateTag());
	}
	
	/**
	 * Method is called once packet has been received by the client. [SER > CLT]
	 */
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		NBTTagCompound tag_ = pkt.getNbtCompound();
		
		this.handleUpdateTag(tag_);
		this.sendUpdates();
	}
	
	/**
	 * Check whether the pocket is locked.
	 * @return boolean of the lock state
	 */
	public boolean getLockState() {
		return this.getPocket().getLockState();
	}
	
	/**
	 * Set the lock state of the pocket.
	 */
	public void setLockState(boolean change) {
		this.getPocket().setLockState(change);
		this.sendUpdates();
	}
	
	@Override
	public int getEnergyScaled(int scale) {
		return (((this.getEnergyStored(EnumFacing.DOWN) / 100) * scale) / (this.getMaxEnergyStored(EnumFacing.DOWN) / 100));
	}
	
	@Override
	public int getField(int id) {
		switch (id) {
		case 1:
			return this.getEnergyStored(EnumFacing.DOWN);
		default:
			return 0;
		}
	}

	@Override
	public void setField(int id, int value) { }

	@Override
	public int getFieldCount() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.INVENTORY_STACKS) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String getName() {
		return "DimensionalPocketWallTile";
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}

	@Override
	public int getSizeInventory() {
		return this.INVENTORY_STACKS.size();
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.INVENTORY_STACKS.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		this.markDirty();
		return ItemStackHelper.getAndSplit(this.INVENTORY_STACKS, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		this.markDirty();
		return ItemStackHelper.getAndRemove(this.INVENTORY_STACKS, index);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = (ItemStack) this.INVENTORY_STACKS.get(index);
		this.INVENTORY_STACKS.set(index, stack);
		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
		
		if (index < DimReference.CONSTANT.POCKET_HELD_ITEMS_SIZE) {
			this.getPocket().items.set(index, stack);
		}
		this.markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) { }

	@Override
	public void closeInventory(EntityPlayer player) { }

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}
	
	@Override
	public void clear() { }

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] { 0 };
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return false;
	}

	public void checkFluidSlots() {
		if (!this.world.isRemote) {
			if (!this.INVENTORY_STACKS.get(54).isEmpty()) {
				if (this.INVENTORY_STACKS.get(54).getItem().equals(Items.BUCKET)) {
					if (this.getCurrentFluidAmount() > 0) {
						ItemStack fillStack = FluidUtil.tryFillContainer(this.INVENTORY_STACKS.get(54), this.getTank(), 1000, null, false).result;
						if (this.INVENTORY_STACKS.get(55).isEmpty()) {
							if (fillStack != null) {
								this.drain(FluidUtil.getFluidContained(fillStack).amount, true);
								this.INVENTORY_STACKS.get(54).shrink(1);
								this.INVENTORY_STACKS.set(55, fillStack);
							}
						}
					}
				}
				
				FluidStack fluid = FluidUtil.getFluidContained(this.INVENTORY_STACKS.get(54));
				
				if (fluid != null) {
					int amount = this.fill(fluid, false);
					if (amount == fluid.amount) {
						if (this.INVENTORY_STACKS.get(55).getItem().equals(Items.BUCKET) && this.INVENTORY_STACKS.get(55).getCount() < 17) {
							this.fill(fluid, true);
							this.INVENTORY_STACKS.get(54).shrink(1);
							this.INVENTORY_STACKS.get(55).grow(1);
						}
						
						if (this.INVENTORY_STACKS.get(55).isEmpty()) {
							this.fill(fluid, true);
							this.INVENTORY_STACKS.get(54).shrink(1);
							this.INVENTORY_STACKS.set(55, new ItemStack(Items.BUCKET));
						}
					}
				}
				this.markDirty();
			}
		}
	}
	
	public void setStacks() {
		for (int i = 0 ; i < this.getPocket().items.size(); i++) {
			if (this.getStackInSlot(i) != this.getPocket().items.get(i)) {
				ItemStack pocket_stack = this.getPocket().items.get(i);
				ItemStack copy = pocket_stack.copy();
				if (!this.world.isRemote) {
					this.INVENTORY_STACKS.set(i, copy);
				}
			}
		}
	}
	
	public void setSurroundingStacks() {
		World worldIn = FMLCommonHandler.instance().getMinecraftServerInstance().getServer().getWorld(this.getPocket().getSourceBlockDim());
		BlockPos pocket_pos = this.getPocket().getLastPos();
		
		if (pocket_pos.getY() >= 0 && pocket_pos.getY() <= 256 ) {
			TileEntity tile_pocket = worldIn.getTileEntity(pocket_pos);
			
			if (tile_pocket instanceof TilePocket) {
				for (EnumFacing c : EnumFacing.VALUES) {
					Block block_other = worldIn.getBlockState(pocket_pos.offset(c)).getBlock();
					this.INVENTORY_STACKS.set(c.getIndex() + DimReference.CONSTANT.POCKET_HELD_ITEMS_SIZE, new ItemStack(block_other));
				}
			}
		}
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return (T) this.getPocket().getFluidTank();
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.dimension == DimReference.CONSTANT.POCKET_DIMENSION_ID) {
			if (!playerIn.isSneaking()) {
				if (ModUtil.isHoldingHammer(playerIn)) {
					this.cycleSide(EnumFacing.UP);
					
					ModUtil.sendPlayerMessage(worldIn, playerIn, TextHelper.LIGHT_GRAY + TextHelper.BOLD + I18n.format("pocket.status.cycle_side.name") + this.getSide(facing).getTextColour() + TextHelper.BOLD + " [" + this.getSide(facing).getDisplayName() + "]");
				}
				
				if (playerIn.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
					String creator = this.getPocket().getCreator();
					
					if (creator != null) {
						if (playerIn.getName().equals(creator)) {
							if (!(worldIn.isRemote)) {
								FMLNetworkHandler.openGui(playerIn, DimensionalPockets.INSTANCE, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());
							}
						} else {
							ModUtil.sendPlayerMessage(worldIn, playerIn, TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.access_set.name"));
						}
					}
				}
			} else {
				if (ModUtil.isHoldingHammer(playerIn)) {
					this.getPocket().removeFromConnectorMap(pos);
					
					worldIn.setBlockState(pos, ModBlockManager.BLOCK_DIMENSIONAL_POCKET_WALL.getDefaultState());
					worldIn.removeTileEntity(pos);
					
					return false;
				} 
				
				else if (!ModUtil.isHoldingHammer(playerIn) && playerIn.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
					if (worldIn.isRemote) {
						return false;
					}
					
					if(this.getPocket() == null) {
						ModUtil.sendPlayerMessage(worldIn, playerIn, TextHelper.RED + "Pocket is null. Teleport disabled.");
					}
					
					if(this.getPocket().getSourceBlockDim() != DimReference.CONSTANT.POCKET_DIMENSION_ID) {
						playerIn.setSneaking(false);
					}
					
					this.getPocket().shiftFromPocket(playerIn);
					
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		if (playerIn.dimension == DimReference.CONSTANT.POCKET_DIMENSION_ID) {
			if (ModUtil.isHoldingHammer(playerIn) && playerIn.isSneaking()) {
				if(this.getPocket().getCreator() != null) {
					String creator = this.getPocket().getCreator();
				} else {
					ModUtil.sendPlayerMessage(worldIn, playerIn, TextHelper.RED + TextHelper.BOLD + I18n.format("pocket.status.creator_null.name"));
				}
			}
		}
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) { }

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) { }

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) { }

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) { }
}