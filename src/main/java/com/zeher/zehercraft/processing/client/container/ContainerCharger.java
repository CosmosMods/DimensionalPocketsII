package com.zeher.zehercraft.processing.client.container;

import com.zeher.zehercraft.core.handler.ItemHandler;
import com.zeher.zeherlib.api.client.container.slot.SlotEnergyItem;
import com.zeher.zeherlib.api.client.container.slot.SlotUpgrade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerCharger extends Container {
	private IInventory inventory;

	private int stored;

	public ContainerCharger(InventoryPlayer invPlayer, IInventory tile) {
		this.inventory = tile;
		
		//this.addSlotToContainer(new SlotEnergyItem(tile, 0, 56, 61));
		
		this.addSlotToContainer(new SlotEnergyItem(tile, 0, 84, 18));
		this.addSlotToContainer(new SlotEnergyItem(tile, 1, 104, 18));
		this.addSlotToContainer(new SlotEnergyItem(tile, 2, 124, 18));
		
		this.addSlotToContainer(new SlotEnergyItem(tile, 3, 84, 38));
		this.addSlotToContainer(new SlotEnergyItem(tile, 4, 104, 38));
		this.addSlotToContainer(new SlotEnergyItem(tile, 5, 124, 38));
		
		this.addSlotToContainer(new SlotEnergyItem(tile, 6, 84, 58));
		this.addSlotToContainer(new SlotEnergyItem(tile, 7, 104, 58));
		this.addSlotToContainer(new SlotEnergyItem(tile, 8, 124, 58));
		
		/**@Upgrades*/
		this.addSlotToContainer(new SlotUpgrade(tile, 9, 36, 18, ItemHandler.UPGRADE_SPEED));
		this.addSlotToContainer(new SlotUpgrade(tile, 10, 36, 38, ItemHandler.UPGRADE_CAPACITY));
		this.addSlotToContainer(new SlotUpgrade(tile, 11, 36, 58, ItemHandler.UPGRADE_EFFICIENCY));
		
		/*
		this.addSlotToContainer(new Slot(invPlayer, 39, 173, 6));
		this.addSlotToContainer(new Slot(invPlayer, 38, 173, 25));
		this.addSlotToContainer(new Slot(invPlayer, 37, 173, 44));
		this.addSlotToContainer(new Slot(invPlayer, 36, 173, 63));
		 */
		
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

			if (this.stored != this.inventory.getField(0)) {
				containerlistener.sendWindowProperty(this, 0, this.inventory.getField(0));
			}
		}
		this.stored = this.inventory.getField(0);
	}

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
				return ItemStack.EMPTY;
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