package com.zeher.dimensionalpockets.pocket.tileentity;

import com.zeher.dimensionalpockets.pocket.Pocket;
import com.zeher.dimensionalpockets.pocket.PocketRegistry;
import com.zeher.zeherlib.api.compat.client.interfaces.IClientUpdatedTile;
import com.zeher.zeherlib.api.compat.core.interfaces.IChannelType;
import com.zeher.zeherlib.api.compat.core.interfaces.EnumChannelSideState;
import com.zeher.zeherlib.api.compat.core.interfaces.EnumConnectionType;
import com.zeher.zeherlib.api.compat.core.interfaces.EnumSideState;
import com.zeher.zeherlib.api.compat.core.interfaces.IConnectionTypeTile;
import com.zeher.zeherlib.api.compat.core.interfaces.IFluidStorage;
import com.zeher.zeherlib.api.compat.core.interfaces.ISidedChannelTile;
import com.zeher.zeherlib.api.compat.core.interfaces.ISidedTile;

import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityDimensionalPocketWallConnector extends TileEntityDim implements IInventory, ISidedInventory, ITickable, ISidedTile, IEnergyReceiver, IEnergyProvider, IFluidHandler, IFluidStorage, IClientUpdatedTile.Storage, IConnectionTypeTile {
	
	private EnumSideState[] SIDE_STATE_ARRAY = EnumSideState.getStandardArray();
	
	private EnumConnectionType TYPE = EnumConnectionType.getStandardValue();
	
	private NonNullList<ItemStack> INVENTORY_STACKS = NonNullList.<ItemStack>withSize(15, ItemStack.EMPTY);
	
	/**
	@SideOnly(Side.CLIENT)
	private Pocket pocket;
	 */
	public Pocket getPocket() {
		/**
		if (this.pocket.equals(PocketRegistry.getPocket(new BlockPos(this.getPos().getX() >> 4, this.getPos().getY() >> 4, this.getPos().getZ() >> 4)))) {
			return this.pocket;
		}
		*/
		
		return PocketRegistry.getPocket(new BlockPos(this.getPos().getX() >> 4, this.getPos().getY() >> 4, this.getPos().getZ() >> 4));
	}

	private int counter = 0;
	
	public void updateRenderers() {
		Minecraft.getMinecraft().renderGlobal.markBlockRangeForRenderUpdate(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
		this.world.markBlockRangeForRenderUpdate(this.getPos(), this.getPos());
		this.markDirty();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		compound.setInteger("index", this.getSide(EnumFacing.DOWN).getIndex());
		
		compound.setInteger("type", this.TYPE.getIndex());

		if (this.getPocket() != null) {
			this.getPocket().writeToNBT(compound);
		}
		
		//ItemStackHelper.saveAllItems(compound, this.getPocket().items);
		
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		EnumSideState state = EnumSideState.getStateFromIndex(compound.getInteger("index"));
		
		this.SIDE_STATE_ARRAY = new EnumSideState[] { state, state, state, state, state, state };
		
		this.setType(EnumConnectionType.getStateFromIndex(compound.getInteger("type")));
		
		//ItemStackHelper.loadAllItems(compound, this.INVENTORY_STACKS);
		/**
		if (this.world != null && this.world.isRemote) {
			this.pocket = Pocket.readFromNBT(compound);
		}
		*/
	}

	@Override
	public void update() {
		TileEntity tile = this.world.getTileEntity(this.getPos());
		
		for (EnumFacing c : EnumFacing.VALUES) {
			TileEntity tile_other = this.world.getTileEntity(this.getPos().offset(c));
			
			if (this.getType().equals(EnumConnectionType.ENERGY) && this.getSide(c).equals(EnumSideState.INTERFACE_OUTPUT)) {
				if (tile_other instanceof IEnergyReceiver && !(tile_other instanceof TileEntityDimensionalPocketWallConnector)) {
					
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
		
		World worldIn = this.getPocket().getBlockWorld();
		BlockPos pocket_pos = this.getPocket().getLastBlockPos();
		
		if (pocket_pos.getY() >= 0 && pocket_pos.getY() <= 256 ) {	
			TileEntity tile_pocket = worldIn.getTileEntity(pocket_pos);
			
			if (tile_pocket instanceof TileEntityDimensionalPocket) {
				for (EnumFacing c : EnumFacing.VALUES) {
					Block block_other = worldIn.getBlockState(pocket_pos.offset(c)).getBlock();
					this.setStack(c, new ItemStack(block_other));
				}
			}
		}
		
		for (int i = 0 ; i < 9; i++) {
			if (this.getStackInSlot(i) != this.getPocket().items.get(i)) {
				ItemStack pocket_stack = this.getPocket().items.get(i);
				ItemStack copy = pocket_stack.copy();
				if (!this.world.isRemote) {
					this.INVENTORY_STACKS.set(i, copy);
				}
			}
		}
	}
	

	@Override
	public EnumConnectionType getType() {
		return this.TYPE;
	}

	@Override
	public void setType(EnumConnectionType type) {
		this.TYPE = type;
		this.sendUpdates();
	}
	
	public void cycleType() {
		EnumConnectionType next_state = TYPE.getNextState();
		this.TYPE = next_state;
		this.sendUpdates();
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

	public int getFluidLevelScaled(int one) {
		return this.getPocket().fluid_tank.getFluidAmount() * one / this.getPocket().fluid_tank.getCapacity();
	}
	
	public int getFillLevel() {
		return this.getPocket().fill_level;
	}

	public void updateFillLevel() {
		this.getPocket().fill_level = this.getCurrentStoredAmount() / 1000;
	}

	public Fluid getCurrentStoredFluid() {
		updateFillLevel();
		this.markDirty();
		if (!isFluidEmpty()) {
			return this.getPocket().fluid_tank.getFluid().getFluid();
		}
		return null;
	}

	public boolean isFluidEmpty() {
		return this.getPocket().fluid_tank.getFluidAmount() == 0;
	}

	public int getCurrentStoredAmount() {
		this.markDirty();
		return this.getPocket().fluid_tank.getFluidAmount();
	}

	public int fill(FluidStack resource, boolean doFill) {
		updateFillLevel();
		this.markDirty();
		return this.getPocket().fluid_tank.fill(resource, doFill);
	}

	public FluidStack drain(FluidStack resource, boolean doDrain) {
		updateFillLevel();
		this.markDirty();
		return this.getPocket().fluid_tank.drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		updateFillLevel();
		this.markDirty();
		return this.getPocket().fluid_tank.drain(maxDrain, doDrain);
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
		return new FluidTankInfo[] { this.getPocket().fluid_tank.getInfo() };
	}

	@Override
	public IFluidTankProperties[] getTankProperties() {
		return this.getPocket().fluid_tank.getTankProperties();
	}

	@Override
	public FluidTank getTank() {
		return this.getPocket().fluid_tank;
	}

	@Override
	public int getFluidCapacity() {
		return this.getPocket().fluid_capacity;
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
		return this.getEnergyStored(EnumFacing.DOWN) * scale / this.getMaxEnergyStored(EnumFacing.DOWN);
	}

	public void setStack(EnumFacing facing, ItemStack stack) {
		this.INVENTORY_STACKS.set((facing.getIndex() + 9), stack);
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
		/**return this.INVENTORY_STACKS.size() + */ return this.INVENTORY_STACKS.size();
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		/**
		if (index > 8) {
			return this.INVENTORY_STACKS.get(index);
		} else {*/
		return this.INVENTORY_STACKS.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		/**
		if (index > 8) {
			return ItemStackHelper.getAndSplit(this.INVENTORY_STACKS, index, count);
		} else {*/

		this.markDirty();
		//ItemStackHelper.getAndSplit(this.getPocket().items, index, count);
		return ItemStackHelper.getAndSplit(this.INVENTORY_STACKS, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		/**
		if (index > 8) {
			return ItemStackHelper.getAndRemove(this.INVENTORY_STACKS, index);
		} else {*/
			//this.sendUpdates();
		this.markDirty();
		//ItemStackHelper.getAndRemove(this.getPocket().items, index);
		return ItemStackHelper.getAndRemove(this.INVENTORY_STACKS, index);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		/**
		if (index > 8) {
			ItemStack itemstack = (ItemStack) this.INVENTORY_STACKS.get(index);
			this.INVENTORY_STACKS.set(index, stack);
			if (stack.getCount() > this.getInventoryStackLimit()) {
				stack.setCount(this.getInventoryStackLimit());
			}
		} else { */
		
		//this.sendUpdates();
		ItemStack itemstack = (ItemStack) this.INVENTORY_STACKS.get(index);
		this.INVENTORY_STACKS.set(index, stack);
		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
		
		this.getPocket().items.set(index, stack);
		
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
		return true;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return true;
	}

}