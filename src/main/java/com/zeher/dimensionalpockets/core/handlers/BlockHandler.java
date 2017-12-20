package com.zeher.dimensionalpockets.core.handlers;

import java.util.HashSet;
import java.util.Set;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.core.block.*;
import com.zeher.trzcore.core.block.*;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.*;

@ObjectHolder(DimensionalPockets.mod_id)
@Mod.EventBusSubscriber(modid = DimensionalPockets.mod_id)
public class BlockHandler {
	
	public static final Block block_dimensional_ore = new TRZBlockOreGem("block_dimensional_ore", ItemHandler.dimensional_shard, "pickaxe", 2, 3, DimensionalPockets.tab_dimensionalpockets);
	public static final Block block_dimensional = new TRZBlock("block_dimensional", Material.IRON, "pickaxe", 2, 8, 8, DimensionalPockets.tab_dimensionalpockets);
	
	public static final BlockDimensionalPocket block_dimensional_pocket = new BlockDimensionalPocket("block_dimensional_pocket", Material.CIRCUITS, "pickaxe", 4, 12, 12, DimensionalPockets.tab_dimensionalpockets);
	public static final BlockDimensionalPocketWall block_dimensional_pocket_wall = new BlockDimensionalPocketWall("block_dimensional_pocket_wall", Material.GROUND, "", 5, 40, 40, null);
	
	@SubscribeEvent
	public static void registerBlockModelLocations(ModelRegistryEvent event) {
		BlockRegistry.registerModelLocation(block_dimensional_ore);
		BlockRegistry.registerModelLocation(block_dimensional);
		BlockRegistry.registerModelLocation(block_dimensional_pocket);
		BlockRegistry.registerModelLocation(block_dimensional_pocket_wall);
	}
	
	@Mod.EventBusSubscriber(modid = DimensionalPockets.mod_id)
	public static class RegistryHandler {
		
		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> event) {
			final IForgeRegistry<Block> registry = event.getRegistry();
			
			final Block[] blocks = {
					block_dimensional_ore,
					block_dimensional,
					block_dimensional_pocket,
					block_dimensional_pocket_wall
			};
			
			registry.registerAll(blocks);
		}
		
		@SubscribeEvent
		public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
			final IForgeRegistry<Item> registry = event.getRegistry();
			
			BlockRegistry.registerItemBlock(registry, block_dimensional_ore);
			BlockRegistry.registerItemBlock(registry, block_dimensional);
			BlockRegistry.registerItemBlock(registry, block_dimensional_pocket);
			BlockRegistry.registerItemBlock(registry, block_dimensional_pocket_wall);
		}
	}
	
	public static class BlockRegistry {
		
		public static void registerModelLocation(Block block) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
		}
		
		public static void registerItemBlock(IForgeRegistry registry, Block block) {
			registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
		}
	}
	
}
