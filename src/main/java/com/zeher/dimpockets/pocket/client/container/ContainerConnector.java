package com.zeher.dimpockets.pocket.client.container;

import java.util.ArrayList;

import com.zeher.dimpockets.pocket.core.tileentity.TileConnector;
import com.zeher.zeherlib.api.client.container.slot.SlotBucket;
import com.zeher.zeherlib.api.client.container.slot.SlotRestrictedAccess;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerConnector extends Container {
	
	private TileConnector tile_entity;
	
	public String name;
	public int stored;
	public ArrayList<String> list;
	
	private int[] DOWN = new int[] { 37, 151 };
	private int[] UP = new int[] { 37, 103 };
	private int[] NORTH = new int[] { 37, 127 };
	private int[] SOUTH = new int[] { 61, 151 };
	private int[] WEST = new int[] { 61, 127 };
	private int[] EAST = new int[] { 13, 127 };
	
	public ContainerConnector(InventoryPlayer invPlayer, TileConnector tile) {
		this.tile_entity = tile;

		/** - Pocket Inventory */
		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 4; x++) {
				//0 - 39
				this.addSlotToContainer(new Slot(tile, x + y * 4, 266 + x * 18, 17 + y * 18 ));
			}
		}

		/** - Interface Stacks */
		for (int y = 0; y < 2; y++) {
			for (int x = 0; x < 4; x++) {
				//40 - 48
				this.addSlotToContainer(new SlotRestrictedAccess(tile, 40 + x + y * 4, 266 + x * 18, 210 + y * 18));
			}
		}
		
		/** - Surrounding Stacks*/
		this.addSlotToContainer(new SlotRestrictedAccess(tile, 48, DOWN[0], DOWN[1]));
		this.addSlotToContainer(new SlotRestrictedAccess(tile, 49, UP[0], UP[1]));
		this.addSlotToContainer(new SlotRestrictedAccess(tile, 50, NORTH[0], NORTH[1]));
		this.addSlotToContainer(new SlotRestrictedAccess(tile, 51, SOUTH[0], SOUTH[1]));
		this.addSlotToContainer(new SlotRestrictedAccess(tile, 52, WEST[0], WEST[1]));
		this.addSlotToContainer(new SlotRestrictedAccess(tile, 53, EAST[0], EAST[1]));
		
		/** - Bucket Slots*/
		this.addSlotToContainer(new SlotBucket(tile, 54, 62, 188));
		this.addSlotToContainer(new SlotRestrictedAccess(tile, 55, 62, 208, 1, true));
		
		/** - Player Inventory*/
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlotToContainer(new Slot(invPlayer, 9 + x + y * 9, 92 + x * 18, 170 + y * 18));
			}
		}
		
		/** - Player ActionBar*/
		for (int x = 0; x < 9; x++) {
			this.addSlotToContainer(new Slot(invPlayer, x, 92 + x * 18, 228));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
	
	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
		return super.slotClick(slotId, dragType, clickTypeIn, player);
    }
	
	@Override
	public void addListener(IContainerListener containerListener) {
		super.addListener(containerListener);
		containerListener.sendAllWindowProperties(this, this.tile_entity);
	}

	@Override
	public void detectAndSendChanges() {
		for (int i = 0; i < this.listeners.size(); i++) {
			IContainerListener containerlistener = this.listeners.get(i);
			
			if (this.stored != this.tile_entity.getField(1)) {
				containerlistener.sendWindowProperty(this, 1, this.tile_entity.getField(1));
			}
			
			this.name = tile_entity.getPocket().getCreator();
			this.list = tile_entity.getPocket().getPlayerMap();
		}
		super.detectAndSendChanges();
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int value) {
		this.tile_entity.setField(id, value);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = (Slot) this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (index == 55) {
				if (!this.mergeItemStack(itemstack1, 55, this.inventorySlots.size() - 9, false)) {
					return ItemStack.EMPTY;
				}
			}
			
			else if (index < 4 * 10) {
				if (!this.mergeItemStack(itemstack1, 55, this.inventorySlots.size() - 9, false)) {
					return ItemStack.EMPTY;
				}
			} 
			
			else if (!this.mergeItemStack(itemstack1, 0, 4 * 10, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}
		return itemstack;
	}
}