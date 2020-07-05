package com.zeher.zeherlib.core.recipe;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class FluidCrafterMeltRecipes {
	
	private static final FluidCrafterMeltRecipes INSTANCE = new FluidCrafterMeltRecipes();
	
	private final Map<ItemStack, FluidStack> recipe_list = Maps.<ItemStack, FluidStack>newHashMap();

	public static FluidCrafterMeltRecipes getInstance() {
		return INSTANCE;
	}

	private FluidCrafterMeltRecipes() {
	}
	
	public void addRecipe(ItemStack input, FluidStack output) {
		if (getRecipeResult(input) != null) {
			net.minecraftforge.fml.common.FMLLog.info("Ignored recipe recipe with conflicting input: " + input + " = " + output);
			return;
		}
		this.recipe_list.put(input, output);
	}

	public FluidStack getRecipeResult(ItemStack stack) {
		for (Entry<ItemStack, FluidStack> entry : this.recipe_list.entrySet()) {
			if (this.compareItemStacks(stack, (ItemStack) entry.getKey())) {
				return (FluidStack) entry.getValue();
			}
		}
		return null;
	}

	private boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
		return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
	}

	public Map<ItemStack, FluidStack> getrecipe_list() {
		return this.recipe_list;
	}

	
}