package com.zeher.zeherlib.core.recipe;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import net.minecraft.item.ItemStack;

public class CompactorRecipes {
	
	private static final CompactorRecipes INSTANCE = new CompactorRecipes();
	
	private final Map<ItemStack, ItemStack> compacting_list = Maps.<ItemStack, ItemStack>newHashMap();
	private final Map<ItemStack, Float> experience_list = Maps.<ItemStack, Float>newHashMap();

	public static CompactorRecipes getInstance() {
		return INSTANCE;
	}

	private CompactorRecipes() { }

	public void addCompacting(ItemStack input, ItemStack stack, float experience) {
		this.addCompactingRecipe(input, stack, experience);
	}

	public void addCompactingRecipe(ItemStack input, ItemStack stack, float experience) {
		if (getCompactingResult(input) != ItemStack.EMPTY) {
		System.out.println("Ignored conflicting recipe: [Compactor] {" + input + " = " + stack + "}");
			return;
		}
		
		this.compacting_list.put(input, stack);
		this.experience_list.put(stack, Float.valueOf(experience));
	}

	public ItemStack getCompactingResult(ItemStack stack) {
		for (Entry<ItemStack, ItemStack> entry : this.compacting_list.entrySet()) {
			if (this.compareItemStacks(stack, (ItemStack) entry.getKey())) {
				return (ItemStack) entry.getValue();
			}
		}
		return ItemStack.EMPTY;
	}

	private boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
		return stack2.getItem() == stack1.getItem();
	}

	public Map<ItemStack, ItemStack> getCompactingList() {
		return this.compacting_list;
	}

	public float getCompactingExperience(ItemStack stack) {
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