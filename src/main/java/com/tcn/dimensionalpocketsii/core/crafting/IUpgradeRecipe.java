package com.tcn.dimensionalpocketsii.core.crafting;

import java.util.List;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public interface IUpgradeRecipe {
	
	/**
	 * Checks if a recipe matches given the ItemStack.
	 * @param stack
	 * @return
	 */
	boolean matches(List<ItemStack> stack);
	
	/**
	 * Returns the output Stack.
	 */
	ItemStack getResult();
	
	/**
	 * Returns the Focus Stack.
	 */
	ItemStack getFocusStack();
	
	/**
	 * Returns the total recipe size.
	 */
	int getRecipeSize();

	/**
	 * Returns the output Stack.
	 */
	ItemStack getOutput();

	/**
	 * Returns the Input Stack List.
	 */
	List<ItemStack> getRecipeInput();
	
	NonNullList<ItemStack> getRemainingItems(List<ItemStack> inv);
	
}