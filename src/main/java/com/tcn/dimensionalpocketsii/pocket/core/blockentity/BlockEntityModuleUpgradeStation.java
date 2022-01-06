package com.tcn.dimensionalpocketsii.pocket.core.blockentity;

import java.util.ArrayList;

import com.tcn.cosmoslibrary.common.chat.CosmosChatUtil;
import com.tcn.cosmoslibrary.common.enums.EnumUIHelp;
import com.tcn.cosmoslibrary.common.enums.EnumUIMode;
import com.tcn.cosmoslibrary.common.interfaces.block.IBlockInteract;
import com.tcn.cosmoslibrary.common.interfaces.blockentity.IBlockEntityUIMode;
import com.tcn.cosmoslibrary.common.lib.ComponentHelper;
import com.tcn.cosmoslibrary.common.lib.CosmosChunkPos;
import com.tcn.cosmoslibrary.common.util.CosmosUtil;
import com.tcn.dimensionalpocketsii.core.crafting.CraftingManagerUpgradeStation;
import com.tcn.dimensionalpocketsii.core.management.ModBusManager;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallUpgradeStation;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;
import com.tcn.dimensionalpocketsii.pocket.core.shift.EnumShiftDirection;
import com.tcn.dimensionalpocketsii.pocket.core.util.PocketUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

public class BlockEntityModuleUpgradeStation extends BlockEntity implements IBlockInteract, Container, IBlockEntityUIMode {

	public NonNullList<ItemStack> inventoryItems = NonNullList.withSize(9, ItemStack.EMPTY);
	
	private Pocket pocket;

	private EnumUIMode uiMode = EnumUIMode.DARK;
	private EnumUIHelp uiHelp = EnumUIHelp.HIDDEN;
	
	private int update;

	public BlockEntityModuleUpgradeStation(BlockPos posIn, BlockState stateIn) {
		super(ModBusManager.UPGRADE_STATION_TILE_TYPE, posIn, stateIn);
	}
	
	public Pocket getPocket() {
		if (level.isClientSide) {
			return this.pocket;
		}
		
		return PocketRegistryManager.getPocketFromChunkPosition(CosmosChunkPos.scaleToChunkPos(this.getBlockPos()));
	}
	
	public void sendUpdates(boolean update) {
		if (level != null) {
			this.setChanged();
			BlockState state = this.getBlockState();
			BlockWallUpgradeStation block = (BlockWallUpgradeStation) state.getBlock();
			
			level.sendBlockUpdated(this.getBlockPos(), state, state, 3);
			
			if (update) {
				if (!level.isClientSide) {
					level.setBlockAndUpdate(this.getBlockPos(), block.defaultBlockState());
					
					if (this.getPocket() != null) {
						this.getPocket().updateBaseConnector(this.getLevel());
					}
				}
			} else {
				if (!level.isClientSide) {
					level.setBlockAndUpdate(this.getBlockPos(), block.defaultBlockState());
				}
			}
		}
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		super.save(compound);
		
		if (this.getPocket().exists()) {
			this.getPocket().writeToNBT(compound);
		}
		
		ContainerHelper.saveAllItems(compound, this.inventoryItems);
		
		compound.putInt("ui_mode", this.uiMode.getIndex());
		compound.putInt("ui_help", this.uiHelp.getIndex());
		
		return compound;
	}

	public void saveToItemStack(ItemStack stackIn) {
		CompoundTag compound = stackIn.getOrCreateTag();
		
		ContainerHelper.saveAllItems(compound, this.inventoryItems);
		
		compound.putInt("ui_mode", this.uiMode.getIndex());
		compound.putInt("ui_help", this.uiHelp.getIndex());
	}
	
	@Override
	public void load(CompoundTag compound) {
		super.load(compound);

		if (PocketUtil.hasPocketKey(compound)) {
			this.pocket = Pocket.readFromNBT(compound);
		}
		
		this.inventoryItems = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(compound, this.inventoryItems);
		
		this.uiMode = EnumUIMode.getStateFromIndex(compound.getInt("ui_mode"));
		this.uiHelp = EnumUIHelp.getStateFromIndex(compound.getInt("ui_help"));
	}

