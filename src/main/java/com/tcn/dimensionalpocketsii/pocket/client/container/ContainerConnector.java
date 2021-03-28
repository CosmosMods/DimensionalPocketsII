package com.tcn.dimensionalpocketsii.pocket.client.container;

import com.tcn.cosmoslibrary.client.container.slot.SlotBucket;
import com.tcn.cosmoslibrary.client.container.slot.SlotRestrictedAccess;
import com.tcn.dimensionalpocketsii.core.management.CoreModBusManager;
import com.tcn.dimensionalpocketsii.pocket.core.tileentity.TileEntityConnector;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;

public class ContainerConnector extends Container {

	public static ContainerConnector createContainerServerSide(int windowID, PlayerInventory playerInventory, TileEntityConnector chestContents, BlockPos pos) {
		return new ContainerConnector(windowID, playerInventory, chestContents, pos);
	}

	public static ContainerConnector createContainerClientSide(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData) {
		BlockPos pos = extraData.readBlockPos();
		
		return new ContainerConnector(windowID, playerInventory, new TileEntityConnector(), pos);
	}
	
	private final World world;
	private final BlockPos pos;
	
	protected ContainerConnector(int id, PlayerInventory playerInventoryIn, TileEntityConnector contentsIn, BlockPos pos) {
		super(CoreModBusManager.CONNECTOR_CONTAINER_TYPE, id);
		
		this.pos = pos;
		this.world = playerInventoryIn.player.level;
		
		/** - Pocket Inventory */
		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 4; x++) {
				//0 - 39
				this.addSlot(new Slot(contentsIn, x + y * 4, 266 + x * 18, 17 + y * 18 ));
			}
		}
		
		/** - Interface Stacks */
		for (int y = 0; y < 2; y++) {
			for (int x = 0; x < 4; x++) {
				//40 - 48
				this.addSlot(new SlotRestrictedAccess(contentsIn, 40 + x + y * 4, 266 + x * 18, 210 + y * 18, false, true));
			}
		}
		
		/** - Surrounding Stacks*/
		this.addSlot(new SlotRestrictedAccess(contentsIn, 48, 26, 46, 1, false, false));
		this.addSlot(new SlotRestrictedAccess(contentsIn, 49, 26, 21, 1, false, false));
		this.addSlot(new SlotRestrictedAccess(contentsIn, 50, 26, 71, 1, false, false));
		this.addSlot(new SlotRestrictedAccess(contentsIn, 51, 26, 98, 1, false, false));
		this.addSlot(new SlotRestrictedAccess(contentsIn, 52, 26, 123, 1, false, false));
		this.addSlot(new SlotRestrictedAccess(contentsIn, 53, 26, 148, 1, false, false));
		
		/** - Bucket Slots*/
		this.addSlot(new SlotBucket(contentsIn, 54, 58, 178));
		this.addSlot(new SlotRestrictedAccess(contentsIn, 55, 58, 201, 1, false, true));
		
		/** - Player Inventory - */
		for (int x = 0; x < 3; ++x) {
			for (int y = 0; y < 9; ++y) {
				this.addSlot(new Slot(playerInventoryIn, y + (x + 1) * 9, 92 + y * 18, 170 + x * 18));
			}
		}

		/** - Player Hotbar - */
		for (int x = 0; x < 9; ++x) {
			this.addSlot(new Slot(playerInventoryIn, x, 92 + x * 18, 228));
		}
	}

	@Override
	public boolean stillValid(PlayerEntity playerIn) {
		return true;
	}
	
	public BlockPos getBlockPos() {
		return this.pos;
	}

	public World getWorld() {
		return this.world;
	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = (Slot) this.slots.get(index);
		
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			
			if (itemstack.getItem() instanceof BucketItem) {
				if (FluidUtil.getFluidContained(itemstack).isPresent()) {
					if (index > 4 * 10 && index != 55) {
						if (!this.moveItemStackTo(itemstack1, 54, index, false)) {
							return ItemStack.EMPTY;
						}
					}
				}
			}
			
			if (index == 55) {
				if (!this.moveItemStackTo(itemstack1, 55, this.slots.size() - 9, false)) {
					return ItemStack.EMPTY;
				}
			}
			
			else if (index < 4 * 10) {
				if (!this.moveItemStackTo(itemstack1, 55, this.slots.size() - 9, false)) {
					return ItemStack.EMPTY;
				}
			} 
			
			else if (!this.moveItemStackTo(itemstack1, 0, 4 * 10, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}
		return itemstack;
	}
}