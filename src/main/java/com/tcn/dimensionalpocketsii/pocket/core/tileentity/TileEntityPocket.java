package com.tcn.dimensionalpocketsii.pocket.core.tileentity;

import java.util.Optional;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.client.interfaces.IClientUpdatedTile;
import com.tcn.cosmoslibrary.common.chat.CosmosChatUtil;
import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper;
import com.tcn.cosmoslibrary.common.comp.CosmosColour;
import com.tcn.cosmoslibrary.common.enums.EnumSideGuide;
import com.tcn.cosmoslibrary.common.enums.EnumSideState;
import com.tcn.cosmoslibrary.common.interfaces.IFluidStorage;
import com.tcn.cosmoslibrary.common.interfaces.block.IBlockInteract;
import com.tcn.cosmoslibrary.common.interfaces.block.IBlockNotifier;
import com.tcn.cosmoslibrary.common.interfaces.tile.ISidedTile;
import com.tcn.cosmoslibrary.common.math.ChunkPos;
import com.tcn.cosmoslibrary.common.util.CompatUtil;
import com.tcn.cosmoslibrary.common.util.CosmosUtil;
import com.tcn.dimensionalpocketsii.DimReference;
import com.tcn.dimensionalpocketsii.core.management.CoreDimensionManager;
import com.tcn.dimensionalpocketsii.core.management.CoreModBusManager;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerPocket;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockPocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;
import com.tcn.dimensionalpocketsii.pocket.core.util.PocketUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.IInventory;
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

public class TileEntityPocket extends TileEntity implements IBlockNotifier, IBlockInteract, ITickableTileEntity, ISidedTile, INamedContainerProvider, IInventory, IFluidHandler, IFluidStorage, IClientUpdatedTile.FluidTile {

	private static final String TAG_CUSTOM_DP_NAME = "customDPName";
	private String customName;
	private NonNullList<ItemStack> INVENTORY_STACKS = NonNullList.<ItemStack>withSize(56, ItemStack.EMPTY);
	private EnumSideGuide SIDE_GUIDE = EnumSideGuide.HIDDEN;
	
	private Pocket pocket_nbt;
	
	public TileEntityPocket() {
		super(CoreModBusManager.POCKET_TILE_TYPE);
	}
	
	@Override
	public void tick() {
		if (this.getPocket() != null) {
			this.setStacks();
			this.checkFluidSlots();
			this.setSurroundingStacks();
		}
	}

	public Pocket getPocket() {
		if (level.isClientSide) {
			return this.pocket_nbt;
		}
		
		return PocketRegistryManager.getOrCreatePocket(this.level, this.getBlockPos());
	}
	
	public boolean getLockState() {
		return this.getPocket().getLockStateValue();
	}
	
	public void setLockState(boolean change, boolean update) {
		this.getPocket().setLockState(change);
		this.sendUpdates(update);
	}
	
	@Override 
	public EnumSideState getSide(Direction facing) {
		return this.getPocket().getSide(facing);
	}
	
	@Override
	public void setSide(Direction facing, EnumSideState side_state, boolean update) {
		this.getPocket().setSide(facing, side_state, update);
		
		this.sendUpdates(update);
	}
	
	@Override
	public EnumSideState[] getSideArray() {
		return this.getPocket().getSideArray();
	}

	@Override
	public void setSideArray(EnumSideState[] new_array, boolean update) {
		this.getPocket().setSideArray(new_array, update);
		this.sendUpdates(update);
	}

	@Override
	public void cycleSide(Direction facing, boolean update) {
		this.getPocket().cycleSide(facing, update);	
		this.sendUpdates(update);
	}

	@Override
	public boolean canConnect(Direction direction) {
		return this.getPocket().canConnect(direction);
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		
		if (this.getPocket().exists()) {
			this.getPocket().writeToNBT(compound);
		}
		
		if (customName != null) {
			compound.putString(TAG_CUSTOM_DP_NAME, customName);
		}
		
		compound.putInt("side_guide", this.getSideGuide().getIndex());
		
		return compound;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		
		if (PocketUtil.hasPocketKey(compound)) {
			this.pocket_nbt = Pocket.readFromNBT(compound);
		}
		
		String tempString = compound.getString(TAG_CUSTOM_DP_NAME);
		if (!tempString.isEmpty()) {
			customName = tempString;
		}
		
		this.setSideGuide(EnumSideGuide.getStateFromIndex(compound.getInt("side_guide")));
	}
	
