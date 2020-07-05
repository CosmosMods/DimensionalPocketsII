package com.zeher.zehercraft.integration.jei.core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.zeher.zeherlib.ZLReference;
import com.zeher.zeherlib.core.recipe.SeparatorRecipes;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class SeparatorRecipeJEI implements IRecipeWrapper {

	private final List<List<ItemStack>> inputs;
	private final ItemStack output;
	private final ItemStack secondary;
	
	public SeparatorRecipeJEI(List<ItemStack>inputs, ItemStack output, ItemStack secondary) {
		this.inputs = Collections.singletonList(inputs);
		this.output = output;
		this.secondary = secondary;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, inputs);
		ingredients.setOutputs(VanillaTypes.ITEM, new ArrayList<ItemStack>() {{
			add(output);
			add(secondary);
		}});
	}
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		SeparatorRecipes recipes = SeparatorRecipes.getInstance();
		
		float experience = recipes.getSeparatingExperience(output);
		
		if (experience > 0) {
			String experience_string = I18n.format(ZLReference.JEI.EXPERIENCE);
			FontRenderer renderer = minecraft.fontRenderer;
			
			int string_width = renderer.getStringWidth(experience_string);
			
			renderer.drawString(experience + " " + experience_string, recipeWidth - string_width + 15, 5, Color.gray.getRGB());
		}
	}
	
	public static class RecipeMaker {
		
		public static List<SeparatorRecipeJEI> getExtractingRecipes(IJeiHelpers helper) {
			IStackHelper stack_helper = helper.getStackHelper();
			SeparatorRecipes recipes = SeparatorRecipes.getInstance();
			
			Map<ItemStack, ItemStack> extracting_map = recipes.getSeperatingList();
			Map<ItemStack, ItemStack> secondary_map = recipes.getSecondaryList();
			
			List<SeparatorRecipeJEI> recipes_list = new ArrayList<>();
			
			for (Map.Entry<ItemStack, ItemStack> entry : extracting_map.entrySet()) {
				ItemStack input = entry.getKey();
				ItemStack output = entry.getValue();
				
				for (Map.Entry<ItemStack, ItemStack> entry_ : secondary_map.entrySet()) {
					ItemStack input_ = entry_.getKey();
					ItemStack secondary = entry_.getValue();
					
					if (input.equals(input_)) {
						List<ItemStack> inputs = stack_helper.getSubtypes(input);
						SeparatorRecipeJEI recipe = new SeparatorRecipeJEI(inputs, output, secondary);
						
						recipes_list.add(recipe);
					}
				}
			}
			return recipes_list;
		}
	}
}