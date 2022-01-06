package com.tcn.dimensionalpocketsii.pocket.client.container;

import com.tcn.cosmoslibrary.client.container.CosmosContainerMenuBlockEntity;
import com.tcn.cosmoslibrary.client.container.slot.SlotBooleanItem;
import com.tcn.cosmoslibrary.client.container.slot.SlotRestrictedAccess;
import com.tcn.dimensionalpocketsii.core.management.ModBusManager;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerModuleUpgradeStation extends CosmosContainerMenuBlockEntity {
	
	public ContainerModuleUpgradeStation(int indexIn, Inventory playerInventoryIn, FriendlyByteBuf extraData) {
		this(indexIn, playerInventoryIn, new SimpleContainer(9), ContainerLevelAccess.NULL, extraData.readBlockPos());
	}

	public ContainerModuleUpgradeStation(int indexIn, Inventory playerInventoryIn, Container contentsIn, ContainerLevelAccess accessIn, BlockPos posIn) {
		super(ModBusManager.UPGRADE_STATION_CONTAINER_TYPE, indexIn, playerInventoryIn, accessIn, posIn);
		
		//Focus Slot
		this.addSlot(new SlotBooleanItem(contentsIn, 0, 70, 42, true, 1));

		//Input Slots
		this.addSlot(new SlotBooleanItem(contentsIn, 1, 49, 21, true, 1));
		this.addSlot(new SlotBooleanItem(contentsIn, 2, 70, 21, true, 1));
		this.addSlot(new SlotBooleanItem(contentsIn, 3, 91, 21, true, 1));
		this.addSlot(new SlotBooleanItem(contentsIn, 4, 49, 63, true, 1));
		this.addSlot(new SlotBooleanItem(contentsIn, 5, 70, 63, true, 1));
		this.addSlot(new SlotBooleanItem(contentsIn, 6, 91, 63, true, 1));

		//Output Slot
		this.addSlot(new SlotRestrictedAccess(contentsIn, 7, 119, 42, false, true));
		
		//Preview Slot
		this.addSlot(new SlotRestrictedAccess(contentsIn, 8, 119, 21, false, false));
		
		//Player Inventory
		for (int k = 0; k < 3; ++k) {
			for (int i1 = 0; i1 < 9; ++i1) {
				this.addSlot(new Slot(playerInventoryIn, i1 + k * 9 + 9, 12 + i1 * 18, 102 + k * 18));
			}
		}

		//Player Hotbar
		for (int l = 0; l < 9; ++l) {
			this.addSlot(new Slot(playerInventoryIn, l, 12 + l * 18, 160));
		}
	}

	public void addSlotListener(ContainerListener listenerIn) {
		super.addSlotListener(listenerIn);
	}

	@OnlyIn(Dist.CLIENT)
	public void removeSlotListener(ContainerListener listenerIn) {
		super.removeSlotListener(listenerIn);
	}

	@Override
	public void slotsChanged(Container inventoryIn) {
		super.slotsChanged(inventoryIn);
		this.broadcastChanges();
	}
	
	@Override
	public void removed(Player playerIn) {
		super.removed(playerIn);
	}

	@Override
	public boolean stillValid(Player playerIn) {
		return stillValid(this.access, playerIn, ModBusManager.BLOCK_WALL_UPGRADE_STATION);
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int indexIn) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(indexIn);
		boolean flag = slot.mayPickup(playerIn);
		
		if (flag && slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			
			if (indexIn >= 0 && indexIn <= 7) {
				if (!this.moveItemStackTo(itemstack1, 8, this.slots.size() - 9, false)) {
					return ItemStack.EMPTY;
				}
			} else if (indexIn > 7 && indexIn < this.slots.size()) {
				if (!this.moveItemStackTo(itemstack1, 0, 7, false)) {
					return ItemStack.EMPTY;
				}
			}
			
			slot.onTake(playerIn, itemstack1);
			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}
		return itemstack;
	}
}