	@Override
	public void sendUpdates(boolean update) {
		if (level != null) {
			this.setChanged();
			BlockState state = this.getBlockState();
			BlockPocket block = (BlockPocket) state.getBlock();
			
			level.sendBlockUpdated(this.getBlockPos(), state, state, 3);
			
			if (update) {
				if (!level.isClientSide) {
					level.setBlockAndUpdate(this.getBlockPos(), block.updateState(state, this.getBlockPos(), level));
					
					if (this.getPocket() != null) {
						this.getPocket().updateBaseConnector(this.getLevel());
					}
				}
			}
		}
	}
	
	//Set the data once it has been received. [NBT > TE]
	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		this.load(state, tag);
	}
	
	//Retrieve the data to be stored. [TE > NBT]
	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT tag = new CompoundNBT();
		
		this.save(tag);
		
		return tag;
	}
	

	//Actually sends the data to the server. [NBT > SER]
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.getBlockPos(), 0, this.getUpdateTag());
	}
	
	//Method is called once packet has been received by the client. [SER > CLT]
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		super.onDataPacket(net, pkt);
		CompoundNBT tag_ = pkt.getTag();
		
		BlockState state = level.getBlockState(pkt.getPos());
		
		this.handleUpdateTag(state, tag_);
		//this.sendUpdates(true);
	}
	
	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
		Pocket pocketIn = this.getPocket();
	
		if (pocketIn.exists()) {
			if (playerIn.isShiftKeyDown()) {
				if (CosmosUtil.handEmpty(playerIn)) {
					pocketIn.shift(playerIn, EnumShiftDirection.ENTER, pos, null);
					
					return ActionResultType.SUCCESS;
				} 
				
				else if (CosmosUtil.holdingWrench(playerIn)) {
					if (pocketIn.checkIfOwner(playerIn)) {
						if (this.getLockState()) {
							CosmosChatUtil.sendServerPlayerMessage(playerIn, CosmosCompHelper.getErrorText("dimensionalpocketsii.pocket.status.remove_locked"));
							return ActionResultType.FAIL;
						} else {
							if (!worldIn.isClientSide) {
								CompatUtil.spawnStack(this.generateItemStackOnRemoval(pos), worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0);
								CosmosUtil.setToAir(worldIn, pos);
							}
							
							return ActionResultType.SUCCESS;
						}
					} else {
						CosmosChatUtil.sendServerPlayerMessage(playerIn, CosmosCompHelper.getErrorText("dimensionalpocketsii.pocket.status.no_access_remove"));
						return ActionResultType.FAIL;
					}
				}
			} else {
				if (CosmosUtil.holdingWrench(playerIn)) {
					if (pocketIn.checkIfOwner(playerIn)) {
						if (!worldIn.isClientSide) {
							this.cycleSide(hit.getDirection(), true);
						}
						
						CosmosChatUtil.sendServerPlayerMessage(playerIn, CosmosCompHelper.locComp(CosmosColour.CYAN, false, "dimensionalpocketsii.pocket.status.cycle_side").append(this.getSide(hit.getDirection()).getColouredComp()));
						return ActionResultType.SUCCESS;
					} else {
						CosmosChatUtil.sendServerPlayerMessage(playerIn, CosmosCompHelper.getErrorText("dimensionalpocketsii.pocket.status.no_access"));
						return ActionResultType.FAIL;
					}
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
	
		worldIn.sendBlockUpdated(pos, state, state, 3);
		return ActionResultType.FAIL;
	}

	@Override
	public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (!worldIn.isClientSide) {
			if (stack.hasTag()) {
				if (!(PocketUtil.isDimensionEqual(worldIn, CoreDimensionManager.POCKET_WORLD))) {
					CompoundNBT compound = stack.getTag();
					
					if (compound.contains("nbt_data")) {
						CompoundNBT nbt_tag = compound.getCompound("nbt_data");
						
						int X = nbt_tag.getCompound("chunk_set").getInt("X");
						int Z = nbt_tag.getCompound("chunk_set").getInt("Z");
		
						ChunkPos chunkSet = new ChunkPos(X, Z);
						boolean success = PocketRegistryManager.getPocketFromChunkPosition(chunkSet) != null;
		
						if (!success) {
							throw new RuntimeException("YOU DESERVED THIS!");
						}
		
						PocketRegistryManager.updatePocket(chunkSet, placer.level.dimension(), getBlockPos());
		
						CompoundNBT side_tag = nbt_tag.getCompound("sides");
						
						for(Direction c : Direction.values()) {
							this.setSide(c, EnumSideState.getStateFromIndex(side_tag.getInt("index_" + c.get3DDataValue())), true);
						}
					}
	
					if (placer instanceof PlayerEntity) {
						ServerPlayerEntity player = (ServerPlayerEntity) placer;
						if (player.getItemInHand(Hand.MAIN_HAND) == stack) {
							player.getItemInHand(Hand.MAIN_HAND).setCount(0);
						}
					}
	
					Pocket pocket = this.getPocket();
					pocket.generatePocket(((PlayerEntity) placer));
					//NetworkHandler.sendPocketSetCreator(placer.getName(), pos);
					//ChunkLoaderManagerRoom.addPocketToChunkLoader(pocket);
					//ChunkLoaderManagerBlock.addPocketBlockToChunkLoader(this.getPos(), placer.dimension.getId());
					
				} else if (PocketUtil.isDimensionEqual(worldIn, CoreDimensionManager.POCKET_WORLD)) {
					CompoundNBT stack_tag = stack.getTag();

					if (stack_tag.contains("nbt_data")) {
						CompoundNBT nbt_tag = stack_tag.getCompound("nbt_data");
						
						int X = nbt_tag.getCompound("chunk_set").getInt("X");
						int Z = nbt_tag.getCompound("chunk_set").getInt("Z");
						
						ChunkPos chunk_pos = new ChunkPos(X, Z);
						ChunkPos actual = ChunkPos.scaleToChunkPos(pos);
						
						if (actual != null) {
							if (actual.equals(chunk_pos)) {
								Pocket test_pocket = PocketRegistryManager.getPocketFromChunkPosition(chunk_pos);
			
								CompatUtil.spawnStack(this.generateItemStackWithNBT(test_pocket, pos, chunk_pos), worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0);
									
								worldIn.removeBlock(pos, false);
							} else {
								PocketRegistryManager.updatePocket(chunk_pos, placer.level.dimension(), getBlockPos());
								
								if (placer instanceof PlayerEntity) {
									ServerPlayerEntity player = (ServerPlayerEntity) placer;
									if (player.getItemInHand(Hand.MAIN_HAND) == stack) {
										player.getItemInHand(Hand.MAIN_HAND).setCount(0);
									}
								}
	
								//Pocket pocket = this.getPocket();
								//pocket.generatePocket(((PlayerEntity) placer));
							}
						}
					}
				} 
			} else {
				Pocket pocket = this.getPocket();
				pocket.generatePocket((PlayerEntity) placer);
			}
		}
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) { }

	@Override
	public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) { }

	@Override
	public void attack(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn) { }

	@Override
	public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) { }

	public ItemStack generateItemStackOnRemoval(BlockPos pos) {
		ItemStack itemStack = new ItemStack(CoreModBusManager.BLOCK_POCKET);

		if (!itemStack.hasTag()) {
			itemStack.setTag(new CompoundNBT());
		}

		ChunkPos chunkSet = this.getPocket().getChunkPos();
		
		CompoundNBT compound = new CompoundNBT();
		
		//Saves the chunk data to NBT
		CompoundNBT chunk_tag = new CompoundNBT();
		chunk_tag.putInt("X", chunkSet.getX());
		chunk_tag.putInt("Z", chunkSet.getZ());

		compound.put("chunk_set", chunk_tag);
		
		Pocket pocket = this.getPocket();
		
		//Saves the side data to NBT
		if (this instanceof ISidedTile) {
			CompoundNBT side_tag = new CompoundNBT();
			
			for (Direction c : Direction.values()) {
				side_tag.putInt("index_" + c.get3DDataValue(), this.getSideArray()[c.get3DDataValue()].getIndex());
			}
			
			compound.put("sides", side_tag);
		}
		
		compound.putInt("colour", pocket.getDisplayColour());
		itemStack.getTag().put("nbt_data", compound);
		
		return itemStack;
	}

	public ItemStack generateItemStackWithNBT(Pocket pocket, BlockPos posIn, ChunkPos chunkPosIn) {
		ItemStack item_stack = new ItemStack(CoreModBusManager.BLOCK_POCKET);

		if (!item_stack.hasTag()) {
			item_stack.setTag(new CompoundNBT());
		}

		CompoundNBT compound = new CompoundNBT();
		CompoundNBT chunk_tag = new CompoundNBT();
		
		int x = chunkPosIn.getX();
		int z = chunkPosIn.getZ();
		
		chunk_tag.putInt("X", x);
		chunk_tag.putInt("Z", z);

		compound.put("chunk_set", chunk_tag);
		
		compound.putInt("colour", pocket.getDisplayColour());
		
		item_stack.getTag().put("nbt_data", compound);
		
		return item_stack;
	}

	@Override
	public ITextComponent getDisplayName() {
		return CosmosCompHelper.locComp("dimensionalpocketsii.gui.pocket.header");
	}

	public boolean canPlayerAccessInventory(PlayerEntity player) {
		if (this.level.getBlockEntity(this.getBlockPos()) != this) return false;
		final double X_CENTRE_OFFSET = 0.5;
		final double Y_CENTRE_OFFSET = 0.5;
		final double Z_CENTRE_OFFSET = 0.5;
		final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
		return player.distanceToSqr(this.getBlockPos().getX() + X_CENTRE_OFFSET, this.getBlockPos().getY() + Y_CENTRE_OFFSET, this.getBlockPos().getZ() + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
	}

	@Override
	public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
		return ContainerPocket.createContainerServerSide(p_createMenu_1_, p_createMenu_2_, this, this.getBlockPos());
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
	
	public void setSurroundingStacks() {
		Pocket pocket = this.getPocket();
		
		if(pocket != null) {
			if (!this.level.isClientSide) {
				for (Direction c : Direction.values()) {
					Block block_other = level.getBlockState(this.getBlockPos().offset(c.getNormal())).getBlock();
					this.INVENTORY_STACKS.set(c.get3DDataValue() + DimReference.CONSTANT.POCKET_HELD_ITEMS_SIZE, new ItemStack(block_other));
				}
			}
		}
	}

	public EnumSideGuide getSideGuide() {
		return this.SIDE_GUIDE;
	}
	
	public void setSideGuide(EnumSideGuide value) {
		this.SIDE_GUIDE = value;
	}
	
	public void toggleSideGuide() {
		this.SIDE_GUIDE = EnumSideGuide.getOpposite(this.SIDE_GUIDE);
	}
	
	public boolean getSideGuideValue() {
		return this.SIDE_GUIDE.getValue();
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

	public boolean canExtract(Direction dir) {
		EnumSideState state = this.getSide(dir);
		
		if (state.equals(EnumSideState.INTERFACE_OUTPUT)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean canReceive(Direction dir) {
		EnumSideState state = this.getSide(dir);
		
		if (state.equals(EnumSideState.INTERFACE_INPUT)) {
			return true;
		} else {
			return false;
		}
	}
	
	private LazyOptional<IEnergyStorage> createEnergyProxy(@Nullable Direction side) {
        return LazyOptional.of(() -> new IEnergyStorage() {
            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                return TileEntityPocket.this.getPocket().extractEnergy(maxExtract, simulate);
            }

            @Override
            public int getEnergyStored() {
                return TileEntityPocket.this.getPocket().getEnergyStored();
            }

            @Override
            public int getMaxEnergyStored() {
                return TileEntityPocket.this.getPocket().getMaxEnergyStored();
            }

            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                return TileEntityPocket.this.getPocket().receiveEnergy(maxReceive, simulate);
            }

            @Override
            public boolean canReceive() {
                return TileEntityPocket.this.canReceive(side);
            }

            @Override
            public boolean canExtract() {
                return TileEntityPocket.this.canExtract(side);
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