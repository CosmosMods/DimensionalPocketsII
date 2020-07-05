package com.zeher.zeherlib.api.client.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotRestrictedAccess extends Slot {

	public int limit;

	public SlotRestrictedAccess(IInventory tile, int par3, int par4, int par5, int limit) {
		super(tile, par3, par4, par5);
		this.limit = limit;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {
		return false;
	}

	@Override
	public int getSlotStackLimit() {
		return this.limit;
	}
}