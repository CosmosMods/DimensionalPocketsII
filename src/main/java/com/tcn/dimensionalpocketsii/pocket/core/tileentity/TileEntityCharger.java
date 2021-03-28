package com.tcn.dimensionalpocketsii.pocket.core.tileentity;

import com.tcn.cosmoslibrary.common.chat.CosmosChatUtil;
import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper;
import com.tcn.cosmoslibrary.common.interfaces.block.IBlockInteract;
import com.tcn.cosmoslibrary.common.interfaces.block.IBlockNotifier;
import com.tcn.cosmoslibrary.common.math.ChunkPos;
import com.tcn.cosmoslibrary.common.util.CosmosUtil;
import com.tcn.dimensionalpocketsii.core.management.CoreDimensionManager;
import com.tcn.dimensionalpocketsii.core.management.CoreModBusManager;
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
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class TileEntityCharger extends TileEntity implements IBlockNotifier, IBlockInteract, IInventory, ISidedInventory, ITickableTileEntity {

	private NonNullList<ItemStack> INVENTORY_STACKS = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
	
	@SuppressWarnings("unused")
	private Pocket pocket;

	public TileEntityCharger() {
		super(CoreModBusManager.CHARGER_TILE_TYPE);
	}
	
	public Pocket getPocket() {
		//if (level.isClientSide) {
		//	return this.pocket;
		//}
		
		return PocketRegistryManager.getPocketFromChunkPosition(ChunkPos.scaleToChunkPos(this.getBlockPos()));
	}

	public void updateRenderers() {
		this.level.sendBlockUpdated(this.getBlockPos(), level.getBlockState(this.getBlockPos()), level.getBlockState(this.getBlockPos()), 3);
		//this.level.markBlockRangeForRenderUpdate(this.getPos(), this.level.getBlockState(this.pos), this.level.getBlockState(this.pos));
		this.setChanged();
	}
	
	@Override
	public void tick() { }
	
	public void sendUpdates(boolean update) {
		if (update) {
			//this.level.markBlockRangeForRenderUpdate(this.getBlockPos(), this.level.getBlockState(this.pos), this.level.getBlockState(this.pos));
			this.level.sendBlockUpdated(this.getBlockPos(), level.getBlockState(this.getBlockPos()), level.getBlockState(this.getBlockPos()), 3);
		}
		
		this.setChanged();
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound) {
		if (this.getPocket() != null) {
			this.getPocket().writeToNBT(compound);
		}
		
		return compound;
	}
	
	@Override
	public void load(BlockState state, CompoundNBT compound) {
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
		this.sendUpdates(true);
	}
	
	
	public void onLoad() { }
	
	
	public void setShifterUses() {
		if (!this.getItem(0).isEmpty()) {
			ItemStack stack = this.getItem(0);
			Item item = stack.getItem();
			
			if (item.equals(CoreModBusManager.DIMENSIONAL_SHIFTER)) {
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
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit) {
		this.setChanged();
		
		if (PocketUtil.isDimensionEqual(worldIn, CoreDimensionManager.POCKET_WORLD)) {
			Pocket pocket = this.getPocket();
			
			if (pocket != null) {
				if (!playerIn.isShiftKeyDown()) {
					if (CosmosUtil.handItem(playerIn, CoreModBusManager.DIMENSIONAL_SHIFTER)) {
						ItemStack stack = playerIn.getItemInHand(hand);
						
						if (this.getItem(0).isEmpty()) {
							this.setItem(0, stack.copy());
							
							if (!worldIn.isClientSide) {
								playerIn.setItemInHand(hand, ItemStack.EMPTY);
							}
							
							this.setShifterUses();
						}

						return ActionResultType.SUCCESS;
					} else if (playerIn.getItemInHand(hand).isEmpty()) {
						ItemStack stack = this.getItem(0);
						
						playerIn.setItemInHand(hand, stack);
						
						this.setItem(0, ItemStack.EMPTY);
					}
					
					return ActionResultType.SUCCESS;
				} else {
					if (!CosmosUtil.holdingWrench(playerIn) && playerIn.getItemInHand(Hand.MAIN_HAND).isEmpty()) {
						pocket.shift(playerIn, EnumShiftDirection.LEAVE, null, null);
						
						return ActionResultType.PASS;
					}
					
					if (CosmosUtil.holdingWrench(playerIn)) {
						worldIn.setBlockAndUpdate(pos, CoreModBusManager.BLOCK_WALL.defaultBlockState());
						worldIn.removeBlockEntity(pos);
						
						if (!playerIn.isCreative()) {
							CosmosUtil.addItem(playerIn, CoreModBusManager.MODULE_CHARGER, 1);
						}
						
						return ActionResultType.SUCCESS;
					} 
				}
			} else {
				CosmosChatUtil.sendServerPlayerMessage(playerIn, CosmosCompHelper.getErrorText("dimensionalpocketsii.pocket.status.action.null"));
				
				return ActionResultType.FAIL;
			}
		}
		return ActionResultType.FAIL;
	}

	@Override
	public void attack(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn) { }

	@Override
	public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) { }

	@Override
	public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) { }

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) { }

	@Override
	public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) { }

	@Override
	public void clearContent() {
	}

	@Override
	public boolean canPlaceItemThroughFace(int arg0, ItemStack arg1, Direction arg2) {
		return false;
	}

	@Override
	public boolean canTakeItemThroughFace(int arg0, ItemStack arg1, Direction arg2) {
		return false;
	}

	@Override
	public int getContainerSize() {
		return this.INVENTORY_STACKS.size();
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
		
		this.setChanged();
	}

	@Override
	public boolean stillValid(PlayerEntity playerIn) {
		return true;
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
	public int[] getSlotsForFace(Direction arg0) {
		return new int [] { 0, 0, 0, 0, 0, 0 };
	}
	
}
