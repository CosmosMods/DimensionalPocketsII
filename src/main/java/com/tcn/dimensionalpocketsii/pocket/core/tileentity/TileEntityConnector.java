package com.tcn.dimensionalpocketsii.pocket.core.tileentity;

import java.util.Optional;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.client.interfaces.IClientUpdatedTile;
import com.tcn.cosmoslibrary.common.chat.CosmosChatUtil;
import com.tcn.cosmoslibrary.common.comp.CosmosColour;
import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper;
import com.tcn.cosmoslibrary.common.enums.EnumConnectionType;
import com.tcn.cosmoslibrary.common.enums.EnumSideState;
import com.tcn.cosmoslibrary.common.interfaces.IFluidStorage;
import com.tcn.cosmoslibrary.common.interfaces.block.IBlockInteract;
import com.tcn.cosmoslibrary.common.interfaces.tile.IConnectionTypeTile;
import com.tcn.cosmoslibrary.common.interfaces.tile.ISidedTile;
import com.tcn.cosmoslibrary.common.math.ChunkPos;
import com.tcn.cosmoslibrary.common.util.CosmosUtil;
import com.tcn.dimensionalpocketsii.DimReference;
import com.tcn.dimensionalpocketsii.core.management.CoreDimensionManager;
import com.tcn.dimensionalpocketsii.core.management.CoreModBusManager;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerConnector;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallConnector;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;
import com.tcn.dimensionalpocketsii.pocket.core.util.PocketUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.network.NetworkHooks;

public class TileEntityConnector extends TileEntity implements IBlockInteract, IInventory, INamedContainerProvider, ISidedInventory, ITickableTileEntity, ISidedTile, IFluidHandler, IFluidStorage, IClientUpdatedTile.FluidTile, IConnectionTypeTile {

	private EnumSideState[] SIDE_STATE_ARRAY = EnumSideState.getStandardArray();
	private EnumConnectionType TYPE = EnumConnectionType.getStandardValue();
	
	private NonNullList<ItemStack> INVENTORY_STACKS = NonNullList.<ItemStack>withSize(56, ItemStack.EMPTY);
	public int NUM_ITEMS = 56;
	
	private Pocket pocket;
	
	public TileEntityConnector() {
		super(CoreModBusManager.CONNECTOR_TILE_TYPE);
	}
	
	public Pocket getPocket() {
		if (level.isClientSide) {
			return this.pocket;
		}
		
		return PocketRegistryManager.getPocketFromChunkPosition(ChunkPos.scaleToChunkPos(this.getBlockPos()));
	}

	public void updateRenderers() {
		this.level.sendBlockUpdated(this.getBlockPos(), level.getBlockState(this.getBlockPos()), level.getBlockState(this.getBlockPos()), 3);
		//this.level.markBlockRangeForRenderUpdate(this.getBlockPos(), this.world.getBlockState(this.getBlockPos()), this.level.getBlockState(this.getBlockPos()));
		this.setChanged();
	}
	
	@Override
	public void tick() {
		for (Direction d : Direction.values()) {
			TileEntity tile = this.getLevel().getBlockEntity(this.getBlockPos().offset(d.getNormal()));
			
			if (tile != null && !tile.isRemoved() && !(tile instanceof TileEntityConnector)) {
				if (this.getSide(d).equals(EnumSideState.INTERFACE_OUTPUT)) {
					if (tile.getCapability(CapabilityEnergy.ENERGY, d).resolve().isPresent()) {
						LazyOptional<?> consumer = tile.getCapability(CapabilityEnergy.ENERGY, d);
						
						if (consumer.resolve().get() instanceof IEnergyStorage) {
							IEnergyStorage storage = (IEnergyStorage) consumer.resolve().get();
							
							if (storage.canReceive()) {
								this.getPocket().extractEnergy(storage.receiveEnergy(this.getPocket().getMaxExtract(), false), false);
								this.sendUpdates(true);
							}
						}
					}
				}
			}
		}
		
		/**
		if (PocketUtil.isDimensionEqual(this.world, CoreDimensionManager.POCKET_WORLD)) {
			for (Direction c : Direction.values()) {
				//TileEntity tile_other = this.world.getTileEntity(this.getPos().offset(c));
				
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
		}*/
			
		if (this.getPocket() != null) {
			this.setSurroundingStacks();
			this.setStacks();
			this.checkFluidSlots();
		}
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
		return SIDE_STATE_ARRAY[facing.get3DDataValue()];
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
		EnumSideState state = SIDE_STATE_ARRAY[facing.get3DDataValue()];
		
		EnumSideState state2 = state.getNextState();
		
		for (int i = 0; i < this.SIDE_STATE_ARRAY.length; i++) {
			SIDE_STATE_ARRAY[i] = state2;
		}
		
		this.sendUpdates(update);
	}

