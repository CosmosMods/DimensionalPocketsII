package com.zhr.dimensionalpocketsii.core.management.bus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Preconditions;
import com.zhr.cosmoslibrary.impl.block.ModBlock;
import com.zhr.cosmoslibrary.impl.block.ModBlockModelUnplaceable;
import com.zhr.cosmoslibrary.impl.interfaces.IItemGroupNone;
import com.zhr.cosmoslibrary.impl.item.ModItem;
import com.zhr.cosmoslibrary.impl.item.ModItemBypassInfo;
import com.zhr.cosmoslibrary.impl.item.ModItemEffect;
import com.zhr.cosmoslibrary.impl.item.ModItemWrench;
import com.zhr.dimensionalpocketsii.DimensionalPockets;
import com.zhr.dimensionalpocketsii.core.management.ModConfigurationManager;
import com.zhr.dimensionalpocketsii.core.management.ModGroupManager;
import com.zhr.dimensionalpocketsii.pocket.core.block.BlockPocket;
import com.zhr.dimensionalpocketsii.pocket.core.block.BlockWall;
import com.zhr.dimensionalpocketsii.pocket.core.block.BlockWallEdge;
import com.zhr.dimensionalpocketsii.pocket.core.block.ItemBlockPocket;
import com.zhr.dimensionalpocketsii.pocket.core.tileentity.TileEntityPocket;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class BusSubscriberMod {
	
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String LOGGER_PREFIX = "< BusSubscriberMod >: ";

	public static final Block BLOCK_DIMENSIONAL_ORE = new ModBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(8).harvestLevel(2).harvestTool(ToolType.PICKAXE)); // = new ModBlockDropsItem("block_dimensional_ore", Material.ROCK, "pickaxe", 2, 3, 8, DimensionalPockets.TAB_DIMENSIONALPOCKETS, ItemHandler.DIMENSIONAL_SHARD)= null;= null;
	public static final Block BLOCK_DIMENSIONAL = new ModBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(8).harvestLevel(2).harvestTool(ToolType.PICKAXE));
	public static final Block BLOCK_DIMENSIONAL_METAL = new ModBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(8).harvestLevel(2).harvestTool(ToolType.PICKAXE));
	
	public static final Block BLOCK_POCKET = new BlockPocket(Block.Properties.create(Material.IRON).hardnessAndResistance(-1).harvestLevel(4).harvestTool(ToolType.PICKAXE).notSolid()); 
	public static final BlockItem ITEMBLOCK_DIMENSIONAL_POCKET = new ItemBlockPocket(BLOCK_POCKET, new Item.Properties().isImmuneToFire().maxStackSize(1).setNoRepair().group(ModGroupManager.DIM_POCKETS_ITEM_GROUP), "Creates a pocket dimension!", "Once placed shift-right click to enter the pocket.", "To exit the pocket, simply shift-right click on any wall.");
	
	public static final Block BLOCK_DIMENSIONAL_CORE = new ModBlockModelUnplaceable(Block.Properties.create(Material.IRON).hardnessAndResistance(8).harvestLevel(2).harvestTool(ToolType.PICKAXE));

	public static final Block BLOCK_DIMENSIONAL_POCKET_WALL = new BlockWall(Block.Properties.create(Material.IRON).hardnessAndResistance(-1,3600000.0F).setLightLevel((state) -> { return 15; }));
	public static final Block BLOCK_DIMENSIONAL_POCKET_WALL_EDGE = new BlockWallEdge(Block.Properties.create(Material.IRON).hardnessAndResistance(-1, 3600000.0F).setLightLevel((state) -> { return 15; }));

	public static final Item DIMENSIONAL_SHARD = new ModItem(new Item.Properties().group(ModGroupManager.DIM_POCKETS_ITEM_GROUP));
	public static final Item DIMENSIONAL_INGOT = new ModItem(new Item.Properties().group(ModGroupManager.DIM_POCKETS_ITEM_GROUP));	
	public static final Item DIMENSIONAL_WRENCH = new ModItemWrench(new Item.Properties().maxStackSize(1).group(ModGroupManager.DIM_POCKETS_ITEM_GROUP));
	public static final Item DIMENSIONAL_SHIFTER = new ModItemBypassInfo(new Item.Properties().group(ModGroupManager.DIM_POCKETS_ITEM_GROUP).maxStackSize(1), "Allows access to a Pocket from anywhere!", "To link: Simply right click this on a Pocket!", "To use: Shift right click this item facing open air!", 1);
	
	public static final Item NETHER_STAR_SHARD = new ModItemEffect(new Item.Properties().group(ModGroupManager.DIM_POCKETS_ITEM_GROUP).maxStackSize(16));
	
	public static final TileEntityType<TileEntityPocket> POCKET_TYPE = TileEntityType.Builder.create(TileEntityPocket::new, BLOCK_POCKET).build(null);
	
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
				setupString("dimensional_ingot", DIMENSIONAL_INGOT),
				setupString("dimensional_wrench", DIMENSIONAL_WRENCH),
				setupString("dimensional_shifter", DIMENSIONAL_SHIFTER),
				setupString("nether_star_shard", NETHER_STAR_SHARD)
		);
		
		//Register BlockItems
		for (final Block block : ForgeRegistries.BLOCKS.getValues()) {
			final ResourceLocation blockRegistryName = block.getRegistryName();
			Preconditions.checkNotNull(blockRegistryName, "Registry Name of Block \"" + block + "\" of class \"" + block.getClass().getName() + "\"is null! This is not allowed!");

			if (!blockRegistryName.getNamespace().equals(DimensionalPockets.MOD_ID)) {
				continue;
			}
			
			
			if (block instanceof IItemGroupNone) {
				final Item.Properties properties = new Item.Properties();
				final BlockItem blockItem = new BlockItem(block, properties);
				registry.register(setupResource(blockRegistryName, blockItem));
			}
			
			else if (block instanceof BlockPocket) {
				registry.register(setupResource(blockRegistryName, ITEMBLOCK_DIMENSIONAL_POCKET));
			} 
			
			else if (block instanceof ModBlockModelUnplaceable) {
				final Item.Properties properties = new Item.Properties().group(ModGroupManager.DIM_POCKETS_ITEM_GROUP).maxStackSize(1);
				final BlockItem blockItem = new BlockItem(block, properties);
				registry.register(setupResource(blockRegistryName, blockItem));
			}
			
			else {
				final Item.Properties properties = new Item.Properties().group(ModGroupManager.DIM_POCKETS_ITEM_GROUP);
				final BlockItem blockItem = new BlockItem(block, properties);
				registry.register(setupResource(blockRegistryName, blockItem));
			}
		}
		
		LOGGER.info("[onItemRegistry] BlockItems Registered...", BusSubscriberMod.class);
		LOGGER.info("[onItemRegistry] Items Registered...", BusSubscriberMod.class);
	}
	
	@SubscribeEvent
	public static void onContainerTypeRegistry(final RegistryEvent.Register<ContainerType<?>> event) { }
	
	@SubscribeEvent
	public static void onTileEntityTypeRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
		setupString("pocket_type", POCKET_TYPE);
				
		LOGGER.info("[onTileEntityTypeRegistry] TileEntityTypes Registered...", BusSubscriberMod.class);
	}
	
	@SubscribeEvent
	public static void onModConfigEvent(final ModConfig.ModConfigEvent event) {
		if (event.getConfig().getSpec().equals(ModConfigurationManager.spec)) {
			//PocketConfigManager.bakeConfig();
		}
	}
	
	public static <T extends IForgeRegistryEntry<T>> T setupString(final String name, final T entry) {
		return setupLow(entry, new ResourceLocation(DimensionalPockets.MOD_ID, name));
	}
	
	public static <T extends IForgeRegistryEntry<T>> T setupResource(final ResourceLocation name, final T entry) {
		return setupLow(entry, name);
	}

	public static <T extends IForgeRegistryEntry<T>> T setupLow(final T entry, final ResourceLocation registryName) {
		entry.setRegistryName(registryName);
		LOGGER.debug("Object Registered: " + registryName);
		return entry;
	}
	
	@SuppressWarnings("unused")
	public static void setRenderLayers(FMLClientSetupEvent event) {
		RenderType solid = RenderType.getSolid();
		RenderType cutout_mipped = RenderType.getCutoutMipped();
		RenderType cutout = RenderType.getCutout();
		RenderType translucent = RenderType.getTranslucent();
		RenderType translucent_no_crumbling = RenderType.getTranslucentNoCrumbling();
		
		RenderTypeLookup.setRenderLayer(BusSubscriberMod.BLOCK_POCKET, cutout_mipped);
		
		DimensionalPockets.LOGGER.info(LOGGER_PREFIX + "Setting Render Layers");
	}
}