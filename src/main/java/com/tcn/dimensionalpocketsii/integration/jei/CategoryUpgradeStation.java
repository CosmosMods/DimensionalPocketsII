package com.tcn.dimensionalpocketsii.integration.jei;

import com.tcn.cosmoslibrary.common.lib.ComponentColour;
import com.tcn.cosmoslibrary.common.lib.ComponentHelper;
import com.tcn.dimensionalpocketsii.DimReference.GUI.RESOURCE;
import com.tcn.dimensionalpocketsii.DimReference.INTEGRATION.JEI;
import com.tcn.dimensionalpocketsii.core.crafting.UpgradeStationRecipe;
import com.tcn.dimensionalpocketsii.core.management.ObjectManager;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CategoryUpgradeStation implements IRecipeCategory<UpgradeStationRecipe> {
	
	private IGuiHelper helper;
	private final IDrawable background;
	
	public CategoryUpgradeStation(IGuiHelper helperIn) {
		this.helper = helperIn;
		background = helper.createDrawable(RESOURCE.UPGRADE_STATION_JEI, 0, 0, 114, 72);
	}

	@Override
	public ResourceLocation getUid() {
		return JEI.UPGRADE_UID;
	}

	@Override
	public Class<? extends UpgradeStationRecipe> getRecipeClass() {
		return UpgradeStationRecipe.class;
	}

	@Override
	public Component getTitle() {
		return ComponentHelper.style(ComponentColour.WHITE, "dimensionalpocketsii.integration.jei.upgrade_category");
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}

	@Override
	public IDrawable getIcon() {
		return this.helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ObjectManager.module_upgrade_station));
	}
	
	@Override
	public void setRecipe(IRecipeLayoutBuilder recipeLayout, UpgradeStationRecipe recipe, IFocusGroup ingredients) {
		recipeLayout.addSlot(RecipeIngredientRole.INPUT, 28, 28).addIngredients(recipe.focus);
		
		recipeLayout.addSlot(RecipeIngredientRole.INPUT, 7, 7).addIngredients(recipe.topRow[0]);
		recipeLayout.addSlot(RecipeIngredientRole.INPUT, 28, 7).addIngredients(recipe.topRow[1]);
		recipeLayout.addSlot(RecipeIngredientRole.INPUT, 49, 7).addIngredients(recipe.topRow[2]);

		recipeLayout.addSlot(RecipeIngredientRole.INPUT, 7, 28).addIngredients(recipe.middleRow[0]);
		recipeLayout.addSlot(RecipeIngredientRole.INPUT, 49, 28).addIngredients(recipe.middleRow[1]);

		recipeLayout.addSlot(RecipeIngredientRole.INPUT, 7, 49).addIngredients(recipe.bottomRow[0]);
		recipeLayout.addSlot(RecipeIngredientRole.INPUT, 28, 49).addIngredients(recipe.bottomRow[1]);
		recipeLayout.addSlot(RecipeIngredientRole.INPUT, 49, 49).addIngredients(recipe.bottomRow[2]);

		recipeLayout.addSlot(RecipeIngredientRole.OUTPUT, 91, 28).addItemStack(recipe.result);
	}
}