package com.tcn.cosmoslibrary.impl.interfaces;

import java.util.List;

import com.tcn.cosmoslibrary.client.impl.tesr.EnumTESRColour;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Advanced recipe interface.
 */
public interface IMultiRecipe {
	
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
	 * Retrieves the Process Time.
	 */
	Integer getProcessTime();

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

	/**
	 * Returns the EnumTESRColour to be used when rendering lasers.
	 * @return
	 */
	EnumTESRColour getColour();	
}