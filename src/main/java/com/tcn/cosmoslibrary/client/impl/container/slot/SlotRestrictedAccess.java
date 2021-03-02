package com.tcn.cosmoslibrary.client.impl.container.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class SlotRestrictedAccess extends Slot {

	public int limit;
	public boolean take;

	public SlotRestrictedAccess(IInventory tile, int par3, int par4, int par5, int limit, boolean take) {
		super(tile, par3, par4, par5);
		this.limit = limit;
		this.take = take;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {
		return false;
	}

	@Override
	public int getSlotStackLimit() {
		return this.limit;
	}
	
	@Override
	public boolean canTakeStack(PlayerEntity playerIn) {
		return take;
	}
}