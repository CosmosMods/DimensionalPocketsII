package com.zeher.zeherlib.core.recipe;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.zeher.zeherlib.ZeherLib;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;

public class FluidCrafterCraftRecipes {
	
	private static final FluidCrafterCraftRecipes INSTANCE = new FluidCrafterCraftRecipes();
	
	private final Map<ItemStack, ItemStack> recipe_list = Maps.<ItemStack, ItemStack>newHashMap();
	private final Map<ItemStack, Fluid> fluid_list = Maps.<ItemStack, Fluid>newHashMap();
	private final Map<ItemStack, Float> experience_list = Maps.<ItemStack, Float>newHashMap();

	public static FluidCrafterCraftRecipes getInstance() {
		return INSTANCE;
	}

	private FluidCrafterCraftRecipes() { }

	public void addRecipe(ItemStack input, ItemStack stack, Fluid fluid, float experience) {
		if (getRecipeResult(input) != ItemStack.EMPTY) {
			ZeherLib.LOGGER.info(ZeherLib.LOGGER_PREFIX, "[FluidCrafterCraft:addRecipe()] Ignored recipe with conflicting input: " + input + " = " + stack);
			return;
		}
		this.recipe_list.put(input, stack);
		this.fluid_list.put(input, fluid);
		this.experience_list.put(stack, Float.valueOf(experience));
	}

	public ItemStack getRecipeResult(ItemStack stack) {
		for (Entry<ItemStack, ItemStack> entry : this.recipe_list.entrySet()) {
			if (this.compareItemStacks(stack, (ItemStack) entry.getKey())) {
				return (ItemStack) entry.getValue();
			}
		}
		return ItemStack.EMPTY;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public Fluid getFluid(ItemStack stack) {
		for(int x = 0; x < this.recipe_list.size(); x++){
			if(this.recipe_list.get(x).equals(stack)){
				Fluid fluid = this.fluid_list.get(x);
				return fluid;
			}
		}
		return null;
	}
	
	private boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
		return stack2.getItem() == stack1.getItem();
	}

	public Map<ItemStack, ItemStack> getGrindingList() {
		return this.recipe_list;
	}
	
	public Map<ItemStack, Fluid> getfluid_list(){
		return this.fluid_list;
	}

	public float getGrindingExperience(ItemStack stack) {
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