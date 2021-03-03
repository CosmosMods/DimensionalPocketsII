package com.tcn.cosmoslibrary.client.impl.container.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;

public class SlotCopiedItem extends Slot {

	public SlotCopiedItem(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	@Override
	public boolean canTakeStack(PlayerEntity playerIn) {
		return false;
	}
}