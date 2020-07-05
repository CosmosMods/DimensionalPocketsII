package com.zeher.zehercraft.integration.jei;

import javax.annotation.Nonnull;

import com.zeher.zehercraft.core.handler.BlockHandler;
import com.zeher.zehercraft.integration.jei.core.CompactorCategory;
import com.zeher.zehercraft.integration.jei.core.CompactorRecipeJEI;
import com.zeher.zehercraft.integration.jei.core.GrinderCategory;
import com.zeher.zehercraft.integration.jei.core.GrinderRecipeJEI;
import com.zeher.zehercraft.integration.jei.core.SeparatorCategory;
import com.zeher.zehercraft.integration.jei.core.SeparatorRecipeJEI;
import com.zeher.zehercraft.integration.jei.core.SynthesiserCategory;
import com.zeher.zehercraft.integration.jei.core.SynthesiserRecipeJEI;
import com.zeher.zehercraft.processing.client.gui.GuiCompactor;
import com.zeher.zehercraft.processing.client.gui.GuiGrinder;
import com.zeher.zehercraft.processing.client.gui.GuiKiln;
import com.zeher.zeherlib.ZLReference;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JeiPlugin implements IModPlugin {
	
	public JeiPlugin () { }
	
	@Override
	public void register(@Nonnull IModRegistry registry) {
		final IJeiHelpers helper = registry.getJeiHelpers();
		final IGuiHelper gui_helper = helper.getGuiHelper();
		
		registry.addRecipes(GrinderRecipeJEI.RecipeMaker.getGrindingRecipes(helper), ZLReference.JEI.GRINDER_UID);
		registry.addRecipes(CompactorRecipeJEI.RecipeMaker.getCompressingRecipes(helper), ZLReference.JEI.COMPACTOR_UID);
		registry.addRecipes(SeparatorRecipeJEI.RecipeMaker.getExtractingRecipes(helper), ZLReference.JEI.SEPARATOR_UID);
		registry.addRecipes(SynthesiserRecipeJEI.RecipeMaker.getSynthesisingRecipes(helper), ZLReference.JEI.SYNTHESISER_UID);
		
		registry.addRecipeClickArea(GuiKiln.class, 102, 38, 16, 16, VanillaRecipeCategoryUid.SMELTING);
		registry.addRecipeClickArea(GuiGrinder.class, 104, 38, 16, 16, ZLReference.JEI.GRINDER_UID);
		registry.addRecipeClickArea(GuiCompactor.class, 104, 38, 16, 16, ZLReference.JEI.COMPACTOR_UID);
		
		registry.addRecipeCatalyst(new ItemStack(BlockHandler.PROCESSING.BLOCK_KILN), VanillaRecipeCategoryUid.SMELTING);
		registry.addRecipeCatalyst(new ItemStack(BlockHandler.PROCESSING.BLOCK_GRINDER), ZLReference.JEI.GRINDER_UID);
		registry.addRecipeCatalyst(new ItemStack(BlockHandler.PROCESSING.BLOCK_COMPACTOR), ZLReference.JEI.COMPACTOR_UID);
		registry.addRecipeCatalyst(new ItemStack(BlockHandler.PROCESSING.BLOCK_SYNTHESISER), ZLReference.JEI.SYNTHESISER_UID);
		registry.addRecipeCatalyst(new ItemStack(BlockHandler.PROCESSING.BLOCK_SYNTHESISER_STAND), ZLReference.JEI.SYNTHESISER_UID);
	}
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		final IJeiHelpers helper = registry.getJeiHelpers();
		final IGuiHelper gui_helper = helper.getGuiHelper();
		
		registry.addRecipeCategories( 
				new GrinderCategory(gui_helper), 
				new CompactorCategory(gui_helper), 
				new SeparatorCategory(gui_helper),
				new SynthesiserCategory(gui_helper));
	}
}