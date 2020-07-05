package com.zeher.zeherlib.api.client.container.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class SlotSpecifiedItem extends Slot {

	public Item item;
	public int limit;

	public SlotSpecifiedItem(IInventory tile, int par3, int par4, int par5, Item par6, int limit) {
		super(tile, par3, par4, par5);
		this.item = par6;
		this.limit = limit;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {
		if (par1ItemStack != null) {
			Item item = par1ItemStack.getItem();
			return (item != null) && (item == this.item);
		}
		return false;
	}

	@Override
	public int getSlotStackLimit() {
		return this.limit;
	}
}