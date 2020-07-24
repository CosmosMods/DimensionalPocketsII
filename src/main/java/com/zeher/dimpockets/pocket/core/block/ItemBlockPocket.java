package com.zeher.dimpockets.pocket.core.block;

import com.zeher.zeherlib.api.compat.core.impl.ItemBlockNBT;

import net.minecraft.block.Block;

public class ItemBlockPocket extends ItemBlockNBT {

	public ItemBlockPocket(Block block, String info, String shift_desc_one, String shift_desc_two) {
		super(block, info, shift_desc_one, shift_desc_two);
		
		this.setMaxStackSize(1);
	}
}