package com.tcn.dimensionalpocketsii.core.management;

import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.core.crafting.UpgradeStationRecipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeManager {
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, DimensionalPockets.MOD_ID);

	public static final RegistryObject<RecipeSerializer<?>> RECIPE_SERIALIZER_UPGRADE_STATION = RECIPE_SERIALIZERS.register("upgrading", () -> new UpgradeStationRecipe.Serializer());
		
	public static void register(IEventBus bus) {
		RECIPE_SERIALIZERS.register(bus);
	}
}
