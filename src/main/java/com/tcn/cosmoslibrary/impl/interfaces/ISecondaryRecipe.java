package com.tcn.cosmoslibrary.impl.interfaces;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface ISecondaryRecipe {
	
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
	ItemStack getSecondaryResult();
	
	/**
	 * Returns the total recipe size.
	 */
	int getRecipeSize();

	/**
	 * Returns the Input Stack List.
	 */
	List<ItemStack> getRecipeInputList();
	
	NonNullList<ItemStack> getRemainingItems(List<ItemStack> inv);
}