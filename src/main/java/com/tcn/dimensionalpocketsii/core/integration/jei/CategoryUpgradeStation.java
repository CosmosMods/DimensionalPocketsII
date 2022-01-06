package com.tcn.dimensionalpocketsii.core.integration.jei;

import java.util.List;

import com.tcn.cosmoslibrary.common.lib.ComponentColour;
import com.tcn.cosmoslibrary.common.lib.ComponentHelper;
import com.tcn.dimensionalpocketsii.DimReference.INTEGRATION.JEI;
import com.tcn.dimensionalpocketsii.core.crafting.RecipeUpgradeStation;
import com.tcn.dimensionalpocketsii.core.management.ModBusManager;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CategoryUpgradeStation implements IRecipeCategory<RecipeUpgradeStation> {
	
	private IGuiHelper helper;
	private final IDrawable background;
	
	public CategoryUpgradeStation(IGuiHelper helperIn) {
		this.helper = helperIn;
		background = helper.createDrawable(JEI.UPGRADE_BACKGROUND, 0, 0, 114, 86);
	}

	@Override
	public ResourceLocation getUid() {
		return JEI.UPGRADE_UID;
	}

	@Override
	public Class<? extends RecipeUpgradeStation> getRecipeClass() {
		return RecipeUpgradeStation.class;
	}

	@Override
	public Component getTitle() {
		return ComponentHelper.locComp(ComponentColour.WHITE, false, "dimensionalpocketsii.integration.jei.upgrade_category");
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}

	@Override
	public IDrawable getIcon() {
		return this.helper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(ModBusManager.MODULE_UPGRADE_STATION));
	}

	@Override
	public void setIngredients(RecipeUpgradeStation recipe, IIngredients ingredients) {
		List<ItemStack> list = recipe.getRecipeInput();
		
		ingredients.setInputs(VanillaTypes.ITEM, list);
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput());
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, RecipeUpgradeStation recipe, IIngredients ingredients) {
		IGuiItemStackGroup stacks = recipeLayout.getItemStacks();

		stacks.init(0, true, 34, 34);
		
		stacks.init(1, true, 13, 13);
		stacks.init(2, true, 34, 13);
		stacks.init(3, true, 55, 13);
		stacks.init(4, true, 13, 55);
		stacks.init(5, true, 34, 55);
		stacks.init(6, true, 55, 55);

		stacks.init(7, false, 83, 34);
		
		stacks.set(ingredients);
	}

}