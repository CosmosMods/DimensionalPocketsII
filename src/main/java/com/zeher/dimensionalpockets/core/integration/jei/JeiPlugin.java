package com.zeher.dimensionalpockets.core.integration.jei;

import javax.annotation.Nonnull;

import com.zeher.dimensionalpockets.core.handler.BlockHandler;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JeiPlugin extends BlankModPlugin {
	
	@Override
	public void register(@Nonnull IModRegistry registry) {
		final IJeiHelpers helper = registry.getJeiHelpers();
		
		IIngredientBlacklist blacklist = helper.getIngredientBlacklist();
		
		blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.BLOCK_DIMENSIONAL_POCKET_WALL));
		blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.BLOCK_DIMENSIONAL_POCKET_WALL_EDGE));
	}
}