	public void loadFromItemStack(ItemStack stackIn) {
		if (stackIn.hasTag()) {
			CompoundTag compound = stackIn.getTag();
			
			this.inventoryItems = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
			ContainerHelper.loadAllItems(compound, this.inventoryItems);
			
			this.uiMode = EnumUIMode.getStateFromIndex(compound.getInt("ui_mode"));
			this.uiHelp = EnumUIHelp.getStateFromIndex(compound.getInt("ui_help"));
		}
	}
	
	/**
	 * Set the data once it has been received. [NBT > TE] (READ)
	 */
	@Override
	public void handleUpdateTag(CompoundTag tag) {
		this.load(tag);
		this.sendUpdates(true);
	}
	
	/**
	 * Retrieve the data to be stored. [TE > NBT] (WRITE)
	 */
	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag tag = new CompoundTag();
		this.save(tag);
		return tag;
	}
	
	/**
	 * Actually sends the data to the server. [NBT > SER]
	 */
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return new ClientboundBlockEntityDataPacket(this.getBlockPos(), 0, this.getUpdateTag());
	}
	
	/**
	 * Method is called once packet has been received by the client. [SER > CLT]
	 */
	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		super.onDataPacket(net, pkt);
		CompoundTag tag_ = pkt.getTag();
		this.handleUpdateTag(tag_);
	}
	
	@Override
	public void onLoad() { }
	
	public static void tick(Level levelIn, BlockPos posIn, BlockState stateIn, BlockEntityModuleUpgradeStation entityIn) {
		entityIn.displayPreviewSlot();
		
		if (entityIn.update > 0) {
			entityIn.update--;
		} else {
			entityIn.update = 40;
			entityIn.sendUpdates(true);
		}
	}

	@Override
	public void attack(BlockState state, Level worldIn, BlockPos pos, Player player) { }
	
	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand handIn, BlockHitResult hit) {
		worldIn.sendBlockUpdated(pos, state, state, 3);
		this.setChanged();
		this.sendUpdates(true);
		
		if (CosmosUtil.getStackItem(playerIn) instanceof BlockItem) {
			return InteractionResult.FAIL;
		}
		
		if (!playerIn.isShiftKeyDown()) {
			if (worldIn.isClientSide) {
				return InteractionResult.SUCCESS;
			} else {
				if (playerIn instanceof ServerPlayer) {
					NetworkHooks.openGui((ServerPlayer)playerIn, state.getMenuProvider(worldIn, pos), (packetBuffer) -> { packetBuffer.writeBlockPos(pos); });
					return InteractionResult.SUCCESS;
				}
			}
		} else {
			if(!worldIn.isClientSide) {
				CosmosChunkPos chunkPos = CosmosChunkPos.scaleToChunkPos(pos);
				Pocket pocketIn = PocketRegistryManager.getPocketFromChunkPosition(chunkPos);
				
				if(pocketIn.exists()) {
					if (CosmosUtil.holdingWrench(playerIn)) {
						if (pocketIn.checkIfOwner(playerIn)) {
							ItemStack stack = new ItemStack(ModBusManager.MODULE_UPGRADE_STATION);
							this.saveToItemStack(stack);
							
							worldIn.setBlockAndUpdate(pos, ModBusManager.BLOCK_WALL.defaultBlockState());
							worldIn.removeBlockEntity(pos);
							
							CosmosUtil.addStack(worldIn, playerIn, stack);
							
							return InteractionResult.SUCCESS;
						} else {
							CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.no_access"));
							return InteractionResult.FAIL;
						}
					} 
					
					else if (CosmosUtil.handEmpty(playerIn)) {
						pocketIn.shift(playerIn, EnumShiftDirection.LEAVE, null, null, null);
						return InteractionResult.SUCCESS;
					}
				} else {
					CosmosChatUtil.sendServerPlayerMessage(playerIn, ComponentHelper.getErrorText("dimensionalpocketsii.pocket.status.action.null"));
					return InteractionResult.FAIL;
				}
			}
		}
		
		return InteractionResult.SUCCESS;
	}
	
	public ItemStack getResultStack() {
		ItemStack inputStack = this.getItem(0);
		ItemStack outputStack = this.getItem(7);
		ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
		
		for (int i = 0; i < 7; i++) {
			ItemStack stack = this.getItem(i);
			
			if (!stack.isEmpty()) {
				stacks.add(stack);
			}
		}
		
		ItemStack checkStack = CraftingManagerUpgradeStation.getInstance().findFocusStack(stacks);
		ItemStack resultStack = CraftingManagerUpgradeStation.getInstance().findMatchingRecipe(stacks);
		
		if (!checkStack.isEmpty() && outputStack.isEmpty()) {
			if (inputStack.getItem().equals(checkStack.getItem())) {
				ItemStack copyStack = resultStack.copy();
				
				if (inputStack.hasTag()) {
					CompoundTag compound = inputStack.getTag();
					
					copyStack.setTag(compound);
				}
				
				return copyStack;
			}
		}
	
	
		return ItemStack.EMPTY;
	}
	
	public void displayPreviewSlot() {
		if (this.canCraft()) {
			this.setItem(8, this.getResultStack());
		} else if (!this.getItem(8).isEmpty()) {
			this.setItem(8, ItemStack.EMPTY);
		}
	}
	
	public boolean canCraft() {
		ItemStack inputStack = this.getItem(0);
		ItemStack outputStack = this.getItem(7);
		
		if (!inputStack.isEmpty() && outputStack.isEmpty()) {
			if (!this.getItem(1).isEmpty() && !this.getItem(2).isEmpty() && !this.getItem(3).isEmpty() && !this.getItem(4).isEmpty() && !this.getItem(5).isEmpty() && !this.getItem(6).isEmpty()) {
				if (!this.getResultStack().isEmpty()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void craftItem() {
		if (this.canCraft()) {
			this.setItem(7, this.getResultStack());
			this.getItem(7).setDamageValue(0);
			
			this.setItem(0, ItemStack.EMPTY);
			
			for (int i = 1; i < 7; i++) {
				this.setItem(i, ItemStack.EMPTY);
			}
		}
		
		this.sendUpdates(true);
	}

	@Override
	public int getContainerSize() {
		return this.inventoryItems.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack inventoryItemstack : this.inventoryItems) {
			if (!inventoryItemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemStack getItem(int indexIn) {
		return this.inventoryItems.get(indexIn);
	}

	@Override
	public ItemStack removeItem(int indexIn, int countIn) {
		this.setChanged();
		return ContainerHelper.removeItem(this.inventoryItems, indexIn, countIn);
	}

	@Override
	public ItemStack removeItemNoUpdate(int indexIn) {
		return ContainerHelper.takeItem(this.inventoryItems, indexIn);
	}

	@Override
	public void setItem(int indexIn, ItemStack stackIn) {
		this.inventoryItems.set(indexIn, stackIn);
		
		if (stackIn.getCount() > this.getMaxStackSize()) {
			stackIn.setCount(this.getMaxStackSize());
		}
		
		this.setChanged();
	}

	@Override
	public boolean stillValid(Player playerIn) {
		if (this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		} else {
			return playerIn.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	public boolean canPlaceItem(int indexIn, ItemStack stackIn) {
		return true;
	}

	@Override
	public void clearContent() {
		this.inventoryItems.clear();
	}

	@Override
	public EnumUIMode getUIMode() {
		return this.uiMode;
	}

	@Override
	public void setUIMode(EnumUIMode modeIn) {
		this.uiMode = modeIn;
	}

	@Override
	public void cycleUIMode() {
		this.uiMode = EnumUIMode.getNextStateFromState(this.uiMode);
	}

	@Override
	public EnumUIHelp getUIHelp() {
		return this.uiHelp;
	}

	@Override
	public void setUIHelp(EnumUIHelp modeIn) {
		this.uiHelp = modeIn;
	}

	@Override
	public void cycleUIHelp() {
		this.uiHelp = EnumUIHelp.getNextStateFromState(this.uiHelp);
	}
}