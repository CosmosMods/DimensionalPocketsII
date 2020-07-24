package com.zeher.dimpockets.core.integration.jei;

import javax.annotation.Nonnull;

import com.zeher.dimpockets.core.manager.ModBlockManager;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JeiPlugin implements IModPlugin {
	
	@Override
	public void register(@Nonnull IModRegistry registry) {
		final IJeiHelpers helper = registry.getJeiHelpers();
		
		IIngredientBlacklist blacklist = helper.getIngredientBlacklist();
		
		blacklist.addIngredientToBlacklist(new ItemStack(ModBlockManager.BLOCK_DIMENSIONAL_POCKET_WALL));
		blacklist.addIngredientToBlacklist(new ItemStack(ModBlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_CONNECTOR));
		blacklist.addIngredientToBlacklist(new ItemStack(ModBlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_EDGE));
		blacklist.addIngredientToBlacklist(new ItemStack(ModBlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_ENERGY_DISPLAY));
		blacklist.addIngredientToBlacklist(new ItemStack(ModBlockManager.BLOCK_DIMENSIONAL_POCKET_WALL_FLUID_DISPLAY));
	}
}