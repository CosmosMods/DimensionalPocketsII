package com.zeher.zehercraft.core.handler.recipe;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zeher.zehercraft.core.handler.BlockHandler;
import com.zeher.zehercraft.core.handler.ItemHandler;
import com.zeher.zeherlib.api.client.tesr.EnumTESRColour;
import com.zeher.zeherlib.api.core.interfaces.IMultiRecipe;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class SynthesiserRecipeHandler {

	private static final boolean TEST_RECIPES_ENABLED = true;

	private static final SynthesiserRecipeHandler INSTANCE = new SynthesiserRecipeHandler();
	private final List<IMultiRecipe> recipes = Lists.<IMultiRecipe>newArrayList();

	public static SynthesiserRecipeHandler getInstance() {
		return INSTANCE;
	}
	
	private SynthesiserRecipeHandler() {
		this.addAllRecipes();
		
		if (this.TEST_RECIPES_ENABLED == true) {
			this.testRecipes();
		}
	}
	
	/**
	 * ONLY -4- or -8- inputs are allowed.
	 */
	public void addAllRecipes() {
		this.addRecipe(new ItemStack(ItemHandler.UPGRADE_SPEED), new ItemStack(ItemHandler.UPGRADE_BASE),
				160, EnumTESRColour.YELLOW, new Object[] {
						new ItemStack(Blocks.GLOWSTONE), new ItemStack(Blocks.GLOWSTONE),
						new ItemStack(Blocks.REDSTONE_BLOCK), new ItemStack(Blocks.REDSTONE_BLOCK),
						new ItemStack(ItemHandler.ENERGY_WAFER), new ItemStack(ItemHandler.ENERGY_WAFER),
						new ItemStack(ItemHandler.CIRCUIT_ONE), new ItemStack(ItemHandler.CIRCUIT_ONE)
				});
		
		this.addRecipe(new ItemStack(ItemHandler.UPGRADE_FLUID_SPEED), new ItemStack(ItemHandler.UPGRADE_SPEED),
				160, EnumTESRColour.RED, new Object[] {
						new ItemStack(Blocks.REDSTONE_BLOCK), new ItemStack(Blocks.REDSTONE_BLOCK),
						new ItemStack(Blocks.GLOWSTONE), new ItemStack(Blocks.GLOWSTONE),
						new ItemStack(Items.WATER_BUCKET), new ItemStack(Items.WATER_BUCKET),
						new ItemStack(ItemHandler.CIRCUIT_ONE), new ItemStack(ItemHandler.CIRCUIT_ONE)
				});
		
		this.addRecipe(new ItemStack(ItemHandler.CIRCUIT_ONE), new ItemStack(ItemHandler.CIRCUIT_ONE_RAW), 
				120, EnumTESRColour.PURPLE, new Object[] { 						
						new ItemStack(ItemHandler.GOLD_DUST), new ItemStack(ItemHandler.GOLD_DUST),
						new ItemStack(Items.REDSTONE), new ItemStack(Items.REDSTONE),
						new ItemStack(Items.REDSTONE), new ItemStack(Items.REDSTONE),
						new ItemStack(ItemHandler.ENERGY_WAFER), new ItemStack(ItemHandler.ENERGY_WAFER)
				});
		
		this.addRecipe(new ItemStack(ItemHandler.CIRCUIT_THREE), new ItemStack(ItemHandler.CIRCUIT_THREE_RAW),
				200, EnumTESRColour.ORANGE, new Object[] { 
						new ItemStack(Blocks.REDSTONE_BLOCK), new ItemStack(Blocks.REDSTONE_BLOCK),
						new ItemStack(ItemHandler.CIRCUIT_ONE), new ItemStack(ItemHandler.CIRCUIT_ONE),
						new ItemStack(Items.DIAMOND), new ItemStack(Items.DIAMOND),
						new ItemStack(ItemHandler.ENERGY_WAFER), new ItemStack(ItemHandler.ENERGY_WAFER)
				});
		
		this.addRecipe(new ItemStack(ItemHandler.ENERGY_WAFER), new ItemStack(ItemHandler.SILICON_WAFER),
				100, EnumTESRColour.PURPLE, new Object[] { 
						new ItemStack(ItemHandler.ENERGY_DUST), new ItemStack(ItemHandler.ENERGY_DUST),
						new ItemStack(ItemHandler.ENERGY_INGOT), new ItemStack(ItemHandler.ENERGY_INGOT)
				});
		
		this.addRecipe(new ItemStack(ItemHandler.SILICON_WAFER), new ItemStack(ItemHandler.SILICON_INGOT),
				60, EnumTESRColour.GRAY, new Object[] {
						new ItemStack(ItemHandler.SILICON), new ItemStack(ItemHandler.SILICON),
						new ItemStack(Items.REDSTONE), new ItemStack(Items.REDSTONE)
				});
		
		this.addRecipe(new ItemStack(ItemHandler.ENERGY_INGOT), new ItemStack(Items.IRON_INGOT),
				80, EnumTESRColour.PURPLE, new Object[] { 
						new ItemStack(Items.REDSTONE), new ItemStack(Items.REDSTONE),
						new ItemStack(ItemHandler.ENERGY_DUST), new ItemStack(ItemHandler.ENERGY_DUST)
				});
		
		this.addRecipe(new ItemStack(ItemHandler.ENERGY_DUST), new ItemStack(Items.GLOWSTONE_DUST),
				40, EnumTESRColour.PURPLE, new Object[] { 
						new ItemStack(Items.REDSTONE), new ItemStack(Items.REDSTONE),
						new ItemStack(Items.REDSTONE), new ItemStack(Items.REDSTONE)
				});
		this.addRecipe(new ItemStack(BlockHandler.PROCESSING.BLOCK_STRUCTURE), new ItemStack(Blocks.IRON_BLOCK),
				140, EnumTESRColour.GRAY, new Object[] {
						new ItemStack(ItemHandler.CIRCUIT_ONE), new ItemStack(ItemHandler.CIRCUIT_ONE),
						new ItemStack(ItemHandler.ENERGY_INGOT), new ItemStack(ItemHandler.ENERGY_INGOT)
				});
	}

	public void testRecipes() {
		this.addRecipe(new ItemStack(Items.DIAMOND), new ItemStack(Items.COAL),
				60, EnumTESRColour.GRAY, new Object[] {
						new ItemStack(Blocks.DIRT), new ItemStack(Blocks.DIRT)
				});
	}

	public void addRecipe(ItemStack output, ItemStack focus, Integer process_time, EnumTESRColour colour, Object... inputs) {
		List<ItemStack> list = Lists.<ItemStack>newArrayList();

		if (inputs.length > 8) {
			throw new IllegalArgumentException("Invalid Synthesiser recipe: out of bounds exception [Recipe ingredient list is more than 8]!");
		}
		
		
		if (!(inputs.length == 2) && !(inputs.length == 4) && !(inputs.length == 8)) {
			throw new IllegalArgumentException("Invalid Synthesiser recipe: incorrect input count [Recipe ingredients list contains an incorrect number of ingredients]!");
		}
		
		for (Object object : inputs) {
			if (object instanceof ItemStack) {
				list.add(((ItemStack) object).copy());
			} else if (object instanceof Item) {
				list.add(new ItemStack((Item) object));
			} else if (object instanceof Block) {
				list.add(new ItemStack((Block) object));
			} else if (!(object instanceof Block)) {
				throw new IllegalArgumentException("Invalid Synthesiser recipe: unknown type [" + object.getClass().getName() + ", Unknown ingredient type]!");
			}
		}
		this.recipes.add(new SynthesiserRecipe(output, list, focus, process_time, colour));
	}

	public void addRecipe(IMultiRecipe recipe) {
		this.recipes.add(recipe);
	}

	public ItemStack findMatchingRecipe(List<ItemStack> list) {
		for (IMultiRecipe irecipe : this.recipes) {
			if (irecipe.matches(list)) {
				return irecipe.getOutput();
			}
		}
		return ItemStack.EMPTY;
	}

	public ItemStack findFocusStack(List<ItemStack> list) {
		for (IMultiRecipe irecipe : this.recipes) {
			if (irecipe.matches(list)) {
				return irecipe.getFocusStack();
			}
		}
		return ItemStack.EMPTY;
	}

	public NonNullList<ItemStack> getRemainingItems(List<ItemStack> list) {
		for (IMultiRecipe irecipe : this.recipes) {
			if (irecipe.matches(list)) {
				return irecipe.getRemainingItems(list);
			}
		}
		NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(list.size(), ItemStack.EMPTY);
		for (int i = 0; i < nonnulllist.size(); ++i) {
			nonnulllist.set(i, list.get(i));
		}
		return nonnulllist;
	}

	public List<IMultiRecipe> getRecipeList() {
		return this.recipes;
	}

	public Integer findProcessTime(List<ItemStack> list) {
		for (IMultiRecipe irecipe : this.recipes) {
			if (irecipe.matches(list)) {
				return irecipe.getProcessTime();
			}
		}
		return 0;
	}

	public EnumTESRColour findColour(List<ItemStack> list) {
		for (IMultiRecipe irecipe : this.recipes) {
			if (irecipe.matches(list)) {
				return irecipe.getColour();
			}
		}
		return EnumTESRColour.WHITE;
	}

	public class SynthesiserRecipe implements IMultiRecipe {

		/**
		 * ItemStack output of the recipe.
		 */
		private final ItemStack STACK_OUTPUT;

		/**
		 * List<ItemStack> of ingredients.
		 */
		public final List<ItemStack> INPUT_STACKS;

		/**
		 * ItemStack centre stack in the synthesiser.
		 */
		private final ItemStack STACK_FOCUS;

		/**
		 * int time it takes to complete the recipe.
		 */
		private final Integer PROCESS_TIME;

		/**
		 * {@link EnumTESRColour} colour of the beam.
		 */
		private final EnumTESRColour COLOUR;

		public SynthesiserRecipe(ItemStack output, List<ItemStack> inputs, ItemStack focus, Integer process_time, EnumTESRColour colour) {
			this.STACK_OUTPUT = output;
			this.INPUT_STACKS = inputs;
			this.STACK_FOCUS = focus;
			this.PROCESS_TIME = process_time;
			this.COLOUR = colour;
		}

		@Override
		public ItemStack getOutput() {
			return this.STACK_OUTPUT;
		}

		@Override
		public NonNullList<ItemStack> getRemainingItems(List<ItemStack> stack) {
			NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(stack.size(), ItemStack.EMPTY);

			for (int i = 0; i < nonnulllist.size(); ++i) {
				ItemStack itemstack = stack.get(i);
				nonnulllist.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
			}

			return nonnulllist;
		}

		/**
		 * Used to check if a recipe matches current center + pedestal stacks.
		 */
		@Override
		public boolean matches(List<ItemStack> stack) {
			List<ItemStack> list = Lists.newArrayList(this.INPUT_STACKS);

			for (int j = 0; j < stack.size(); ++j) {
				ItemStack itemstack = stack.get(j);

				if (!itemstack.isEmpty()) {
					boolean flag = false;

					for (ItemStack itemstack1 : list) {
						if (itemstack.isItemEqual(itemstack1) && (itemstack1.getMetadata() == 32767 || itemstack.getMetadata() == itemstack1.getMetadata())) {
							flag = true;
							list.remove(itemstack1);
							break;
						}
					}

					if (!flag) {
						return false;
					}
				}
			}
			return list.isEmpty();
		}

		@Override
		public ItemStack getResult() {
			return this.STACK_OUTPUT.copy();
		}

		@Override
		public int getRecipeSize() {
			return this.INPUT_STACKS.size();
		}

		@Override
		public List<ItemStack> getRecipeInput() {
			return this.INPUT_STACKS;
		}

		@Override
		public ItemStack getFocusStack() {
			return STACK_FOCUS;
		}

		@Override
		public Integer getProcessTime() {
			return PROCESS_TIME;
		}

		@Override
		public EnumTESRColour getColour() {
			return COLOUR;
		}
	}
}