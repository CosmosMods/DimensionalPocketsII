package com.zeher.zeherlib.core.handler;

import com.zeher.zeherlib.core.recipe.CompactorRecipes;
import com.zeher.zeherlib.core.recipe.FluidCrafterMeltRecipes;
import com.zeher.zeherlib.core.recipe.GrinderRecipes;
import com.zeher.zeherlib.core.recipe.OrePlantCleaningRecipes;
import com.zeher.zeherlib.core.recipe.OrePlantRefiningRecipes;
import com.zeher.zeherlib.core.recipe.SeparatorRecipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeRegistryHandler {
	/**
	 * Adds a specific recipe into registry. See specific constructor for which category.
	 */
	public static class RECIPE {
		
		/**
		 * Register a recipe for the grinder.
		 * @param input {@link ItemStack}
		 * @param output {@link ItemStack}
		 * @param xp [float]
		 */
		public static void registerGrinderRecipe(ItemStack input, ItemStack output, ItemStack second, float xp) {
			GrinderRecipes.getInstance().addGrinding(input, output, second, xp);
		}

		/**
		 * Register a recipe for the separator.
		 * @param input {@link ItemStack}
		 * @param output {@link ItemStack}
		 * @param xp [float]
		 */
		public static void registerSeparatorRecipe(ItemStack input, ItemStack output, ItemStack secondary, float xp) {
			SeparatorRecipes.getInstance().addSeparating(input, output, secondary, xp);
		}
		
		/**
		 * Register a recipe for the compactor.
		 * @param input {@link ItemStack}
		 * @param output {@link ItemStac}
		 * @param xp [float]
		 */
		public static void registerCompactorRecipe(ItemStack input, ItemStack output, float xp) {
			CompactorRecipes.getInstance().addCompacting(input, output, xp);
		}
		
		/**
		 * Register a recipe for the ore plant [clean].
		 * @param input {@link ItemStack}
		 * @param output {@link ItemStack}
		 * @param xp [float]
		 */
		public static void registerOrePlantCleanRecipe(ItemStack input, ItemStack output, float xp) {
			OrePlantCleaningRecipes.getInstance().addCleaning(input, output, xp);
		}
		
		/**
		 * Register a recipe for the ore plant [refine].
		 * @param input {@link ItemStack}
		 * @param output {@link ItemStack}
		 * @param xp [float]
		 */
		public static void registerOrePlantRefineRecipe(ItemStack input, ItemStack output, float xp) {
			OrePlantRefiningRecipes.getInstance().addRefining(input, output, xp);
		}
		
		/**
		 * Register a recipe for the ore plant [melt].
		 * @param input {@link ItemStack}
		 * @param output {@link FluidStack}
		 * @param xp [float]
		 */
		public static void registerFluidCrafterMeltRecipe(ItemStack input, FluidStack output) {
			FluidCrafterMeltRecipes.getInstance().addRecipe(input, output);
		}
	}
}