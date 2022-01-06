package com.tcn.dimensionalpocketsii.core.management;

import org.lwjgl.glfw.GLFW;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.platform.InputConstants;
import com.tcn.cosmoslibrary.client.entity.layer.CosmosLayerArmourColourable;
import com.tcn.cosmoslibrary.client.entity.layer.CosmosLayerElytra;
import com.tcn.cosmoslibrary.common.block.CosmosBlock;
import com.tcn.cosmoslibrary.common.block.CosmosBlockModelUnplaceable;
import com.tcn.cosmoslibrary.common.block.CosmosItemBlock;
import com.tcn.cosmoslibrary.common.interfaces.IBlankCreativeTab;
import com.tcn.cosmoslibrary.common.item.CosmosItem;
import com.tcn.cosmoslibrary.common.item.CosmosItemEffect;
import com.tcn.cosmoslibrary.common.item.CosmosItemTool;
import com.tcn.cosmoslibrary.common.runtime.CosmosRuntimeHelper;
import com.tcn.cosmoslibrary.common.tab.CosmosCreativeModeTab;
import com.tcn.cosmoslibrary.energy.item.CosmosEnergyArmourItemColourable;
import com.tcn.cosmoslibrary.energy.item.CosmosEnergyItem;
import com.tcn.dimensionalpocketsii.DimReference;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.client.colour.ColourBlockPocket;
import com.tcn.dimensionalpocketsii.client.colour.ColourBlockWall;
import com.tcn.dimensionalpocketsii.client.colour.ColourItem;
import com.tcn.dimensionalpocketsii.client.container.ContainerElytraplate;
import com.tcn.dimensionalpocketsii.client.renderer.RendererDimensionalTrident;
import com.tcn.dimensionalpocketsii.client.renderer.RendererDimensionalTridentEnhanced;
import com.tcn.dimensionalpocketsii.client.renderer.ter.RendererConduitEnergy;
import com.tcn.dimensionalpocketsii.client.screen.ScreenConfiguration;
import com.tcn.dimensionalpocketsii.client.screen.ScreenElytraplate;
import com.tcn.dimensionalpocketsii.core.entity.DimensionalTridentEnhancedEntity;
import com.tcn.dimensionalpocketsii.core.entity.DimensionalTridentEntity;
import com.tcn.dimensionalpocketsii.core.external.block.BlockConduitEnergy;
import com.tcn.dimensionalpocketsii.core.external.blockentity.BlockEntityEnergyConduit;
import com.tcn.dimensionalpocketsii.core.impl.IRarityPocket;
import com.tcn.dimensionalpocketsii.core.item.CoreArmourMaterial;
import com.tcn.dimensionalpocketsii.core.item.CoreItemTier;
import com.tcn.dimensionalpocketsii.core.item.DimensionalEjector;
import com.tcn.dimensionalpocketsii.core.item.DimensionalEnergyCell;
import com.tcn.dimensionalpocketsii.core.item.DimensionalEnergyCellEnhanced;
import com.tcn.dimensionalpocketsii.core.item.DimensionalShifter;
import com.tcn.dimensionalpocketsii.core.item.DimensionalShifterEnhanced;
import com.tcn.dimensionalpocketsii.core.item.DimensionalTome;
import com.tcn.dimensionalpocketsii.core.item.armour.DimensionalElytraplate;
import com.tcn.dimensionalpocketsii.core.item.armour.DimensionalElytraplateScreen;
import com.tcn.dimensionalpocketsii.core.item.armour.DimensionalElytraplateShift;
import com.tcn.dimensionalpocketsii.core.item.armour.module.ItemModuleBattery;
import com.tcn.dimensionalpocketsii.core.item.armour.module.ItemModuleScreen;
import com.tcn.dimensionalpocketsii.core.item.armour.module.ItemModuleShifter;
import com.tcn.dimensionalpocketsii.core.item.armour.module.ItemModuleSolar;
import com.tcn.dimensionalpocketsii.core.item.armour.module.ItemModuleVisor;
import com.tcn.dimensionalpocketsii.core.item.tool.DimensionalAxe;
import com.tcn.dimensionalpocketsii.core.item.tool.DimensionalBow;
import com.tcn.dimensionalpocketsii.core.item.tool.DimensionalHoe;
import com.tcn.dimensionalpocketsii.core.item.tool.DimensionalPickaxe;
import com.tcn.dimensionalpocketsii.core.item.tool.DimensionalShovel;
import com.tcn.dimensionalpocketsii.core.item.tool.DimensionalSword;
import com.tcn.dimensionalpocketsii.core.item.tool.DimensionalTrident;
import com.tcn.dimensionalpocketsii.core.item.tool.DimensionalTridentEnhanced;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerFocus;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerModuleArmourWorkbench;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerModuleCharger;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerModuleConnector;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerModuleCrafter;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerModuleFurnace;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerModuleGenerator;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerModuleUpgradeStation;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerPocket;
import com.tcn.dimensionalpocketsii.pocket.client.renderer.ter.RendererTileEntityModuleFluidDisplay;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenFocus;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenModuleArmourWorkbench;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenModuleCharger;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenModuleConnector;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenModuleCrafter;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenModuleFurnace;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenModuleGenerator;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenModuleUpgradeStation;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenPocket;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockFocus;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockPocket;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallArmourWorkbench;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallBase;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallCharger;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallConnector;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallCrafter;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallEdge;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallEnergyDisplay;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallFluidDisplay;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallFurnace;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallGenerator;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallUpgradeStation;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityFocus;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleArmourWorkbench;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleCharger;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleConnector;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleCrafter;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleFluidDisplay;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleFurnace;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleGenerator;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleUpgradeStation;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityPocket;
import com.tcn.dimensionalpocketsii.pocket.core.item.block.ItemBlockFocus;
import com.tcn.dimensionalpocketsii.pocket.core.item.block.ItemBlockPocket;
import com.tcn.dimensionalpocketsii.pocket.core.item.module.ModuleArmourWorkbench;
import com.tcn.dimensionalpocketsii.pocket.core.item.module.ModuleBase;
import com.tcn.dimensionalpocketsii.pocket.core.item.module.ModuleCharger;
import com.tcn.dimensionalpocketsii.pocket.core.item.module.ModuleConnector;
import com.tcn.dimensionalpocketsii.pocket.core.item.module.ModuleCrafter;
import com.tcn.dimensionalpocketsii.pocket.core.item.module.ModuleEnergyDisplay;
import com.tcn.dimensionalpocketsii.pocket.core.item.module.ModuleFluidDisplay;
import com.tcn.dimensionalpocketsii.pocket.core.item.module.ModuleFocus;
import com.tcn.dimensionalpocketsii.pocket.core.item.module.ModuleFurnace;
import com.tcn.dimensionalpocketsii.pocket.core.item.module.ModuleGenerator;
import com.tcn.dimensionalpocketsii.pocket.core.item.module.ModuleUpgradeStation;

