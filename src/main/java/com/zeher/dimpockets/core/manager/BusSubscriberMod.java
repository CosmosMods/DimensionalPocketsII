package com.zeher.dimpockets.core.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Preconditions;
import com.zeher.dimpockets.DimensionalPockets;
import com.zeher.dimpockets.pocket.core.block.BlockPocket;
import com.zeher.dimpockets.pocket.core.block.BlockWall;
import com.zeher.dimpockets.pocket.core.block.BlockWallEdge;
import com.zeher.dimpockets.pocket.core.tileentity.TilePocket;
import com.zeher.zeherlib.api.compat.core.impl.ZLWrench;
import com.zeher.zeherlib.mod.block.ModBlock;
import com.zeher.zeherlib.mod.block.ModBlockModelUnplaceable;
import com.zeher.zeherlib.mod.item.ModItem;

import net.minecraft.block.Block;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class BusSubscriberMod {
	
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String LOGGER_PREFIX = "< BusSubscriberForge >: ";

	public static final Block BLOCK_DIMENSIONAL_ORE = new ModBlock(Properties.create(Material.ROCK).hardnessAndResistance(8).harvestLevel(2).harvestTool(ToolType.PICKAXE)); // = new ModBlockDropsItem("block_dimensional_ore", Material.ROCK, "pickaxe", 2, 3, 8, DimensionalPockets.TAB_DIMENSIONALPOCKETS, ItemHandler.DIMENSIONAL_SHARD)= null;= null;
	public static final Block BLOCK_DIMENSIONAL = new ModBlock(Properties.create(Material.IRON).hardnessAndResistance(8).harvestLevel(2).harvestTool(ToolType.PICKAXE));
	public static final Block BLOCK_DIMENSIONAL_METAL = new ModBlock(Properties.create(Material.IRON).hardnessAndResistance(8).harvestLevel(2).harvestTool(ToolType.PICKAXE));
	
	public static final Block BLOCK_POCKET = new BlockPocket(Properties.create(Material.IRON).hardnessAndResistance(-1).harvestLevel(4).harvestTool(ToolType.PICKAXE)); 
	public static final BlockItem ITEMBLOCK_DIMENSIONAL_POCKET = null; //new ItemBlockPocket(block, properties, "Creates a pocket dimension!", "Once placed shift-right click to enter the pocket.", "To exit the pocket, simply shift-right click on any wall."));
	
	public static final Block BLOCK_DIMENSIONAL_CORE = new ModBlockModelUnplaceable(Properties.create(Material.IRON).hardnessAndResistance(8).harvestLevel(2).harvestTool(ToolType.PICKAXE));
	
	public static final Block BLOCK_DIMENSIONAL_POCKET_WALL = new BlockWall(Properties.create(Material.IRON).hardnessAndResistance(-1, 3600000.0F).lightValue(15));
	public static final Block BLOCK_DIMENSIONAL_POCKET_WALL_EDGE = new BlockWallEdge(Properties.create(Material.IRON).hardnessAndResistance(-1, 3600000.0F).lightValue(15));
	
	public static final Item DIMENSIONAL_SHARD = new ModItem(new Item.Properties().group(DPItemGroupManager.DIM_POCKETS_ITEM_GROUP));
	public static final Item DIMENSIONAL_INGOT = new ModItem(new Item.Properties().group(DPItemGroupManager.DIM_POCKETS_ITEM_GROUP));	
	public static final Item DIMENSIONAL_WRENCH = new ZLWrench(new Item.Properties().maxStackSize(1).group(DPItemGroupManager.DIM_POCKETS_ITEM_GROUP));
	
	@SubscribeEvent
	public static void onBlockRegistry(final RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(
			setupString("block_dimensional_ore", BLOCK_DIMENSIONAL_ORE),
			setupString("block_dimensional", BLOCK_DIMENSIONAL),
			setupString("block_dimensional_metal", BLOCK_DIMENSIONAL_METAL),
			
			setupString("block_pocket", BLOCK_POCKET),
			setupString("block_dimensional_core", BLOCK_DIMENSIONAL_CORE),
			
			setupString("block_dimensional_pocket_wall", BLOCK_DIMENSIONAL_POCKET_WALL),
			setupString("block_dimensional_pocket_wall_edge", BLOCK_DIMENSIONAL_POCKET_WALL_EDGE)
		);
		
		LOGGER.info("[onBlockRegistry] Blocks Registered...", BusSubscriberMod.class);
	}
	
	@SubscribeEvent
	public static void onItemRegistry(final RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();
		
		event.getRegistry().registerAll(
				setupString("dimensional_shard", DIMENSIONAL_SHARD),
				setupString("dimensional_ingot", DIMENSIONAL_INGOT)	,
				setupString("dimensional_wrench", DIMENSIONAL_WRENCH)	
		);
		
		//Register BlockItems
		for (final Block block : ForgeRegistries.BLOCKS.getValues()) {
			final ResourceLocation blockRegistryName = block.getRegistryName();
			Preconditions.checkNotNull(blockRegistryName, "Registry Name of Block \"" + block + "\" of class \"" + block.getClass().getName() + "\"is null! This is not allowed!");

			if (!blockRegistryName.getNamespace().equals(DimensionalPockets.MOD_ID)) {
				continue;
			}
			
			/**
			if (block instanceof IItemGroupNone) {
				final Item.Properties properties = new Item.Properties();
				final BlockItem blockItem = new BlockItem(block, properties);
				registry.register(setupResource(blockRegistryName, blockItem));
			}*/
			
			/**
			if (block instanceof BlockPocket) {
				final Item.Properties properties = new Item.Properties().group(DPItemGroupManager.DIM_POCKETS_ITEM_GROUP).maxStackSize(1);
				registry.register(setupResource(blockRegistryName, );
				
			}*/

			final Item.Properties properties = new Item.Properties().group(DPItemGroupManager.DIM_POCKETS_ITEM_GROUP);
			final BlockItem blockItem = new BlockItem(block, properties);
			registry.register(setupResource(blockRegistryName, blockItem));
		}
		
		LOGGER.info("[onItemRegistry] BlockItems Registered...", BusSubscriberMod.class);
		LOGGER.info("[onItemRegistry] Items Registered...", BusSubscriberMod.class);
	}
	
	@SubscribeEvent
	public static void onContainerTypeRegistry(final RegistryEvent.Register<ContainerType<?>> event) { }
	
	@SubscribeEvent
	public static void onTileEntityTypeRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
		setupString("pocket", TileEntityType.Builder.create(TilePocket::new, BLOCK_POCKET).build(null));
				
		LOGGER.info("[onTileEntityTypeRegistry] TileEntityTypes Registered...", BusSubscriberMod.class);
	}
	
	@SubscribeEvent
	public static void onModDimensionRegistry(final RegistryEvent.Register<ModDimension> event) {
		event.getRegistry().registerAll (
			ModDimensionManager.POCKET_DIMENSION
		);
		
		LOGGER.info("[onModDimensionRegistry] Registered Dimensions...", BusSubscriberMod.class);
	}
	
	public static <T extends IForgeRegistryEntry<T>> T setupString(final String name, final T entry) {
		return setupLow(entry, new ResourceLocation(DimensionalPockets.MOD_ID, name));
	}
	
	public static <T extends IForgeRegistryEntry<T>> T setupResource(final ResourceLocation name, final T entry) {
		return setupLow(entry, name);
	}

	public static <T extends IForgeRegistryEntry<T>> T setupLow(final T entry, final ResourceLocation registryName) {
		entry.setRegistryName(registryName);
		return entry;
	}
}