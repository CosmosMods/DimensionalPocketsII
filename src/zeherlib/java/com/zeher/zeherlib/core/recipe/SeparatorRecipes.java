package com.zeher.zeherlib.core.recipe;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.zeher.zeherlib.ZeherLib;

import net.minecraft.item.ItemStack;

public class SeparatorRecipes {
	
	private static final SeparatorRecipes INSTANCE = new SeparatorRecipes();
	
	private final Map<ItemStack, ItemStack> separating_list = Maps.<ItemStack, ItemStack>newHashMap();
	private final Map<ItemStack, ItemStack> secondary_list = Maps.<ItemStack,ItemStack>newHashMap();
	private final Map<ItemStack, Float> experience_list = Maps.<ItemStack, Float>newHashMap();

	public static SeparatorRecipes getInstance() {
		return INSTANCE;
	}

	private SeparatorRecipes() { }

	public void addSeparating(ItemStack input, ItemStack stack, ItemStack second, float experience) {
		this.addSeparatingRecipe(input, stack, second, experience);
	}

	public void addSeparatingRecipe(ItemStack input, ItemStack stack, ItemStack second, float experience) {
		if (this.getSeparatingResult(input) != ItemStack.EMPTY && this.getSecondaryOutput(second) != ItemStack.EMPTY) {
			ZeherLib.LOGGER.info(ZeherLib.LOGGER_PREFIX, "[Separator:addRecipe()] Ignored recipe with conflicting input: " + input + " = " + stack);
			return;
		}
		this.separating_list.put(input, stack);
		this.secondary_list.put(input, second);
		this.experience_list.put(stack, Float.valueOf(experience));
	}

	public ItemStack getSeparatingResult(ItemStack stack) {
		for (Entry<ItemStack, ItemStack> entry : this.separating_list.entrySet()) {
			if (this.compareItemStacks(stack, (ItemStack) entry.getKey())) {
				return (ItemStack) entry.getValue();
			}
		}
		return ItemStack.EMPTY;
	}
	
	public ItemStack getSecondaryOutput(ItemStack stack) {
		for (Entry<ItemStack, ItemStack> entry : this.secondary_list.entrySet()) {
			if (this.compareItemStacks(stack, (ItemStack) entry.getKey())) {
				return (ItemStack) entry.getValue();
			}
		}
		return ItemStack.EMPTY;
	}

	private boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
		return stack2.getItem() == stack1.getItem();
	}

	public Map<ItemStack, ItemStack> getSeperatingList() {
		return this.separating_list;
	}
	
	public Map<ItemStack, ItemStack> getSecondaryList(){
		return this.secondary_list;
	}

	public float getSeparatingExperience(ItemStack stack) {
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