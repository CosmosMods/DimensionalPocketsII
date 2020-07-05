package com.zeher.zehercraft.processing.core.tile;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.zeher.zehercraft.ZCReference;
import com.zeher.zehercraft.core.handler.SoundHandler;
import com.zeher.zehercraft.core.handler.recipe.SynthesiserRecipeHandler;
import com.zeher.zehercraft.processing.client.container.ContainerSynthesiser;
import com.zeher.zehercraft.processing.core.block.BlockSynthesiserStand;
import com.zeher.zeherlib.api.azrf.IClientUpdatedTile;
import com.zeher.zeherlib.api.client.tesr.EnumTESRColour;

import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import cofh.redstoneflux.impl.EnergyStorage;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;

public class TileEntitySynthesiser extends TileEntity implements ITickable, ISidedInventory, IInventory, IEnergyReceiver, IEnergyProvider, IClientUpdatedTile {
	
	private static final int[] ACC_SLOTS = { 0 };
	private NonNullList<ItemStack> center_stacks = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
	private String custom_name;
	
	public int process_time;
	
	public int sound_timer = 0;
	
	private EnergyStorage storage = new EnergyStorage(140000, 2000);
	
	public void update() {
		if (this.canProcessItemEight()) {
			this.sound_timer++;
			
			if (this.sound_timer > 13) {
				this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundHandler.MACHINE.LASERHUM, SoundCategory.BLOCKS, 1F, 1F, false);
				
				this.sound_timer = 0;
			}
			
			this.process_time++;
			
			if (this.process_time == this.getProcessTimeEight().intValue()) {
				this.process_time = 0;
				this.processItemEight();
				this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE,this.pos.getX(), this.pos.getY() + 0.5D, this.pos.getZ(), 0.0D, 0.0D, 0.0D, new int[0]);
			}
		} else if (this.canProcessItemFour()) {
			this.sound_timer++;
			
			if (this.sound_timer > 13) {
				this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundHandler.MACHINE.LASERHUM, SoundCategory.BLOCKS, 1F, 1F, false);
				
				this.sound_timer = 0;
			}
			
			this.process_time++;
			
			if (this.process_time == this.getProcessTimeFour().intValue()) {
				this.process_time = 0;
				this.processItemFour();
				this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE,this.pos.getX(), this.pos.getY() + 0.5D, this.pos.getZ(), 0.0D, 0.0D, 0.0D, new int[0]);
			}
		} else {
			this.sound_timer = 0;
			this.process_time = 0;
		}
		
		if (this.process_time > 0) {
			this.markDirty();
		}
	}

	public int getSizeInventory() {
		return this.center_stacks.size();
	}
	
	public void processItemFour() {
		if (this.isSetupFour() && this.canProcessItemFour()) {
			ArrayList<TileEntity> tiles = this.getTilesFour();
			
			if (!(tiles.isEmpty())) {
				ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
				List<ItemStack> list = Lists.newArrayList();
				ItemStack output_stack = this.getStackInSlot(0);
				
				for (int x = 0; x < tiles.size(); x++) {
					if (tiles.get(x) instanceof TileEntitySynthesiserStand) {
						stacks.add(((TileEntitySynthesiserStand) tiles.get(x)).getStackInSlot(0));
					}
				}
				
				for (int j = 0; j < stacks.size(); j++) {
					if (!(stacks.get(j).isEmpty())) {
						list.add(stacks.get(j));
					}
				}
				
				ItemStack result_stack = SynthesiserRecipeHandler.getInstance().findMatchingRecipe(list);
				if (!(result_stack.isEmpty()) && !(output_stack.isEmpty())) {
					if (output_stack.isItemEqual(SynthesiserRecipeHandler.getInstance().findFocusStack(list))) {
						output_stack.shrink(1);
						
						this.setInventorySlotContents(0, result_stack.copy());
						
						for (int i = 0; i < stacks.size(); i++) {
							stacks.get(i).shrink(1);
						}
					}
				}
			}
		}
	}
	
	public void processItemEight() {
		if (this.isSetupEight() && this.canProcessItemEight()) {
			ArrayList<TileEntity> tiles = this.getTilesEight();
			
			if (!(tiles.isEmpty())) {
				ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
				List<ItemStack> list = Lists.newArrayList();
				ItemStack output_stack = this.getStackInSlot(0);
				
				for (int x = 0; x < tiles.size(); x++) {
					if (tiles.get(x) instanceof TileEntitySynthesiserStand) {
						stacks.add(((TileEntitySynthesiserStand) tiles.get(x)).getStackInSlot(0));
					}
				}
				
				for (int j = 0; j < stacks.size(); j++) {
					if (!(stacks.get(j).isEmpty())) {
						list.add(stacks.get(j));
					}
				}
				
				ItemStack result_stack = SynthesiserRecipeHandler.getInstance().findMatchingRecipe(list);
				if (!(result_stack.isEmpty()) && !(output_stack.isEmpty())) {
					if (output_stack.isItemEqual(SynthesiserRecipeHandler.getInstance().findFocusStack(list))) {
						output_stack.shrink(1);
						
						this.setInventorySlotContents(0, result_stack.copy());
						
						for (int i = 0; i < stacks.size(); i++) {
							stacks.get(i).shrink(1);
						}
					}
				}
			}
		}
	}
	
	public boolean canProcessItemFour() {
		if (this.isSetupFour()) {
			ArrayList<TileEntity> tiles = this.getTilesFour();
			
			if (!tiles.isEmpty()) {
				ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
				List<ItemStack> list = Lists.newArrayList();
				ItemStack output_stack = this.getStackInSlot(0);
				
				for (int x = 0; x < tiles.size(); x++) {
					if (tiles.get(x) instanceof TileEntitySynthesiserStand) {
						stacks.add(((TileEntitySynthesiserStand) tiles.get(x)).getStackInSlot(0));
					}
				}
				
				for (int j = 0; j < stacks.size(); j++) {
					if (!(stacks.get(j).isEmpty())) {
						list.add(stacks.get(j));
					}
				}
				
				ItemStack result_stack = SynthesiserRecipeHandler.getInstance().findMatchingRecipe(list);
				if (!(result_stack.isEmpty()) && !(output_stack.isEmpty())) {
					if (output_stack.isItemEqual(SynthesiserRecipeHandler.getInstance().findFocusStack(list))) {
						return true;
					}
				}
				return false;
			}	
		}
		return false;
	}
	
	public boolean canProcessItemEight() {
		if (this.isSetupEight()) {
			ArrayList<TileEntity> tiles = this.getTilesEight();
			
			if (!tiles.isEmpty()) {
				ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
				List<ItemStack> list = Lists.newArrayList();
				ItemStack output_stack = this.getStackInSlot(0);
				
				for (int x = 0; x < tiles.size(); x++) {
					if (tiles.get(x) instanceof TileEntitySynthesiserStand) {
						stacks.add(((TileEntitySynthesiserStand) tiles.get(x)).getStackInSlot(0));
					}
				}
				
				for (int j = 0; j < stacks.size(); j++) {
					if (!(stacks.get(j).isEmpty())) {
						list.add(stacks.get(j));
					}
				}
				
				ItemStack result_stack = SynthesiserRecipeHandler.getInstance().findMatchingRecipe(list);
				if (!(result_stack.isEmpty()) && !(output_stack.isEmpty())) {
					if (output_stack.isItemEqual(SynthesiserRecipeHandler.getInstance().findFocusStack(list))) {
						return true;
					}
				}
				return false;
			}	
		}
		return false;
	}
	
	public Integer getProcessTimeFour() {
		if (this.isSetupFour()) {
			ArrayList<TileEntity> tiles = this.getTilesFour();
			
			if (!tiles.isEmpty()) {
				ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
				List<ItemStack> list = Lists.newArrayList();
				ItemStack output_stack = this.getStackInSlot(0);
				
				for (int x = 0; x < tiles.size(); x++) {
					if (tiles.get(x) instanceof TileEntitySynthesiserStand) {
						stacks.add(((TileEntitySynthesiserStand) tiles.get(x)).getStackInSlot(0));
					}
				}
				
				for (int j = 0; j < stacks.size(); j++) {
					if (!(stacks.get(j).isEmpty())) {
						list.add(stacks.get(j));
					}
				}
				
				ItemStack result_stack = SynthesiserRecipeHandler.getInstance().findMatchingRecipe(list);
				if (!(result_stack.isEmpty()) && !(output_stack.isEmpty())) {
					if (output_stack.isItemEqual(SynthesiserRecipeHandler.getInstance().findFocusStack(list))) {
						return SynthesiserRecipeHandler.getInstance().findProcessTime(list);
					}
				}
				return 0;
			}
		}
		return 0;
	}
	
	public Integer getProcessTimeEight() {
		if (this.isSetupEight()) {
			ArrayList<TileEntity> tiles = this.getTilesEight();
			
			if (!tiles.isEmpty()) {
				ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
				List<ItemStack> list = Lists.newArrayList();
				ItemStack output_stack = this.getStackInSlot(0);
				
				for (int x = 0; x < tiles.size(); x++) {
					if (tiles.get(x) instanceof TileEntitySynthesiserStand) {
						stacks.add(((TileEntitySynthesiserStand) tiles.get(x)).getStackInSlot(0));
					}
				}
				
				for (int j = 0; j < stacks.size(); j++) {
					if (!(stacks.get(j).isEmpty())) {
						list.add(stacks.get(j));
					}
				}
				
				ItemStack result_stack = SynthesiserRecipeHandler.getInstance().findMatchingRecipe(list);
				if (!(result_stack.isEmpty()) && !(output_stack.isEmpty())) {
					if (output_stack.isItemEqual(SynthesiserRecipeHandler.getInstance().findFocusStack(list))) {
						return SynthesiserRecipeHandler.getInstance().findProcessTime(list);
					}
				}
				return 0;
			}
		}
		return 0;
	}
	
	public boolean isSetupFour() {
		ArrayList<TileEntity> tiles = this.getTilesFour();
		ArrayList<Block> blocks = this.getBlocksFour();
		
		if (tiles.get(0) instanceof TileEntitySynthesiserStand && tiles.get(1) instanceof TileEntitySynthesiserStand &&
				tiles.get(2) instanceof TileEntitySynthesiserStand && tiles.get(3) instanceof TileEntitySynthesiserStand) {
			if (blocks.get(0) instanceof BlockSynthesiserStand && blocks.get(1) instanceof BlockSynthesiserStand &&
					blocks.get(2) instanceof BlockSynthesiserStand && blocks.get(3) instanceof BlockSynthesiserStand) {
				return true;
			}
			return false;
		}
		return false;
	}
	
	public boolean isSetupEight() {
		ArrayList<TileEntity> tiles = this.getTilesEight();
		ArrayList<Block> blocks = this.getBlocksEight();
		
		if (tiles.get(0) instanceof TileEntitySynthesiserStand && tiles.get(1) instanceof TileEntitySynthesiserStand &&
				tiles.get(2) instanceof TileEntitySynthesiserStand && tiles.get(3) instanceof TileEntitySynthesiserStand &&
					tiles.get(4) instanceof TileEntitySynthesiserStand && tiles.get(5) instanceof TileEntitySynthesiserStand &&
						tiles.get(6) instanceof TileEntitySynthesiserStand && tiles.get(7) instanceof TileEntitySynthesiserStand) {
			if (blocks.get(0) instanceof BlockSynthesiserStand && blocks.get(1) instanceof BlockSynthesiserStand &&
					blocks.get(2) instanceof BlockSynthesiserStand && blocks.get(3) instanceof BlockSynthesiserStand && 
						blocks.get(4) instanceof BlockSynthesiserStand && blocks.get(5) instanceof BlockSynthesiserStand &&
							blocks.get(6) instanceof BlockSynthesiserStand && blocks.get(7) instanceof BlockSynthesiserStand) {
				return true;
			}
			return false;
		}
		return false;
	}
	
	public ArrayList<TileEntity> getTilesFour() {
		ArrayList<TileEntity> tiles = new ArrayList<TileEntity>();
		
		for (EnumFacing c : EnumFacing.VALUES) {
			if (c != EnumFacing.UP && c != EnumFacing.DOWN) {
				tiles.add(this.world.getTileEntity(pos.offset(c, 3)));
			}
		}
		return tiles;
	}
	
	public ArrayList<Block> getBlocksFour() {
		ArrayList<Block> blocks = new ArrayList<Block>();
		
		for (EnumFacing c : EnumFacing.VALUES) {
			if (c != EnumFacing.UP && c != EnumFacing.DOWN) {
				blocks.add(this.world.getBlockState(pos.offset(c, 3)).getBlock());
			}
		}
		return blocks;
	}
	
	public ArrayList<TileEntity> getTilesEight() {
		ArrayList<TileEntity> tiles = new ArrayList<TileEntity>();
		
		for (EnumFacing c : EnumFacing.VALUES) {
			if (c != EnumFacing.UP && c != EnumFacing.DOWN) {
				tiles.add(this.world.getTileEntity(pos.offset(c, 3)));
				
				if (c.equals(EnumFacing.NORTH) || c.equals(EnumFacing.SOUTH)) {
					tiles.add(this.world.getTileEntity(pos.offset(c, 2).offset(EnumFacing.WEST, 2)));
					tiles.add(this.world.getTileEntity(pos.offset(c, 2).offset(EnumFacing.EAST, 2)));
				}
			}
		}
		return tiles;
	}
	
	public ArrayList<Block> getBlocksEight() {
		ArrayList<Block> blocks = new ArrayList<Block>();
		
		for (EnumFacing c : EnumFacing.VALUES) {
			if (c != EnumFacing.UP && c != EnumFacing.DOWN) {
				blocks.add(this.world.getBlockState(pos.offset(c, 3)).getBlock());
				
				if (c.equals(EnumFacing.NORTH) || c.equals(EnumFacing.SOUTH)) {
					blocks.add(this.world.getBlockState(pos.offset(c, 2).offset(EnumFacing.WEST, 2)).getBlock());
					blocks.add(this.world.getBlockState(pos.offset(c, 2).offset(EnumFacing.EAST, 2)).getBlock());
				}
			}
		}
		return blocks;
	}
	
	public EnumTESRColour getColour() {
		if (this.isSetupEight()) {
			ArrayList<TileEntity> tiles = this.getTilesEight();
			
			if (!tiles.isEmpty()) {
				ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
				List<ItemStack> list = Lists.newArrayList();
				ItemStack output_stack = this.getStackInSlot(0);
				
				for (int x = 0; x < tiles.size(); x++) {
					if (tiles.get(x) instanceof TileEntitySynthesiserStand) {
						stacks.add(((TileEntitySynthesiserStand) tiles.get(x)).getStackInSlot(0));
					}
				}
				
				for (int j = 0; j < stacks.size(); j++) {
					if (!(stacks.get(j).isEmpty())) {
						list.add(stacks.get(j));
					}
				}
				
				ItemStack result_stack = SynthesiserRecipeHandler.getInstance().findMatchingRecipe(list);
				if (!(result_stack.isEmpty()) && !(output_stack.isEmpty())) {
					if (output_stack.isItemEqual(SynthesiserRecipeHandler.getInstance().findFocusStack(list))) {
						return SynthesiserRecipeHandler.getInstance().findColour(list);
					}
				}
				return EnumTESRColour.WHITE;
			}
		} else if (this.isSetupFour()) {
			ArrayList<TileEntity> tiles = this.getTilesFour();
			
			if (!tiles.isEmpty()) {
				ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
				List<ItemStack> list = Lists.newArrayList();
				ItemStack output_stack = this.getStackInSlot(0);
				
				for (int x = 0; x < tiles.size(); x++) {
					if (tiles.get(x) instanceof TileEntitySynthesiserStand) {
						stacks.add(((TileEntitySynthesiserStand) tiles.get(x)).getStackInSlot(0));
					}
				}
				
				for (int j = 0; j < stacks.size(); j++) {
					if (!(stacks.get(j).isEmpty())) {
						list.add(stacks.get(j));
					}
				}
				
				ItemStack result_stack = SynthesiserRecipeHandler.getInstance().findMatchingRecipe(list);
				if (!(result_stack.isEmpty()) && !(output_stack.isEmpty())) {
					if (output_stack.isItemEqual(SynthesiserRecipeHandler.getInstance().findFocusStack(list))) {
						return SynthesiserRecipeHandler.getInstance().findColour(list);
					}
				}
				return EnumTESRColour.WHITE;
			}
		}
		return EnumTESRColour.WHITE;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.center_stacks) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = (ItemStack) this.center_stacks.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.center_stacks.set(index, stack);

		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
		if (index == 0 && !flag) {
			this.process_time = 0;
			this.markDirty();
		}
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.center_stacks.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.center_stacks, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.center_stacks, index);
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
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
	public int getField(int id) {
		switch (id) {
			case 0:
				return this.storage.getEnergyStored();
		}
		return 0;
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
		this.center_stacks.clear();
	}

	@Override
	public String getName() {
		return this.hasCustomName() ? this.custom_name : ZCReference.RESOURCE.PROCESSING.SYNTHESISER_NAME;
	}

	@Override
	public boolean hasCustomName() {
		return this.custom_name != null && !this.custom_name.isEmpty();
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return ACC_SLOTS;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		if (!this.center_stacks.get(0).isEmpty()) {
			return this.isItemValidForSlot(0, itemStackIn);
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return true;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		//ItemStackHelper.loadAllItems(compound, this.center_stacks);
		
		//this.storage = storage.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		//ItemStackHelper.saveAllItems(compound, this.center_stacks);
		
		//this.storage.writeToNBT(compound);

		return compound;
	}

	@Override
	public int getEnergyStored(EnumFacing from) {
		return this.storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return this.storage.getMaxEnergyStored();
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		if (from.equals(EnumFacing.DOWN)) {
			return true;
		}
		return false;
	}

	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		return this.storage.extractEnergy(maxExtract, simulate);
	}
	
	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		if (this.storage.getEnergyStored() < this.storage.getMaxEnergyStored()) {
			this.markDirty();
			world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);	
		}
		return this.storage.receiveEnergy(maxReceive, simulate);
	}
	
	@Override
	public int getEnergyScaled(int scale) {
		return storage.getEnergyStored() * scale / storage.getMaxEnergyStored();
	}
	
	@Override
	public boolean hasEnergy() {
		return this.storage.getEnergyStored() > 0;
	}
	
	/**
	 * Set the data once it has been received. [NBT > TE]
	 */
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		this.storage.setEnergyStored(tag.getInteger("energy"));
		ItemStackHelper.loadAllItems(tag, this.center_stacks);
		
		this.readFromNBT(tag);
	}
	
	/**
	 * Retrieve the data to be stored. [TE > NBT]
	 */
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = super.getUpdateTag();

		tag.setInteger("energy", this.storage.getEnergyStored());
		ItemStackHelper.saveAllItems(tag, this.center_stacks);
		
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

	@Override
	public int getProcessSpeed() {
		return 0;
	}

	@Override
	public int getProcessTime(int i) {
		return this.process_time;
	}

	@Override
	public int getProcessProgressScaled(int scale) {
		return 0;
	}

	@Override
	public boolean canProcess() {
		return true;
	}
}