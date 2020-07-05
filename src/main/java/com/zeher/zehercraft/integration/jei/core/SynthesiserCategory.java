package com.zeher.zehercraft.integration.jei.core;

import com.zeher.zehercraft.ZCReference;
import com.zeher.zehercraft.ZeherCraft;
import com.zeher.zeherlib.ZLReference;
import com.zeher.zeherlib.api.client.tesr.EnumTESRColour;
import com.zeher.zeherlib.mod.util.ModGuiUtil;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class SynthesiserCategory implements IRecipeCategory {

	private final String loc_title;
	private final IDrawable background;
	
	private final IGuiHelper helper;
	
	private IDrawable laser;
	private IDrawable highlight;
	private IDrawable highlight8;
	
	private final ResourceLocation[] RESOURCES = new ResourceLocation[] { 
			ZCReference.RESOURCE.PROCESSING.SYNTHESISER_LOC_GUI_JEI_LASER_8,
			ZCReference.RESOURCE.PROCESSING.SYNTHESISER_LOC_GUI_JEI_LASER_4, 
			ZCReference.RESOURCE.PROCESSING.SYNTHESISER_LOC_GUI_JEI, 
			ZCReference.RESOURCE.PROCESSING.SYNTHESISER_LOC_GUI_JEI_LASER_2 };
	
	public SynthesiserCategory(IGuiHelper helper) {
		this.helper = helper;
		
		loc_title = I18n.format(ZCReference.JEI.SYNTHESISER_UNLOC);
		
		background = helper.createDrawable(ZCReference.RESOURCE.PROCESSING.SYNTHESISER_LOC_GUI_JEI, 0, 0, 182, 222);	
	}
	
	@Override
	public String getUid() {
		return ZLReference.JEI.SYNTHESISER_UID;
	}

	@Override
	public String getTitle() {
		return loc_title;
	}

	@Override
	public String getModName() {
		return ZeherCraft.MOD_NAME;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}
	
	@Override
	public void drawExtras(Minecraft minecraft) {
		FontRenderer renderer = minecraft.fontRenderer;
		
		this.laser.draw(minecraft, 33, 43);
		
		this.highlight.draw(minecraft, 140, 19);
		this.highlight8.draw(minecraft, 140, 54);
		
		ModGuiUtil.FONT.DRAW.drawString(renderer, new int[] { 0, 0 }, 6, 145, "The Synthesiser is an in-world", false, false, ModGuiUtil.DEFAULT_COLOUR_FONT_LIST);
		ModGuiUtil.FONT.DRAW.drawString(renderer, new int[] { 0, 0 }, 6, 155, "multiblock. To construct: place a", false, false, ModGuiUtil.DEFAULT_COLOUR_FONT_LIST);
		ModGuiUtil.FONT.DRAW.drawString(renderer, new int[] { 0, 0 }, 6, 165, "Synthesiser with 4 or 8 stands in", false, false, ModGuiUtil.DEFAULT_COLOUR_FONT_LIST);
		ModGuiUtil.FONT.DRAW.drawString(renderer, new int[] { 0, 0 }, 6, 175, "the arrangement above, with 2", false, false, ModGuiUtil.DEFAULT_COLOUR_FONT_LIST);
		ModGuiUtil.FONT.DRAW.drawString(renderer, new int[] { 0, 0 }, 6, 185, "blocks between each stand and", false, false, ModGuiUtil.DEFAULT_COLOUR_FONT_LIST);
		ModGuiUtil.FONT.DRAW.drawString(renderer, new int[] { 0, 0 }, 6, 195, "the center Synthesiser.", false, false, ModGuiUtil.DEFAULT_COLOUR_FONT_LIST);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		if (!(recipeWrapper instanceof SynthesiserRecipeJEI)) {
			return;
		}

		IGuiItemStackGroup gui_stacks = recipeLayout.getItemStacks();
		int size = ingredients.getInputs(VanillaTypes.ITEM).size();
		
		//IDrawable 'laser' set here because if set inside the IRecipeWrapper, the element is drawn dark.
		EnumTESRColour colour = ((SynthesiserRecipeJEI) recipeWrapper).getColour();
		
		if (size == 3) {
			this.laser = this.helper.createDrawable(RESOURCES[3], ZCReference.RESOURCE.PROCESSING.LASER_JEI_ARRAY_X[colour.getIndex()], ZCReference.RESOURCE.PROCESSING.LASER_JEI_ARRAY_Y[colour.getIndex()], 60, 60);
			this.highlight = this.helper.createDrawable(RESOURCES[2], 182, 0, 32, 32);
			this.highlight8 = this.helper.createDrawable(RESOURCES[2], 182, 0, 32, 32);
		} else if (size == 5) {
			this.laser = this.helper.createDrawable(RESOURCES[1], ZCReference.RESOURCE.PROCESSING.LASER_JEI_ARRAY_X[colour.getIndex()], ZCReference.RESOURCE.PROCESSING.LASER_JEI_ARRAY_Y[colour.getIndex()], 60, 60);
			this.highlight = this.helper.createDrawable(RESOURCES[2], 182, 0, 32, 32);
			this.highlight8 = this.helper.createDrawable(RESOURCES[2], 182, 0, 32, 32);
		} else if (size == 9) {
			this.laser = this.helper.createDrawable(RESOURCES[0], ZCReference.RESOURCE.PROCESSING.LASER_JEI_ARRAY_X[colour.getIndex()], ZCReference.RESOURCE.PROCESSING.LASER_JEI_ARRAY_Y[colour.getIndex()], 60, 60);
			this.highlight = this.helper.createDrawable(RESOURCES[2], 182, 32, 32, 32);
			this.highlight8 = this.helper.createDrawable(RESOURCES[2], 182, 0, 32, 32);
		}
		
		//Focus
		gui_stacks.init(0, true, 54, 64);
		
		//Perpendicular
		gui_stacks.init(1, true, 54, 22);
		gui_stacks.init(2, true, 54, 106);
		gui_stacks.init(3, true, 12, 64);
		gui_stacks.init(4, true, 96, 64);
		
		//Diagonal
		gui_stacks.init(5, true, 20, 30);
		gui_stacks.init(6, true, 88, 98);
		gui_stacks.init(7, true, 88, 30);
		gui_stacks.init(8, true, 20, 98);
		
		//Output
		gui_stacks.init(9, false, 147, 102);
		
		//Set each stack with the correct ingredient.
		for (int i = 0; i < size; i++) {
			gui_stacks.set(i, ingredients.getInputs(VanillaTypes.ITEM).get(i));
		}
		
		gui_stacks.set(9, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
	}	
}