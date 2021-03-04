package com.tcn.dimensionalpocketsii.pocket.core.tileentity;

import com.tcn.cosmoslibrary.impl.colour.ChatColour;
import com.tcn.cosmoslibrary.impl.interfaces.block.IBlockInteract;
import com.tcn.cosmoslibrary.impl.interfaces.block.IBlockNotifier;
import com.tcn.cosmoslibrary.impl.util.CosmosChatUtil;
import com.tcn.cosmoslibrary.impl.util.CosmosUtil;
import com.tcn.dimensionalpocketsii.core.management.CoreDimensionManager;
import com.tcn.dimensionalpocketsii.core.management.ModBusManager;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;
import com.tcn.dimensionalpocketsii.pocket.core.util.PocketUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public abstract class TileEntityChargerBase extends TileEntity implements IBlockNotifier, IBlockInteract, IInventory, ISidedInventory, ITickableTileEntity {
	
	private NonNullList<ItemStack> INVENTORY_STACKS = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
	
	@SuppressWarnings("unused")
	private Pocket pocket;
	
	public TileEntityChargerBase(TileEntityType<?> type) {
		super(type);
	}
	
	public Pocket getPocket() {
		//if (world.isRemote) {
		//	return this.pocket;
		//}
		
		return PocketRegistryManager.getPocketFromChunk(PocketUtil.scaleToChunkPos(pos));
	}

	public void updateRenderers() {
		this.world.notifyBlockUpdate(this.getPos(), world.getBlockState(this.getPos()), world.getBlockState(this.getPos()), 3);
		this.world.markBlockRangeForRenderUpdate(this.getPos(), this.world.getBlockState(this.pos), this.world.getBlockState(this.pos));
		this.markDirty();
	}
	
	@Override
	public void tick() { }
	
	public void sendUpdates(boolean update) {
		if (update) {
			this.world.markBlockRangeForRenderUpdate(this.getPos(), this.world.getBlockState(this.pos), this.world.getBlockState(this.pos));
			this.world.notifyBlockUpdate(this.getPos(), world.getBlockState(this.getPos()), world.getBlockState(this.getPos()), 3);
		}
		
		this.markDirty();
	}
	
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		if (this.getPocket() != null) {
			this.getPocket().writeToNBT(compound);
		}
		
		return compound;
	}
	
	@Override
	public void read(BlockState state, CompoundNBT compound) {
		if (PocketUtil.hasPocketKey(compound)) {
			this.pocket = Pocket.readFromNBT(compound);
		}
	}
	
	/**
	 * Set the data once it has been received. [NBT > TE] (READ)
	 */
	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		if (PocketUtil.hasPocketKey(tag)) {
			//this.pocket = Pocket.readFromNBT(tag);
		}
	}
	
	/**
	 * Retrieve the data to be stored. [TE > NBT] (WRITE)
	 */
	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT tag = new CompoundNBT();
		
		if (this.getPocket() != null) {
			//this.getPocket().writeToNBT(tag);
		}
		
		return tag;
	}
	
	/**
	 * Actually sends the data to the server. [NBT > SER]
	 */
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.getPos(), 0, this.getUpdateTag());
	}
	
	/**
	 * Method is called once packet has been received by the client. [SER > CLT]
	 */
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		super.onDataPacket(net, pkt);
		CompoundNBT tag_ = pkt.getNbtCompound();

		BlockState state = world.getBlockState(pkt.getPos());
		
		this.handleUpdateTag(state, tag_);
		this.sendUpdates(true);
	}
	
	
	public void onLoad() { }
	
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
	
	@SuppressWarnings("unused")
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = (ItemStack) this.INVENTORY_STACKS.get(index);
		
		this.INVENTORY_STACKS.set(index, stack);
		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
		
		this.markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return true;
	}

	@Override
	public void openInventory(PlayerEntity player) { }

	@Override
	public void closeInventory(PlayerEntity player) { }

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}
	
	@Override
	public void clear() { }

	@Override
	public int[] getSlotsForFace(Direction side) {
		return new int[] { 0 };
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
		return false;
	}
	
	public void setShifterUses() {
		if (!this.getStackInSlot(0).isEmpty()) {
			ItemStack stack = this.getStackInSlot(0);
			Item item = stack.getItem();
			
			if (item.equals(ModBusManager.DIMENSIONAL_SHIFTER)) {
				if (stack.hasTag()) {
					CompoundNBT stack_tag = stack.getTag();
					
					if (stack_tag.contains("use_data")) {
						CompoundNBT use_data = stack_tag.getCompound("use_data");
						int uses = use_data.getInt("uses");
						
						if (uses < 30) {
							use_data.putInt("uses", 30);
							stack_tag.put("use_data", use_data);
						}
					}
				}
			}
		}
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit) {
		this.markDirty();
		
		if (PocketUtil.isDimensionEqual(worldIn, CoreDimensionManager.POCKET_WORLD)) {
			Pocket pocket = this.getPocket();
			
			if (pocket != null) {
				if (!playerIn.isSneaking()) {
					if (CosmosUtil.isHoldingHammer(playerIn)) {
						worldIn.setBlockState(pos, ModBusManager.BLOCK_WALL.getDefaultState());
						worldIn.removeTileEntity(pos);
					} else if (CosmosUtil.isHoldingItemMainHand(playerIn, ModBusManager.DIMENSIONAL_SHIFTER)) {
						ItemStack stack = playerIn.getHeldItem(hand);
						
						if (this.getStackInSlot(0).isEmpty()) {
							this.setInventorySlotContents(0, stack.copy());
							
							playerIn.setHeldItem(hand, ItemStack.EMPTY);
							
							this.setShifterUses();
						}

						return ActionResultType.SUCCESS;
					} else if (playerIn.getHeldItem(hand).isEmpty()) {
						ItemStack stack = this.getStackInSlot(0);
						
						playerIn.setHeldItem(hand, stack);
						
						this.setInventorySlotContents(0, ItemStack.EMPTY);
					}
					
					return ActionResultType.SUCCESS;
				} else {
					if (!CosmosUtil.isHoldingHammer(playerIn) && playerIn.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
						pocket.shift(playerIn, EnumShiftDirection.LEAVE, null, null);
						
						return ActionResultType.PASS;
					}
				}
			} else {
				CosmosChatUtil.sendPlayerMessage(playerIn, false, ChatColour.RED + "Unable to shift to complete action. Pocket is null.");
				
				return ActionResultType.FAIL;
			}
		}
		return ActionResultType.FAIL;
	}

	@Override
	public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn) { }

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) { }

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) { }

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) { }

	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) { }
	
}