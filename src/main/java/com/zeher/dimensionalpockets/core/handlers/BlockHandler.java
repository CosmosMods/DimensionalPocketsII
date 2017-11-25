package com.zeher.dimensionalpockets.core.handlers;

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
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.*;

@ObjectHolder(DimensionalPockets.mod_id)
public class BlockHandler {
	
	public static Block block_dimensional_ore;
	public static Block block_dimensional;
	
	public static BlockDimensionalPocket block_dimensional_pocket;
	public static BlockDimensionalPocketWall block_dimensional_pocket_wall;
	
	public static void preInit() {
		block_dimensional_ore = new TRZBlockOreGem("block_dimensional_ore", ItemHandler.dimensional_shard, "pickaxe", 2, 3, DimensionalPockets.tab_dimensionalpockets);
		block_dimensional = new TRZBlock("block_dimensional", Material.IRON, "pickaxe", 2, 8, 8, DimensionalPockets.tab_dimensionalpockets);
		
		block_dimensional_pocket = new BlockDimensionalPocket("block_dimensional_pocket", Material.CIRCUITS, "pickaxe", 4, 12, 12, DimensionalPockets.tab_dimensionalpockets);
		block_dimensional_pocket_wall = new BlockDimensionalPocketWall("block_dimensional_pocket_wall", Material.GROUND, "", 5, 40, 40, null);
	}
	
	 
	public static void register(){
		registerBlock(block_dimensional_ore);
		registerBlock(block_dimensional);
		
		registerBlock(block_dimensional_pocket);
		registerBlock(block_dimensional_pocket_wall);
		
	}
	 
	public static void registerModelLocations(){
		registerBlockModelLocation(block_dimensional_ore);
		registerBlockModelLocation(block_dimensional);
		
		registerBlockModelLocation(block_dimensional_pocket);
		registerBlockModelLocation(block_dimensional_pocket_wall);
		
	}
	 
	public static void registerBlock(Block block){
		ItemBlock item = new ItemBlock(block);
		GameRegistry.register(block);
		GameRegistry.register(item, block.getRegistryName());
		
	}
	
	public static void registerBlock(Block block, ItemBlock item){
		GameRegistry.register(block);
		GameRegistry.register(item, block.getRegistryName());
	}
	
	public static void registerBlockModelLocation(Block block){
		Item item = Item.getItemFromBlock(block);
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	
}
