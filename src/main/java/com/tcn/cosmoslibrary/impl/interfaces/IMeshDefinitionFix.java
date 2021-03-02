package com.tcn.cosmoslibrary.impl.interfaces;

import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Used for {@link ModelResourceLocation} fixes.
 */
@OnlyIn(Dist.CLIENT)
@Deprecated
public interface IMeshDefinitionFix { //extends ItemMeshDefinition {
	/**
	ModelResourceLocation getLocation(ItemStack stack);

	static ItemMeshDefinition create(IMeshDefinitionFix lambda) {
		return lambda;
	}

	default ModelResourceLocation getModelLocation(ItemStack stack) {
		return getLocation(stack);
	} */
}