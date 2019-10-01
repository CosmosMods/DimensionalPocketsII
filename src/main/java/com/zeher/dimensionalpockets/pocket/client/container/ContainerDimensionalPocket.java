package com.zeher.dimensionalpockets.pocket.client.container;

import java.util.ArrayList;

import com.zeher.dimensionalpockets.pocket.client.tileentity.TileEntityDimensionalPocket;
import com.zeher.zeherlib.client.slot.SlotFake;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerDimensionalPocket extends Container {
	private TileEntityDimensionalPocket tile_entity;
	
	public String name;
	public ArrayList<String> list;

	public ContainerDimensionalPocket(InventoryPlayer invPlayer, TileEntityDimensionalPocket tile) {
		this.tile_entity = tile;
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
			
			this.name = tile_entity.getPocket().getCreator();
			
			this.list = tile_entity.getPocket().getAllowedPlayers();
		}
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int value) {
		//this.tile_entity.setField(id, value);
	}
	
}