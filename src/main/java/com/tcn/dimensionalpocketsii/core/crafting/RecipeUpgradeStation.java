package com.tcn.dimensionalpocketsii.core.crafting;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

public class RecipeUpgradeStation implements IUpgradeRecipe {
	public static RecipeUpgradeStation EMPTY = new RecipeUpgradeStation(ItemStack.EMPTY, NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY).subList(0, 0), false);
	
	private final List<ItemStack> inputs;
	private final ItemStack output;
	private boolean doesTagCarry;
	
	public RecipeUpgradeStation(ItemStack outputIn, List<ItemStack> inputsIn, boolean doesTagCarryIn) {
		this.output = outputIn;
		this.inputs = inputsIn;
		this.doesTagCarry = doesTagCarryIn;
	}
	
	@Override
	public ItemStack getOutput() {
		return this.output;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(List<ItemStack> stack) {
		NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(stack.size(), ItemStack.EMPTY);

		for (int i = 0; i < nonnulllist.size(); ++i) {
			ItemStack itemstack = stack.get(i);
			nonnulllist.set(i, ForgeHooks.getContainerItem(itemstack));
		}

		return nonnulllist;
	}
	
	@Override
	public boolean matches(List<ItemStack> stack) {
		List<ItemStack> list = Lists.newArrayList(this.inputs);

		for (int j = 0; j < stack.size(); ++j) {
			ItemStack itemstack = stack.get(j);
			
			if (!itemstack.isEmpty()) {
				boolean flag = false;

				for (ItemStack itemstack1 : list) {
					if (itemstack.getItem().equals(itemstack1.getItem())) {
						flag = true;
						list.remove(itemstack1);
						break;
					}
				}

				if (!flag) {
					return false;
				}
			}
		}
		
		
		return list.isEmpty();
	}

	@Override
	public ItemStack getResult() {
		return this.output.copy();
	}

	@Override
	public int getRecipeSize() {
		return this.inputs.size();
	}

	@Override
	public List<ItemStack> getRecipeInput() {
		return this.inputs;
	}

	@Override
	public ItemStack getFocusStack() {
		return this.inputs.get(0).copy();
	}
	
	public boolean doesTagCarry() {
		return this.doesTagCarry;
	}
}