import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ConfigGuiHandler.ConfigGuiFactory;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModBusManager {
	
	private static final Rarity RARITYPOCKET = Rarity.create("Pocket", ChatFormatting.DARK_PURPLE);
	private static final Rarity RARITY_ARMOUR = Rarity.create("Armour Module", ChatFormatting.GOLD);
	private static final Rarity RARITY_POCKET = Rarity.create("Pocket Module", ChatFormatting.AQUA);
	
	
	public static final CosmosCreativeModeTab DIM_POCKETS_BLOCKS_GROUP = new CosmosCreativeModeTab(DimensionalPockets.MOD_ID + ".blocks", () -> new ItemStack(ModBusManager.BLOCK_POCKET));
	public static final CosmosCreativeModeTab DIM_POCKETS_ITEMS_GROUP = new CosmosCreativeModeTab(DimensionalPockets.MOD_ID + ".items", () -> new ItemStack(ModBusManager.DIMENSIONAL_INGOT));
	public static final CosmosCreativeModeTab DIM_POCKETS_TOOLS_GROUP = new CosmosCreativeModeTab(DimensionalPockets.MOD_ID + ".tools", () -> new ItemStack(ModBusManager.DIMENSIONAL_SWORD));

	public static final Item DIMENSIONAL_TOME = new DimensionalTome(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(1).rarity(RARITYPOCKET));
	
	public static final Item DIMENSIONAL_SHARD = new CosmosItem(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).rarity(RARITYPOCKET));
	public static final Item DIMENSIONAL_INGOT = new CosmosItem(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).rarity(RARITYPOCKET));
	public static final Item DIMENSIONAL_INGOT_ENHANCED = new CosmosItemEffect(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(1).rarity(Rarity.RARE));
	public static final Item DIMENSIONAL_GEM = new CosmosItem(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).rarity(RARITYPOCKET));
	
	public static final Item DIMENSIONAL_DUST = new CosmosItem(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).rarity(RARITYPOCKET));
	public static final Item DIMENSIONAL_PEARL = new CosmosItem(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(16).rarity(RARITYPOCKET));
	public static final Item DIMENSIONAL_THREAD = new CosmosItem(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).rarity(RARITYPOCKET));
	
	public static final Item NETHER_STAR_SHARD = new CosmosItemEffect(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(16).rarity(Rarity.RARE));
	public static final Item ELYTRA_WING = new CosmosItem(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(2).rarity(Rarity.RARE));
	
	public static final Item DIMENSIONAL_WRENCH = new CosmosItemTool(new Item.Properties().stacksTo(1).tab(DIM_POCKETS_TOOLS_GROUP));
	
	public static final Item DIMENSIONAL_DEVICE_BASE = new CosmosItem(new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).stacksTo(16));
	public static final Item DIMENSIONAL_EJECTOR = new DimensionalEjector(new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).stacksTo(4));
	
	public static final Item DIMENSIONAL_SHIFTER = new DimensionalShifter(new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).stacksTo(1).rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().maxEnergyStored(5000000).maxIO(50000).maxUse(100000));
	public static final Item DIMENSIONAL_SHIFTER_ENHANCED = new DimensionalShifterEnhanced(new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).stacksTo(1).rarity(Rarity.RARE), new CosmosEnergyItem.Properties().maxEnergyStored(10000000).maxIO(100000).maxUse(50000));
	
	public static final Item DIMENSIONAL_ENERGY_CELL = new DimensionalEnergyCell(new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).stacksTo(1).rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().maxEnergyStored(10000000).maxIO(100000));
	public static final Item DIMENSIONAL_ENERGY_CELL_ENHANCED = new DimensionalEnergyCellEnhanced(new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).stacksTo(1).rarity(Rarity.RARE), new CosmosEnergyItem.Properties().maxEnergyStored(50000000).maxIO(200000));

	public static final Item DIMENSIONAL_SWORD = new DimensionalSword(CoreItemTier.DIMENSIONAL, 5, -1.0F, false, new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().setStatsFromArray(DimReference.CONSTANT.ENERGY));
	public static final Item DIMENSIONAL_PICKAXE = new DimensionalPickaxe(CoreItemTier.DIMENSIONAL, 2, -2.0F, false, new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().setStatsFromArray(DimReference.CONSTANT.ENERGY));
	public static final Item DIMENSIONAL_AXE = new DimensionalAxe(CoreItemTier.DIMENSIONAL, 7, -1.5F, false, new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().setStatsFromArray(DimReference.CONSTANT.ENERGY));
	public static final Item DIMENSIONAL_SHOVEL = new DimensionalShovel(CoreItemTier.DIMENSIONAL,  1, -2.5F, false, new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().setStatsFromArray(DimReference.CONSTANT.ENERGY));
	public static final Item DIMENSIONAL_HOE = new DimensionalHoe(CoreItemTier.DIMENSIONAL, 1, -2.5F, false, new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().setStatsFromArray(DimReference.CONSTANT.ENERGY));
	
	public static final Item DIMENSIONAL_BOW = new DimensionalBow(new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().maxEnergyStored(2000000).maxIO(100000).maxUse(10000));
	public static final Item DIMENSIONAL_TRIDENT = new DimensionalTrident(new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().setStatsFromArray(DimReference.CONSTANT.ENERGY));
	
	public static final Item DIMENSIONAL_SWORD_ENHANCED = new DimensionalSword(CoreItemTier.DIMENSIONAL_ENHANCED, 5, 0.0F, true, new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(Rarity.RARE), new CosmosEnergyItem.Properties().setStatsFromArray(DimReference.CONSTANT.ENERGY_ENHANCED));
	public static final Item DIMENSIONAL_PICKAXE_ENHANCED = new DimensionalPickaxe(CoreItemTier.DIMENSIONAL_ENHANCED, 2, -2.0F, true, new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(Rarity.RARE), new CosmosEnergyItem.Properties().setStatsFromArray(DimReference.CONSTANT.ENERGY_ENHANCED));
	public static final Item DIMENSIONAL_AXE_ENHANCED = new DimensionalAxe(CoreItemTier.DIMENSIONAL_ENHANCED, 7, -1.5F, true, new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(Rarity.RARE), new CosmosEnergyItem.Properties().setStatsFromArray(DimReference.CONSTANT.ENERGY_ENHANCED));
	public static final Item DIMENSIONAL_SHOVEL_ENHANCED = new DimensionalShovel(CoreItemTier.DIMENSIONAL_ENHANCED, 1, -2.5F, true, new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(Rarity.RARE), new CosmosEnergyItem.Properties().setStatsFromArray(DimReference.CONSTANT.ENERGY_ENHANCED));
	public static final Item DIMENSIONAL_HOE_ENHANCED = new DimensionalHoe(CoreItemTier.DIMENSIONAL_ENHANCED, 1, -2.5F, true, new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(Rarity.RARE), new CosmosEnergyItem.Properties().setStatsFromArray(DimReference.CONSTANT.ENERGY_ENHANCED));
	
	//public static final Item DIMENSIONAL_BOW_ENHANCED = new DimensionalBow(new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(RARITYPOCKET)); //, new CosmosEnergyItem.Properties().maxEnergyStored(2000000).maxIO(100000).maxUse(10000));
	public static final Item DIMENSIONAL_TRIDENT_ENHANCED = new DimensionalTridentEnhanced(new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(Rarity.RARE), new CosmosEnergyItem.Properties().setStatsFromArray(DimReference.CONSTANT.ENERGY_ENHANCED));
	
	public static final Item DIMENSIONAL_HELMET = new CosmosEnergyArmourItemColourable(CoreArmourMaterial.DIMENSIONAL, EquipmentSlot.HEAD, true, (new Item.Properties()).tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().maxEnergyStored(2000000).maxIO(100000).maxUse(10000));
	public static final Item DIMENSIONAL_CHESTPLATE = new CosmosEnergyArmourItemColourable(CoreArmourMaterial.DIMENSIONAL, EquipmentSlot.CHEST, false, (new Item.Properties()).tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().maxEnergyStored(2000000).maxIO(100000).maxUse(12000));
	public static final Item DIMENSIONAL_LEGGINGS = new CosmosEnergyArmourItemColourable(CoreArmourMaterial.DIMENSIONAL, EquipmentSlot.LEGS, false, (new Item.Properties()).tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().maxEnergyStored(2000000).maxIO(100000).maxUse(10000));
	public static final Item DIMENSIONAL_BOOTS = new CosmosEnergyArmourItemColourable(CoreArmourMaterial.DIMENSIONAL, EquipmentSlot.FEET, false, (new Item.Properties()).tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().maxEnergyStored(2000000).maxIO(100000).maxUse(8000));

	public static final Item DIMENSIONAL_HELMET_ENHANCED = new CosmosEnergyArmourItemColourable(CoreArmourMaterial.DIMENSIONAL_ENHANCED, EquipmentSlot.HEAD, true, (new Item.Properties()).tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(Rarity.RARE), new CosmosEnergyItem.Properties().maxEnergyStored(6000000).maxIO(100000).maxUse(10000));
	public static final Item DIMENSIONAL_CHESTPLATE_ENHANCED = new CosmosEnergyArmourItemColourable(CoreArmourMaterial.DIMENSIONAL_ENHANCED, EquipmentSlot.CHEST, false, (new Item.Properties()).tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(Rarity.RARE), new CosmosEnergyItem.Properties().maxEnergyStored(6000000).maxIO(100000).maxUse(12000));
	public static final Item DIMENSIONAL_LEGGINGS_ENHANCED = new CosmosEnergyArmourItemColourable(CoreArmourMaterial.DIMENSIONAL_ENHANCED, EquipmentSlot.LEGS, false, (new Item.Properties()).tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(Rarity.RARE), new CosmosEnergyItem.Properties().maxEnergyStored(6000000).maxIO(100000).maxUse(10000));
	public static final Item DIMENSIONAL_BOOTS_ENHANCED = new CosmosEnergyArmourItemColourable(CoreArmourMaterial.DIMENSIONAL_ENHANCED, EquipmentSlot.FEET, false, (new Item.Properties()).tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(Rarity.RARE), new CosmosEnergyItem.Properties().maxEnergyStored(6000000).maxIO(100000).maxUse(8000));
	
	public static final Item DIMENSIONAL_ELYTRAPLATE = new DimensionalElytraplate(CoreArmourMaterial.DIMENSIONAL_ENHANCED, EquipmentSlot.CHEST, (new Item.Properties()).tab(DIM_POCKETS_TOOLS_GROUP).rarity(Rarity.RARE).fireResistant(), false, new CosmosEnergyItem.Properties().maxEnergyStored(10000000).maxIO(200000).maxUse(6000));
	public static MenuType<ContainerElytraplate> ELYTRAPLATE_CONTAINER_TYPE;
	
	@Deprecated(forRemoval = true, since = "1.18.1-5.2.0.0")
	public static final Item DIMENSIONAL_ELYTRAPLATE_SHIFT = new DimensionalElytraplateShift(CoreArmourMaterial.DIMENSIONAL_ENHANCED, EquipmentSlot.CHEST, (new Item.Properties()).tab(DIM_POCKETS_TOOLS_GROUP).rarity(Rarity.RARE).fireResistant(), false, new CosmosEnergyItem.Properties().maxEnergyStored(10000000).maxIO(200000).maxUse(6000));
	@Deprecated(forRemoval = true, since = "1.18.1-5.2.0.0")
	public static final Item DIMENSIONAL_ELYTRAPLATE_SCREEN = new DimensionalElytraplateScreen(CoreArmourMaterial.DIMENSIONAL_ENHANCED, EquipmentSlot.CHEST, (new Item.Properties()).tab(DIM_POCKETS_TOOLS_GROUP).rarity(Rarity.RARE).fireResistant(), false, new CosmosEnergyItem.Properties().maxEnergyStored(10000000).maxIO(200000).maxUse(6000));
	
	public static final Item ARMOUR_MODULE_SCREEN = new ItemModuleScreen(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(1).rarity(RARITY_ARMOUR));
	public static final Item ARMOUR_MODULE_SHIFTER = new ItemModuleShifter(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(1).rarity(RARITY_ARMOUR));
	public static final Item ARMOUR_MODULE_VISOR = new ItemModuleVisor(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(1).rarity(RARITY_ARMOUR));
	public static final Item ARMOUR_MODULE_SOLAR = new ItemModuleSolar(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(1).rarity(RARITY_ARMOUR));
	public static final Item ARMOUR_MODULE_BATTERY = new ItemModuleBattery(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(1).rarity(RARITY_ARMOUR));
	
	public static final Item MODULE_BASE = new ModuleBase(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(8).rarity(RARITY_POCKET));
	public static final Item MODULE_CONNECTOR = new ModuleConnector(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(8).rarity(RARITY_POCKET));
	public static final Item MODULE_CHARGER = new ModuleCharger(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(8).rarity(RARITY_POCKET));
	public static final Item MODULE_CRAFTER = new ModuleCrafter(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(8).rarity(RARITY_POCKET));
	public static final Item MODULE_FURNACE = new ModuleFurnace(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(8).rarity(RARITY_POCKET));
	public static final Item MODULE_ENERGY_DISPLAY = new ModuleEnergyDisplay(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(8).rarity(RARITY_POCKET));
	public static final Item MODULE_FLUID_DISPLAY = new ModuleFluidDisplay(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(8).rarity(RARITY_POCKET));
	public static final Item MODULE_ARMOUR_WORKBENCH = new ModuleArmourWorkbench(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(8).rarity(RARITY_POCKET));
	public static final Item MODULE_GENERATOR = new ModuleGenerator(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(8).rarity(RARITY_POCKET));
	public static final Item MODULE_UPGRADE_STATION = new ModuleUpgradeStation(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(8).rarity(RARITY_POCKET));
	public static final Item MODULE_FOCUS = new ModuleFocus(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(8).rarity(RARITY_POCKET));
	
	public static final Block BLOCK_DIMENSIONAL_ORE = new CosmosBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(4.0F, 4.0F));
	public static final Block BLOCK_DEEPSLATE_DIMENSIONAL_ORE = new CosmosBlock(Block.Properties.of(Material.STONE, MaterialColor.DEEPSLATE).requiresCorrectToolForDrops().strength(8.0F, 8.0F).sound(SoundType.DEEPSLATE));
	public static final Block BLOCK_DIMENSIONAL_ORE_NETHER = new CosmosBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(6.0F, 8.0F));
	public static final Block BLOCK_DIMENSIONAL_ORE_END = new CosmosBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(8.0F, 8.0F));
	
	public static final Block BLOCK_DIMENSIONAL = new CosmosBlock(Block.Properties.of(Material.HEAVY_METAL).requiresCorrectToolForDrops().strength(6.0F, 8.0F));
	public static final Block BLOCK_DIMENSIONAL_METAL = new CosmosBlock(Block.Properties.of(Material.HEAVY_METAL).requiresCorrectToolForDrops().strength(6.0F, 8.0F));
	public static final Block BLOCK_DIMENSIONAL_GEM = new CosmosBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(6.0F, 8.0F));
	
	public static final Block BLOCK_DIMENSIONAL_CORE = new CosmosBlockModelUnplaceable(Block.Properties.of(Material.HEAVY_METAL).requiresCorrectToolForDrops().strength(6.0F, 8.0F));
	
	public static final Block BLOCK_POCKET = new BlockPocket(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).requiresCorrectToolForDrops().noOcclusion());
	public static final BlockItem BLOCK_ITEM_POCKET = new ItemBlockPocket(BLOCK_POCKET, new Item.Properties().stacksTo(1).setNoRepair().tab(DIM_POCKETS_BLOCKS_GROUP).rarity(RARITYPOCKET).fireResistant());
	public static BlockEntityType<BlockEntityPocket> POCKET_TILE_TYPE;
	public static MenuType<ContainerPocket> POCKET_CONTAINER_TYPE;
	
	public static final Block BLOCK_FOCUS = new BlockFocus(Block.Properties.of(Material.HEAVY_METAL).requiresCorrectToolForDrops().strength(-1, 3600000.0F));
	public static final BlockItem BLOCK_ITEM_FOCUS = new ItemBlockFocus(BLOCK_FOCUS, new Item.Properties().setNoRepair().tab(DIM_POCKETS_BLOCKS_GROUP).rarity(RARITYPOCKET).fireResistant());
	public static BlockEntityType<BlockEntityFocus> FOCUS_TILE_TYPE;
	public static MenuType<ContainerFocus> FOCUS_CONTAINER_TYPE;
	
	public static final Block BLOCK_WALL_CONNECTOR = new BlockWallConnector(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; }));
	public static BlockEntityType<BlockEntityModuleConnector> CONNECTOR_TILE_TYPE;
	public static MenuType<ContainerModuleConnector> CONNECTOR_CONTAINER_TYPE;
	
	public static final Block BLOCK_WALL_CHARGER = new BlockWallCharger(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; }));
	public static BlockEntityType<BlockEntityModuleCharger> CHARGER_TILE_TYPE;
	public static MenuType<ContainerModuleCharger> CHARGER_CONTAINER_TYPE;
	
	public static final Block BLOCK_WALL_CRAFTER = new BlockWallCrafter(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; }));
	public static BlockEntityType<BlockEntityModuleCrafter> CRAFTER_TILE_TYPE;
	public static MenuType<ContainerModuleCrafter> CRAFTER_CONTAINER_TYPE;

	public static final Block BLOCK_WALL_FURNACE = new BlockWallFurnace(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; }));
	public static BlockEntityType<BlockEntityModuleFurnace> FURNACE_TILE_TYPE;
	public static MenuType<ContainerModuleFurnace> FURNACE_CONTAINER_TYPE;

	public static final Block BLOCK_WALL_ENERGY_DISPLAY = new BlockWallEnergyDisplay(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; }).randomTicks());
	
	public static final Block BLOCK_WALL_FLUID_DISPLAY = new BlockWallFluidDisplay(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; }).randomTicks().dynamicShape().noOcclusion());
	public static BlockEntityType<BlockEntityModuleFluidDisplay> FLUID_DISPLAY_TILE_TYPE;

	public static final Block BLOCK_WALL_ARMOUR_WORKBENCH = new BlockWallArmourWorkbench(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; }).randomTicks());
	public static BlockEntityType<BlockEntityModuleArmourWorkbench> ARMOUR_WORKBENCH_TILE_TYPE;
	public static MenuType<ContainerModuleArmourWorkbench> ARMOUR_WORKBENCH_CONTAINER_TYPE;

	public static final Block BLOCK_WALL_UPGRADE_STATION = new BlockWallUpgradeStation(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; }).randomTicks());
	public static BlockEntityType<BlockEntityModuleUpgradeStation> UPGRADE_STATION_TILE_TYPE;
	public static MenuType<ContainerModuleUpgradeStation> UPGRADE_STATION_CONTAINER_TYPE;

	public static final Block BLOCK_WALL_GENERATOR = new BlockWallGenerator(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; }));
	public static BlockEntityType<BlockEntityModuleGenerator> GENERATOR_TILE_TYPE;
	public static MenuType<ContainerModuleGenerator> GENERATOR_CONTAINER_TYPE;
	
	public static final Block BLOCK_CONDUIT_ENERGY = new BlockConduitEnergy(Block.Properties.of(Material.HEAVY_METAL).requiresCorrectToolForDrops().strength(4.0F, 4.0F).dynamicShape().noOcclusion());
	public static final BlockItem BLOCK_ITEM_CONDUIT_ENERGY = new CosmosItemBlock(BLOCK_CONDUIT_ENERGY, new Item.Properties().fireResistant().tab(DIM_POCKETS_BLOCKS_GROUP).rarity(RARITYPOCKET), "info", "desc1", "desc2");
	public static BlockEntityType<BlockEntityEnergyConduit> CONDUIT_ENERGY_TILE_TYPE;
	
	public static final Block BLOCK_WALL = new BlockWallBase(Block.Properties.of(Material.HEAVY_METAL).strength(-1,3600000.0F).lightLevel((state) -> { return 15; }));
	public static final Block BLOCK_WALL_EDGE = new BlockWallEdge(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; }));

	public static EntityType<DimensionalTridentEntity> TRIDENT_TYPE;
	public static EntityType<DimensionalTridentEnhancedEntity> TRIDENT_TYPE_ENHANCED;
	
	public static KeyMapping SUIT_SCREEN;
	public static KeyMapping SUIT_SHIFT;
	public static KeyMapping SUIT_SETTINGS;
	
	@SubscribeEvent
	public static void onItemRegistry(final RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();
		
		event.getRegistry().registerAll(
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_tome", DIMENSIONAL_TOME),
			
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_shard", DIMENSIONAL_SHARD),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_ingot", DIMENSIONAL_INGOT),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_ingot_enhanced", DIMENSIONAL_INGOT_ENHANCED),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_gem", DIMENSIONAL_GEM),
			
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_dust", DIMENSIONAL_DUST),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_pearl", DIMENSIONAL_PEARL),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_thread", DIMENSIONAL_THREAD),
			
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "nether_star_shard", NETHER_STAR_SHARD),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "elytra_wing", ELYTRA_WING),

			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_wrench", DIMENSIONAL_WRENCH),
			
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_device_base", DIMENSIONAL_DEVICE_BASE),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_ejector", DIMENSIONAL_EJECTOR),
			
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_shifter", DIMENSIONAL_SHIFTER),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_shifter_enhanced", DIMENSIONAL_SHIFTER_ENHANCED),
			
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_energy_cell", DIMENSIONAL_ENERGY_CELL),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_energy_cell_enhanced", DIMENSIONAL_ENERGY_CELL_ENHANCED),
			
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_sword", DIMENSIONAL_SWORD),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_pickaxe", DIMENSIONAL_PICKAXE),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_axe", DIMENSIONAL_AXE),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_shovel", DIMENSIONAL_SHOVEL),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_hoe", DIMENSIONAL_HOE),
			
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_bow", DIMENSIONAL_BOW),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_trident", DIMENSIONAL_TRIDENT),

			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_sword_enhanced", DIMENSIONAL_SWORD_ENHANCED),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_pickaxe_enhanced", DIMENSIONAL_PICKAXE_ENHANCED),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_axe_enhanced", DIMENSIONAL_AXE_ENHANCED),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_shovel_enhanced", DIMENSIONAL_SHOVEL_ENHANCED),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_hoe_enhanced", DIMENSIONAL_HOE_ENHANCED),
			
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_trident_enhanced", DIMENSIONAL_TRIDENT_ENHANCED),
			
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_helmet", DIMENSIONAL_HELMET),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_chestplate", DIMENSIONAL_CHESTPLATE),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_leggings", DIMENSIONAL_LEGGINGS),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_boots", DIMENSIONAL_BOOTS),

			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_helmet_enhanced", DIMENSIONAL_HELMET_ENHANCED),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_chestplate_enhanced", DIMENSIONAL_CHESTPLATE_ENHANCED),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_leggings_enhanced", DIMENSIONAL_LEGGINGS_ENHANCED),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_boots_enhanced", DIMENSIONAL_BOOTS_ENHANCED),

			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_elytraplate", DIMENSIONAL_ELYTRAPLATE),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_elytraplate_shift", DIMENSIONAL_ELYTRAPLATE_SHIFT),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "dimensional_elytraplate_screen", DIMENSIONAL_ELYTRAPLATE_SCREEN),

			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "armour_module_screen", ARMOUR_MODULE_SCREEN),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "armour_module_shifter", ARMOUR_MODULE_SHIFTER),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "armour_module_visor", ARMOUR_MODULE_VISOR),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "armour_module_solar", ARMOUR_MODULE_SOLAR),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "armour_module_battery", ARMOUR_MODULE_BATTERY),
			
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "module_base", MODULE_BASE),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "module_connector", MODULE_CONNECTOR),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "module_charger", MODULE_CHARGER),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "module_crafter", MODULE_CRAFTER),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "module_furnace", MODULE_FURNACE),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "module_energy_display", MODULE_ENERGY_DISPLAY),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "module_fluid_display", MODULE_FLUID_DISPLAY),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "module_armour_workbench", MODULE_ARMOUR_WORKBENCH),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "module_upgrade_station", MODULE_UPGRADE_STATION),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "module_generator", MODULE_GENERATOR),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "module_focus", MODULE_FOCUS)
		);
		
		//Register BlockItems
		for (final Block block : ForgeRegistries.BLOCKS.getValues()) {
			final ResourceLocation blockRegistryName = block.getRegistryName();
			Preconditions.checkNotNull(blockRegistryName, "Registry Name of Block \"" + block + "\" of class \"" + block.getClass().getName() + "\"is null! This is not allowed!");

			if (!blockRegistryName.getNamespace().equals(DimensionalPockets.MOD_ID)) {
				continue;
			}
			
			if (block instanceof IRarityPocket) {
				final Item.Properties properties = new Item.Properties().rarity(RARITYPOCKET);
				final BlockItem blockItem = new BlockItem(block, properties);
				registry.register(CosmosRuntimeHelper.setupResource(blockRegistryName, blockItem));
			} 
			
			else if (block instanceof IBlankCreativeTab) {
				final Item.Properties properties = new Item.Properties();
				final BlockItem blockItem = new BlockItem(block, properties);
				registry.register(CosmosRuntimeHelper.setupResource(blockRegistryName, blockItem));
			}
			
			else if (block instanceof BlockPocket) {
				registry.register(CosmosRuntimeHelper.setupResource(blockRegistryName, BLOCK_ITEM_POCKET));
			} else if (block instanceof BlockConduitEnergy) {
				//registry.register(CosmosRuntimeHelper.setupResource(blockRegistryName, BLOCK_ITEM_CONDUIT_ENERGY));
			} else if (block instanceof BlockFocus) {
				registry.register(CosmosRuntimeHelper.setupResource(blockRegistryName, BLOCK_ITEM_FOCUS));
			}
			
			else if (block instanceof CosmosBlockModelUnplaceable) {
				final Item.Properties properties = new Item.Properties().tab(DIM_POCKETS_BLOCKS_GROUP).stacksTo(1);
				final BlockItem blockItem = new BlockItem(block, properties);
				registry.register(CosmosRuntimeHelper.setupResource(blockRegistryName, blockItem));
			}
			
			else {
				final Item.Properties properties = new Item.Properties().tab(DIM_POCKETS_BLOCKS_GROUP);
				final BlockItem blockItem = new BlockItem(block, properties);
				registry.register(CosmosRuntimeHelper.setupResource(blockRegistryName, blockItem));
			}
		}
		
		DimensionalPockets.CONSOLE.startup("Item Registration complete.");
	}

	@SubscribeEvent
	public static void onBlockRegistry(final RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "block_dimensional_ore", BLOCK_DIMENSIONAL_ORE),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "block_deepslate_dimensional_ore", BLOCK_DEEPSLATE_DIMENSIONAL_ORE),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "block_dimensional_ore_nether", BLOCK_DIMENSIONAL_ORE_NETHER),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "block_dimensional_ore_end", BLOCK_DIMENSIONAL_ORE_END),
			
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "block_dimensional", BLOCK_DIMENSIONAL),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "block_dimensional_metal", BLOCK_DIMENSIONAL_METAL),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "block_dimensional_gem", BLOCK_DIMENSIONAL_GEM),
			
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "block_dimensional_core", BLOCK_DIMENSIONAL_CORE),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "block_pocket", BLOCK_POCKET),

			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "block_dimensional_focus", BLOCK_FOCUS),
			
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "block_wall", BLOCK_WALL),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "block_wall_edge", BLOCK_WALL_EDGE),
			
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "block_wall_connector", BLOCK_WALL_CONNECTOR),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "block_wall_charger", BLOCK_WALL_CHARGER),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "block_wall_crafter", BLOCK_WALL_CRAFTER),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "block_wall_furnace", BLOCK_WALL_FURNACE),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "block_wall_energy_display", BLOCK_WALL_ENERGY_DISPLAY),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "block_wall_fluid_display", BLOCK_WALL_FLUID_DISPLAY),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "block_wall_armour_workbench", BLOCK_WALL_ARMOUR_WORKBENCH),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "block_wall_upgrade_station", BLOCK_WALL_UPGRADE_STATION),
			CosmosRuntimeHelper.setupString(DimensionalPockets.MOD_ID, "block_wall_generator", BLOCK_WALL_GENERATOR)
			
			//setupString(DimensionalPockets.MOD_ID, "block_conduit_energy", BLOCK_CONDUIT_ENERGY)
		);
		
		DimensionalPockets.CONSOLE.startup("Block Registration complete.");
	}
	
	@SubscribeEvent
	public static void onMenuTypeRegistry(final RegistryEvent.Register<MenuType<?>> event) {
		POCKET_CONTAINER_TYPE = IForgeMenuType.create(ContainerPocket::createContainerClientSide); POCKET_CONTAINER_TYPE.setRegistryName(new ResourceLocation(DimensionalPockets.MOD_ID, "container_pocket"));
		CONNECTOR_CONTAINER_TYPE = IForgeMenuType.create(ContainerModuleConnector::createContainerClientSide); CONNECTOR_CONTAINER_TYPE.setRegistryName(new ResourceLocation(DimensionalPockets.MOD_ID, "container_connector"));
		CHARGER_CONTAINER_TYPE = IForgeMenuType.create(ContainerModuleCharger::new); CHARGER_CONTAINER_TYPE.setRegistryName(new ResourceLocation(DimensionalPockets.MOD_ID, "container_charger"));
		CRAFTER_CONTAINER_TYPE = IForgeMenuType.create(ContainerModuleCrafter::new); CRAFTER_CONTAINER_TYPE.setRegistryName(new ResourceLocation(DimensionalPockets.MOD_ID, "container_crafter"));
		FURNACE_CONTAINER_TYPE = IForgeMenuType.create(ContainerModuleFurnace::new); FURNACE_CONTAINER_TYPE.setRegistryName(new ResourceLocation(DimensionalPockets.MOD_ID, "container_furnace"));
		ARMOUR_WORKBENCH_CONTAINER_TYPE = IForgeMenuType.create(ContainerModuleArmourWorkbench::new); ARMOUR_WORKBENCH_CONTAINER_TYPE.setRegistryName(new ResourceLocation(DimensionalPockets.MOD_ID, "container_armour_workbench"));
		UPGRADE_STATION_CONTAINER_TYPE = IForgeMenuType.create(ContainerModuleUpgradeStation::new); UPGRADE_STATION_CONTAINER_TYPE.setRegistryName(new ResourceLocation(DimensionalPockets.MOD_ID, "container_upgrade_station"));
		GENERATOR_CONTAINER_TYPE = IForgeMenuType.create(ContainerModuleGenerator::new); GENERATOR_CONTAINER_TYPE.setRegistryName(new ResourceLocation(DimensionalPockets.MOD_ID, "container_generator"));
		FOCUS_CONTAINER_TYPE = IForgeMenuType.create(ContainerFocus::new); FOCUS_CONTAINER_TYPE.setRegistryName(new ResourceLocation(DimensionalPockets.MOD_ID, "container_focus"));
		
		ELYTRAPLATE_CONTAINER_TYPE = IForgeMenuType.create(ContainerElytraplate::createContainerClientSide); ELYTRAPLATE_CONTAINER_TYPE.setRegistryName(new ResourceLocation(DimensionalPockets.MOD_ID, "container_elytraplate"));
		
		event.getRegistry().registerAll(
			POCKET_CONTAINER_TYPE, CONNECTOR_CONTAINER_TYPE, CHARGER_CONTAINER_TYPE, CRAFTER_CONTAINER_TYPE, FURNACE_CONTAINER_TYPE, 
			ARMOUR_WORKBENCH_CONTAINER_TYPE, UPGRADE_STATION_CONTAINER_TYPE, GENERATOR_CONTAINER_TYPE, ELYTRAPLATE_CONTAINER_TYPE,
			FOCUS_CONTAINER_TYPE
		);
		
		DimensionalPockets.CONSOLE.startup("MenuType<> Registration complete.");
	}
	
	@SubscribeEvent
	public static void onBlockEntityTypeRegistry(final RegistryEvent.Register<BlockEntityType<?>> event) {	
		POCKET_TILE_TYPE = BlockEntityType.Builder.<BlockEntityPocket>of(BlockEntityPocket::new, BLOCK_POCKET).build(null); POCKET_TILE_TYPE.setRegistryName(DimensionalPockets.MOD_ID, "tile_entity_pocket");
		CONNECTOR_TILE_TYPE = BlockEntityType.Builder.<BlockEntityModuleConnector>of(BlockEntityModuleConnector::new, BLOCK_WALL_CONNECTOR).build(null); CONNECTOR_TILE_TYPE.setRegistryName(DimensionalPockets.MOD_ID, "tile_entity_connector");
		CHARGER_TILE_TYPE = BlockEntityType.Builder.<BlockEntityModuleCharger>of(BlockEntityModuleCharger::new, BLOCK_WALL_CHARGER).build(null); CHARGER_TILE_TYPE.setRegistryName(DimensionalPockets.MOD_ID, "tile_entity_charger");
		CRAFTER_TILE_TYPE = BlockEntityType.Builder.<BlockEntityModuleCrafter>of(BlockEntityModuleCrafter::new, BLOCK_WALL_CRAFTER).build(null); CRAFTER_TILE_TYPE.setRegistryName(DimensionalPockets.MOD_ID, "tile_entity_crafter");
		FURNACE_TILE_TYPE = BlockEntityType.Builder.<BlockEntityModuleFurnace>of(BlockEntityModuleFurnace::new, BLOCK_WALL_FURNACE).build(null); FURNACE_TILE_TYPE.setRegistryName(DimensionalPockets.MOD_ID, "tile_entity_furnace");
		FLUID_DISPLAY_TILE_TYPE = BlockEntityType.Builder.<BlockEntityModuleFluidDisplay>of(BlockEntityModuleFluidDisplay::new, BLOCK_WALL_FLUID_DISPLAY).build(null); FLUID_DISPLAY_TILE_TYPE.setRegistryName(DimensionalPockets.MOD_ID, "tile_entity_fluid_displau");
		ARMOUR_WORKBENCH_TILE_TYPE = BlockEntityType.Builder.<BlockEntityModuleArmourWorkbench>of(BlockEntityModuleArmourWorkbench::new, BLOCK_WALL_ARMOUR_WORKBENCH).build(null); ARMOUR_WORKBENCH_TILE_TYPE.setRegistryName(DimensionalPockets.MOD_ID, "tile_entity_armour_workbench");
		UPGRADE_STATION_TILE_TYPE = BlockEntityType.Builder.<BlockEntityModuleUpgradeStation>of(BlockEntityModuleUpgradeStation::new, BLOCK_WALL_UPGRADE_STATION).build(null); UPGRADE_STATION_TILE_TYPE.setRegistryName(DimensionalPockets.MOD_ID, "tile_entity_upgrade_station");
		GENERATOR_TILE_TYPE = BlockEntityType.Builder.<BlockEntityModuleGenerator>of(BlockEntityModuleGenerator::new, BLOCK_WALL_GENERATOR).build(null); GENERATOR_TILE_TYPE.setRegistryName(DimensionalPockets.MOD_ID, "tile_entity_generator");
		FOCUS_TILE_TYPE = BlockEntityType.Builder.<BlockEntityFocus>of(BlockEntityFocus::new, BLOCK_FOCUS).build(null); FOCUS_TILE_TYPE.setRegistryName(DimensionalPockets.MOD_ID, "tile_entity_focus");
		
		CONDUIT_ENERGY_TILE_TYPE = BlockEntityType.Builder.<BlockEntityEnergyConduit>of(BlockEntityEnergyConduit::new, BLOCK_CONDUIT_ENERGY).build(null); CONDUIT_ENERGY_TILE_TYPE.setRegistryName(DimensionalPockets.MOD_ID, "tile_entity_conduit_energy");
		
		event.getRegistry().registerAll(
			POCKET_TILE_TYPE, CONNECTOR_TILE_TYPE, CHARGER_TILE_TYPE, CRAFTER_TILE_TYPE, FURNACE_TILE_TYPE, 
			FLUID_DISPLAY_TILE_TYPE, ARMOUR_WORKBENCH_TILE_TYPE, UPGRADE_STATION_TILE_TYPE, GENERATOR_TILE_TYPE, CONDUIT_ENERGY_TILE_TYPE,
			FOCUS_TILE_TYPE
		);
		
		DimensionalPockets.CONSOLE.startup("BlockEntityType<> Registration complete.");
	}

	@SubscribeEvent
	public static void onBlockEntityRendererRegistry(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(FLUID_DISPLAY_TILE_TYPE, RendererTileEntityModuleFluidDisplay::new);
		event.registerBlockEntityRenderer(CONDUIT_ENERGY_TILE_TYPE, RendererConduitEnergy::new);

		DimensionalPockets.CONSOLE.startup("BlockEntityRenderer Registration complete.");
	}
	
	@SubscribeEvent
	public static void onEntityTypeRegistry(final RegistryEvent.Register<EntityType<?>> event) {
		TRIDENT_TYPE = EntityType.Builder.<DimensionalTridentEntity>of(DimensionalTridentEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("dimensional_trident_type");
		TRIDENT_TYPE.setRegistryName(DimensionalPockets.MOD_ID, "dimensional_trident_type");
		
		TRIDENT_TYPE_ENHANCED = EntityType.Builder.<DimensionalTridentEnhancedEntity>of(DimensionalTridentEnhancedEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("dimensional_trident_enhanced_type");
		TRIDENT_TYPE_ENHANCED.setRegistryName(DimensionalPockets.MOD_ID, "dimensional_trident_enhanced_type");
		
		event.getRegistry().registerAll(TRIDENT_TYPE, TRIDENT_TYPE_ENHANCED);
		
		DimensionalPockets.CONSOLE.startup("EntityType<> Registration complete.");
	}
	
	@SubscribeEvent
	public static void onEntityRenderersAddLayersEvent(final EntityRenderersEvent.AddLayers event) {
		EntityModelSet modelSet = event.getEntityModels();
		
		LivingEntityRenderer<Player, PlayerModel<Player>> playerRendererAlt = event.getSkin("default");
		LivingEntityRenderer<ArmorStand, ArmorStandArmorModel> armorRenderer = event.getRenderer(EntityType.ARMOR_STAND);
		
		if (playerRendererAlt != null) {
			playerRendererAlt.addLayer(new CosmosLayerElytra<>(playerRendererAlt, modelSet, new ResourceLocation(DimensionalPockets.MOD_ID, "textures/entity/dimensional_elytra_base.png")));
			playerRendererAlt.addLayer(new CosmosLayerArmourColourable<>(playerRendererAlt, new HumanoidModel<>(modelSet.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel<>(modelSet.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR))));
		} else {
			DimensionalPockets.CONSOLE.fatal("Player Renderer <null>!! Report this issue to the Mod Author");
		}
		
		if (armorRenderer != null) {
			armorRenderer.addLayer(new CosmosLayerElytra<>(armorRenderer, modelSet, new ResourceLocation(DimensionalPockets.MOD_ID, "textures/entity/dimensional_elytra_base.png")));
			armorRenderer.addLayer(new CosmosLayerArmourColourable<>(armorRenderer, new HumanoidModel<>(modelSet.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel<>(modelSet.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR))));
		} else {
			DimensionalPockets.CONSOLE.fatal("ArmorStand Renderer <null>!! Report this issue to the Mod Author");
		}
		
		DimensionalPockets.CONSOLE.startup("EntityRenderer Layer Registration complete.");
	}
	
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onModelRegistryEvent(ModelRegistryEvent event) {
		ForgeModelBakery.addSpecialModel(new ResourceLocation(DimensionalPockets.MOD_ID, "item/dimensional_elytraplate_base"));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(DimensionalPockets.MOD_ID, "item/dimensional_elytraplate_shifter"));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(DimensionalPockets.MOD_ID, "item/dimensional_elytraplate_connect"));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(DimensionalPockets.MOD_ID, "item/dimensional_elytraplate_visor"));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(DimensionalPockets.MOD_ID, "item/dimensional_elytraplate_solar"));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(DimensionalPockets.MOD_ID, "item/dimensional_elytraplate_battery"));
		
		DimensionalPockets.CONSOLE.startup("Model Registration complete..");
	}
	
	@OnlyIn(Dist.CLIENT)
	public static void registerClient(ModLoadingContext context) {
		context.registerExtensionPoint(ConfigGuiFactory.class, () -> ScreenConfiguration.getInstance());
	}
	
	@SuppressWarnings("unused")
	@OnlyIn(Dist.CLIENT)
	public static void onFMLClientSetup(FMLClientSetupEvent event) {
		RenderType cutoutMipped = RenderType.cutoutMipped();
		RenderType translucent = RenderType.translucent();
		RenderType translucentMoving = RenderType.translucentMovingBlock();
		
		MenuScreens.register(POCKET_CONTAINER_TYPE, ScreenPocket::new);
		MenuScreens.register(CONNECTOR_CONTAINER_TYPE, ScreenModuleConnector::new);
		MenuScreens.register(CHARGER_CONTAINER_TYPE, ScreenModuleCharger::new);
		MenuScreens.register(CRAFTER_CONTAINER_TYPE, ScreenModuleCrafter::new);
		MenuScreens.register(FURNACE_CONTAINER_TYPE, ScreenModuleFurnace::new);
		MenuScreens.register(ARMOUR_WORKBENCH_CONTAINER_TYPE, ScreenModuleArmourWorkbench::new);
		MenuScreens.register(UPGRADE_STATION_CONTAINER_TYPE, ScreenModuleUpgradeStation::new);
		MenuScreens.register(GENERATOR_CONTAINER_TYPE, ScreenModuleGenerator::new);
		MenuScreens.register(ELYTRAPLATE_CONTAINER_TYPE, ScreenElytraplate::new);
		MenuScreens.register(FOCUS_CONTAINER_TYPE, ScreenFocus::new);
		
		SUIT_SCREEN = new KeyMapping("dimensionalpocketsii.keybind.suit_screen", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_BRACKET, "dimensionalpocketsii.keybind.category");
		SUIT_SHIFT = new KeyMapping("dimensionalpocketsii.keybind.suit_shift", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_BRACKET, "dimensionalpocketsii.keybind.category");
		SUIT_SETTINGS = new KeyMapping("dimensionalpocketsii.keybind.suit_mode_change", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_APOSTROPHE, "dimensionalpocketsii.keybind.category");
		
		CosmosRuntimeHelper.registerKeyBindings(SUIT_SCREEN, SUIT_SHIFT, SUIT_SETTINGS);
		
		CosmosRuntimeHelper.setRenderLayers(cutoutMipped,
			BLOCK_POCKET, 
			
			BLOCK_WALL_CHARGER, BLOCK_WALL_CONNECTOR, BLOCK_WALL_CRAFTER, BLOCK_WALL_FURNACE, 
			BLOCK_WALL_ENERGY_DISPLAY, BLOCK_WALL_FLUID_DISPLAY, BLOCK_WALL_ARMOUR_WORKBENCH, BLOCK_WALL_UPGRADE_STATION, BLOCK_WALL_GENERATOR,
			BLOCK_DIMENSIONAL_CORE, BLOCK_FOCUS
		);
		
		CosmosRuntimeHelper.registerBlockColours(new ColourBlockPocket(), BLOCK_POCKET);
		
		CosmosRuntimeHelper.registerBlockColours(new ColourBlockWall(), 
			BLOCK_WALL, BLOCK_WALL_EDGE, BLOCK_FOCUS,
			
			BLOCK_WALL_CONNECTOR, BLOCK_WALL_CHARGER, BLOCK_WALL_CRAFTER, BLOCK_WALL_FURNACE, BLOCK_WALL_ENERGY_DISPLAY, BLOCK_WALL_FLUID_DISPLAY, BLOCK_WALL_ARMOUR_WORKBENCH, BLOCK_WALL_UPGRADE_STATION, BLOCK_WALL_GENERATOR
		);
		
		CosmosRuntimeHelper.registerItemColours(new ColourItem(),
			BLOCK_ITEM_POCKET, BLOCK_DIMENSIONAL_CORE, BLOCK_FOCUS, BLOCK_WALL, BLOCK_WALL_EDGE,
			
			DIMENSIONAL_DEVICE_BASE, DIMENSIONAL_SHIFTER, DIMENSIONAL_SHIFTER_ENHANCED, DIMENSIONAL_EJECTOR,
			DIMENSIONAL_ENERGY_CELL, DIMENSIONAL_ENERGY_CELL_ENHANCED,
			
			DIMENSIONAL_HELMET, DIMENSIONAL_CHESTPLATE, DIMENSIONAL_LEGGINGS, DIMENSIONAL_BOOTS,
			DIMENSIONAL_HELMET_ENHANCED, DIMENSIONAL_CHESTPLATE_ENHANCED, DIMENSIONAL_LEGGINGS_ENHANCED, DIMENSIONAL_BOOTS_ENHANCED,
			
			DIMENSIONAL_ELYTRAPLATE,
			
			BLOCK_WALL_CONNECTOR, BLOCK_WALL_CHARGER, BLOCK_WALL_CRAFTER, BLOCK_WALL_FURNACE, BLOCK_WALL_ENERGY_DISPLAY, BLOCK_WALL_FLUID_DISPLAY, BLOCK_WALL_ARMOUR_WORKBENCH, BLOCK_WALL_UPGRADE_STATION, BLOCK_WALL_GENERATOR
		);

		//ItemModelsProperties.register(DIMENSIONAL_SHIELD, new ResourceLocation("blocking"), (stackIn, clientWorldIn, livingEntityIn) -> { return livingEntityIn != null && livingEntityIn.isUsingItem() && livingEntityIn.getUseItem() == stackIn ? 1.0F : 0.0F; });
		ItemProperties.register(DIMENSIONAL_TRIDENT, new ResourceLocation("throwing"), (stackIn, clientWorldIn, livingEntityIn, o) -> { return livingEntityIn != null && livingEntityIn.isUsingItem() && livingEntityIn.getUseItem() == stackIn ? 1.0F : 0.0F; });
		ItemProperties.register(DIMENSIONAL_TRIDENT_ENHANCED, new ResourceLocation("throwing"), (stackIn, clientWorldIn, livingEntityIn, o) -> { return livingEntityIn != null && livingEntityIn.isUsingItem() && livingEntityIn.getUseItem() == stackIn ? 1.0F : 0.0F; });
		
		ItemProperties.register(DIMENSIONAL_BOW, new ResourceLocation("pull"), (stackIn, clientWorldIn, livingEntityIn, o) -> { if (livingEntityIn == null) { return 0.0F; } else { return livingEntityIn.getUseItem() != stackIn ? 0.0F : (float) (stackIn.getUseDuration() - livingEntityIn.getUseItemRemainingTicks()) / 20.0F; }});
		ItemProperties.register(DIMENSIONAL_BOW, new ResourceLocation("pulling"), (stackIn, clientWorldIn, livingEntityIn, o) -> { return livingEntityIn != null && livingEntityIn.isUsingItem() && livingEntityIn.getUseItem() == stackIn ? 1.0F : 0.0F; });

		//ItemProperties.register(DIMENSIONAL_BOW_ENHANCED, new ResourceLocation("pull"), (stackIn, clientWorldIn, livingEntityIn, o) -> { if (livingEntityIn == null) { return 0.0F; } else { return livingEntityIn.getUseItem() != stackIn ? 0.0F : (float) (stackIn.getUseDuration() - livingEntityIn.getUseItemRemainingTicks()) / 20.0F; }});
		//ItemProperties.register(DIMENSIONAL_BOW_ENHANCED, new ResourceLocation("pulling"), (stackIn, clientWorldIn, livingEntityIn, o) -> { return livingEntityIn != null && livingEntityIn.isUsingItem() && livingEntityIn.getUseItem() == stackIn ? 1.0F : 0.0F; });
		
		EntityRenderers.register(TRIDENT_TYPE, RendererDimensionalTrident::new);
		EntityRenderers.register(TRIDENT_TYPE_ENHANCED, RendererDimensionalTridentEnhanced::new);
	}
}