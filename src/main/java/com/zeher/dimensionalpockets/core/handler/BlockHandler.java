package com.zeher.dimensionalpockets.core.handler;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.pocket.block.BlockDimensionalPocket;
import com.zeher.dimensionalpockets.pocket.block.BlockDimensionalPocketWall;
import com.zeher.dimensionalpockets.pocket.block.BlockDimensionalPocketWallEdge;
import com.zeher.dimensionalpockets.pocket.block.ItemBlockDimensionalPocket;
import com.zeher.zeherlib.core.block.ModBlock;
import com.zeher.zeherlib.core.block.BlockGemedOre;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID)
@ObjectHolder(DimensionalPockets.MOD_ID)
public class BlockHandler {
	
	private static ItemBlock[] blockItems;
	
	public static final Block BLOCK_DIMENSIONAL_ORE = new BlockGemedOre("block_dimensional_ore", Material.ROCK, "pickaxe", 2, 3, 8, DimensionalPockets.TAB_DIMENSIONALPOCKETS, ItemHandler.dimensional_shard);;
	public static final Block BLOCK_DIMENSIONAL = new ModBlock("block_dimensional", Material.IRON, "pickaxe", 2, 8, 8, DimensionalPockets.TAB_DIMENSIONALPOCKETS);
	
	public static final BlockDimensionalPocket BLOCK_DIMENSIONAL_POCKET = new BlockDimensionalPocket("block_dimensional_pocket", Material.IRON, "pickaxe", 3, 12, 12, DimensionalPockets.TAB_DIMENSIONALPOCKETS);
	public static final ItemBlockDimensionalPocket ITEMBLOCK_DIMENSIONAL_POCKET = new ItemBlockDimensionalPocket(BLOCK_DIMENSIONAL_POCKET);
	
	public static final BlockDimensionalPocketWall BLOCK_DIMENSIONAL_POCKET_WALL = new BlockDimensionalPocketWall("block_dimensional_pocket_wall", Material.BARRIER, "", -1, null);
	public static final BlockDimensionalPocketWallEdge BLOCK_DIMENSIONAL_POCKET_WALL_EDGE = new BlockDimensionalPocketWallEdge("block_dimensional_pocket_wall_edge", Material.BARRIER, "", -1, null);
	
	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		event.getRegistry().register(BLOCK_DIMENSIONAL_POCKET);
		
		registerAllBlocks(event, BLOCK_DIMENSIONAL_ORE, BLOCK_DIMENSIONAL, BLOCK_DIMENSIONAL_POCKET_WALL, BLOCK_DIMENSIONAL_POCKET_WALL_EDGE);
	}
	
	@SubscribeEvent 
	public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
		event.getRegistry().register(ITEMBLOCK_DIMENSIONAL_POCKET.setRegistryName(BLOCK_DIMENSIONAL_POCKET.getRegistryName()));
		
		event.getRegistry().registerAll(blockItems);
		
		blockItems = null;
	}
	
	private static void registerAllBlocks(RegistryEvent.Register<Block> event, Block... blocks) {
		blockItems = new ItemBlock[blocks.length];
		event.getRegistry().registerAll(blocks);
		for (int i = 0; i < blocks.length; i++) {
			blockItems[i] = new ItemBlock(blocks[i]);
			blockItems[i].setRegistryName(blocks[i].getRegistryName());
		}
	}

	@SubscribeEvent
	public static void registerModelLocations(final ModelRegistryEvent event) {
		registerBlockModelLocation(BLOCK_DIMENSIONAL_ORE);
		registerBlockModelLocation(BLOCK_DIMENSIONAL);
		registerBlockModelLocation(BLOCK_DIMENSIONAL_POCKET);
		registerBlockModelLocation(BLOCK_DIMENSIONAL_POCKET_WALL);
		registerBlockModelLocation(BLOCK_DIMENSIONAL_POCKET_WALL_EDGE);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerBlockModelLocation(Block block){
		Item item = Item.getItemFromBlock(block);
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}
