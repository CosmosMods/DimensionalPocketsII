package com.tcn.dimensionalpocketsii.core.management;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Preconditions;
import com.tcn.cosmoslibrary.impl.block.CosmosBlock;
import com.tcn.cosmoslibrary.impl.block.CosmosBlockModelUnplaceable;
import com.tcn.cosmoslibrary.impl.interfaces.IItemGroupNone;
import com.tcn.cosmoslibrary.impl.item.CosmosItem;
import com.tcn.cosmoslibrary.impl.item.CosmosItemEffect;
import com.tcn.cosmoslibrary.impl.item.CosmosItemWrench;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.core.impl.IRarityPocket;
import com.tcn.dimensionalpocketsii.core.item.DimensionalShifter;
import com.tcn.dimensionalpocketsii.core.item.EnhancedDimensionalShifter;
import com.tcn.dimensionalpocketsii.core.item.EnumDimensionalItemTier;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockPocket;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockPocketItem;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWall;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallCharger;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallChargerItem;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallConnector;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallConnectorItem;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallEdge;
import com.tcn.dimensionalpocketsii.pocket.core.tileentity.TileEntityCharger;
import com.tcn.dimensionalpocketsii.pocket.core.tileentity.TileEntityConnector;
import com.tcn.dimensionalpocketsii.pocket.core.tileentity.TileEntityPocket;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.Rarity;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModBusManager {
	
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String LOGGER_PREFIX = "< BusSubscriberMod >: ";

	public static final Block BLOCK_DIMENSIONAL_ORE = new CosmosBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(8).harvestLevel(2).harvestTool(ToolType.PICKAXE));
	public static final Block BLOCK_DIMENSIONAL_ORE_NETHER = new CosmosBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(8).harvestLevel(2).harvestTool(ToolType.PICKAXE));
	public static final Block BLOCK_DIMENSIONAL_ORE_END = new CosmosBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(8).harvestLevel(2).harvestTool(ToolType.PICKAXE));
	
	public static final Block BLOCK_DIMENSIONAL = new CosmosBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(8).harvestLevel(2).harvestTool(ToolType.PICKAXE));
	public static final Block BLOCK_DIMENSIONAL_METAL = new CosmosBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(8).harvestLevel(2).harvestTool(ToolType.PICKAXE));
	public static final Block BLOCK_DIMENSIONAL_CORE = new CosmosBlockModelUnplaceable(Block.Properties.create(Material.IRON).hardnessAndResistance(8).harvestLevel(2).harvestTool(ToolType.PICKAXE));

	public static final Block BLOCK_POCKET = new BlockPocket(Block.Properties.create(Material.IRON).hardnessAndResistance(-1).harvestLevel(4).harvestTool(ToolType.PICKAXE).notSolid()); 
	public static final BlockItem BLOCKITEM_POCKET = new BlockPocketItem(BLOCK_POCKET, new Item.Properties().isImmuneToFire().maxStackSize(1).setNoRepair().group(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).rarity(Rarity.create("Pocket", TextFormatting.DARK_PURPLE)));
	
	public static final Block BLOCK_WALL = new BlockWall(Block.Properties.create(Material.IRON).hardnessAndResistance(-1,3600000.0F).setLightLevel((state) -> { return 15; }));
	public static final Block BLOCK_WALL_EDGE = new BlockWallEdge(Block.Properties.create(Material.IRON).hardnessAndResistance(-1, 3600000.0F).setLightLevel((state) -> { return 15; }));
	
	public static final Block BLOCK_WALL_CONNECTOR = new BlockWallConnector(Block.Properties.create(Material.IRON).hardnessAndResistance(-1, 3600000.0F).setLightLevel((state) -> { return 15; }));
	public static final BlockItem BLOCKITEM_WALL_CONNECTOR = new BlockWallConnectorItem(BLOCK_WALL_CONNECTOR, new Item.Properties().isImmuneToFire().maxStackSize(8).setNoRepair().group(CoreGroupManager.DIM_POCKETS_ITEM_GROUP));
	public static final Block BLOCK_WALL_CHARGER = new BlockWallCharger(Block.Properties.create(Material.IRON).hardnessAndResistance(-1, 3600000.0F).setLightLevel((state) -> { return 15; }));
	public static final BlockItem BLOCKITEM_WALL_CHARGER = new BlockWallChargerItem(BLOCK_WALL_CHARGER, new Item.Properties().isImmuneToFire().maxStackSize(8).setNoRepair().group(CoreGroupManager.DIM_POCKETS_ITEM_GROUP));
	
	public static final Item DIMENSIONAL_SHARD = new CosmosItem(new Item.Properties().group(CoreGroupManager.DIM_POCKETS_ITEM_GROUP));
	public static final Item DIMENSIONAL_INGOT = new CosmosItem(new Item.Properties().group(CoreGroupManager.DIM_POCKETS_ITEM_GROUP));
	public static final Item DIMENSIONAL_WRENCH = new CosmosItemWrench(new Item.Properties().maxStackSize(1).group(CoreGroupManager.DIM_POCKETS_ITEM_GROUP));
	public static final Item DIMENSIONAL_SHIFTER = new DimensionalShifter(new Item.Properties().group(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).maxStackSize(1));
	public static final Item ENHANCED_DIMENSIONAL_SHIFTER = new EnhancedDimensionalShifter(new Item.Properties().group(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).maxStackSize(1).rarity(Rarity.create("Pocket", TextFormatting.DARK_PURPLE)));
	
	public static final Item NETHER_STAR_SHARD = new CosmosItemEffect(new Item.Properties().group(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).maxStackSize(16));
	
	public static final Item DIMENSIONAL_SWORD = new SwordItem(EnumDimensionalItemTier.DIMENSIONAL, 5, -1F, (new Item.Properties()).group(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).isImmuneToFire());
	public static final Item DIMENSIONAL_PICKAXE = new PickaxeItem(EnumDimensionalItemTier.DIMENSIONAL, 2, -2F, (new Item.Properties()).group(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).isImmuneToFire());
	public static final Item DIMENSIONAL_AXE = new AxeItem(EnumDimensionalItemTier.DIMENSIONAL, 7, -1.5F, (new Item.Properties()).group(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).isImmuneToFire());
	public static final Item DIMENSIONAL_SHOVEL = new ShovelItem(EnumDimensionalItemTier.DIMENSIONAL, 1, -2.5F, (new Item.Properties()).group(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).isImmuneToFire());
	
	public static TileEntityType<TileEntityPocket> POCKET_TYPE = TileEntityType.Builder.create(TileEntityPocket::new, BLOCK_POCKET).build(null);
	public static TileEntityType<TileEntityConnector> CONNECTOR_TYPE = TileEntityType.Builder.create(TileEntityConnector::new, BLOCK_WALL_CONNECTOR).build(null);
	public static TileEntityType<TileEntityCharger> CHARGER_TYPE = TileEntityType.Builder.create(TileEntityCharger::new, BLOCK_WALL_CHARGER).build(null);
	
	@SubscribeEvent
	public static void onBlockRegistry(final RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(
			setupString("block_dimensional_ore", BLOCK_DIMENSIONAL_ORE),
			setupString("block_dimensional_ore_nether", BLOCK_DIMENSIONAL_ORE_NETHER),
			setupString("block_dimensional_ore_end", BLOCK_DIMENSIONAL_ORE_END),
			
			setupString("block_dimensional", BLOCK_DIMENSIONAL),
			setupString("block_dimensional_metal", BLOCK_DIMENSIONAL_METAL),
			setupString("block_dimensional_core", BLOCK_DIMENSIONAL_CORE),
			
			setupString("block_pocket", BLOCK_POCKET),
			
			setupString("block_wall", BLOCK_WALL),
			setupString("block_wall_edge", BLOCK_WALL_EDGE),
			
			setupString("block_wall_connector", BLOCK_WALL_CONNECTOR),
			setupString("block_wall_charger", BLOCK_WALL_CHARGER)
		);
		
		LOGGER.info("[onBlockRegistry] Blocks Registered...", ModBusManager.class);
	}
	
	@SubscribeEvent
	public static void onItemRegistry(final RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();
		
		event.getRegistry().registerAll(
				setupString("dimensional_shard", DIMENSIONAL_SHARD),
				setupString("dimensional_ingot", DIMENSIONAL_INGOT),
				setupString("dimensional_wrench", DIMENSIONAL_WRENCH),
				setupString("dimensional_shifter", DIMENSIONAL_SHIFTER),
				setupString("enhanced_dimensional_shifter", ENHANCED_DIMENSIONAL_SHIFTER),
				setupString("nether_star_shard", NETHER_STAR_SHARD),
				
				setupString("dimensional_sword", DIMENSIONAL_SWORD),
				setupString("dimensional_pickaxe", DIMENSIONAL_PICKAXE),
				setupString("dimensional_axe", DIMENSIONAL_AXE),
				setupString("dimensional_shovel", DIMENSIONAL_SHOVEL)
		);
		
		//Register BlockItems
		for (final Block block : ForgeRegistries.BLOCKS.getValues()) {
			final ResourceLocation blockRegistryName = block.getRegistryName();
			Preconditions.checkNotNull(blockRegistryName, "Registry Name of Block \"" + block + "\" of class \"" + block.getClass().getName() + "\"is null! This is not allowed!");

			if (!blockRegistryName.getNamespace().equals(DimensionalPockets.MOD_ID)) {
				continue;
			}
			
			if (block instanceof IRarityPocket) {
				final Item.Properties properties = new Item.Properties().rarity(Rarity.create("Pocket", TextFormatting.DARK_PURPLE));
				final BlockItem blockItem = new BlockItem(block, properties);
				registry.register(setupResource(blockRegistryName, blockItem));
			} 
			
			else if (block instanceof IItemGroupNone) {
				final Item.Properties properties = new Item.Properties();
				final BlockItem blockItem = new BlockItem(block, properties);
				registry.register(setupResource(blockRegistryName, blockItem));
			}
			
			else if (block instanceof BlockPocket) {
				registry.register(setupResource(blockRegistryName, BLOCKITEM_POCKET));
			}  else if (block instanceof BlockWallConnector) {
				registry.register(setupResource(blockRegistryName, BLOCKITEM_WALL_CONNECTOR));
			}  else if (block instanceof BlockWallCharger) {
				registry.register(setupResource(blockRegistryName, BLOCKITEM_WALL_CHARGER));
			} 
			
			else if (block instanceof CosmosBlockModelUnplaceable) {
				final Item.Properties properties = new Item.Properties().group(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).maxStackSize(1);
				final BlockItem blockItem = new BlockItem(block, properties);
				registry.register(setupResource(blockRegistryName, blockItem));
			}
			
			else {
				final Item.Properties properties = new Item.Properties().group(CoreGroupManager.DIM_POCKETS_ITEM_GROUP);
				final BlockItem blockItem = new BlockItem(block, properties);
				registry.register(setupResource(blockRegistryName, blockItem));
			}
		}
		
		LOGGER.info("[onItemRegistry] BlockItems Registered...", ModBusManager.class);
		LOGGER.info("[onItemRegistry] Items Registered...", ModBusManager.class);
	}
	
	@SubscribeEvent
	public static void onContainerTypeRegistry(final RegistryEvent.Register<ContainerType<?>> event) { }
	
	@SubscribeEvent
	public static void onTileEntityTypeRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
		POCKET_TYPE.setRegistryName(DimensionalPockets.MOD_ID, "tile_entity_pocket");
		CONNECTOR_TYPE.setRegistryName(DimensionalPockets.MOD_ID, "tile_entity_connector");
		CHARGER_TYPE.setRegistryName(DimensionalPockets.MOD_ID, "tile_entity_charger");
		
		event.getRegistry().register(POCKET_TYPE);
		event.getRegistry().register(CONNECTOR_TYPE);
		event.getRegistry().register(CHARGER_TYPE);
		
		LOGGER.info("[onTileEntityTypeRegistry] TileEntityTypes Registered...", ModBusManager.class);
	}
	
	@SubscribeEvent
	public static void onModConfigEvent(final ModConfig.ModConfigEvent event) { }
	
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
}