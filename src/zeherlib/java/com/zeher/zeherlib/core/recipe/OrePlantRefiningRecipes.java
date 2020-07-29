package com.zeher.zeherlib.core.recipe;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.zeher.zeherlib.ZeherLib;

import net.minecraft.item.ItemStack;

public class OrePlantRefiningRecipes {
	
	private static final OrePlantRefiningRecipes INSTANCE = new OrePlantRefiningRecipes();
	
	private final Map<ItemStack, ItemStack> cleaning_list = Maps.<ItemStack, ItemStack>newHashMap();
	private final Map<ItemStack, Float> experience_list = Maps.<ItemStack, Float>newHashMap();

	public static OrePlantRefiningRecipes getInstance() {
		return INSTANCE;
	}

	private OrePlantRefiningRecipes() { }

	public void addRefining(ItemStack input, ItemStack stack, float experience) {
		this.addRefiningRecipe(input, stack, experience);
	}

	public void addRefiningRecipe(ItemStack input, ItemStack stack, float experience) {
		if (getRefiningResult(input) != ItemStack.EMPTY) {
			ZeherLib.LOGGER.info(ZeherLib.LOGGER_PREFIX, "[OrePlantRefining:addRecipe()] Ignored recipe with conflicting input: " + input + " = " + stack);
			return;
		}
		this.cleaning_list.put(input, stack);
		this.experience_list.put(stack, Float.valueOf(experience));
	}

	public ItemStack getRefiningResult(ItemStack stack) {
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

	public Map<ItemStack, ItemStack> getRefiningList() {
		return this.cleaning_list;
	}

	public float getRefiningExperience(ItemStack stack) {
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