	@Override
	public boolean canConnect(Direction facing) {
		EnumSideState state = SIDE_STATE_ARRAY[facing.get3DDataValue()];
		
		if (state.equals(EnumSideState.DISABLED)) {
			return false;
		}
		return true;
	}

	@Override
	public void sendUpdates(boolean update) {
		if (level != null) {
			this.setChanged();
			BlockState state = this.getBlockState();
			BlockWallConnector block = (BlockWallConnector) state.getBlock();
			
			level.sendBlockUpdated(this.getBlockPos(), state, state, 3);
			
			if (update) {
				if (!level.isClientSide) {
					level.setBlockAndUpdate(this.getBlockPos(), block.updateState(state, this.getBlockPos(), level));
					
					if (this.getPocket() != null) {
						this.getPocket().updateBaseConnector(this.getLevel());
					}
				}
			} else {
				if (!level.isClientSide) {
					level.setBlockAndUpdate(this.getBlockPos(), block.updateState(state, this.getBlockPos(), level));
				}
			}
		}
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
		this.setChanged();
		return this.getPocket().getCurrentStoredFluid();
	}

	@Override
	public boolean isFluidEmpty() {
		return this.getPocket().isFluidTankEmpty();
	}

	@Override
	public int getCurrentFluidAmount() {
		this.setChanged();
		return this.getPocket().getCurrentFluidAmount();
	}
	
	public String getCurrentStoredFluidName() {
		return this.getPocket().getCurrentStoredFluidName();
	}

	public int fill(FluidStack resource, FluidAction doFill) {
		this.setChanged();
		return this.getPocket().fill(resource, doFill);
	}

	public FluidStack drain(FluidStack resource, FluidAction doDrain) {
		this.setChanged();
		return this.getPocket().drain(resource.getAmount(), doDrain);
	}

	@Override
	public FluidStack drain(int maxDrain, FluidAction doDrain) {
		this.setChanged();
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
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);

		compound.putInt("side", this.getSide(Direction.UP).getIndex());
		compound.putInt("type", this.getConnectionType().getIndex());
		
		if (this.getPocket().exists()) {
			this.getPocket().writeToNBT(compound);
		}
		
