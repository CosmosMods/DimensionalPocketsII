package com.zeher.zeherlib.core.recipe;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.zeher.zeherlib.ZeherLib;

import net.minecraft.item.ItemStack;

public class GrinderRecipes {
	
	private static final GrinderRecipes INSTANCE = new GrinderRecipes();
	
	private final Map<ItemStack, ItemStack> grinding_list = Maps.<ItemStack, ItemStack>newHashMap();
	private final Map<ItemStack, ItemStack> secondary_list = Maps.<ItemStack, ItemStack>newHashMap();
	private final Map<ItemStack, Float> experience_list = Maps.<ItemStack, Float>newHashMap();

	public static GrinderRecipes getInstance() {
		return INSTANCE;
	}

	private GrinderRecipes() { }

	public void addGrinding(ItemStack input, ItemStack stack, ItemStack second, float experience) {
		this.addGrindingRecipe(input, stack, second, experience);
	}

	public void addGrindingRecipe(ItemStack input, ItemStack stack, ItemStack second, float experience) {
		if (this.getGrindingResult(input) != ItemStack.EMPTY) {
			ZeherLib.LOGGER.info(ZeherLib.LOGGER_PREFIX, "[Grinder:addRecipe()] Ignored recipe with conflicting input: " + input + " = " + stack);
			return;
		}
		this.grinding_list.put(input, stack);
		this.secondary_list.put(input, second);
		this.experience_list.put(stack, experience);
	}

	public ItemStack getGrindingResult(ItemStack input) {
		for (Entry<ItemStack, ItemStack> entry : this.grinding_list.entrySet()) {
			if (this.compareItemStacks(input, entry.getKey())) {
				return entry.getValue();
			}
		}
		return ItemStack.EMPTY;
	}
	
	public ItemStack getSecondaryOutput(ItemStack input) {
		for (Entry<ItemStack, ItemStack> entry : this.secondary_list.entrySet()) {
			if (this.compareItemStacks(input, entry.getKey())) {
				return entry.getValue();
			}
		}
		return ItemStack.EMPTY;
	}

	private boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
		return stack2.getItem() == stack1.getItem();
	}

	public Map<ItemStack, ItemStack> getGrindingList() {
		return this.grinding_list;
	}
	
	public Map<ItemStack, ItemStack> getSecondaryList(){
		return this.secondary_list;
	}

	public float getGrindingExperience(ItemStack stack) {
		for (Entry<ItemStack, Float> entry : this.experience_list.entrySet()) {
			if (this.compareItemStacks(stack, entry.getKey())) {
				return entry.getValue().floatValue();
			}
		}
		return 0.0F;
	}
}