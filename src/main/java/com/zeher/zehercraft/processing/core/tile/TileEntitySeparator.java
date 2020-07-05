package com.zeher.zehercraft.processing.core.tile;

import java.util.Random;

import com.zeher.zehercraft.ZCReference;
import com.zeher.zehercraft.processing.client.container.ContainerSeparator;
import com.zeher.zehercraft.processing.core.block.BlockSeparator;
import com.zeher.zeherlib.api.azrf.IClientUpdatedTile;
import com.zeher.zeherlib.core.recipe.SeparatorRecipes;

import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import cofh.redstoneflux.impl.EnergyStorage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public class TileEntitySeparator extends TileEntity implements ITickable, ISidedInventory, IEnergyReceiver, IEnergyProvider, IClientUpdatedTile {
	
	private static final int[] SLOTS_TOP = new int[] { 0 };
	private static final int[] SLOTS_BOTTOM = new int[] { 2, 1 };
	private static final int[] SLOTS_SIDES = new int[] { 1 };
	
	private NonNullList<ItemStack> INVENTORY_STACKS = NonNullList.<ItemStack>withSize(6, ItemStack.EMPTY);

	private int process_time;
	private int process_speed = ZCReference.RESOURCE.PROCESSING.SPEED_RATE[0];
	private String custom_name;
	
	public int capacity_internal = ZCReference.RESOURCE.PROCESSING.CAPACITY[0];
	public int max_input = ZCReference.RESOURCE.PROCESSING.MAX_INPUT[0];
	
	public EnergyStorage storage = new EnergyStorage(capacity_internal, max_input);

	@Override
	public int getSizeInventory() {
		return this.INVENTORY_STACKS.size();
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
	public ItemStack getStackInSlot(int index) {
		return (ItemStack) this.INVENTORY_STACKS.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.INVENTORY_STACKS, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.INVENTORY_STACKS, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = (ItemStack) this.INVENTORY_STACKS.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.INVENTORY_STACKS.set(index, stack);

		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}

		if (index == 0 && !flag) {
			this.process_time = 0;
			this.markDirty();
		}
	}

	@Override
	public String getName() {
		return ZCReference.RESOURCE.PROCESSING.SEPARATOR_NAME;
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public void update() {
		if (this.canProcess() && this.hasEnergy()) {
			
			int x = this.INVENTORY_STACKS.get(4).getCount();
			int rf_tick = ZCReference.RESOURCE.PROCESSING.RF_TICK_RATE[x];
			
			this.extractEnergy(EnumFacing.DOWN, rf_tick, false);
			
			this.process_time++;
			
			if (this.process_time == this.process_speed) {
				this.process_time = 0;
				if (!this.world.isRemote) {
					this.processItem();
				}
			}
			
		} else {
			this.process_time = 0;
		}
		
		if (this.process_time > 0) {
			this.markDirty();
		}
		
		if (this.canProcess() && this.hasEnergy()) {
			Random rand = new Random();
			
			if (rand.nextDouble() < 0.1D) {
				this.world.playSound(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}
			
			this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.pos.getX() + 0.25, this.pos.getY() + 0.4, this.pos.getZ() + 0.25, 0.0F, 0.0F, 0.0F, new int[0]);
			this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.pos.getX() + 0.25, this.pos.getY() + 0.4, this.pos.getZ() + 0.75, 0.0F, 0.0F, 0.0F, new int[0]);
			this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.pos.getX() + 0.75, this.pos.getY() + 0.4, this.pos.getZ() + 0.25, 0.0F, 0.0F, 0.0F, new int[0]);
			this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.pos.getX() + 0.75, this.pos.getY() + 0.4, this.pos.getZ() + 0.75, 0.0F, 0.0F, 0.0F, new int[0]);
			
			this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.pos.getX() + 0.5 , this.pos.getY() + 0.4, this.pos.getZ() + 0.5 , 0.0F, 0.0F, 0.0F, new int[0]);
		}
		
		int i = this.INVENTORY_STACKS.get(4).getCount();
		this.process_speed = ZCReference.RESOURCE.PROCESSING.SPEED_RATE[i];
		
		int j = this.INVENTORY_STACKS.get(5).getCount();
		this.capacity_internal = ZCReference.RESOURCE.PROCESSING.CAPACITY[j];
	}
	
	@Override
	public boolean canProcess() {
		if (this.INVENTORY_STACKS.get(0).isEmpty()) {
			return false;
		} else {
			ItemStack recipeout = SeparatorRecipes.getInstance().getSeparatingResult(this.INVENTORY_STACKS.get(0));
			ItemStack secondaryout = SeparatorRecipes.getInstance().getSecondaryOutput(this.INVENTORY_STACKS.get(0));
			
			if (recipeout.isEmpty() && secondaryout.isEmpty()) {
				return false;
			} else {
				ItemStack output = this.INVENTORY_STACKS.get(1);
				ItemStack second = this.INVENTORY_STACKS.get(2);
				
				if (output.isEmpty()) {
					return true; 
				} if (!output.isItemEqual(recipeout)) {
					return false;
				} if (second.isEmpty()) {
					return true;
				} if (!second.isItemEqual(secondaryout)) {
					return false;
				}
				
				int result = output.getCount() + recipeout.getCount();
				int result2 = second.getCount() + secondaryout.getCount();
				return result <= getInventoryStackLimit() && result <= output.getMaxStackSize() && result2 <= this.getInventoryStackLimit() && result2 <= second.getMaxStackSize();
			}
		}
	}

	public void processItem() {
		if (this.canProcess()) {
			ItemStack input = this.INVENTORY_STACKS.get(0);
			ItemStack output = this.INVENTORY_STACKS.get(1);
			ItemStack second = this.INVENTORY_STACKS.get(2);
			
			ItemStack recipeout = SeparatorRecipes.getInstance().getSeparatingResult(input);
			ItemStack secondaryout = SeparatorRecipes.getInstance().getSecondaryOutput(input);

			if (output.isEmpty()) {
				this.INVENTORY_STACKS.set(1, recipeout.copy());
			} else if (output.getItem() == recipeout.getItem()) {
				output.grow(recipeout.getCount());
			} if (second.isEmpty()) {
				this.INVENTORY_STACKS.set(2, secondaryout.copy());
			} else if (second.getItem().equals(secondaryout.getItem())) {
				second.grow(secondaryout.getCount());
			} if (input.getItem() == Item.getItemFromBlock(Blocks.SPONGE) && input.getMetadata() == 1 && !(this.INVENTORY_STACKS.get(1)).isEmpty() && (this.INVENTORY_STACKS.get(1)).getItem() == Items.BUCKET) {
				this.INVENTORY_STACKS.set(1, new ItemStack(Items.WATER_BUCKET));
			}
			input.shrink(1);
		}
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.world.getTileEntity(this.pos) != this ? false : player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
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
			return this.process_time;
		case 1:
			return this.storage.getEnergyStored();
		default:
			return 0;
		}
	}

	@Override
	public void setField(int id, int value) {
		switch (id) {
		case 0:
			this.process_time = value;
			break;
		case 1:
			this.storage.setEnergyStored(value);
			break;
		}
	}

	@Override
	public int getFieldCount() {
		return 2;
	}

	@Override
	public void clear() {
		this.INVENTORY_STACKS.clear();
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
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.INVENTORY_STACKS = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
		
		ItemStackHelper.loadAllItems(compound, this.INVENTORY_STACKS);
		
		this.process_time = compound.getInteger("process_time");

		if (compound.hasKey("CustomName", 8)) {
			this.custom_name = compound.getString("CustomName");
		}
		
		storage.readFromNBT(compound);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		compound.setInteger("process_time", (short) this.process_time);
		
		ItemStackHelper.saveAllItems(compound, this.INVENTORY_STACKS);

		if (this.hasCustomName()) {
			compound.setString("CustomName", this.custom_name);
		}
		
		storage.writeToNBT(compound);

		return compound;
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
		
		if (state.getBlock() instanceof BlockSeparator) {
			EnumFacing facing = state.getValue(BlockSeparator.FACING);
			if (from.equals(facing)) {
				return false;
			}
		}
		
		if (from.equals(EnumFacing.UP)) {
			return false;
		}
		return true;
	}

	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		return storage.extractEnergy(maxExtract, simulate);
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
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
		return this.process_speed;
	}
	
	@Override
	public int getProcessTime(int i) {
		if (i == 0) {
			return this.process_time;
		}
		return -1;
	}
	
	@Override
	public int getProcessProgressScaled(int scale) {
		return this.process_time * scale / this.process_speed;
	}
}