package com.zeher.zehercraft.integration.jei.core;

import java.util.ArrayList;
import java.util.List;

import com.zeher.zehercraft.ZCReference;
import com.zeher.zehercraft.core.handler.recipe.SynthesiserRecipeHandler;
import com.zeher.zeherlib.api.client.tesr.EnumTESRColour;
import com.zeher.zeherlib.api.core.interfaces.IMultiRecipe;
import com.zeher.zeherlib.mod.util.ModGuiUtil;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class SynthesiserRecipeJEI implements IRecipeWrapper {

	private final ItemStack output;
	private final List<ItemStack> stacks;
	private final int process_time;
	
	private final EnumTESRColour colour;
	
	private final IJeiHelpers helpers;
	
	public SynthesiserRecipeJEI(ItemStack output, List<ItemStack> stacks, int process_time, EnumTESRColour colour, IJeiHelpers helpers) {
		this.output = output;
		this.stacks = stacks;
		this.process_time = process_time;
		this.colour = colour;
		
		this.helpers = helpers;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputs(VanillaTypes.ITEM, stacks);
		ingredients.setOutput(VanillaTypes.ITEM, output);
	}
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		FontRenderer renderer = minecraft.fontRenderer;
		
		ModGuiUtil.FONT.DRAW.drawString(renderer, new int[] { 0, 0 }, 6, 5, this.output.getDisplayName(), false, false);
	}
	
	public static class RecipeMaker {
		public static List<SynthesiserRecipeJEI> getSynthesisingRecipes(IJeiHelpers helper) {
			SynthesiserRecipeHandler recipes = SynthesiserRecipeHandler.getInstance();
			List<IMultiRecipe> recipe_list = recipes.getRecipeList();
			
			List<SynthesiserRecipeJEI> recipes_list_jei = new ArrayList<>();
			
			for (int x = 0; x < recipe_list.size(); x++) {
				ItemStack output = recipe_list.get(x).getOutput();
				List<ItemStack> list = recipe_list.get(x).getRecipeInput();
				ItemStack focus_stack = recipe_list.get(x).getFocusStack();
				
				EnumTESRColour colour = recipe_list.get(x).getColour();
				
				int process_time_ = recipe_list.get(x).getProcessTime();
				
				List<ItemStack> inputs = new ArrayList<ItemStack>() {};
				
				inputs.add(focus_stack);
				inputs.addAll(list);
				
				SynthesiserRecipeJEI recipe = new SynthesiserRecipeJEI(output, inputs, process_time_, colour, helper);
				
				recipes_list_jei.add(recipe);
			}
			return recipes_list_jei;
		}
	}
	
	public EnumTESRColour getColour() {
		return this.colour;
	}
}