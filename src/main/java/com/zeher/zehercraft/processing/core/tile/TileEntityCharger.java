package com.zeher.zehercraft.processing.core.tile;

import com.zeher.zehercraft.ZCReference;
import com.zeher.zehercraft.processing.client.container.ContainerCharger;
import com.zeher.zeherlib.api.azrf.IClientUpdatedTile;

import cofh.redstoneflux.api.IEnergyReceiver;
import cofh.redstoneflux.impl.EnergyStorage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public class TileEntityCharger extends TileEntity implements ITickable, ISidedInventory, IEnergyReceiver, IClientUpdatedTile {
	
	private static final int[] SLOTS_TOP = new int[] { 0 };
	private static final int[] SLOTS_BOTTOM = new int[] { 2, 1 };
	private static final int[] SLOTS_SIDES = new int[] { 1 };
	
	private NonNullList<ItemStack> charge_stacks = NonNullList.<ItemStack>withSize(12, ItemStack.EMPTY);
	
	private String custom_name;

	public int capacity_internal = ZCReference.RESOURCE.PROCESSING.CAPACITY[0];
	public int max_input = ZCReference.RESOURCE.PROCESSING.MAX_INPUT[0];
	
	public EnergyStorage storage = new EnergyStorage(capacity_internal, max_input);

	@Override
	public int getSizeInventory() {
		return this.charge_stacks.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.charge_stacks) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return (ItemStack) this.charge_stacks.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.charge_stacks, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.charge_stacks, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = (ItemStack) this.charge_stacks.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.charge_stacks.set(index, stack);

		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}

		if (index == 0 && !flag) {
			this.markDirty();
		}
	}

	@Override
	public String getName() {
		return this.hasCustomName() ? this.custom_name : ZCReference.RESOURCE.PROCESSING.CHARGER_NAME;
	}

	@Override
	public boolean hasCustomName() {
		return this.custom_name != null && !this.custom_name.isEmpty();
	}

	public void setCustomInventoryName(String p_145951_1_) {
		this.custom_name = p_145951_1_;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.charge_stacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.charge_stacks);
		
		if (compound.hasKey("CustomName", 8)) {
			this.custom_name = compound.getString("CustomName");
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		ItemStackHelper.saveAllItems(compound, this.charge_stacks);

		if (this.hasCustomName()) {
			compound.setString("CustomName", this.custom_name);
		}
		return compound;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public void update() {
		//int i = this.charge_stacks.get(11).getCount();
		//this.capacity = Reference.VALUE.MACHINE.poweredStored(i);
		
		for (int i = 0; i < this.getSizeInventory(); i++) {
			if (i < 9) {
				//CHARGE ITEMS
			}
		}
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.world.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
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
	public int[] getSlotsForFace(EnumFacing side) {
		return side == EnumFacing.DOWN ? SLOTS_BOTTOM : (side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES);
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return this.isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		if (direction == EnumFacing.DOWN && index == 1) {
			Item item = stack.getItem();

			if (item != Items.WATER_BUCKET && item != Items.BUCKET) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int getField(int id) {
		switch (id) {
		case 0:
			return this.storage.getEnergyStored();
		default:
			return 0;
		}
	}

	@Override
	public void setField(int id, int value) {
		switch (id) {
		case 0:
			this.storage.setEnergyStored(value);
		}
	}
	
	@Override
	public int getFieldCount() {
		return 1;
	}

	@Override
	public void clear() {
		this.charge_stacks.clear();
	}

	IItemHandler handlerTop = new SidedInvWrapper(this, EnumFacing.UP);
	IItemHandler handlerBottom = new SidedInvWrapper(this, EnumFacing.DOWN);
	IItemHandler handlerSide = new SidedInvWrapper(this, EnumFacing.WEST);

	@Override
	public <T> T getCapability(Capability<T> capability, @javax.annotation.Nullable EnumFacing facing) {
		if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			if (facing == EnumFacing.DOWN)
				return (T) handlerBottom;
			else if (facing == EnumFacing.UP)
				return (T) handlerTop;
			else
				return (T) handlerSide;
		return super.getCapability(capability, facing);
	}
	
	@Override
	public int getEnergyStored(EnumFacing from) {
		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return storage.getMaxEnergyStored();
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		IBlockState state = this.world.getBlockState(this.getPos());
		
		if (from.equals(EnumFacing.DOWN)) {
			return true;
		}
		return false;
	}
	
	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		if (this.storage.getEnergyStored() < this.storage.getMaxEnergyStored()) {
			this.markDirty();
			world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);	
		}
		return storage.receiveEnergy(maxReceive, simulate);
	}
	
	@Override
	public boolean hasEnergy() {
		return this.getEnergyStored(EnumFacing.DOWN) > 0;
	}

	@Override
	public int getEnergyScaled(int scale) {
		return storage.getEnergyStored() * scale / storage.getMaxEnergyStored();
	}
	
	@Override
	public int getProcessSpeed() {
		return 0;
	}
	
	@Override
	public int getProcessTime(int i) {
		return 0;
	}

	@Override
	public int getProcessProgressScaled(int scale) {
		return 0;
	}

	@Override
	public boolean canProcess() {
		return true;
	}
	
	/**
	 * Set the data once it has been received. [NBT > TE]
	 */
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		this.storage.setEnergyStored(tag.getInteger("energy"));
		ItemStackHelper.loadAllItems(tag, this.charge_stacks);
		
		this.readFromNBT(tag);
	}
	
	/**
	 * Retrieve the data to be stored. [TE > NBT]
	 */
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = super.getUpdateTag();

		tag.setInteger("energy", this.storage.getEnergyStored());
		ItemStackHelper.saveAllItems(tag, this.charge_stacks);
		
		return tag;
	}
	
	/**
	 * Actually sends the data to the server. [NBT > SER]
	 */
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound tag = this.getUpdateTag();

		return new SPacketUpdateTileEntity(this.pos, 0, tag);
	}
	
	/**
	 * Method is called once packet has been received by the client. [SER > CLT]
	 */
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		NBTTagCompound tag = pkt.getNbtCompound();
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
		this.handleUpdateTag(tag);
	}
}