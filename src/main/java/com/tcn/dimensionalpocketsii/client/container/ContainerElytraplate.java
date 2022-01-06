package com.tcn.dimensionalpocketsii.client.container;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.client.container.slot.SlotArmourItem;
import com.tcn.cosmoslibrary.client.container.slot.SlotRestrictedAccess;
import com.tcn.cosmoslibrary.common.lib.CosmosChunkPos;
import com.tcn.dimensionalpocketsii.DimReference;
import com.tcn.dimensionalpocketsii.core.management.ModBusManager;
import com.tcn.dimensionalpocketsii.pocket.core.Pocket;
import com.tcn.dimensionalpocketsii.pocket.core.management.PocketRegistryManager;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ContainerElytraplate extends AbstractContainerMenu {

	private ItemStack stack;
	protected final Player player;
	private final Level world;
	private boolean screen;
	private Pocket pocket;
	
	public static ContainerElytraplate createContainerServerSide(int windowID, Inventory playerInventory, ItemStack stackIn, boolean screen) {
		Pocket pocket = null;
		
		if (stackIn.hasTag()) {
			CompoundTag compound = stackIn.getTag();
			
			if (compound.contains("nbt_data")) {
				CompoundTag nbtData = compound.getCompound("nbt_data");
				
				if (nbtData.contains("chunk_pos")) {
					CompoundTag chunkPos = nbtData.getCompound("chunk_pos");

					int x = chunkPos.getInt("x");
					int z = chunkPos.getInt("z");
					CosmosChunkPos chunk = new CosmosChunkPos(x, z);
					
					Pocket pocket_ = PocketRegistryManager.getPocketFromChunkPosition(chunk);
					
					if (pocket_ != null) {
						pocket = pocket_;
					}
				}
			}
		}
		
		if (pocket != null) {
			return new ContainerElytraplate(windowID, playerInventory, pocket, pocket, stackIn, screen);
		}
		
		return new ContainerElytraplate(windowID, playerInventory, new SimpleContainer(DimReference.CONSTANT.POCKET_HELD_ITEMS_SIZE), new Pocket(null), stackIn, screen);
	}

	public static ContainerElytraplate createContainerClientSide(int windowID, Inventory playerInventory, FriendlyByteBuf extraData) {
		CompoundTag tag = extraData.readNbt();
		return new ContainerElytraplate(windowID, playerInventory, new SimpleContainer(DimReference.CONSTANT.POCKET_HELD_ITEMS_SIZE), Pocket.readFromNBT(tag), extraData.readItem(), extraData.readBoolean());
	}
	
	protected ContainerElytraplate(int id, Inventory playerInventoryIn, Container pocket, @Nullable Pocket pocketActual, ItemStack stackIn, boolean screenIn) {
		super(ModBusManager.ELYTRAPLATE_CONTAINER_TYPE, id);
		
		this.stack = stackIn;
		this.player = playerInventoryIn.player;
		this.world = playerInventoryIn.player.level;
		this.screen = screenIn;
		
		if (pocketActual != null) {
			this.pocket = pocketActual;
		}
		
		if (this.screen) {
			/** - Pocket Inventory - */
			for (int y = 0; y < 10; y++) {
				for (int x = 0; x < 4; x++) {
					//0 - 39
					this.addSlot(new Slot(pocket, x + y * 4, 266 + x * 18, 17 + y * 18 ));
				}
			}
			
			/** - Pocket Interface Stacks - */
			for (int y = 0; y < 2; y++) {
				for (int x = 0; x < 4; x++) {
					//40 - 47
					this.addSlot(new SlotRestrictedAccess(pocket, 40 + x + y * 4, 266 + x * 18, 210 + y * 18, true, true));
				}
			}
			
			/** - Player Inventory - */
			for (int x = 0; x < 3; ++x) {
				for (int y = 0; y < 9; ++y) {
					this.addSlot(new Slot(playerInventoryIn, y + (x + 1) * 9, 92 + y * 18, 170 + x * 18));
				}
			}
	
			/** - Player Hotbar - */
			for (int x = 0; x < 9; ++x) {
				this.addSlot(new Slot(playerInventoryIn, x, 92 + x * 18, 228));
			}
		} else {
			//Player Inventory
			for (int k = 0; k < 3; ++k) {
				for (int i1 = 0; i1 < 9; ++i1) {
					this.addSlot(new Slot(playerInventoryIn, i1 + k * 9 + 9, 26 + i1 * 18, 65 + k * 18));
				}
			}

			//Player Hotbar
			for (int l = 0; l < 9; ++l) {
				this.addSlot(new Slot(playerInventoryIn, l, 26 + l * 18, 122));
			}
			
			//Armour Slots
			this.addSlot(new SlotArmourItem(playerInventoryIn, 39, 6, 65, this.player, 0));
			this.addSlot(new SlotArmourItem(playerInventoryIn, 38, 6, 84, this.player, 1));
			this.addSlot(new SlotArmourItem(playerInventoryIn, 37, 6, 103, this.player, 2));
			this.addSlot(new SlotArmourItem(playerInventoryIn, 36, 6, 122, this.player, 3));
		}
	}

	@Override
	public boolean stillValid(Player playerIn) {
		return true;
	}
	
	@Override
	public ItemStack quickMoveStack(Player playerIn, int indexIn) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = (Slot) this.slots.get(indexIn);
		boolean flag = slot.mayPickup(playerIn);
		
		if (!this.screen) {
			if (flag && slot != null && slot.hasItem()) {
				ItemStack itemstack1 = slot.getItem();
				itemstack = itemstack1.copy();
				
				if (indexIn >= this.slots.size() - 4 && indexIn < this.slots.size()) {
					if (!this.moveItemStackTo(itemstack1, 0, this.slots.size() - 4, false)) {
						return ItemStack.EMPTY;
					}
				} else if (indexIn > this.slots.size() - 13 && indexIn < this.slots.size() - 4) {
					if (itemstack.getItem() instanceof ArmorItem) {
						if (!this.moveItemStackTo(itemstack1, this.slots.size() - 4, this.slots.size(), false)) {
							if (!this.moveItemStackTo(itemstack1, 11, this.slots.size() - 13, false)) {
								return ItemStack.EMPTY;
							}
						}
					} else if (!this.moveItemStackTo(itemstack1, 0, this.slots.size() - 13, false)) {
						return ItemStack.EMPTY;
					}
				} else if (indexIn > 0 && indexIn < this.slots.size() - 13) {
					if (itemstack.getItem() instanceof ArmorItem) {
						if (!this.moveItemStackTo(itemstack1, this.slots.size() - 4, this.slots.size(), false)) {
							if (!this.moveItemStackTo(itemstack1, 11, this.slots.size() - 13, false)) {
								return ItemStack.EMPTY;
							}
						}
					} else if (!this.moveItemStackTo(itemstack1, this.slots.size() - 13, this.slots.size() - 4, false)) {
						return ItemStack.EMPTY;
					}
				}
				
				slot.onTake(playerIn, itemstack1);
				if (itemstack1.isEmpty()) {
					slot.set(ItemStack.EMPTY);
				} else {
					slot.setChanged();
				}
				return itemstack;
			}
		} else {
			if (flag && slot != null && slot.hasItem()) {
				ItemStack itemstack1 = slot.getItem();
				itemstack = itemstack1.copy();
				
				if (indexIn >= 0 && indexIn < 48) {
					if (!this.moveItemStackTo(itemstack1, 48, this.slots.size(), false)) {
						return ItemStack.EMPTY;
					}
				} else if (indexIn > 48 && indexIn < this.slots.size() - 9) {
					if (!this.moveItemStackTo(itemstack1, 0, 48, false)) {
						return ItemStack.EMPTY;
					}
				} else if (indexIn > this.slots.size() - 9 && indexIn < this.slots.size()) {
					if (!this.moveItemStackTo(itemstack1, 48, this.slots.size() - 9, false)) {
						return ItemStack.EMPTY;
					}
				}
				
				slot.onTake(playerIn, itemstack1);
				if (itemstack1.isEmpty()) {
					slot.set(ItemStack.EMPTY);
				} else {
					slot.setChanged();
				}
				return itemstack;
			}
		}
		return ItemStack.EMPTY;
	}

	public ItemStack getStack() {
		return this.stack;
	}

	public Level getLevel() {
		return this.world;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public boolean getScreen() {
		return this.screen;
	}
	
	public Pocket getPocket() {
		return this.pocket;
	}
}