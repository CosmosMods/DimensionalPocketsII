package com.zeher.zeherlib.core.manager;

import com.zeher.zeherlib.core.recipe.CompactorRecipes;
import com.zeher.zeherlib.core.recipe.FluidCrafterMeltRecipes;
import com.zeher.zeherlib.core.recipe.GrinderRecipes;
import com.zeher.zeherlib.core.recipe.OrePlantCleaningRecipes;
import com.zeher.zeherlib.core.recipe.OrePlantRefiningRecipes;
import com.zeher.zeherlib.core.recipe.SeparatorRecipes;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * Handler for registry.
 */
public class RegistryManager {
	
	/**
	 * Used in Item & Block Registry.
	 */
	public static class REGISTRY_EVENT {
		
		@Deprecated // Use new method
		public static void registerAllModels(final ModelRegistryEvent event, Block... blocks) {
			for (int i = 0; i < blocks.length; i++) {
				registerModel(blocks[i]);
			}
		}
		
		@OnlyIn(Dist.CLIENT)
		@Deprecated
		public static void registerModel(Block block){
			Item item = Item.getItemFromBlock(block);
			ModelLoader.addSpecialModel(new ModelResourceLocation(item.getRegistryName(), "inventory"));
			//ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		}
		
		
		public static void registerAllBlocks(RegistryEvent.Register<Block> event, Block... blocks) {
			event.getRegistry().registerAll(blocks);
		}
		
		@Deprecated
		public static BlockItem[] getBlockItems(RegistryEvent.Register<Block> event, Block[] blocks, Block.Properties[] properties) {
			BlockItem[] item_blocks = new BlockItem[blocks.length];
			event.getRegistry().registerAll(blocks);
			for (int i = 0; i < blocks.length; i++) {
				//item_blocks[i] = new BlockItem(blocks[i]);
				item_blocks[i].setRegistryName(blocks[i].getRegistryName());
			}
			
			return item_blocks;
		}
		
		@Deprecated
		public static void registerBlockItemArray(RegistryEvent.Register<Item> event, BlockItem[] block_items) {
			event.getRegistry().registerAll(block_items);
		}
		
		@Deprecated
		public static void registerAllBlockItems(RegistryEvent.Register<Item> event, Item... block_items) {
			event.getRegistry().registerAll(block_items);
		}
		
		public static ItemStack[] getItemsForItemGroup(RegistryEvent.Register<Item> event, Item... items) {
			ItemStack[] item = new ItemStack[items.length];
			for (int i = 0; i < items.length; i++) {
				item[i] = new ItemStack(items[i]);
			}
			return item;
		}
		
		
		/**
		 * Registers Blocks, Items etc into the registry. New version of registry code WIP
		 * @param <T> = the type of object to be registered.
		 * @param mod_id = The mod_id of the mod registering objects.
		 * @param entry = the exact object to be registered.
		 * @param name = the registry name of the object.
		 * @return
		 */
		public static <T extends IForgeRegistryEntry<T>> T setupHighString(String mod_id, final String name, final T entry) {
			return setup(entry, new ResourceLocation(mod_id, name));
		}
		
		public static <T extends IForgeRegistryEntry<T>> T setupHighResource(String mod_id, final ResourceLocation name, final T entry) {
			return setup(entry, name);
		}

		public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final ResourceLocation registryName) {
			entry.setRegistryName(registryName);
			return entry;
		}
	}
	
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