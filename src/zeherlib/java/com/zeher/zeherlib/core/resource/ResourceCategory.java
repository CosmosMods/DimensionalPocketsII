package com.zeher.zeherlib.core.resource;

import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraftforge.fml.common.Mod;

/**
 * Used for {@link ModelResourceLocation} customisation.
 * Specifically used to identify between certain areas of your {@link Mod}.
 */
public enum ResourceCategory {
	
	BASE("base/"),
	MATERIAL("material/"),
	ENERGY("energy/"),
	TOOL("tool/"),
	PROCESSING("processing/"),
	PRODUCTION("production/"),
	STORAGE("storage/"),
	TRANSPORT("transport/");
	
	public final String basic_name;
	
	private ResourceCategory(String name) {
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