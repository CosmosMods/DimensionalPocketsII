package com.zeher.zehercraft.core.handler.recipe;

import com.zeher.zehercraft.core.handler.BlockHandler;
import com.zeher.zehercraft.core.handler.ItemHandler;
import com.zeher.zeherlib.core.handler.RecipeRegistryHandler;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RecipeHandler {
		
	public static void initialization() {
		registerVanillaRecipes();
		registerModRecipes();
	}
	
	public static void registerVanillaRecipes() {
		GameRegistry.addSmelting(BlockHandler.BASE.BLOCK_ORE_COPPER, new ItemStack(ItemHandler.COPPER_INGOT), 1F);
		GameRegistry.addSmelting(ItemHandler.COPPER_DUST, new ItemStack(ItemHandler.COPPER_INGOT), 0.5F);
		GameRegistry.addSmelting(BlockHandler.BASE.BLOCK_ORE_TIN, new ItemStack(ItemHandler.TIN_INGOT), 1F);
		GameRegistry.addSmelting(ItemHandler.TIN_DUST, new ItemStack(ItemHandler.TIN_INGOT), 0.5F);
		GameRegistry.addSmelting(BlockHandler.BASE.BLOCK_ORE_SILVER, new ItemStack(ItemHandler.SILVER_INGOT), 1F);
		GameRegistry.addSmelting(ItemHandler.SILVER_DUST, new ItemStack(ItemHandler.SILVER_INGOT), 0.5F);
		
		GameRegistry.addSmelting(BlockHandler.BASE.BLOCK_ORE_SILICON, new ItemStack(ItemHandler.SILICON), 1F);
		GameRegistry.addSmelting(ItemHandler.BRONZE_DUST, new ItemStack(ItemHandler.BRONZE_INGOT), 0.5F);
		GameRegistry.addSmelting(ItemHandler.STEEL_INGOT_UNREFINED, new ItemStack(ItemHandler.STEEL_INGOT), 0.5F);
		GameRegistry.addSmelting(ItemHandler.IRON_DUST, new ItemStack(Items.IRON_INGOT), 0.5F);
		GameRegistry.addSmelting(ItemHandler.GOLD_DUST, new ItemStack(Items.GOLD_INGOT), 0.5F);
		
		GameRegistry.addSmelting(new ItemStack(Items.DYE, 1, 0), new ItemStack(ItemHandler.RUBBER), 1F);
		GameRegistry.addSmelting(new ItemStack(ItemHandler.SILICON), new ItemStack(ItemHandler.SILICON_REFINED), 0.5F);
		//GameRegistry.addSmelting(new ItemStack(ItemHandler.CIRCUIT_ONE_RAW), new ItemStack(ItemHandler.CIRCUIT_ONE), 1f);
		//GameRegistry.addSmelting(new ItemStack(ItemHandler.CIRCUIT_THREE_RAW), new ItemStack(ItemHandler.CIRCUIT_THREE), 1f);
	}
	
	public static void registerModRecipes() {
		RecipeRegistryHandler.RECIPE.registerGrinderRecipe(new ItemStack(BlockHandler.BASE.BLOCK_ORE_COPPER),  new ItemStack(ItemHandler.COPPER_DUST, 2), new ItemStack(ItemHandler.STONE_DUST, 2), 1F);
		RecipeRegistryHandler.RECIPE.registerGrinderRecipe(new ItemStack(BlockHandler.BASE.BLOCK_ORE_TIN),     new ItemStack(ItemHandler.TIN_DUST, 2),    new ItemStack(ItemHandler.STONE_DUST, 2), 1F);
		RecipeRegistryHandler.RECIPE.registerGrinderRecipe(new ItemStack(BlockHandler.BASE.BLOCK_ORE_SILVER),  new ItemStack(ItemHandler.SILVER_DUST, 2), new ItemStack(ItemHandler.STONE_DUST, 2), 1F);
		RecipeRegistryHandler.RECIPE.registerGrinderRecipe(new ItemStack(BlockHandler.BASE.BLOCK_ORE_SILICON), new ItemStack(ItemHandler.SILICON, 4),     new ItemStack(ItemHandler.STONE_DUST, 2), 1.5F);
		
		RecipeRegistryHandler.RECIPE.registerGrinderRecipe(new ItemStack(Blocks.IRON_ORE),    new ItemStack(ItemHandler.IRON_DUST, 2), new ItemStack(ItemHandler.STONE_DUST, 2), 1F);
		RecipeRegistryHandler.RECIPE.registerGrinderRecipe(new ItemStack(Blocks.GOLD_ORE),    new ItemStack(ItemHandler.GOLD_DUST, 2), new ItemStack(ItemHandler.STONE_DUST, 2), 1F);
		RecipeRegistryHandler.RECIPE.registerGrinderRecipe(new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.GRAVEL, 2),         new ItemStack(ItemHandler.STONE_DUST, 2), 1F);
		RecipeRegistryHandler.RECIPE.registerGrinderRecipe(new ItemStack(Blocks.GRAVEL),      new ItemStack(Blocks.SAND, 2),           new ItemStack(ItemHandler.STONE_DUST, 2), 1F);
		
		RecipeRegistryHandler.RECIPE.registerGrinderRecipe(new ItemStack(ItemHandler.COPPER_INGOT), new ItemStack(ItemHandler.COPPER_DUST),  ItemStack.EMPTY, 0.5F);
		RecipeRegistryHandler.RECIPE.registerGrinderRecipe(new ItemStack(ItemHandler.TIN_INGOT),    new ItemStack(ItemHandler.TIN_DUST),     ItemStack.EMPTY, 0.5F);
		RecipeRegistryHandler.RECIPE.registerGrinderRecipe(new ItemStack(ItemHandler.SILVER_INGOT), new ItemStack(ItemHandler.SILVER_DUST),  ItemStack.EMPTY, 1F);
		RecipeRegistryHandler.RECIPE.registerGrinderRecipe(new ItemStack(ItemHandler.BRONZE_INGOT), new ItemStack(ItemHandler.BRONZE_DUST),  ItemStack.EMPTY, 0.5F);
		RecipeRegistryHandler.RECIPE.registerGrinderRecipe(new ItemStack(Items.DIAMOND),            new ItemStack(ItemHandler.DIAMOND_DUST), ItemStack.EMPTY, 1F);
	
		RecipeRegistryHandler.RECIPE.registerCompactorRecipe(new ItemStack(ItemHandler.COPPER_INGOT), new ItemStack(ItemHandler.COPPER_PLATE), 1F);
		RecipeRegistryHandler.RECIPE.registerCompactorRecipe(new ItemStack(ItemHandler.TIN_INGOT),    new ItemStack(ItemHandler.TIN_PLATE), 1F);
		RecipeRegistryHandler.RECIPE.registerCompactorRecipe(new ItemStack(ItemHandler.SILVER_INGOT), new ItemStack(ItemHandler.SILVER_PLATE), 1F);
		RecipeRegistryHandler.RECIPE.registerCompactorRecipe(new ItemStack(ItemHandler.BRONZE_INGOT), new ItemStack(ItemHandler.BRONZE_PLATE), 1F);
		RecipeRegistryHandler.RECIPE.registerCompactorRecipe(new ItemStack(ItemHandler.BRASS_INGOT),  new ItemStack(ItemHandler.BRASS_PLATE), 1F);
		RecipeRegistryHandler.RECIPE.registerCompactorRecipe(new ItemStack(ItemHandler.STEEL_INGOT),  new ItemStack(ItemHandler.STEEL_PLATE), 1F);
		
		RecipeRegistryHandler.RECIPE.registerCompactorRecipe(new ItemStack(Items.IRON_INGOT), new ItemStack(ItemHandler.IRON_PLATE), 1F);
		RecipeRegistryHandler.RECIPE.registerCompactorRecipe(new ItemStack(Items.GOLD_INGOT), new ItemStack(ItemHandler.GOLD_PLATE), 1F);
		RecipeRegistryHandler.RECIPE.registerCompactorRecipe(new ItemStack(Items.DIAMOND),    new ItemStack(ItemHandler.DIAMOND_PLATE), 1.5F);
	
		//RecipeRegistryHandler.RECIPE.registerSeparatorRecipe(new ItemStack(Blocks.COAL_BLOCK), new ItemStack(Blocks.DIAMOND_BLOCK), 1.0F);
		RecipeRegistryHandler.RECIPE.registerSeparatorRecipe(new ItemStack(Blocks.IRON_ORE), new ItemStack(ItemHandler.IRON_DUST, 4), new ItemStack(Blocks.COBBLESTONE, 2), 0.5F);
		
		RecipeRegistryHandler.RECIPE.registerOrePlantCleanRecipe(new ItemStack(ItemHandler.IRON_DUST),         new ItemStack(ItemHandler.IRON_DUST_REFINE), 1F);
		RecipeRegistryHandler.RECIPE.registerOrePlantCleanRecipe(new ItemStack(ItemHandler.GOLD_DUST),         new ItemStack(ItemHandler.GOLD_DUST_REFINE), 1F);
		
		RecipeRegistryHandler.RECIPE.registerOrePlantRefineRecipe(new ItemStack(ItemHandler.IRON_DUST_REFINE), new ItemStack(Items.IRON_INGOT), 1F);
		RecipeRegistryHandler.RECIPE.registerOrePlantRefineRecipe(new ItemStack(ItemHandler.GOLD_DUST_REFINE), new ItemStack(Items.GOLD_INGOT), 1F);

		RecipeRegistryHandler.RECIPE.registerFluidCrafterMeltRecipe(new ItemStack(Blocks.DIRT),    new FluidStack(FluidRegistry.WATER, 1000));
		RecipeRegistryHandler.RECIPE.registerFluidCrafterMeltRecipe(new ItemStack(Items.REDSTONE), new FluidStack(FluidRegistry.getFluid("energized_redstone"), 500));
	}
}