package com.tcn.dimensionalpocketsii.core.management;

import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.core.crafting.UpgradeStationRecipe;

import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CraftingManager {
	
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, DimensionalPockets.MOD_ID);
	public static final DeferredRegister<RecipeType<?>> RECIPES = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, DimensionalPockets.MOD_ID);

	public static final RegistryObject<RecipeType<?>> RECIPE_TYPE_UPGRADE_STATION = RECIPES.register("upgrading", () -> RecipeType.register("upgrading"));
	public static final RegistryObject<RecipeSerializer<?>> RECIPE_SERIALIZER_UPGRADE_STATION = RECIPE_SERIALIZERS.register("upgrading", () -> new UpgradeStationRecipe.Serializer());
	
	public static void register(IEventBus bus) {
		RECIPES.register(bus);
		RECIPE_SERIALIZERS.register(bus);
	}
}