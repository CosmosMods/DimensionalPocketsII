package com.zeher.dimpockets.core.manager;

import com.zeher.dimpockets.DimensionalPockets;
import com.zeher.dimpockets.pocket.core.block.BlockConnector;
import com.zeher.dimpockets.pocket.core.block.BlockEnergyDisplay;
import com.zeher.dimpockets.pocket.core.block.BlockFluidDisplay;
import com.zeher.dimpockets.pocket.core.block.BlockPocket;
import com.zeher.dimpockets.pocket.core.block.BlockWall;
import com.zeher.dimpockets.pocket.core.block.BlockWallEdge;
import com.zeher.dimpockets.pocket.core.block.ItemBlockPocket;
import com.zeher.zeherlib.core.manager.RegistryManager;
import com.zeher.zeherlib.mod.block.ModBlock;
import com.zeher.zeherlib.mod.block.ModBlockGemedOre;
import com.zeher.zeherlib.mod.block.ModBlockModelUnplaceable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID)
@ObjectHolder(DimensionalPockets.MOD_ID)
public class ModBlockManager {
	
	private static ItemBlock[] BLOCK_ITEMS;
	
	public static final Block BLOCK_DIMENSIONAL_ORE = new ModBlockGemedOre("block_dimensional_ore", Material.ROCK, "pickaxe", 2, 3, 8, ModItemGroupManager.GROUP_DIM, ModItemManager.DIMENSIONAL_SHARD, 4);
	public static final Block BLOCK_DIMENSIONAL = new ModBlock("block_dimensional", Material.IRON, "pickaxe", 2, 8, 8, ModItemGroupManager.GROUP_DIM);
	public static final Block BLOCK_DIMENSIONAL_METAL = new ModBlock("block_dimensional_metal", Material.IRON, "pickaxe", 2, 8, 8, ModItemGroupManager.GROUP_DIM);
	
	public static final Block BLOCK_DIMENSIONAL_POCKET = new BlockPocket("block_dimensional_pocket", Material.IRON, "pickaxe", 3, 12, 12, ModItemGroupManager.GROUP_DIM);
	public static final ItemBlock ITEMBLOCK_DIMENSIONAL_POCKET = new ItemBlockPocket(BLOCK_DIMENSIONAL_POCKET,"Creates a pocket dimension!", "Once placed shift-right click to enter the pocket.", "To exit the pocket, simply shift-right click on any wall.");
	
	public static final Block BLOCK_DIMENSIONAL_CORE = new ModBlockModelUnplaceable("block_dimensional_core", Material.IRON, "pickaxe", 2, 100, 100, ModItemGroupManager.GROUP_DIM);
	
	public static final Block BLOCK_DIMENSIONAL_POCKET_WALL = new BlockWall("block_dimensional_pocket_wall", Material.BARRIER);
	public static final Block BLOCK_DIMENSIONAL_POCKET_WALL_EDGE = new BlockWallEdge("block_dimensional_pocket_wall_edge", Material.BARRIER);
	
	public static final Block BLOCK_DIMENSIONAL_POCKET_WALL_CONNECTOR = new BlockConnector("block_dimensional_pocket_wall_connector", Material.BARRIER);
	public static final Block BLOCK_DIMENSIONAL_POCKET_WALL_ENERGY_DISPLAY = new BlockEnergyDisplay("block_dimensional_pocket_wall_energy_display", Material.BARRIER);
	public static final Block BLOCK_DIMENSIONAL_POCKET_WALL_FLUID_DISPLAY = new BlockFluidDisplay("block_dimensional_pocket_wall_fluid_display", Material.BARRIER);
	
	@SubscribeEvent
	public static void registerB(final RegistryEvent.Register<Block> event) {
		RegistryManager.REGISTRY_EVENT.registerAllBlocks(event, BLOCK_DIMENSIONAL_POCKET);
		
		BLOCK_ITEMS = RegistryManager.REGISTRY_EVENT.getItemBlocks(event,
				BLOCK_DIMENSIONAL_CORE, BLOCK_DIMENSIONAL_ORE, BLOCK_DIMENSIONAL, BLOCK_DIMENSIONAL_METAL,
				
				BLOCK_DIMENSIONAL_POCKET_WALL, BLOCK_DIMENSIONAL_POCKET_WALL_EDGE,
				
				BLOCK_DIMENSIONAL_POCKET_WALL_CONNECTOR, 
				
				BLOCK_DIMENSIONAL_POCKET_WALL_ENERGY_DISPLAY, BLOCK_DIMENSIONAL_POCKET_WALL_FLUID_DISPLAY );
	}
	
	@SubscribeEvent 
	public static void registerI(final RegistryEvent.Register<Item> event) {
		RegistryManager.REGISTRY_EVENT.registerAllItemBlocks(event, ITEMBLOCK_DIMENSIONAL_POCKET);
		RegistryManager.REGISTRY_EVENT.registerItemBlockArray(event, BLOCK_ITEMS);
	}
	
	@SubscribeEvent
	public static void registerModels(final ModelRegistryEvent event) {
		RegistryManager.REGISTRY_EVENT.registerAllModels(event, 
				BLOCK_DIMENSIONAL_POCKET, BLOCK_DIMENSIONAL_CORE, BLOCK_DIMENSIONAL_ORE, BLOCK_DIMENSIONAL, BLOCK_DIMENSIONAL_METAL,
				
				BLOCK_DIMENSIONAL_POCKET_WALL, BLOCK_DIMENSIONAL_POCKET_WALL_EDGE,
				
				BLOCK_DIMENSIONAL_POCKET_WALL_CONNECTOR, BLOCK_DIMENSIONAL_POCKET_WALL_ENERGY_DISPLAY, BLOCK_DIMENSIONAL_POCKET_WALL_FLUID_DISPLAY );
	}
}