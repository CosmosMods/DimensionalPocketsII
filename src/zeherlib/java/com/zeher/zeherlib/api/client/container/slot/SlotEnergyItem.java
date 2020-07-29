package com.zeher.zeherlib.api.client.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class SlotEnergyItem extends Slot {
	
	public SlotEnergyItem(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}


	public boolean isItemValid(ItemStack stack) {
		/**
		if (stack.getItem() instanceof IEnergyContainerItem) {
			return true;
		}
		*/
		return false;
	}
}
