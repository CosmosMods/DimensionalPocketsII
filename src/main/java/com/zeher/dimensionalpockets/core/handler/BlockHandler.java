package com.zeher.dimensionalpockets.core.handler;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.dimensionalpockets.pocket.block.BlockDimensionalGlass;
import com.zeher.dimensionalpockets.pocket.block.BlockDimensionalPocket;
import com.zeher.dimensionalpockets.pocket.block.BlockDimensionalPocketWall;
import com.zeher.dimensionalpockets.pocket.block.BlockDimensionalPocketWallConnector;
import com.zeher.dimensionalpockets.pocket.block.BlockDimensionalPocketWallEdge;
import com.zeher.dimensionalpockets.pocket.block.ItemBlockDimensionalPocket;
import com.zeher.zeherlib.mod.block.ModBlock;
import com.zeher.zeherlib.mod.block.ModBlockDropsItem;
import com.zeher.zeherlib.mod.block.ModBlockModelUnplaceable;

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
	
	private static ItemBlock[] BLOCK_ITEMS;
	
	public static final Block BLOCK_DIMENSIONAL_ORE = new ModBlockDropsItem("block_dimensional_ore", Material.ROCK, "pickaxe", 2, 3, 8, DimensionalPockets.TAB_DIMENSIONALPOCKETS, ItemHandler.DIMENSIONAL_SHARD);;
	public static final Block BLOCK_DIMENSIONAL = new ModBlock("block_dimensional", Material.IRON, "pickaxe", 2, 8, 8, DimensionalPockets.TAB_DIMENSIONALPOCKETS);
	public static final Block BLOCK_DIMENSIONAL_METAL = new ModBlock("block_dimensional_metal", Material.IRON, "pickaxe", 2, 8, 8, DimensionalPockets.TAB_DIMENSIONALPOCKETS);
	
	public static final Block BLOCK_DIMENSIONAL_POCKET = new BlockDimensionalPocket("block_dimensional_pocket", Material.IRON, "pickaxe", 3, 12, 12, DimensionalPockets.TAB_DIMENSIONALPOCKETS);
	public static final ItemBlock ITEMBLOCK_DIMENSIONAL_POCKET = new ItemBlockDimensionalPocket(BLOCK_DIMENSIONAL_POCKET,"Creates a pocket dimension!", "Once placed shift-right click to enter the pocket.", "To exit the pocket, simply shift-right click on any wall.");
	
	public static final Block BLOCK_DIMENSIONAL_CORE = new ModBlockModelUnplaceable("block_dimensional_core", Material.IRON, "pickaxe", 2, 100, 100, DimensionalPockets.TAB_DIMENSIONALPOCKETS);
	
	public static final Block BLOCK_DIMENSIONAL_POCKET_WALL = new BlockDimensionalPocketWall("block_dimensional_pocket_wall", Material.BARRIER, "", -1, null);
	public static final Block BLOCK_DIMENSIONAL_POCKET_WALL_EDGE = new BlockDimensionalPocketWallEdge("block_dimensional_pocket_wall_edge", Material.BARRIER, "", -1, null);
	
	public static final Block BLOCK_DIMENSIONAL_POCKET_WALL_CONNECTOR = new BlockDimensionalPocketWallConnector("block_dimensional_pocket_wall_connector", Material.BARRIER, "", -1, null);
	
	public static final Block BLOCK_DIMENSIONAL_GLASS = new BlockDimensionalGlass("block_dimensional_glass", null);
	
	@SubscribeEvent
	public static void registerB(final RegistryEvent.Register<Block> event) {
		RegistryHandler.REGISTRY_EVENT.registerAllBlocks(event, BLOCK_DIMENSIONAL_POCKET);
		
		BLOCK_ITEMS = RegistryHandler.REGISTRY_EVENT.getItemBlocks(event,
				BLOCK_DIMENSIONAL_CORE, BLOCK_DIMENSIONAL_ORE, BLOCK_DIMENSIONAL, BLOCK_DIMENSIONAL_METAL,
				BLOCK_DIMENSIONAL_POCKET_WALL, BLOCK_DIMENSIONAL_POCKET_WALL_CONNECTOR, BLOCK_DIMENSIONAL_POCKET_WALL_EDGE, 
				BLOCK_DIMENSIONAL_GLASS);
	}
	
	@SubscribeEvent 
	public static void registerI(final RegistryEvent.Register<Item> event) {
		RegistryHandler.REGISTRY_EVENT.registerAllItemBlocks(event, ITEMBLOCK_DIMENSIONAL_POCKET);
		RegistryHandler.REGISTRY_EVENT.registerItemBlockArray(event, BLOCK_ITEMS);
	}
	
	@SubscribeEvent
	public static void registerModels(final ModelRegistryEvent event) {
		RegistryHandler.REGISTRY_EVENT.registerAllModels(event, 
				BLOCK_DIMENSIONAL_POCKET, BLOCK_DIMENSIONAL_CORE,
				BLOCK_DIMENSIONAL_ORE, BLOCK_DIMENSIONAL, BLOCK_DIMENSIONAL_METAL,
				BLOCK_DIMENSIONAL_POCKET_WALL, BLOCK_DIMENSIONAL_POCKET_WALL_CONNECTOR, BLOCK_DIMENSIONAL_POCKET_WALL_EDGE, 
				BLOCK_DIMENSIONAL_GLASS);
	}
}