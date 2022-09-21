package com.tcn.dimensionalpocketsii.integration.jei;

import javax.annotation.Nullable;

import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.core.crafting.UpgradeStationRecipe;
import com.tcn.dimensionalpocketsii.core.management.ObjectManager;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerModuleBlastFurnace;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerModuleCrafter;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerModuleFurnace;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerModuleSmithingTable;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerModuleUpgradeStation;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenModuleBlastFurnace;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenModuleCrafter;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenModuleFurnace;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenModuleSmithingTable;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenModuleUpgradeStation;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IAdvancedRegistration;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class CoreJeiPlugin implements IModPlugin {
	
	@Nullable
	private IRecipeCategory<UpgradeStationRecipe> upgradeStationCategory;
	public RecipeType<UpgradeStationRecipe> UPGRADING = RecipeType.create("dimensionalpocketsii", "upgrade_category", UpgradeStationRecipe.class);
	
	public CoreJeiPlugin() { }

	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(DimensionalPockets.MOD_ID, "integration_jei");
	}
	
	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) { }

	@Override
	public void registerIngredients(IModIngredientRegistration registration) { }

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IJeiHelpers jeiHelpers = registration.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		registration.addRecipeCategories(upgradeStationCategory = new CategoryUpgradeStation(guiHelper));
	}

	@Override
	public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) { }

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		DimensionalRecipes dimensionalRecipes = new DimensionalRecipes();
		
		registration.addRecipes(UPGRADING, dimensionalRecipes.getSmithingRecipes(upgradeStationCategory));
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(ContainerModuleUpgradeStation.class, UPGRADING, 0, 9, 10, 36);
		
		registration.addRecipeTransferHandler(ContainerModuleCrafter.class, RecipeTypes.CRAFTING, 1, 9, 10, 36);
		registration.addRecipeTransferHandler(ContainerModuleSmithingTable.class, RecipeTypes.SMITHING, 0, 2, 3, 36);
		registration.addRecipeTransferHandler(ContainerModuleFurnace.class, RecipeTypes.SMELTING, 0, 1, 3, 36);
		registration.addRecipeTransferHandler(ContainerModuleFurnace.class, RecipeTypes.FUELING, 1, 1, 3, 36);

		registration.addRecipeTransferHandler(ContainerModuleBlastFurnace.class, RecipeTypes.BLASTING, 0, 1, 3, 36);
		registration.addRecipeTransferHandler(ContainerModuleBlastFurnace.class, RecipeTypes.FUELING, 1, 1, 3, 36);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) { 
		registration.addRecipeCatalyst(new ItemStack(ObjectManager.module_upgrade_station), UPGRADING);
		
		registration.addRecipeCatalyst(new ItemStack(ObjectManager.module_crafter), RecipeTypes.CRAFTING);
		registration.addRecipeCatalyst(new ItemStack(ObjectManager.module_furnace), RecipeTypes.SMELTING);
		registration.addRecipeCatalyst(new ItemStack(ObjectManager.module_blast_furnace), RecipeTypes.BLASTING);
		registration.addRecipeCatalyst(new ItemStack(ObjectManager.module_smithing_table), RecipeTypes.SMITHING);
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(ScreenModuleUpgradeStation.class, 103, 38, 20, 24, UPGRADING);
		
		registration.addRecipeClickArea(ScreenModuleFurnace.class, 76, 35, 22, 15, RecipeTypes.SMELTING);
		registration.addRecipeClickArea(ScreenModuleBlastFurnace.class, 76, 35, 22, 15, RecipeTypes.BLASTING);
		registration.addRecipeClickArea(ScreenModuleCrafter.class, 94, 35, 22, 15, RecipeTypes.CRAFTING);
		registration.addRecipeClickArea(ScreenModuleSmithingTable.class, 105, 56, 22, 15, RecipeTypes.SMITHING);
	}

	@Override
	public void registerAdvanced(IAdvancedRegistration registration) { }

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) { }
}