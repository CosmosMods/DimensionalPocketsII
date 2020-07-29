package com.zeher.zeherlib.core.recipe;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.zeher.zeherlib.ZeherLib;

import net.minecraft.item.ItemStack;

public class OrePlantCleaningRecipes {
	
	private static final OrePlantCleaningRecipes INSTANCE = new OrePlantCleaningRecipes();
	
	private final Map<ItemStack, ItemStack> cleaning_list = Maps.<ItemStack, ItemStack>newHashMap();
	private final Map<ItemStack, Float> experience_list = Maps.<ItemStack, Float>newHashMap();

	public static OrePlantCleaningRecipes getInstance() {
		return INSTANCE;
	}

	private OrePlantCleaningRecipes() { }

	public void addCleaning(ItemStack input, ItemStack stack, float experience) {
		this.addCleaningRecipe(input, stack, experience);
	}

	public void addCleaningRecipe(ItemStack input, ItemStack stack, float experience) {
		if (getCleaningResult(input) != ItemStack.EMPTY) {
			ZeherLib.LOGGER.info(ZeherLib.LOGGER_PREFIX, "[OrePlantCleaning:addRecipe()] Ignored recipe with conflicting input: " + input + " = " + stack);
			return;
		}
		this.cleaning_list.put(input, stack);
		this.experience_list.put(stack, Float.valueOf(experience));
	}

	public ItemStack getCleaningResult(ItemStack stack) {
		for (Entry<ItemStack, ItemStack> entry : this.cleaning_list.entrySet()) {
			if (this.compareItemStacks(stack, (ItemStack) entry.getKey())) {
				return (ItemStack) entry.getValue();
			}
		}
		return ItemStack.EMPTY;
	}

	private boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
		return stack2.getItem() == stack1.getItem();
	}

	public Map<ItemStack, ItemStack> getcleaning_list() {
		return this.cleaning_list;
	}

	public float getCleaningExperience(ItemStack stack) {
		float ret = stack.getItem().getSmeltingExperience(stack);
		if (ret != -1)
			return ret;

		for (Entry<ItemStack, Float> entry : this.experience_list.entrySet()) {
			if (this.compareItemStacks(stack, (ItemStack) entry.getKey())) {
				return ((Float) entry.getValue()).floatValue();
			}
		}
		return 0.0F;
	}
}