package com.tcn.dimensionalpocketsii.core.integration;

import com.tcn.dimensionalpocketsii.DimReference.INTEGRATION.JEI;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.core.crafting.CraftingManagerUpgradeStation;
import com.tcn.dimensionalpocketsii.core.integration.jei.CategoryUpgradeStation;
import com.tcn.dimensionalpocketsii.core.management.ModBusManager;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerModuleCrafter;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerModuleFurnace;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerModuleUpgradeStation;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenModuleUpgradeStation;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
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
		registration.addRecipeCategories(new CategoryUpgradeStation(registration.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) { }

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		registration.addRecipes(CraftingManagerUpgradeStation.getInstance().getRecipeList(), JEI.UPGRADE_UID);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(ContainerModuleCrafter.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
		registration.addRecipeTransferHandler(ContainerModuleFurnace.class, VanillaRecipeCategoryUid.FURNACE, 0, 1, 3, 36);
		registration.addRecipeTransferHandler(ContainerModuleFurnace.class, VanillaRecipeCategoryUid.FUEL, 1, 1, 3, 36);
		
		registration.addRecipeTransferHandler(ContainerModuleUpgradeStation.class, JEI.UPGRADE_UID, 0, 7, 9, 36);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) { 
		registration.addRecipeCatalyst(new ItemStack(ModBusManager.MODULE_UPGRADE_STATION), JEI.UPGRADE_UID);
		registration.addRecipeCatalyst(new ItemStack(ModBusManager.MODULE_CRAFTER), VanillaRecipeCategoryUid.CRAFTING);
		registration.addRecipeCatalyst(new ItemStack(ModBusManager.MODULE_FURNACE), VanillaRecipeCategoryUid.FURNACE);
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(ScreenModuleUpgradeStation.class, 89, 40, 21, 20, JEI.UPGRADE_UID);
	}

	@Override
	public void registerAdvanced(IAdvancedRegistration registration) { }

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) { }
}