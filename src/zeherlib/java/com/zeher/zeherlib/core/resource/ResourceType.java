package com.zeher.zeherlib.core.resource;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.Item;

/**
 * Used for {@link ModelResourceLocation} customisation. Specifically used to
 * identify between {@link Block} & {@link Item}.
 */
public enum ResourceType {
	
	BLOCK("block/"), 
	ITEM("item/");

	public final String basic_name;

	private ResourceType(String name) {
		this.basic_name = name;
	}

	public String getName() {
		return this.basic_name;
	}

	@Override
	public String toString() {
		return this.basic_name;
	}
}