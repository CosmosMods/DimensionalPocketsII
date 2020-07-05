package com.zeher.zehercraft.processing.client.container;

import com.zeher.zehercraft.core.handler.ItemHandler;
import com.zeher.zehercraft.processing.client.container.slot.SlotCompactor;
import com.zeher.zeherlib.api.client.container.slot.SlotUpgrade;
import com.zeher.zeherlib.core.recipe.CompactorRecipes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerCompactor extends Container {
	
	private IInventory inventory;
	private int cook_time;
	private int stored;

	public ContainerCompactor(InventoryPlayer invPlayer, IInventory tile) {
		this.inventory = tile;

		/**@Inputslot*/
		this.addSlotToContainer(new Slot(tile, 0, 104, 18));

		/**@Powerslot*/
		//this.addSlotToContainer(new SlotBattery(tile, 1, 77, 61));

		/**@OutputSlot*/
		this.addSlotToContainer(new SlotCompactor(invPlayer.player, tile, 2, 104, 58));

		/**@Upgradeslots*/
		this.addSlotToContainer(new SlotUpgrade(tile, 3, 56, 18, ItemHandler.UPGRADE_SPEED));
		this.addSlotToContainer(new SlotUpgrade(tile, 4, 56, 38, ItemHandler.UPGRADE_CAPACITY));
		this.addSlotToContainer(new SlotUpgrade(tile, 5, 56, 58, ItemHandler.UPGRADE_EFFICIENCY));
		
		/**@Inventory*/
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlotToContainer(new Slot(invPlayer, 9 + x + y * 9, 8 + x * 18, 95 + y * 18));
			}
		}
		
		/**@Actionbar*/
		for (int x = 0; x < 9; x++) {
			this.addSlotToContainer(new Slot(invPlayer, x, 8 + x * 18, 153));
		}
	}

	@Override
	public void addListener(IContainerListener containerListener) {
		super.addListener(containerListener);
		containerListener.sendAllWindowProperties(this, this.inventory);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (int i = 0; i < this.listeners.size(); i++) {
			IContainerListener containerlistener = (IContainerListener) this.listeners.get(i);

			if (this.stored != this.inventory.getField(1)) {
				containerlistener.sendWindowProperty(this, 1, this.inventory.getField(1));
			}
			if (this.cook_time != this.inventory.getField(0)) {
				containerlistener.sendWindowProperty(this, 0, this.inventory.getField(0));
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int value) {
		this.inventory.setField(id, value);
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return this.inventory.isUsableByPlayer(entityplayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = (Slot) this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index == 2) {
				if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index != 1 && index != 0) {
				if (!CompactorRecipes.getInstance().getCompactingResult(itemstack1).isEmpty()) {
					if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 3 && index < 30) {
					if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, itemstack1);
		}

		return itemstack;
	}
}