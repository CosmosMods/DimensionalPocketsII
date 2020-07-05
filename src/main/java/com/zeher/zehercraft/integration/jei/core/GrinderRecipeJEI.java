package com.zeher.zehercraft.integration.jei.core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.zeher.zeherlib.ZLReference;
import com.zeher.zeherlib.core.recipe.GrinderRecipes;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class GrinderRecipeJEI implements IRecipeWrapper {

	private final List<List<ItemStack>> inputs;
	private final ItemStack output;
	private final ItemStack second;
	
	public GrinderRecipeJEI(List<ItemStack> inputs, ItemStack output, ItemStack second) {
		this.inputs = Collections.singletonList(inputs);
		this.output = output;
		this.second = second;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, inputs);
		ingredients.setOutputs(VanillaTypes.ITEM, new ArrayList<ItemStack>() {{
			add(output);
			add(second);
		}}); //output);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		GrinderRecipes recipes = GrinderRecipes.getInstance();
		
		float experience = recipes.getGrindingExperience(output);
		
		if (experience > 0) {
			String experience_string = I18n.format(ZLReference.JEI.EXPERIENCE);
			FontRenderer renderer = minecraft.fontRenderer;
			
			int string_width = renderer.getStringWidth(experience_string);
			
			renderer.drawString(experience + " " + experience_string, recipeWidth - string_width + 15, 5, Color.gray.getRGB());
		}
	}
	
	public static class RecipeMaker {
		public static List<GrinderRecipeJEI> getGrindingRecipes(IJeiHelpers helper) {
			IStackHelper stack_helper = helper.getStackHelper();
			GrinderRecipes recipes = GrinderRecipes.getInstance();
			
			Map<ItemStack, ItemStack> grinding_map = recipes.getGrindingList();
			Map<ItemStack, ItemStack> secondary_map = recipes.getSecondaryList();
			
			List<GrinderRecipeJEI> recipes_list = new ArrayList<>();
			
			for (Map.Entry<ItemStack, ItemStack> entry : grinding_map.entrySet()) {
				ItemStack input = entry.getKey();
				ItemStack output = entry.getValue();
				
				for (Map.Entry<ItemStack, ItemStack> entry_ : secondary_map.entrySet()) {
					ItemStack input_ = entry_.getKey();
					ItemStack secondary = entry_.getValue();
					
					if (input.equals(input_)) {
						List<ItemStack> inputs = stack_helper.getSubtypes(input);
						GrinderRecipeJEI recipe = new GrinderRecipeJEI(inputs, output, secondary);
						
						recipes_list.add(recipe);
					}
				}
			}
			return recipes_list;
		}
	}
}