package com.zeher.zehercraft.core.handler;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabHandler {
	
	/**
	 * Tab for Blocks.
	 */
	public static final CreativeTabs TAB_BLOCKS = new CreativeTabs(CreativeTabs.getNextID(), "tab_zc_blocks") {
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem() {
			return new ItemStack(BlockHandler.BASE.BLOCK_REINFORCEDBRICK);
		}
	};
	
	/**
	 * Tab for Items.
	 */
	public static final CreativeTabs TAB_ITEMS = new CreativeTabs(CreativeTabs.getNextID(), "tab_zc_items") {
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem() {
			return new ItemStack(ItemHandler.MACHINE_WRENCH);
		}
	};
	
	/**
	 * Tab for all devices.
	 */
	public static final CreativeTabs TAB_DEVICES = new CreativeTabs(CreativeTabs.getNextID(), "tab_zc_devices") {
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem() {
			return new ItemStack(BlockHandler.PROCESSING.BLOCK_STRUCTURE);
		}
	};
}