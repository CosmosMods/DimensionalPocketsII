package com.zeher.zehercraft.integration.jei.core;

import com.zeher.zehercraft.ZCReference;
import com.zeher.zehercraft.ZeherCraft;
import com.zeher.zeherlib.ZLReference;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class SeparatorCategory implements IRecipeCategory {

	private final String loc_title;
	private final IDrawable background;
	
	private final IDrawableAnimated process;
	private final IDrawableAnimated stored;
	
	protected static final int input_slot = 0;
	protected static final int output_slot = 2;
	
	public SeparatorCategory(IGuiHelper helper) {
		loc_title = I18n.format(ZCReference.JEI.SEPARATOR_UNLOC);
		background = helper.createDrawable(ZCReference.RESOURCE.PROCESSING.SEPARATOR_LOC_GUI_JEI, 0, 0, 74, 66);
		
		IDrawableStatic process_draw = helper.createDrawable(ZCReference.RESOURCE.PROCESSING.SEPARATOR_LOC_GUI, 176, 0, 16, 16);
		IDrawableStatic stored_draw = helper.createDrawable(ZLReference.RESOURCE.BASE.GUI_ENERGY_BAR_LOC, ZLReference.RESOURCE.INFO.ENERGY_BAR_SMALL[0], ZLReference.RESOURCE.INFO.ENERGY_BAR_SMALL[1], 18, 40);
		
		this.process = helper.createAnimatedDrawable(process_draw, 100, IDrawableAnimated.StartDirection.TOP, false);
		this.stored = helper.createAnimatedDrawable(stored_draw, 200, IDrawableAnimated.StartDirection.TOP, true);
	}
	
	@Override
	public String getUid() {
		return ZLReference.JEI.SEPARATOR_UID;
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
		process.draw(minecraft, 55, 28);
		stored.draw(minecraft, 27, 5);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		if (!(recipeWrapper instanceof SeparatorRecipeJEI)) {
			return;
		}
		
		IGuiItemStackGroup gui_stacks = recipeLayout.getItemStacks();
		
		gui_stacks.init(input_slot, true, 54, 6);
		gui_stacks.init(output_slot, false, 54, 48);
		
		gui_stacks.set(ingredients);
	}

}
