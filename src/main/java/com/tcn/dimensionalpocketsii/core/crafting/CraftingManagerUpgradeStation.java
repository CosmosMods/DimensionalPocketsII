package com.tcn.dimensionalpocketsii.core.crafting;

import java.util.List;

import com.google.common.collect.Lists;
import com.tcn.dimensionalpocketsii.core.management.ModBusManager;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CraftingManagerUpgradeStation {

	private static final CraftingManagerUpgradeStation INSTANCE = new CraftingManagerUpgradeStation();
	private final List<IUpgradeRecipe> recipes = Lists.<IUpgradeRecipe>newArrayList();
	

	public static CraftingManagerUpgradeStation getInstance() {
		return INSTANCE;
	}
	
	private CraftingManagerUpgradeStation() {
		this.addRecipes();
	}
	
	public void addRecipes() {
		this.addRecipe(new ItemStack(ModBusManager.DIMENSIONAL_ELYTRAPLATE), new ItemStack(ModBusManager.DIMENSIONAL_CHESTPLATE_ENHANCED), true, 
			Items.NETHERITE_BLOCK, ModBusManager.BLOCK_DIMENSIONAL_GEM.asItem(), Items.NETHERITE_BLOCK, ModBusManager.ELYTRA_WING, Items.BLAZE_POWDER, ModBusManager.ELYTRA_WING
		);
		
		this.addRecipe(new ItemStack(ModBusManager.DIMENSIONAL_SWORD), new ItemStack(Items.NETHERITE_SWORD), true, 
			ModBusManager.DIMENSIONAL_INGOT, Items.NETHERITE_INGOT, ModBusManager.DIMENSIONAL_INGOT, ModBusManager.DIMENSIONAL_GEM, Items.NETHERITE_INGOT, ModBusManager.DIMENSIONAL_GEM
		);
		this.addRecipe(new ItemStack(ModBusManager.DIMENSIONAL_PICKAXE), new ItemStack(Items.NETHERITE_PICKAXE), true, 
			ModBusManager.DIMENSIONAL_INGOT, Items.NETHERITE_INGOT, ModBusManager.DIMENSIONAL_INGOT, ModBusManager.DIMENSIONAL_GEM, Items.NETHERITE_INGOT, ModBusManager.DIMENSIONAL_GEM
		);
		this.addRecipe(new ItemStack(ModBusManager.DIMENSIONAL_AXE), new ItemStack(Items.NETHERITE_AXE), true, 
			ModBusManager.DIMENSIONAL_INGOT, Items.NETHERITE_INGOT, ModBusManager.DIMENSIONAL_INGOT, ModBusManager.DIMENSIONAL_GEM, Items.NETHERITE_INGOT, ModBusManager.DIMENSIONAL_GEM
		);
		this.addRecipe(new ItemStack(ModBusManager.DIMENSIONAL_SHOVEL), new ItemStack(Items.NETHERITE_SHOVEL), true, 
			ModBusManager.DIMENSIONAL_INGOT, Items.NETHERITE_INGOT, ModBusManager.DIMENSIONAL_INGOT, ModBusManager.DIMENSIONAL_GEM, Items.NETHERITE_INGOT, ModBusManager.DIMENSIONAL_GEM
		);
		this.addRecipe(new ItemStack(ModBusManager.DIMENSIONAL_HOE), new ItemStack(Items.NETHERITE_HOE), true, 
			ModBusManager.DIMENSIONAL_INGOT, Items.NETHERITE_INGOT, ModBusManager.DIMENSIONAL_INGOT, ModBusManager.DIMENSIONAL_GEM, Items.NETHERITE_INGOT, ModBusManager.DIMENSIONAL_GEM
		);
		
		this.addRecipe(new ItemStack(ModBusManager.DIMENSIONAL_TRIDENT), new ItemStack(Items.TRIDENT), true, 
			ModBusManager.DIMENSIONAL_INGOT, Items.NETHERITE_INGOT, ModBusManager.DIMENSIONAL_INGOT, ModBusManager.DIMENSIONAL_GEM, Items.NETHERITE_INGOT, ModBusManager.DIMENSIONAL_GEM
		);
		this.addRecipe(new ItemStack(ModBusManager.DIMENSIONAL_BOW), new ItemStack(Items.BOW), true, 
			Items.NETHERITE_INGOT, ModBusManager.DIMENSIONAL_INGOT, Items.NETHERITE_INGOT, ModBusManager.DIMENSIONAL_THREAD, ModBusManager.DIMENSIONAL_THREAD, ModBusManager.DIMENSIONAL_THREAD
		);
		
		this.addRecipe(new ItemStack(ModBusManager.DIMENSIONAL_HELMET), new ItemStack(Items.NETHERITE_HELMET), true, 
			ModBusManager.DIMENSIONAL_GEM, Items.NETHERITE_INGOT, ModBusManager.DIMENSIONAL_GEM, Items.NETHERITE_INGOT, ModBusManager.DIMENSIONAL_GEM, Items.NETHERITE_INGOT
		);
		this.addRecipe(new ItemStack(ModBusManager.DIMENSIONAL_CHESTPLATE), new ItemStack(Items.NETHERITE_CHESTPLATE), true, 
			ModBusManager.DIMENSIONAL_GEM, Items.NETHERITE_INGOT, ModBusManager.DIMENSIONAL_GEM, Items.NETHERITE_INGOT, ModBusManager.DIMENSIONAL_GEM, Items.NETHERITE_INGOT
		);
		this.addRecipe(new ItemStack(ModBusManager.DIMENSIONAL_LEGGINGS), new ItemStack(Items.NETHERITE_LEGGINGS), true, 
			ModBusManager.DIMENSIONAL_GEM, Items.NETHERITE_INGOT, ModBusManager.DIMENSIONAL_GEM, Items.NETHERITE_INGOT, ModBusManager.DIMENSIONAL_GEM, Items.NETHERITE_INGOT
		);
		this.addRecipe(new ItemStack(ModBusManager.DIMENSIONAL_BOOTS), new ItemStack(Items.NETHERITE_BOOTS), true, 
			ModBusManager.DIMENSIONAL_GEM, Items.NETHERITE_INGOT, ModBusManager.DIMENSIONAL_GEM, Items.NETHERITE_INGOT, ModBusManager.DIMENSIONAL_GEM, Items.NETHERITE_INGOT
		);
	}
	
	public void addRecipe(ItemStack output, ItemStack focus, boolean carry, Item... inputs) {
		List<ItemStack> list = Lists.<ItemStack>newArrayList();
		
		if (inputs.length != 6) {
			throw new IllegalArgumentException("Invalid Upgrade Station recipe: out of bounds exception [Recipe ingredient list must be 6]!");
		}

		list.add(focus);
		for (Item item : inputs) {
			list.add(new ItemStack(item));
		}
		
		this.recipes.add(new RecipeUpgradeStation(output, list, carry));
	}
	
	public void addRecipe(IUpgradeRecipe recipe) {
		this.recipes.add(recipe);
	}

	public IUpgradeRecipe findMatchingRecipeRaw(List<ItemStack> inputItems) {
		for (IUpgradeRecipe irecipe : this.recipes) {
			if (irecipe.matches(inputItems)) {
				return irecipe;
			}
		}
		return RecipeUpgradeStation.EMPTY;
	}

	public ItemStack findMatchingRecipe(List<ItemStack> inputItems) {
		for (IUpgradeRecipe irecipe : this.recipes) {
			
			if (irecipe.matches(inputItems)) {
				return irecipe.getOutput();
			}
		}
		return ItemStack.EMPTY;
	}

	public ItemStack findFocusStack(List<ItemStack> inputItems) {
		for (IUpgradeRecipe irecipe : this.recipes) {
			if (irecipe.matches(inputItems)) {
				return irecipe.getFocusStack();
			}
		}
		return ItemStack.EMPTY;
	}

	public NonNullList<ItemStack> getRemainingItems(List<ItemStack> inputItems) {
		for (IUpgradeRecipe irecipe : this.recipes) {
			if (irecipe.matches(inputItems)) {
				return irecipe.getRemainingItems(inputItems);
			}
		}
		
		NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(inputItems.size(), ItemStack.EMPTY);
		
		for (int i = 0; i < nonnulllist.size(); ++i) {
			nonnulllist.set(i, inputItems.get(i));
		}
		return nonnulllist;
	}

	
	public List<IUpgradeRecipe> getRecipeList() {
		return this.recipes;
	}
}