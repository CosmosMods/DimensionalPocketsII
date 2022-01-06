package com.tcn.dimensionalpocketsii.pocket.client.container;

import com.tcn.cosmoslibrary.client.container.CosmosContainerMenuBlockEntity;
import com.tcn.cosmoslibrary.client.container.slot.SlotArmourItem;
import com.tcn.cosmoslibrary.client.container.slot.SlotColourItem;
import com.tcn.cosmoslibrary.client.container.slot.SlotColourableArmourItem;
import com.tcn.cosmoslibrary.client.container.slot.SlotRestrictedAccess;
import com.tcn.dimensionalpocketsii.core.item.armour.module.IModuleItem;
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
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerModuleArmourWorkbench extends CosmosContainerMenuBlockEntity {
	
	public ContainerModuleArmourWorkbench(int indexIn, Inventory playerInventoryIn, FriendlyByteBuf extraData) {
		this(indexIn, playerInventoryIn, new SimpleContainer(11), ContainerLevelAccess.NULL, extraData.readBlockPos());
	}

	public ContainerModuleArmourWorkbench(int indexIn, Inventory playerInventoryIn, Container contentsIn, ContainerLevelAccess accessIn, BlockPos posIn) {
		super(ModBusManager.ARMOUR_WORKBENCH_CONTAINER_TYPE, indexIn, playerInventoryIn, accessIn, posIn);
		
		//Armour Slot
		this.addSlot(new SlotColourableArmourItem(contentsIn, 0, 116, 32, 1));
		
		//Colour Slots
		this.addSlot(new SlotColourItem(contentsIn, 1, 93, 21, ModBusManager.DIMENSIONAL_SHARD, 1));
		this.addSlot(new SlotColourItem(contentsIn, 2, 93, 42, ModBusManager.DIMENSIONAL_SHARD, 1));

		//Preview Slot
		this.addSlot(new SlotRestrictedAccess(contentsIn, 3, 139, 21, false, false));
		this.addSlot(new SlotRestrictedAccess(contentsIn, 4, 139, 42, false, false));

		//Module Slots
		this.addSlot(new SlotModuleItem(contentsIn, 5,  29, 21));
		this.addSlot(new SlotModuleItem(contentsIn, 6,  50, 21));
		this.addSlot(new SlotModuleItem(contentsIn, 7,  71, 21));
		this.addSlot(new SlotModuleItem(contentsIn, 8,  29, 42));
		this.addSlot(new SlotModuleItem(contentsIn, 9,  50, 42));
		this.addSlot(new SlotModuleItem(contentsIn, 10, 71, 42));

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

		//Armour Slots
		this.addSlot(new SlotArmourItem(playerInventoryIn, 39, 21, 63, this.player, 0));
		this.addSlot(new SlotArmourItem(playerInventoryIn, 38, 42, 63, this.player, 1));
		this.addSlot(new SlotArmourItem(playerInventoryIn, 37, 63, 63, this.player, 2));
		this.addSlot(new SlotArmourItem(playerInventoryIn, 36, 84, 63, this.player, 3));
		
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
		return stillValid(this.access, playerIn, ModBusManager.BLOCK_WALL_ARMOUR_WORKBENCH);
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int indexIn) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(indexIn);
		boolean flag = slot.mayPickup(playerIn);
		
		if (flag && slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			
			if (indexIn == 0) {
				if (itemstack.getItem() instanceof ArmorItem) {
					if (!this.moveItemStackTo(itemstack1, this.slots.size() - 4, this.slots.size(), false)) {
						if (!this.moveItemStackTo(itemstack1, 11, this.slots.size() - 13, false)) {
							return ItemStack.EMPTY;
						}
					}
				} else {
					if (!this.moveItemStackTo(itemstack1, 11, this.slots.size() - 13, false)) {
						return ItemStack.EMPTY;
					}
				}
			} else if (indexIn >= 1 && indexIn <= 2) {
				if (!this.moveItemStackTo(itemstack1, 11, this.slots.size() - 13, false)) {
					return ItemStack.EMPTY;
				}
			} else if (indexIn >= 3 && indexIn <= 10) {
				if (!this.moveItemStackTo(itemstack1, 11, this.slots.size() - 13, false)) {
					return ItemStack.EMPTY;
				}
			} else if (indexIn >= 11 && indexIn < this.slots.size()) {
				if (itemstack.getItem() instanceof ArmorItem) {
					if (!this.moveItemStackTo(itemstack, 0, 1, false)) {
						if (!this.moveItemStackTo(itemstack1, this.slots.size() - 4, this.slots.size(), false)) {
							if (!this.moveItemStackTo(itemstack1, 11, this.slots.size() - 13, false)) {
								return ItemStack.EMPTY;
							}
						}
					}
					slot.set(ItemStack.EMPTY);
				} else if (itemstack.getItem() instanceof IModuleItem) {
					if (!this.moveItemStackTo(itemstack, 3, 11, false)) {
						return ItemStack.EMPTY;
					}
				
					slot.set(ItemStack.EMPTY);
				} else if (DyeColor.getColor(itemstack) != null || itemstack.getItem().equals(ModBusManager.DIMENSIONAL_SHARD)) {
					if (!this.moveItemStackTo(itemstack, 0, 3, false)) {
						return ItemStack.EMPTY;
					}
					slot.set(ItemStack.EMPTY);
				} else if (itemstack.getItem() instanceof ArmorItem && indexIn < this.slots.size() - 4) {
					if (!this.moveItemStackTo(itemstack1, this.slots.size() - 4, this.slots.size(), false)) {
						return ItemStack.EMPTY;
					}
				} else {
					if (indexIn > this.slots.size() - 13 && indexIn < this.slots.size()) {
						if (!this.moveItemStackTo(itemstack1, 11, this.slots.size() - 13, false)) {
							return ItemStack.EMPTY;
						}
					}
					
					if (indexIn > 8 && indexIn < this.slots.size() - 13) {
						if (!this.moveItemStackTo(itemstack1, this.slots.size() - 13, this.slots.size(), false)) {
							return ItemStack.EMPTY;
						}
					}
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