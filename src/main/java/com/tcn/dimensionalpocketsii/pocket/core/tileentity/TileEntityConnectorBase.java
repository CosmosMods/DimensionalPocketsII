package com.tcn.dimensionalpocketsii.pocket.core.tileentity;

import com.tcn.cosmoslibrary.client.impl.IClientUpdatedTile;
import com.tcn.cosmoslibrary.impl.colour.ChatColour;
import com.tcn.cosmoslibrary.impl.colour.EnumMinecraftColour;
import com.tcn.cosmoslibrary.impl.enums.EnumConnectionType;
import com.tcn.cosmoslibrary.impl.enums.EnumSideState;
import com.tcn.cosmoslibrary.impl.interfaces.IFluidStorage;
import com.tcn.cosmoslibrary.impl.interfaces.block.IBlockInteract;
import com.tcn.cosmoslibrary.impl.interfaces.block.IBlockNotifier;
import com.tcn.cosmoslibrary.impl.interfaces.tile.IConnectionTypeTile;
import com.tcn.cosmoslibrary.impl.interfaces.tile.ISidedTile;
import com.tcn.cosmoslibrary.impl.util.CosmosChatUtil;
import com.tcn.cosmoslibrary.impl.util.CosmosUtil;
import com.tcn.dimensionalpocketsii.DimReference;
import com.tcn.dimensionalpocketsii.core.management.CoreDimensionManager;
import com.tcn.dimensionalpocketsii.core.management.ModBusManager;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;
import com.tcn.dimensionalpocketsii.pocket.core.util.PocketUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public abstract class TileEntityConnectorBase extends TileEntity implements IBlockNotifier, IBlockInteract, IInventory, ISidedInventory, ITickableTileEntity, ISidedTile, IFluidHandler, IFluidStorage, IClientUpdatedTile.FluidTile, IConnectionTypeTile {
	
	private EnumSideState[] SIDE_STATE_ARRAY = EnumSideState.getStandardArray();
	private EnumConnectionType TYPE = EnumConnectionType.getStandardValue();
	
	private NonNullList<ItemStack> INVENTORY_STACKS = NonNullList.<ItemStack>withSize(56, ItemStack.EMPTY);
	
	@SuppressWarnings("unused")
	private Pocket pocket;
	
	public TileEntityConnectorBase(TileEntityType<?> type) {
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
		
		//FMLCommonHandler.instance().getMinecraftServerInstance().saveAllWorlds(false);
	}
	
	@Override
	public void tick() {
		//TileEntity tile = this.world.getTileEntity(this.getPos());
		/**
		if (PocketUtil.isDimensionEqual(this.world, CoreDimensionManager.POCKET_WORLD)) {
			for (Direction c : Direction.values()) {
				//TileEntity tile_other = this.world.getTileEntity(this.getPos().offset(c));
				
				/*
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
				}*/
			/*}
			
			this.setSurroundingStacks();
			this.setStacks();
			this.checkFluidSlots();
		}*/
	}
	
	@Override
	public EnumConnectionType getConnectionType() {
		return this.TYPE;
	}

	@Override
	public void setConnectionType(EnumConnectionType type, boolean update) {
		this.TYPE = type;
		
		this.sendUpdates(update);
	}
	
	@Override
	public void cycleConnectionType(boolean update) {
		EnumConnectionType next_state = TYPE.getNextState();
		this.TYPE = next_state;
		
		this.sendUpdates(update);
	}
	
	@Override 
	public EnumSideState getSide(Direction facing) {
		return SIDE_STATE_ARRAY[facing.getIndex()];
	}
	
	@Override
	public void setSide(Direction facing, EnumSideState side_state, boolean update) {
		for (int i = 0; i < this.SIDE_STATE_ARRAY.length; i++) {
			SIDE_STATE_ARRAY[i] = side_state;
		}
		this.sendUpdates(update);
	}
	
	@Override
	public EnumSideState[] getSideArray() {
		return this.SIDE_STATE_ARRAY;
	}

	@Override
	public void setSideArray(EnumSideState[] new_array, boolean update) {
		SIDE_STATE_ARRAY = new_array;
		
		this.sendUpdates(update);
	}

	@Override
	public void cycleSide(Direction facing, boolean update) {
		EnumSideState state = SIDE_STATE_ARRAY[facing.getIndex()];
		
		EnumSideState state2 = state.getNextState();
		
		for (int i = 0; i < this.SIDE_STATE_ARRAY.length; i++) {
			SIDE_STATE_ARRAY[i] = state2;
		}
		
		this.sendUpdates(update);
	}

	@Override
	public boolean canConnect(Direction facing) {
		EnumSideState state = SIDE_STATE_ARRAY[facing.getIndex()];
		
		if (state.equals(EnumSideState.DISABLED)) {
			return false;
		}
		return true;
	}

	@Override
	public void sendUpdates(boolean update) {
		if (update) {
			this.world.markBlockRangeForRenderUpdate(this.getPos(), this.world.getBlockState(this.pos), this.world.getBlockState(this.pos));
			this.world.notifyBlockUpdate(this.getPos(), world.getBlockState(this.getPos()), world.getBlockState(this.getPos()), 3);
			//this.world.scheduleBlockUpdate(this.getPos(), this.getBlockType(), 0, 0);
		}
		
		this.markDirty();
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

	public int fill(FluidStack resource, FluidAction doFill) {
		this.markDirty();
		return this.getPocket().fill(resource, doFill);
	}

	public FluidStack drain(FluidStack resource, FluidAction doDrain) {
		this.markDirty();
		return this.getPocket().drain(resource.getAmount(), doDrain);
	}

	@Override
	public FluidStack drain(int maxDrain, FluidAction doDrain) {
		this.markDirty();
		return this.getPocket().drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(Direction from, Fluid fluid) {
		return true;
	}

	@Override
	public boolean canDrain(Direction from, Fluid fluid) {
		return true;
	}
	
	@Override
	public int getTanks() {
		return 1;
	}

	@Override
	public FluidStack getFluidInTank(int tank) {
		// TODO Auto-generated method stub
		return this.getPocket().getFluidTank().getFluidInTank(tank);
	}

	@Override
	public int getTankCapacity(int tank) {
		return this.getPocket().getFluidTankCapacity();
	}

	@Override
	public boolean isFluidValid(int tank, FluidStack stack) {
		return true;
	}

	@Override
	public FluidTank getTank() {
		return this.getPocket().getFluidTank();
	}

	@Override
	public int getFluidCapacity() {
		return this.getPocket().getFluidTankCapacity();
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		
		if (this.getPocket() != null) {
			this.getPocket().writeToNBT(compound);
		}
		
		return compound;
	}
	
	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		
		if (PocketUtil.hasPocketKey(compound)) {
			this.pocket = Pocket.readFromNBT(compound);
		}
	}
	
	/**
	 * Set the data once it has been received. [NBT > TE] (READ)
	 */
	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		this.read(state, tag);
	}
	
	/**
	 * Retrieve the data to be stored. [TE > NBT] (WRITE)
	 */
	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT tag = new CompoundNBT();
		
		this.write(tag);
		
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
		//this.sendUpdates(true);
	}
	
	
	public void onLoad() { }
	
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
	public void setLockState(boolean change, boolean update) {
		this.getPocket().setLockState(change);
		this.sendUpdates(update);
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
		
		if (index < DimReference.CONSTANT.POCKET_HELD_ITEMS_SIZE) {
			if (this.getPocket() != null) {
				this.getPocket().item_array.set(index, stack);
			}
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

	public void checkFluidSlots() {
		if (!this.world.isRemote) {
			if (!this.INVENTORY_STACKS.get(54).isEmpty()) {
				if (this.INVENTORY_STACKS.get(54).getItem().equals(Items.BUCKET)) {
					if (this.getCurrentFluidAmount() > 0) {
						ItemStack fillStack = FluidUtil.tryFillContainer(this.INVENTORY_STACKS.get(54), this.getTank(), 1000, null, false).result;
						if (this.INVENTORY_STACKS.get(55).isEmpty()) {
							if (fillStack != null) {
								this.drain(FluidUtil.getFluidContained(fillStack).get().getAmount(), FluidAction.EXECUTE);
								this.INVENTORY_STACKS.get(54).shrink(1);
								this.INVENTORY_STACKS.set(55, fillStack);
							}
						}
					}
				}
				
				FluidStack fluid = FluidUtil.getFluidContained(this.INVENTORY_STACKS.get(54)).get();
				
				if (fluid != null) {
					int amount = this.fill(fluid, FluidAction.SIMULATE);
					if (amount == fluid.getAmount()) {
						if (this.INVENTORY_STACKS.get(55).getItem().equals(Items.BUCKET) && this.INVENTORY_STACKS.get(55).getCount() < 17) {
							this.fill(fluid, FluidAction.EXECUTE);
							this.INVENTORY_STACKS.get(54).shrink(1);
							this.INVENTORY_STACKS.get(55).grow(1);
						}
						
						if (this.INVENTORY_STACKS.get(55).isEmpty()) {
							this.fill(fluid, FluidAction.EXECUTE);
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
		Pocket pocket = this.getPocket();
		
		if (pocket != null) {
			for (int i = 0 ; i < pocket.item_array.size(); i++) {
				if (this.getStackInSlot(i) != pocket.item_array.get(i)) {
					ItemStack pocket_stack = pocket.item_array.get(i);
					ItemStack copy = pocket_stack.copy();
					if (!this.world.isRemote) {
						this.INVENTORY_STACKS.set(i, copy);
					}
				}
			}
		}
	}
	
	//@SideOnly(Side.CLIENT)
	public void setSurroundingStacks() {
		Pocket pocket = this.getPocket();
		
		if(pocket != null) {
			//RegistryKey<World> dim = pocket.getSourceBlockDimension();
			World pocket_world = this.getPocket().getSourceBlockWorld();
			BlockPos pocket_pos = this.getPocket().getLastBlockPos();
			
			//if (pocket_pos.getY() >= 0 && pocket_pos.getY() <= 256) {
				if (pocket_world != null) {
					TileEntity tile_pocket = pocket_world.getTileEntity(pocket_pos);
					
					if (tile_pocket != null && tile_pocket instanceof TileEntityPocket) {
						for (Direction c : Direction.values()) {
							Block block_other = pocket_world.getBlockState(pocket_pos.offset(c)).getBlock();
							this.INVENTORY_STACKS.set(c.getIndex() + DimReference.CONSTANT.POCKET_HELD_ITEMS_SIZE, new ItemStack(block_other));
						}
					}
				}
			//}
		}
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit) {
		this.markDirty();
		
		if (PocketUtil.isDimensionEqual(worldIn, CoreDimensionManager.POCKET_WORLD)) {
			Pocket pocketIn = this.getPocket();
			
			if (pocketIn != null) {
				if (!playerIn.isSneaking()) {
					if (CosmosUtil.isHoldingHammer(playerIn)) {
						this.cycleSide(Direction.UP, true);
						
						CosmosChatUtil.sendPlayerMessage(playerIn, true, ChatColour.LIGHT_GRAY + ChatColour.BOLD + I18n.format("pocket.status.cycle_side.name") + this.getSide(hit.getFace()).getTextColour() + ChatColour.BOLD + " [" + this.getSide(hit.getFace()).getDisplayName() + "]");
						return ActionResultType.SUCCESS;
					}
					
					else if (playerIn.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
						if (this.getPocket().checkIfOwner(playerIn)) {
							//if (!(worldIn.isRemote)) {
								//FMLNetworkHandler.openGui(playerIn, DimensionalPockets.INSTANCE, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());
							//}
							
							return ActionResultType.SUCCESS;
						} else {
							CosmosChatUtil.sendPlayerMessage(playerIn, true,ChatColour.RED + ChatColour.BOLD + I18n.format("pocket.status.access_set.name"));
							
							return ActionResultType.FAIL;
						}
					}
					
					else if (!playerIn.getHeldItem(Hand.MAIN_HAND).isEmpty()) {						
						ItemStack stack = playerIn.getHeldItem(Hand.MAIN_HAND);
						
						if (!stack.getItem().equals(ModBusManager.DIMENSIONAL_SHARD)) {
							DyeColor stack_color = DyeColor.getColor(stack);
							
							if (stack_color != null) {
								int id = stack_color.getId();
								
								EnumMinecraftColour colour = EnumMinecraftColour.byIndex(id);
								int decimal_colour = colour.getDecimal();
								String colour_name = colour.getColouredName();
								
								pocketIn.setDisplayColour(decimal_colour);
								CosmosChatUtil.sendPlayerMessage(playerIn, false, "Pocket colour changed to: " + colour_name);
								
								worldIn.notifyBlockUpdate(pos, state, worldIn.getBlockState(pos).getBlockState().getBlock().getDefaultState(), 3);
								
								return ActionResultType.SUCCESS;
							}
						} else if (stack.getItem().equals(ModBusManager.DIMENSIONAL_SHARD)) {
							pocketIn.setDisplayColour(EnumMinecraftColour.POCKET_PURPLE.getDecimal());
							CosmosChatUtil.sendPlayerMessage(playerIn, false, "Pocket colour changed to: " + EnumMinecraftColour.POCKET_PURPLE.getColouredName());
							
							return ActionResultType.SUCCESS;
						}
					}
				} else {
					if (CosmosUtil.isHoldingHammer(playerIn)) {
						worldIn.setBlockState(pos, ModBusManager.BLOCK_WALL.getDefaultState());
						worldIn.removeTileEntity(pos);
						
						return ActionResultType.SUCCESS;
					} 
					
					else if (!CosmosUtil.isHoldingHammer(playerIn) && playerIn.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
						if (!worldIn.isRemote) {
							pocketIn.shift(playerIn, EnumShiftDirection.LEAVE, null, null);
							return ActionResultType.SUCCESS;
						}
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