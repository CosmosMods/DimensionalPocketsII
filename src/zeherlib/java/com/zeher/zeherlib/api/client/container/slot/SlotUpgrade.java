package com.zeher.zeherlib.api.client.container.slot;

import com.zeher.zeherlib.mod.item.ModItemUpgradeEnergy;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotUpgrade extends Slot {

	public Item item;

	public SlotUpgrade(IInventory tile, int index, int x, int y, Item upgrade) {
		super(tile, index, x, y);

		item = upgrade;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {
		if (par1ItemStack != null) {
			Item item = par1ItemStack.getItem();

			return item != null && item.equals(this.item);
		}
		return false;
	}

	@Override
	public int getSlotStackLimit() {
		return 4;
	}
}