		return compound;
	}
	
	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);

		int side = compound.getInt("side");
		int type = compound.getInt("type");
		
		this.setSide(Direction.UP, EnumSideState.getStateFromIndex(side), false);
		this.setConnectionType(EnumConnectionType.getStateFromIndex(type), false);

		if (PocketUtil.hasPocketKey(compound)) {
			this.pocket = Pocket.readFromNBT(compound);
		}
	}
	
	/**
	 * Set the data once it has been received. [NBT > TE] (READ)
	 */
	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		this.load(state, tag);
		
		this.sendUpdates(true);
	}
	
	/**
	 * Retrieve the data to be stored. [TE > NBT] (WRITE)
	 */
	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT tag = new CompoundNBT();
		
		this.save(tag);
		
		return tag;
	}
	
	/**
	 * Actually sends the data to the server. [NBT > SER]
	 */
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.getBlockPos(), 0, this.getUpdateTag());
	}
	
	/**
	 * Method is called once packet has been received by the client. [SER > CLT]
	 */
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		super.onDataPacket(net, pkt);
		CompoundNBT tag_ = pkt.getTag();

		BlockState state = level.getBlockState(pkt.getPos());
		
		this.handleUpdateTag(state, tag_);
		//this.sendUpdates(true);
	}
	
	
	public void onLoad() { }
	
	/**
	 * Check whether the pocket is locked.
	 * @return boolean of the lock state
	 */
	public boolean getLockState() {
		return this.getPocket().getLockStateValue();
	}
	
	/**
	 * Set the lock state of the pocket.
	 */
	public void setLockState(boolean change, boolean update) {
		this.getPocket().setLockState(change);
		this.sendUpdates(update);
	}
	
	public void checkFluidSlots() {
		if (!this.level.isClientSide) {
			if (!this.INVENTORY_STACKS.get(54).isEmpty()) {
				if (this.INVENTORY_STACKS.get(54).getItem() instanceof BucketItem) {
					Optional<FluidStack> fluidStack = FluidUtil.getFluidContained(this.INVENTORY_STACKS.get(54));
					
					if (fluidStack.isPresent()) {
						FluidStack fluid = fluidStack.get();
						
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
					} else {
						if (this.getCurrentFluidAmount() > 0) {
							if (this.INVENTORY_STACKS.get(55).isEmpty()) {
								ItemStack fillStack = FluidUtil.tryFillContainer(this.INVENTORY_STACKS.get(54), this.getTank(), 1000, null, true).result;
								
								if (fillStack.getItem() instanceof BucketItem) {
									this.INVENTORY_STACKS.get(54).shrink(1);
									this.INVENTORY_STACKS.set(55, fillStack);
								} else {
									this.INVENTORY_STACKS.get(54).shrink(1);
									this.INVENTORY_STACKS.set(55, new ItemStack(Items.BUCKET, 1));
								}
							}
						}
					
					}
				}
				this.setChanged();
				this.sendUpdates(true);
			}
		}
	}
	
	public void setStacks() {
		Pocket pocket = this.getPocket();
		
		if (!this.level.isClientSide) {
			if (pocket != null) {
				for (int i = 0 ; i < pocket.item_array.size(); i++) {
					if (this.getItem(i) != pocket.item_array.get(i)) {
						ItemStack pocket_stack = pocket.item_array.get(i);
						ItemStack copy = pocket_stack.copy();
						if (!this.level.isClientSide) {
							this.INVENTORY_STACKS.set(i, copy);
						}
					}
				}
			}
		}
	}
	
	//@SideOnly(Side.CLIENT)
	public void setSurroundingStacks() {
		Pocket pocket = this.getPocket();
		
		if(pocket != null) {
			if (!this.level.isClientSide) {
				World pocket_world = this.getPocket().getSourceBlockWorld();
				BlockPos pocket_pos = this.getPocket().getLastBlockPos();
				
				if (pocket_world != null) {
					TileEntity tile_pocket = pocket_world.getBlockEntity(pocket_pos);
					
					if (tile_pocket != null && tile_pocket instanceof TileEntityPocket) {
						for (Direction c : Direction.values()) {
							Block block_other = pocket_world.getBlockState(pocket_pos.offset(c.getNormal())).getBlock();
							this.INVENTORY_STACKS.set(c.get3DDataValue() + DimReference.CONSTANT.POCKET_HELD_ITEMS_SIZE, new ItemStack(block_other));
						}
					}
				}
			}
		}
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit) {
		if (PocketUtil.isDimensionEqual(worldIn, CoreDimensionManager.POCKET_WORLD)) {
			Pocket pocketIn = this.getPocket();
			
			if (pocketIn.exists()) {
				if (playerIn.isShiftKeyDown()) {
					if (CosmosUtil.holdingWrench(playerIn)) {
						if (pocketIn.checkIfOwner(playerIn)) {
							if (!worldIn.isClientSide) {
								worldIn.setBlockAndUpdate(pos, CoreModBusManager.BLOCK_WALL.defaultBlockState());
								worldIn.removeBlockEntity(pos);
								
								if (!playerIn.isCreative()) {
									CosmosUtil.addItem(playerIn, CoreModBusManager.MODULE_CONNECTOR, 1);
								}
							}
							
							return ActionResultType.SUCCESS;
						} else {
							CosmosChatUtil.sendServerPlayerMessage(playerIn, CosmosCompHelper.getErrorText("dimensionalpocketsii.pocket.status.no_access"));
							return ActionResultType.FAIL;
						}
					} else if (CosmosUtil.handEmpty(playerIn)) {
						pocketIn.shift(playerIn, EnumShiftDirection.LEAVE, null, null);
						return ActionResultType.SUCCESS;
					}
				} else {
					if (CosmosUtil.holdingWrench(playerIn)) {
						if (!worldIn.isClientSide) {
							this.cycleSide(Direction.UP, true);
						}
						
						CosmosChatUtil.sendServerPlayerMessage(playerIn, CosmosCompHelper.locComp(CosmosColour.CYAN, false, "dimensionalpocketsii.pocket.status.cycle_side").append(this.getSide(Direction.UP).getColouredComp()));
						return ActionResultType.SUCCESS;
					}
					
					else if (CosmosUtil.getStackItem(playerIn) instanceof DyeItem || CosmosUtil.getStackItem(playerIn).equals(CoreModBusManager.DIMENSIONAL_SHARD)) {
						if (pocketIn.checkIfOwner(playerIn)) {
							ItemStack stack = CosmosUtil.getStack(playerIn);
							DyeColor dyeColour = DyeColor.getColor(stack);
							
							CosmosColour colour = dyeColour != null ? CosmosColour.fromIndex(dyeColour.getId()) : CosmosColour.POCKET_PURPLE;
							
							if (pocketIn.getDisplayColour() != colour.dec()) {
								pocketIn.setDisplayColour(playerIn, worldIn, colour.dec());
								this.sendUpdates(true);
								
								CosmosChatUtil.sendServerPlayerMessage(playerIn, CosmosCompHelper.locComp(CosmosColour.CYAN, false, "dimensionalpocketsii.pocket.status.colour_update").append(colour.getColouredName()));
								return ActionResultType.SUCCESS;
							} else {
								return ActionResultType.FAIL;
							}
						} else {
							CosmosChatUtil.sendServerPlayerMessage(playerIn, CosmosCompHelper.getErrorText("dimensionalpocketsii.pocket.status.no_access"));
							return ActionResultType.FAIL;
						}
					}
					
					else if (!(CosmosUtil.getStackItem(playerIn) instanceof BlockItem)) {
						if (pocketIn.checkIfOwner(playerIn)) {
							if (playerIn instanceof ServerPlayerEntity) {
								NetworkHooks.openGui((ServerPlayerEntity) playerIn, this, (packetBuffer)->{ packetBuffer.writeBlockPos(this.getBlockPos()); });
							}
							
							return ActionResultType.SUCCESS;
						} else {
							CosmosChatUtil.sendServerPlayerMessage(playerIn, CosmosCompHelper.getErrorText("dimensionalpocketsii.pocket.status.no_access"));
							return ActionResultType.FAIL;
						}
					}
				}
			} else {
				CosmosChatUtil.sendServerPlayerMessage(playerIn, CosmosCompHelper.getErrorText("dimensionalpocketsii.pocket.status.action.null"));
				return ActionResultType.FAIL;
			}
		}

		worldIn.sendBlockUpdated(pos, state, state, 3);
		return ActionResultType.FAIL;
	}

	@Override
	public void attack(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn) { }

	@Override
	public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
		return ContainerConnector.createContainerServerSide(p_createMenu_1_, p_createMenu_2_, this, this.getBlockPos());
	}

	@Override
	public ITextComponent getDisplayName() {
		return CosmosCompHelper.locComp("dimensionalpocketsii.gui.connector.header");
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
	public int getContainerSize() {
		return 64;
	}

	@Override
	public ItemStack getItem(int index) {
		return this.INVENTORY_STACKS.get(index);
	}

	@Override
	public ItemStack removeItem(int index, int count) {
		this.setChanged();
		return ItemStackHelper.removeItem(this.INVENTORY_STACKS, index, count);
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		this.setChanged();
		return ItemStackHelper.takeItem(this.INVENTORY_STACKS, index);
	}
	
	@Override
	public void setItem(int index, ItemStack stack) {
		this.INVENTORY_STACKS.set(index, stack);
		if (stack.getCount() > this.getContainerSize()) {
			stack.setCount(this.getContainerSize());
		}
		
		if (this.level != null) {
			if (!this.level.isClientSide) {
				if (index < DimReference.CONSTANT.POCKET_HELD_ITEMS_SIZE) {
					if (this.getPocket() != null) {
						this.getPocket().item_array.set(index, stack);
					}
				}
			}
		}
		this.setChanged();
	}

	@Override
	public boolean stillValid(PlayerEntity player) {
		return true;
	}

	@Override
	public void clearContent() { }

	@Override
	public boolean canPlaceItemThroughFace(int arg0, ItemStack arg1, Direction arg2) {
		return false;
	}

	@Override
	public boolean canTakeItemThroughFace(int arg0, ItemStack arg1, Direction arg2) {
		return false;
	}

	@Override
	public int[] getSlotsForFace(Direction arg0) {
		return null;
	}

	public boolean canExtract(Direction dir) {
		this.sendUpdates(true);
		EnumConnectionType type = this.getConnectionType();
		
		if (type.equals(EnumConnectionType.ENERGY)) {
			EnumSideState state = this.getSide(dir.getOpposite());
			
			if (state.equals(EnumSideState.INTERFACE_OUTPUT)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean canReceive(Direction dir) {
		this.sendUpdates(true);
		EnumConnectionType type = this.getConnectionType();
		
		if (type.equals(EnumConnectionType.ENERGY)) {
			EnumSideState state = this.getSide(dir.getOpposite());
			
			if (state.equals(EnumSideState.INTERFACE_INPUT)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	private LazyOptional<IEnergyStorage> createEnergyProxy(@Nullable Direction side) {
        return LazyOptional.of(() -> new IEnergyStorage() {
            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                return TileEntityConnector.this.getPocket().extractEnergy(maxExtract, simulate);
            }

            @Override
            public int getEnergyStored() {
                return TileEntityConnector.this.getPocket().getEnergyStored();
            }

            @Override
            public int getMaxEnergyStored() {
                return TileEntityConnector.this.getPocket().getMaxEnergyStored();
            }

            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                return TileEntityConnector.this.getPocket().receiveEnergy(maxReceive, simulate);
            }

            @Override
            public boolean canReceive() {
                return TileEntityConnector.this.canReceive(side);
            }

            @Override
            public boolean canExtract() {
                return TileEntityConnector.this.canExtract(side);
            }
        });
    }
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityEnergy.ENERGY) {
			return this.createEnergyProxy(side).cast();
		}
		return super.getCapability(cap, side);
	}

}