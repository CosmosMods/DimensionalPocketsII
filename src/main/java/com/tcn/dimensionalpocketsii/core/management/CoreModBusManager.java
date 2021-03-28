package com.tcn.dimensionalpocketsii.core.management;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import com.google.common.base.Preconditions;
import com.tcn.cosmoslibrary.client.entity.layer.CosmosElytraArmourLayer;
import com.tcn.cosmoslibrary.client.entity.layer.CosmosElytraLayer;
import com.tcn.cosmoslibrary.common.block.CosmosBlock;
import com.tcn.cosmoslibrary.common.block.CosmosBlockModelUnplaceable;
import com.tcn.cosmoslibrary.common.interfaces.IItemGroupNone;
import com.tcn.cosmoslibrary.common.item.CosmosItem;
import com.tcn.cosmoslibrary.common.item.CosmosItemEffect;
import com.tcn.cosmoslibrary.common.item.CosmosItemWrench;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.client.colour.ColourBlockPocket;
import com.tcn.dimensionalpocketsii.client.colour.ColourBlockWall;
import com.tcn.dimensionalpocketsii.client.colour.ColourItem;
import com.tcn.dimensionalpocketsii.client.renderer.RendererDimensionalTrident;
import com.tcn.dimensionalpocketsii.client.renderer.TridentItemStackRenderer;
import com.tcn.dimensionalpocketsii.client.screen.ScreenConfiguration;
import com.tcn.dimensionalpocketsii.core.entity.DimensionalTridentEntity;
import com.tcn.dimensionalpocketsii.core.impl.IRarityPocket;
import com.tcn.dimensionalpocketsii.core.item.DimensionalBow;
import com.tcn.dimensionalpocketsii.core.item.DimensionalShifter;
import com.tcn.dimensionalpocketsii.core.item.DimensionalShifterEnhanced;
import com.tcn.dimensionalpocketsii.core.item.DimensionalTome;
import com.tcn.dimensionalpocketsii.core.item.DimensionalTrident;
import com.tcn.dimensionalpocketsii.core.item.ModuleBase;
import com.tcn.dimensionalpocketsii.core.item.ModuleCharger;
import com.tcn.dimensionalpocketsii.core.item.ModuleConnector;
import com.tcn.dimensionalpocketsii.core.item.ModuleCrafter;
import com.tcn.dimensionalpocketsii.core.item.ModuleEnergyDisplay;
import com.tcn.dimensionalpocketsii.core.item.armour.DimensionalElytraplate;
import com.tcn.dimensionalpocketsii.core.item.armour.DimensionalElytraplateScreen;
import com.tcn.dimensionalpocketsii.core.item.armour.DimensionalElytraplateShift;
import com.tcn.dimensionalpocketsii.core.material.CoreArmourMaterial;
import com.tcn.dimensionalpocketsii.core.material.CoreItemTier;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerConnector;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerCrafter;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerPocket;
import com.tcn.dimensionalpocketsii.pocket.client.renderer.RendererCharger;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenConnector;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenCrafter;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenPocket;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockPocket;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWall;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallCharger;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallConnector;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallCrafter;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallEdge;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallEnergyDisplay;
import com.tcn.dimensionalpocketsii.pocket.core.item.block.ItemBlockCharger;
import com.tcn.dimensionalpocketsii.pocket.core.item.block.ItemBlockConnector;
import com.tcn.dimensionalpocketsii.pocket.core.item.block.ItemBlockCrafter;
import com.tcn.dimensionalpocketsii.pocket.core.item.block.ItemBlockEnergyDisplay;
import com.tcn.dimensionalpocketsii.pocket.core.item.block.ItemBlockPocket;
import com.tcn.dimensionalpocketsii.pocket.core.tileentity.TileEntityCharger;
import com.tcn.dimensionalpocketsii.pocket.core.tileentity.TileEntityConnector;
import com.tcn.dimensionalpocketsii.pocket.core.tileentity.TileEntityCrafter;
import com.tcn.dimensionalpocketsii.pocket.core.tileentity.TileEntityPocket;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.Rarity;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CoreModBusManager {
	
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String LOGGER_PREFIX = "< CoreModBusManager >: ";

	public static final Item DIMENSIONAL_SHARD = new CosmosItem(new Item.Properties().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP));
	public static final Item DIMENSIONAL_INGOT = new CosmosItem(new Item.Properties().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP));
	public static final Item DIMENSIONAL_DUST = new CosmosItem(new Item.Properties().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP));
	public static final Item DIMENSIONAL_PEARL = new CosmosItem(new Item.Properties().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).stacksTo(16));
	public static final Item DIMENSIONAL_THREAD = new CosmosItem(new Item.Properties().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP));
	public static final Item NETHER_STAR_SHARD = new CosmosItemEffect(new Item.Properties().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).stacksTo(16).rarity(Rarity.RARE));
	public static final Item ELYTRA_WING = new CosmosItem(new Item.Properties().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).stacksTo(2).rarity(Rarity.RARE));
	
	public static final Item MODULE_BASE = new ModuleBase(new Item.Properties().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).stacksTo(8));
	public static final Item MODULE_CONNECTOR = new ModuleConnector(new Item.Properties().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).stacksTo(8));
	public static final Item MODULE_CHARGER = new ModuleCharger(new Item.Properties().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).stacksTo(8));
	public static final Item MODULE_CRAFTER = new ModuleCrafter(new Item.Properties().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).stacksTo(8));
	public static final Item MODULE_ENERGY_DISPLAY = new ModuleEnergyDisplay(new Item.Properties().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).stacksTo(8));
	
	public static final Item DIMENSIONAL_WRENCH = new CosmosItemWrench(new Item.Properties().stacksTo(1).tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP));
	public static final Item DIMENSIONAL_SHIFTER = new DimensionalShifter(new Item.Properties().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).stacksTo(1));
	public static final Item DIMENSIONAL_SHIFTER_ENHANCED = new DimensionalShifterEnhanced(new Item.Properties().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).stacksTo(1).rarity(Rarity.create("Pocket", TextFormatting.DARK_PURPLE)));

	public static final Item DIMENSIONAL_SWORD = new SwordItem(CoreItemTier.DIMENSIONAL, 5, -1F, new Item.Properties().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).fireResistant());
	public static final Item DIMENSIONAL_PICKAXE = new PickaxeItem(CoreItemTier.DIMENSIONAL, 2, -2F, new Item.Properties().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).fireResistant());
	public static final Item DIMENSIONAL_AXE = new AxeItem(CoreItemTier.DIMENSIONAL, 7, -1.5F, new Item.Properties().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).fireResistant());
	public static final Item DIMENSIONAL_SHOVEL = new ShovelItem(CoreItemTier.DIMENSIONAL, 1, -2.5F, new Item.Properties().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).fireResistant());
	public static final Item DIMENSIONAL_HOE = new HoeItem(CoreItemTier.DIMENSIONAL, 1, -2.5F, new Item.Properties().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).fireResistant());
	public static final Item DIMENSIONAL_BOW = new DimensionalBow(new Item.Properties().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).durability(500).fireResistant());
	public static final Item DIMENSIONAL_TRIDENT = new DimensionalTrident(new Item.Properties().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).durability(500).fireResistant().setISTER(() -> TridentItemStackRenderer::new).rarity(Rarity.create("Pocket", TextFormatting.DARK_PURPLE)));
	
	public static final Item DIMENSIONAL_HELMET = new ArmorItem(CoreArmourMaterial.DIMENSIONAL, EquipmentSlotType.HEAD, (new Item.Properties()).tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).fireResistant());
	public static final Item DIMENSIONAL_CHESTPLATE = new ArmorItem(CoreArmourMaterial.DIMENSIONAL, EquipmentSlotType.CHEST, (new Item.Properties()).tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).fireResistant());
	public static final Item DIMENSIONAL_ELYTRAPLATE = new DimensionalElytraplate(CoreArmourMaterial.DIMENSIONAL_ENHANCED, EquipmentSlotType.CHEST, (new Item.Properties()).tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).rarity(Rarity.create("Pocket", TextFormatting.DARK_PURPLE)).fireResistant(), DIMENSIONAL_INGOT, false);
	public static final Item DIMENSIONAL_ELYTRAPLATE_SCREEN = new DimensionalElytraplateScreen(CoreArmourMaterial.DIMENSIONAL_ENHANCED, EquipmentSlotType.CHEST, (new Item.Properties()).tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).rarity(Rarity.create("Pocket", TextFormatting.DARK_PURPLE)).fireResistant(), DIMENSIONAL_INGOT, false);
	public static final Item DIMENSIONAL_ELYTRAPLATE_SHIFT = new DimensionalElytraplateShift(CoreArmourMaterial.DIMENSIONAL_ENHANCED, EquipmentSlotType.CHEST, (new Item.Properties()).tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).rarity(Rarity.create("Pocket", TextFormatting.DARK_PURPLE)).fireResistant(), DIMENSIONAL_INGOT, false);
	public static final Item DIMENSIONAL_LEGGINGS = new ArmorItem(CoreArmourMaterial.DIMENSIONAL, EquipmentSlotType.LEGS, (new Item.Properties()).tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).fireResistant());
	public static final Item DIMENSIONAL_BOOTS = new ArmorItem(CoreArmourMaterial.DIMENSIONAL, EquipmentSlotType.FEET, (new Item.Properties()).tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).fireResistant());
	
	public static final Item DIMENSIONAL_TOME = new DimensionalTome(new Item.Properties().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).stacksTo(1).rarity(Rarity.create("Pocket", TextFormatting.DARK_PURPLE)));
	
	public static final Block BLOCK_DIMENSIONAL_ORE = new CosmosBlock(Block.Properties.of(Material.STONE).harvestLevel(3).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(4.0F, 4.0F));
	public static final Block BLOCK_DIMENSIONAL_ORE_NETHER = new CosmosBlock(Block.Properties.of(Material.STONE).harvestLevel(3).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(6.0F, 8.0F));
	public static final Block BLOCK_DIMENSIONAL_ORE_END = new CosmosBlock(Block.Properties.of(Material.STONE).harvestLevel(3).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(8.0F, 8.0F));
	public static final Block BLOCK_DIMENSIONAL = new CosmosBlock(Block.Properties.of(Material.HEAVY_METAL).harvestLevel(3).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(6.0F, 8.0F));
	public static final Block BLOCK_DIMENSIONAL_METAL = new CosmosBlock(Block.Properties.of(Material.HEAVY_METAL).harvestLevel(3).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(6.0F, 8.0F));
	public static final Block BLOCK_DIMENSIONAL_CORE = new CosmosBlockModelUnplaceable(Block.Properties.of(Material.HEAVY_METAL).harvestLevel(3).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(6.0F, 8.0F));

	public static final Block BLOCK_POCKET = new BlockPocket(Block.Properties.of(Material.HEAVY_METAL).strength(-1).harvestLevel(4).harvestTool(ToolType.PICKAXE).dynamicShape().noOcclusion()); 
	public static final BlockItem BLOCKITEM_POCKET = new ItemBlockPocket(BLOCK_POCKET, new Item.Properties().stacksTo(1).setNoRepair().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).rarity(Rarity.create("Pocket", TextFormatting.DARK_PURPLE)).fireResistant());
	public static final Block BLOCK_WALL_CONNECTOR = new BlockWallConnector(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; }));
	public static final BlockItem BLOCKITEM_WALL_CONNECTOR = new ItemBlockConnector(BLOCK_WALL_CONNECTOR, new Item.Properties().fireResistant().stacksTo(8).setNoRepair().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).fireResistant());
	public static final Block BLOCK_WALL_CHARGER = new BlockWallCharger(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; }));
	public static final BlockItem BLOCKITEM_WALL_CHARGER = new ItemBlockCharger(BLOCK_WALL_CHARGER, new Item.Properties().fireResistant().stacksTo(8).setNoRepair().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).fireResistant());
	public static final Block BLOCK_WALL_CRAFTER = new BlockWallCrafter(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; }));
	public static final BlockItem BLOCKITEM_WALL_CRAFTER = new ItemBlockCrafter(BLOCK_WALL_CRAFTER, new Item.Properties().fireResistant().stacksTo(8).setNoRepair().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).fireResistant());
	public static final Block BLOCK_WALL_ENERGY_DISPLAY = new BlockWallEnergyDisplay(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; }).randomTicks());
	public static final BlockItem BLOCKITEM_WALL_ENERGY_DISPLAY = new ItemBlockEnergyDisplay(BLOCK_WALL_ENERGY_DISPLAY, new Item.Properties().fireResistant().stacksTo(8).setNoRepair().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).fireResistant());
	
	public static final Block BLOCK_WALL = new BlockWall(Block.Properties.of(Material.HEAVY_METAL).strength(-1,3600000.0F).lightLevel((state) -> { return 15; }));
	public static final Block BLOCK_WALL_EDGE = new BlockWallEdge(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; }));
	
	public static TileEntityType<TileEntityPocket> POCKET_TILE_TYPE;
	public static ContainerType<ContainerPocket> POCKET_CONTAINER_TYPE;
	
	public static TileEntityType<TileEntityConnector> CONNECTOR_TILE_TYPE;
	public static ContainerType<ContainerConnector> CONNECTOR_CONTAINER_TYPE;
	
	public static TileEntityType<TileEntityCharger> CHARGER_TILE_TYPE;
	
	public static TileEntityType<TileEntityCrafter> CRAFTER_TILE_TYPE;
	public static ContainerType<ContainerCrafter> CRAFTER_CONTAINER_TYPE;
	
	public static EntityType<DimensionalTridentEntity> TRIDENT_TYPE;
	  
	public static KeyBinding SUIT_GUI;
	public static KeyBinding SUIT_SHIFT;
	public static KeyBinding SUIT_MODE_CHANGE;
	
	@SubscribeEvent
	public static void onItemRegistry(final RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();
		
		event.getRegistry().registerAll(
				setupString("dimensional_shard", DIMENSIONAL_SHARD),
				setupString("dimensional_ingot", DIMENSIONAL_INGOT),
				setupString("dimensional_dust", DIMENSIONAL_DUST),
				setupString("dimensional_pearl", DIMENSIONAL_PEARL),
				setupString("dimensional_thread", DIMENSIONAL_THREAD),
				setupString("nether_star_shard", NETHER_STAR_SHARD),
				setupString("elytra_wing", ELYTRA_WING),

				setupString("module_base", MODULE_BASE),
				setupString("module_connector", MODULE_CONNECTOR),
				setupString("module_charger", MODULE_CHARGER),
				setupString("module_crafter", MODULE_CRAFTER),
				setupString("module_energy_display", MODULE_ENERGY_DISPLAY),
				
				setupString("dimensional_wrench", DIMENSIONAL_WRENCH),
				setupString("dimensional_shifter", DIMENSIONAL_SHIFTER),
				setupString("dimensional_shifter_enhanced", DIMENSIONAL_SHIFTER_ENHANCED),
				
				setupString("dimensional_sword", DIMENSIONAL_SWORD),
				setupString("dimensional_pickaxe", DIMENSIONAL_PICKAXE),
				setupString("dimensional_axe", DIMENSIONAL_AXE),
				setupString("dimensional_shovel", DIMENSIONAL_SHOVEL),
				setupString("dimensional_hoe", DIMENSIONAL_HOE),
				setupString("dimensional_bow", DIMENSIONAL_BOW),
				setupString("dimensional_trident", DIMENSIONAL_TRIDENT),
				
				setupString("dimensional_helmet", DIMENSIONAL_HELMET),
				setupString("dimensional_chestplate", DIMENSIONAL_CHESTPLATE),
				setupString("dimensional_elytraplate", DIMENSIONAL_ELYTRAPLATE),
				setupString("dimensional_elytraplate_screen", DIMENSIONAL_ELYTRAPLATE_SCREEN),
				setupString("dimensional_elytraplate_shift", DIMENSIONAL_ELYTRAPLATE_SHIFT),
				setupString("dimensional_leggings", DIMENSIONAL_LEGGINGS),
				setupString("dimensional_boots", DIMENSIONAL_BOOTS),
				
				setupString("dimensional_tome", DIMENSIONAL_TOME)
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
			} else if (block instanceof BlockWallConnector) {
				registry.register(setupResource(blockRegistryName, BLOCKITEM_WALL_CONNECTOR));
			} else if (block instanceof BlockWallCharger) {
				registry.register(setupResource(blockRegistryName, BLOCKITEM_WALL_CHARGER));
			} else if (block instanceof BlockWallCrafter) {
				registry.register(setupResource(blockRegistryName, BLOCKITEM_WALL_CRAFTER));
			} else if (block instanceof BlockWallEnergyDisplay) {
				registry.register(setupResource(blockRegistryName, BLOCKITEM_WALL_ENERGY_DISPLAY));
			}
			
			else if (block instanceof CosmosBlockModelUnplaceable) {
				final Item.Properties properties = new Item.Properties().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP).stacksTo(1);
				final BlockItem blockItem = new BlockItem(block, properties);
				registry.register(setupResource(blockRegistryName, blockItem));
			}
			
			else {
				final Item.Properties properties = new Item.Properties().tab(CoreGroupManager.DIM_POCKETS_ITEM_GROUP);
				final BlockItem blockItem = new BlockItem(block, properties);
				registry.register(setupResource(blockRegistryName, blockItem));
			}
		}
		
		LOGGER.info("BlockItems Registered...");
		LOGGER.info("Items Registered...");
	}

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
			setupString("block_wall_charger", BLOCK_WALL_CHARGER),
			setupString("block_wall_crafter", BLOCK_WALL_CRAFTER),
			setupString("block_wall_energy_display", BLOCK_WALL_ENERGY_DISPLAY)
		);
		
		LOGGER.info("Blocks Registered.");
	}
	
	@SubscribeEvent
	public static void onContainerTypeRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
		POCKET_CONTAINER_TYPE = IForgeContainerType.create(ContainerPocket::createContainerClientSide);
		POCKET_CONTAINER_TYPE.setRegistryName(new ResourceLocation(DimensionalPockets.MOD_ID, "container_pocket"));
		
		CONNECTOR_CONTAINER_TYPE = IForgeContainerType.create(ContainerConnector::createContainerClientSide);
		CONNECTOR_CONTAINER_TYPE.setRegistryName(new ResourceLocation(DimensionalPockets.MOD_ID, "container_connector"));
		
		CRAFTER_CONTAINER_TYPE = IForgeContainerType.create(ContainerCrafter::new);
		CRAFTER_CONTAINER_TYPE.setRegistryName(new ResourceLocation(DimensionalPockets.MOD_ID, "container_crafter"));
		
		event.getRegistry().registerAll(POCKET_CONTAINER_TYPE, CONNECTOR_CONTAINER_TYPE, CRAFTER_CONTAINER_TYPE);
		
		LOGGER.info("ContainerTypes Registered.");
	}
	
	@SubscribeEvent
	public static void onTileEntityTypeRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {	
		POCKET_TILE_TYPE = TileEntityType.Builder.<TileEntityPocket>of(TileEntityPocket::new, BLOCK_POCKET).build(null);
		POCKET_TILE_TYPE.setRegistryName(DimensionalPockets.MOD_ID, "tile_entity_pocket");
		
		CONNECTOR_TILE_TYPE = TileEntityType.Builder.<TileEntityConnector>of(TileEntityConnector::new, BLOCK_WALL_CONNECTOR).build(null);
		CONNECTOR_TILE_TYPE.setRegistryName(DimensionalPockets.MOD_ID, "tile_entity_connector");
		
		CHARGER_TILE_TYPE = TileEntityType.Builder.<TileEntityCharger>of(TileEntityCharger::new, BLOCK_WALL_CHARGER).build(null);
		CHARGER_TILE_TYPE.setRegistryName(DimensionalPockets.MOD_ID, "tile_entity_charger");
		
		CRAFTER_TILE_TYPE = TileEntityType.Builder.<TileEntityCrafter>of(TileEntityCrafter::new, BLOCK_WALL_CRAFTER).build(null);
		CRAFTER_TILE_TYPE.setRegistryName(DimensionalPockets.MOD_ID, "tile_entity_crafter");
		
		event.getRegistry().registerAll(POCKET_TILE_TYPE, CONNECTOR_TILE_TYPE, CHARGER_TILE_TYPE, CRAFTER_TILE_TYPE);
		
		LOGGER.info("TileEntityTypes Registered.");
	}
	
	@SubscribeEvent
	public static void onEntityTypeRegistry(final RegistryEvent.Register<EntityType<?>> event) {
		TRIDENT_TYPE = EntityType.Builder.<DimensionalTridentEntity>of(DimensionalTridentEntity::new, EntityClassification.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("dimensional_trident_type");
		TRIDENT_TYPE.setRegistryName(DimensionalPockets.MOD_ID, "dimensional_trident_type");
		
		event.getRegistry().registerAll(TRIDENT_TYPE);
		
		LOGGER.info("EntityTypes Registered.");
	}
	
	@SubscribeEvent
	public static void onModConfigEvent(final ModConfig.ModConfigEvent event) { }
	
	@OnlyIn(Dist.CLIENT)
	public static void registerClient(ModLoadingContext context) {
		context.registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, screen) -> new ScreenConfiguration(screen));
		
		LOGGER.info("ClientRegistry complete.");
	}
	
	@OnlyIn(Dist.CLIENT)
	public static void onFMLClientSetup(FMLClientSetupEvent event) {
		RenderType cutout_mipped = RenderType.cutoutMipped();
		
		RenderTypeLookup.setRenderLayer(BLOCK_POCKET, cutout_mipped);
		RenderTypeLookup.setRenderLayer(BLOCK_WALL_CHARGER, cutout_mipped);
		RenderTypeLookup.setRenderLayer(BLOCK_WALL_CONNECTOR, cutout_mipped);
		RenderTypeLookup.setRenderLayer(BLOCK_WALL_CRAFTER, cutout_mipped);
		RenderTypeLookup.setRenderLayer(BLOCK_WALL_ENERGY_DISPLAY, cutout_mipped);
		RenderTypeLookup.setRenderLayer(BLOCK_DIMENSIONAL_CORE, cutout_mipped);
		
		ScreenManager.register(POCKET_CONTAINER_TYPE, ScreenPocket::new);
		ScreenManager.register(CONNECTOR_CONTAINER_TYPE, ScreenConnector::new);
		ScreenManager.register(CRAFTER_CONTAINER_TYPE, ScreenCrafter::new);
		
		ClientRegistry.bindTileEntityRenderer(CHARGER_TILE_TYPE, RendererCharger::new);
		
		SUIT_GUI = new KeyBinding("dimensionalpocketsii.keybind.suit_gui", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_BRACKET, "dimensionalpocketsii.keybind.category");
		ClientRegistry.registerKeyBinding(SUIT_GUI);
		
		SUIT_SHIFT = new KeyBinding("dimensionalpocketsii.keybind.suit_shift", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_BRACKET, "dimensionalpocketsii.keybind.category");
		ClientRegistry.registerKeyBinding(SUIT_SHIFT);
		
		SUIT_MODE_CHANGE = new KeyBinding("dimensionalpocketsii.keybind.suit_mode_change", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_APOSTROPHE, "dimensionalpocketsii.keybind.category");
		ClientRegistry.registerKeyBinding(SUIT_MODE_CHANGE);
		
		BlockColors blockColours = Minecraft.getInstance().getBlockColors();

		blockColours.register(new ColourBlockWall(), BLOCK_WALL);
		blockColours.register(new ColourBlockWall(), BLOCK_WALL_EDGE);
		blockColours.register(new ColourBlockWall(), BLOCK_WALL_CONNECTOR);
		blockColours.register(new ColourBlockWall(), BLOCK_WALL_CHARGER);
		blockColours.register(new ColourBlockWall(), BLOCK_WALL_CRAFTER);
		blockColours.register(new ColourBlockWall(), BLOCK_WALL_ENERGY_DISPLAY);
		blockColours.register(new ColourBlockPocket(), BLOCK_POCKET);

		ItemColors itemColours = Minecraft.getInstance().getItemColors();
		
		itemColours.register(new ColourItem(), BLOCKITEM_POCKET);
		itemColours.register(new ColourItem(), BLOCKITEM_WALL_CONNECTOR);
		itemColours.register(new ColourItem(), BLOCKITEM_WALL_CHARGER);
		itemColours.register(new ColourItem(), BLOCKITEM_WALL_CRAFTER);
		itemColours.register(new ColourItem(), BLOCKITEM_WALL_ENERGY_DISPLAY);
		
		itemColours.register(new ColourItem(), BLOCK_DIMENSIONAL_CORE);
		itemColours.register(new ColourItem(), BLOCK_WALL);
		itemColours.register(new ColourItem(), BLOCK_WALL_EDGE);
		
		itemColours.register(new ColourItem(), DIMENSIONAL_SHIFTER);
		itemColours.register(new ColourItem(), DIMENSIONAL_SHIFTER_ENHANCED);

		itemColours.register(new ColourItem(), DIMENSIONAL_ELYTRAPLATE_SCREEN);
		itemColours.register(new ColourItem(), DIMENSIONAL_ELYTRAPLATE_SHIFT);
		
		itemColours.register(new ColourItem(), MODULE_BASE);
		itemColours.register(new ColourItem(), MODULE_CONNECTOR);
		itemColours.register(new ColourItem(), MODULE_CHARGER);
		itemColours.register(new ColourItem(), MODULE_CRAFTER);
		itemColours.register(new ColourItem(), MODULE_ENERGY_DISPLAY);

		//ItemModelsProperties.register(Items.SHIELD, new ResourceLocation("blocking"), (p_239421_0_, p_239421_1_, p_239421_2_) -> { return p_239421_2_ != null && p_239421_2_.isUsingItem() && p_239421_2_.getUseItem() == p_239421_0_ ? 1.0F : 0.0F; });
	    ItemModelsProperties.register(DIMENSIONAL_TRIDENT, new ResourceLocation("throwing"), (stackIn, clientWorldIn, livingEntityIn) -> { return livingEntityIn != null && livingEntityIn.isUsingItem() && livingEntityIn.getUseItem() == stackIn ? 1.0F : 0.0F; });
		ItemModelsProperties.register(DIMENSIONAL_BOW, new ResourceLocation("pull"), (stackIn, clientWorldIn, livingEntityIn) -> { if (livingEntityIn == null) { return 0.0F; } else { return livingEntityIn.getUseItem() != stackIn ? 0.0F : (float) (stackIn.getUseDuration() - livingEntityIn.getUseItemRemainingTicks()) / 20.0F; }});
		ItemModelsProperties.register(DIMENSIONAL_BOW, new ResourceLocation("pulling"), (stackIn, clientWorldIn, livingEntityIn) -> { return livingEntityIn != null && livingEntityIn.isUsingItem() && livingEntityIn.getUseItem() == stackIn ? 1.0F : 0.0F; });
		
		EntityRendererManager renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
		
		renderManager.getSkinMap().values().forEach((player) -> player.addLayer(new CosmosElytraLayer(player, new ResourceLocation(DimensionalPockets.MOD_ID, "textures/entity/dimensional_elytra.png"))));
		renderManager.getSkinMap().values().forEach((player) -> player.addLayer(new CosmosElytraArmourLayer<>(player, new BipedModel<>(0.5F), new BipedModel<>(1.0F))));
		
		renderManager.register(TRIDENT_TYPE, new RendererDimensionalTrident(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(TRIDENT_TYPE, RendererDimensionalTrident::new);
		
		DimensionalPockets.LOGGER.info("FMLClientSetup complete.");
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
}