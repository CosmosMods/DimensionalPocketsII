package com.zeher.zeherlib.api.client.container.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class SlotBucket extends Slot {

	public SlotBucket(IInventory tile, int par3, int par4, int par5) {
		super(tile, par3, par4, par5);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		if (stack != null) {
			FluidStack liquid = FluidUtil.getFluidContained(stack);
			if (liquid != null) {
				return true;
			} else if (stack.getItem().equals(Items.BUCKET)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	@Override
	public int getSlotStackLimit() {
		return 16;
	}
}