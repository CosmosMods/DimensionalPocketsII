package com.tcn.dimensionalpocketsii.core.management;

import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.core.crafting.UpgradeStationRecipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeManager {
	
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, DimensionalPockets.MOD_ID);
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, DimensionalPockets.MOD_ID);

	public static final RegistryObject<RecipeSerializer<?>> recipe_serializer_upgrade_station = RECIPE_SERIALIZERS.register("upgrading", () -> UpgradeStationRecipe.Serializer.INSTANCE);
	public static final RegistryObject<RecipeType<?>> recipe_type_upgrade_station = RECIPE_TYPES.register("upgrading", () -> UpgradeStationRecipe.Type.INSTANCE);
	
	public static void register(IEventBus bus) {
		RECIPE_TYPES.register(bus);
		RECIPE_SERIALIZERS.register(bus);
	}
}
