package com.zeher.dimpockets.pocket.core.block;

import com.zeher.zeherlib.api.compat.core.impl.ItemBlockNBT;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class ItemBlockPocket extends ItemBlockNBT {

	public ItemBlockPocket(Block block, Item.Properties prop, String info, String shift_desc_one, String shift_desc_two) {
		super(block, prop, info, shift_desc_one, shift_desc_two);
	}

}