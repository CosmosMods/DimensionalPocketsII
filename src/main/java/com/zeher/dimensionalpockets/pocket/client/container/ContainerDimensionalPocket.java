package com.zeher.dimensionalpockets.pocket.client.container;

import java.util.ArrayList;

import com.zeher.dimensionalpockets.pocket.tileentity.TileEntityDimensionalPocket;
import com.zeher.zeherlib.api.client.container.slot.SlotRestrictedAccess;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerDimensionalPocket extends Container {
	
	private TileEntityDimensionalPocket tile_entity;
	
	public String name;
	public int stored;
	public ArrayList<String> list;
	
	private int[] DOWN = new int[] { 37, 145 };
	private int[] UP = new int[] { 37, 101 };
	private int[] NORTH = new int[] { 37, 123 };
	private int[] SOUTH = new int[] { 59, 145 };
	private int[] WEST = new int[] { 59, 123 };
	private int[] EAST = new int[] { 15, 123 };
	
	private int[] POCKET_INV = new int[] { 
			19, 101, 37, 101, 55, 101, 
			19, 119, 37, 119, 55, 119, 
			19, 137, 37, 137, 55, 137};

	public ContainerDimensionalPocket(InventoryPlayer invPlayer, TileEntityDimensionalPocket tile) {
		this.tile_entity = tile;
		
		this.addSlotToContainer(new Slot(tile, 0, POCKET_INV[0], POCKET_INV[1]));
		this.addSlotToContainer(new Slot(tile, 1, POCKET_INV[2], POCKET_INV[3]));
		this.addSlotToContainer(new Slot(tile, 2, POCKET_INV[4], POCKET_INV[5]));
		this.addSlotToContainer(new Slot(tile, 3, POCKET_INV[6], POCKET_INV[7]));
		this.addSlotToContainer(new Slot(tile, 4, POCKET_INV[8], POCKET_INV[9]));
		this.addSlotToContainer(new Slot(tile, 5, POCKET_INV[10], POCKET_INV[11]));
		this.addSlotToContainer(new Slot(tile, 6, POCKET_INV[12], POCKET_INV[13]));
		this.addSlotToContainer(new Slot(tile, 7, POCKET_INV[14], POCKET_INV[15]));
		this.addSlotToContainer(new Slot(tile, 8, POCKET_INV[16], POCKET_INV[17]));
		
		/**@Inventory*/
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlotToContainer(new Slot(invPlayer, 9 + x + y * 9, 92 + x * 18, 170 + y * 18));
			}
		}
		
		/**@Actionbar*/
		for (int x = 0; x < 9; x++) {
			this.addSlotToContainer(new Slot(invPlayer, x, 92 + x * 18, 228));
		}
	}

	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		return ItemStack.EMPTY;
	}

	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
	
	@Override
	public void addListener(IContainerListener containerListener) {
		super.addListener(containerListener);
		containerListener.sendAllWindowProperties(this, this.tile_entity);
	}

	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (int i = 0; i < this.listeners.size(); i++) {
			IContainerListener containerlistener = this.listeners.get(i);
			
			if (this.stored != this.tile_entity.getField(1)) {
				containerlistener.sendWindowProperty(this, 1, this.tile_entity.getField(1));
			}
			
			this.name = tile_entity.getPocket().getCreator();
			
			this.list = tile_entity.getPocket().getAllowedPlayers();
		}
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int value) {
		this.tile_entity.setField(id, value);
	}
	
}