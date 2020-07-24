package com.zeher.dimpockets.core.manager;

import com.zeher.zeherlib.core.itemgroup.ModItemGroup;

import net.minecraft.creativetab.CreativeTabs;

public class ModItemGroupManager {
	
	public static final CreativeTabs GROUP_DIM = new ModItemGroup(CreativeTabs.getNextID(), "tab_dimensionalpockets", ModBlockManager.BLOCK_DIMENSIONAL_POCKET);

}