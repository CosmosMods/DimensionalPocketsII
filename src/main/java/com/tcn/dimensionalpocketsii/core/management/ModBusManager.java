package com.tcn.dimensionalpocketsii.core.management;

import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;
import com.tcn.cosmoslibrary.client.entity.layer.CosmosLayerArmourColourable;
import com.tcn.cosmoslibrary.client.entity.layer.CosmosLayerElytra;
import com.tcn.cosmoslibrary.common.block.CosmosBlock;
import com.tcn.cosmoslibrary.common.block.CosmosBlockModelUnplaceable;
import com.tcn.cosmoslibrary.common.item.CosmosItem;
import com.tcn.cosmoslibrary.common.item.CosmosItemEffect;
import com.tcn.cosmoslibrary.common.item.CosmosItemTool;
import com.tcn.cosmoslibrary.common.runtime.CosmosRuntimeHelper;
import com.tcn.cosmoslibrary.common.tab.CosmosCreativeModeTab;
import com.tcn.cosmoslibrary.energy.item.CosmosEnergyArmourItemColourable;
import com.tcn.cosmoslibrary.energy.item.CosmosEnergyItem;
import com.tcn.dimensionalpocketsii.DimReference;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.client.container.ContainerElytraplateConnector;
import com.tcn.dimensionalpocketsii.client.container.ContainerElytraplateEnderChest;
import com.tcn.dimensionalpocketsii.client.container.ContainerElytraplateSettings;
import com.tcn.dimensionalpocketsii.client.screen.ScreenConfiguration;
import com.tcn.dimensionalpocketsii.core.entity.DimensionalTridentEnhancedEntity;
import com.tcn.dimensionalpocketsii.core.entity.DimensionalTridentEntity;
import com.tcn.dimensionalpocketsii.core.item.CoreArmourMaterial;
import com.tcn.dimensionalpocketsii.core.item.CoreItemTier;
import com.tcn.dimensionalpocketsii.core.item.DimensionalTome;
import com.tcn.dimensionalpocketsii.core.item.armour.DimensionalElytraplate;
import com.tcn.dimensionalpocketsii.core.item.armour.module.ItemModuleBattery;
import com.tcn.dimensionalpocketsii.core.item.armour.module.ItemModuleEnderChest;
import com.tcn.dimensionalpocketsii.core.item.armour.module.ItemModuleScreen;
import com.tcn.dimensionalpocketsii.core.item.armour.module.ItemModuleShifter;
import com.tcn.dimensionalpocketsii.core.item.armour.module.ItemModuleSolar;
import com.tcn.dimensionalpocketsii.core.item.armour.module.ItemModuleVisor;
import com.tcn.dimensionalpocketsii.core.item.device.DimensionalEjector;
import com.tcn.dimensionalpocketsii.core.item.device.DimensionalEnergyCell;
import com.tcn.dimensionalpocketsii.core.item.device.DimensionalEnergyCellEnhanced;
import com.tcn.dimensionalpocketsii.core.item.device.DimensionalShifter;
import com.tcn.dimensionalpocketsii.core.item.device.DimensionalShifterEnhanced;
import com.tcn.dimensionalpocketsii.core.item.tool.DimensionalAxe;
import com.tcn.dimensionalpocketsii.core.item.tool.DimensionalBow;
import com.tcn.dimensionalpocketsii.core.item.tool.DimensionalHoe;
import com.tcn.dimensionalpocketsii.core.item.tool.DimensionalPickaxe;
import com.tcn.dimensionalpocketsii.core.item.tool.DimensionalShield;
import com.tcn.dimensionalpocketsii.core.item.tool.DimensionalShieldEnhanced;
import com.tcn.dimensionalpocketsii.core.item.tool.DimensionalShovel;
import com.tcn.dimensionalpocketsii.core.item.tool.DimensionalSword;
import com.tcn.dimensionalpocketsii.core.item.tool.DimensionalTrident;
import com.tcn.dimensionalpocketsii.core.item.tool.DimensionalTridentEnhanced;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerFocus;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerModuleArmourWorkbench;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerModuleBlastFurnace;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerModuleCharger;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerModuleConnector;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerModuleCrafter;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerModuleFurnace;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerModuleGenerator;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerModuleSmithingTable;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerModuleUpgradeStation;
import com.tcn.dimensionalpocketsii.pocket.client.container.ContainerPocket;
import com.tcn.dimensionalpocketsii.pocket.client.renderer.ter.RendererBlockEntityModuleCreativeFluid;
import com.tcn.dimensionalpocketsii.pocket.client.renderer.ter.RendererBlockEntityModuleFluidDisplay;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockFocus;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockPocket;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallArmourWorkbench;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallBase;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallBlastFurnace;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallCharger;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallConnector;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallCrafter;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallDoor;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallEdge;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallEnergyDisplay;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallFluidDisplay;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallFurnace;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallGenerator;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallSmithingTable;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallUpgradeStation;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallZCreativeEnergy;
import com.tcn.dimensionalpocketsii.pocket.core.block.BlockWallZCreativeFluid;
import com.tcn.dimensionalpocketsii.pocket.core.block.ItemBlockFocus;
import com.tcn.dimensionalpocketsii.pocket.core.block.ItemBlockPocket;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityFocus;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleArmourWorkbench;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleBlastFurnace;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleCharger;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleConnector;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleCrafter;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleFluidDisplay;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleFurnace;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleGenerator;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleSmithingTable;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityModuleUpgradeStation;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityPocket;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityZModuleCreativeEnergy;
import com.tcn.dimensionalpocketsii.pocket.core.blockentity.BlockEntityZModuleCreativeFluid;
import com.tcn.dimensionalpocketsii.pocket.core.item.module.ModuleArmourWorkbench;
import com.tcn.dimensionalpocketsii.pocket.core.item.module.ModuleBase;
import com.tcn.dimensionalpocketsii.pocket.core.item.module.ModuleBlastFurnace;
import com.tcn.dimensionalpocketsii.pocket.core.item.module.ModuleCharger;
import com.tcn.dimensionalpocketsii.pocket.core.item.module.ModuleConnector;
import com.tcn.dimensionalpocketsii.pocket.core.item.module.ModuleCrafter;
import com.tcn.dimensionalpocketsii.pocket.core.item.module.ModuleEnergyDisplay;
import com.tcn.dimensionalpocketsii.pocket.core.item.module.ModuleFluidDisplay;
import com.tcn.dimensionalpocketsii.pocket.core.item.module.ModuleFocus;
import com.tcn.dimensionalpocketsii.pocket.core.item.module.ModuleFurnace;
import com.tcn.dimensionalpocketsii.pocket.core.item.module.ModuleGenerator;
import com.tcn.dimensionalpocketsii.pocket.core.item.module.ModuleSmithingTable;
import com.tcn.dimensionalpocketsii.pocket.core.item.module.ModuleUpgradeStation;
import com.tcn.dimensionalpocketsii.pocket.core.item.module.ModuleZCreativeEnergy;
import com.tcn.dimensionalpocketsii.pocket.core.item.module.ModuleZCreativeFluid;

import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
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
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = DimensionalPockets.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModBusManager {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DimensionalPockets.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DimensionalPockets.MOD_ID);
	
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, DimensionalPockets.MOD_ID);
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, DimensionalPockets.MOD_ID);
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, DimensionalPockets.MOD_ID);
	
	
	
	private static final Rarity RARITYPOCKET = Rarity.create("Pocket", ChatFormatting.DARK_PURPLE);
	private static final Rarity RARITY_ARMOUR = Rarity.create("Armour Module", ChatFormatting.GOLD);
	private static final Rarity RARITY_POCKET = Rarity.create("Pocket Module", ChatFormatting.AQUA);
	private static final Rarity RARITY_CREATIVE = Rarity.create("Creative", ChatFormatting.LIGHT_PURPLE);
	
	public static final CosmosCreativeModeTab DIM_POCKETS_BLOCKS_GROUP = new CosmosCreativeModeTab(DimensionalPockets.MOD_ID + ".blocks", () -> new ItemStack(ObjectManager.block_pocket));
	public static final CosmosCreativeModeTab DIM_POCKETS_ITEMS_GROUP = new CosmosCreativeModeTab(DimensionalPockets.MOD_ID + ".items", () -> new ItemStack(ObjectManager.dimensional_ingot));
	public static final CosmosCreativeModeTab DIM_POCKETS_TOOLS_GROUP = new CosmosCreativeModeTab(DimensionalPockets.MOD_ID + ".tools", () -> new ItemStack(ObjectManager.dimensional_sword));

	private static final RegistryObject<Item> DIMENSIONAL_TOME  = ITEMS.register("dimensional_tome", () -> new  DimensionalTome(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(1).rarity(RARITYPOCKET)));
	
	private static final RegistryObject<Item> DIMENSIONAL_SHARD  = ITEMS.register("dimensional_shard", () -> new  CosmosItem(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).rarity(RARITYPOCKET)));
	private static final RegistryObject<Item> DIMENSIONAL_INGOT  = ITEMS.register("dimensional_ingot", () -> new  CosmosItem(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).rarity(RARITYPOCKET)));
	private static final RegistryObject<Item> DIMENSIONAL_INGOT_ENHANCED  = ITEMS.register("dimensional_ingot_enhanced", () -> new  CosmosItemEffect(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(1).rarity(Rarity.RARE).fireResistant()));
	private static final RegistryObject<Item> DIMENSIONAL_GEM  = ITEMS.register("dimensional_gem", () -> new  CosmosItem(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).rarity(RARITYPOCKET)));
	
	private static final RegistryObject<Item> DIMENSIONAL_DUST  = ITEMS.register("dimensional_dust", () -> new  CosmosItem(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).rarity(RARITYPOCKET)));
	private static final RegistryObject<Item> DIMENSIONAL_PEARL  = ITEMS.register("dimensional_pearl", () -> new  CosmosItem(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(16).rarity(RARITYPOCKET)));
	private static final RegistryObject<Item> DIMENSIONAL_THREAD  = ITEMS.register("dimensional_thread", () -> new  CosmosItem(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).rarity(RARITYPOCKET)));
	
	private static final RegistryObject<Item> NETHER_STAR_SHARD  = ITEMS.register("nether_star_shard", () -> new  CosmosItemEffect(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(16).rarity(Rarity.RARE).fireResistant()));
	private static final RegistryObject<Item> ELYTRA_WING  = ITEMS.register("elytra_wing", () -> new  CosmosItem(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(2).rarity(Rarity.RARE)));
	
	private static final RegistryObject<Item> DIMENSIONAL_WRENCH  = ITEMS.register("dimensional_wrench", () -> new  CosmosItemTool(new Item.Properties().stacksTo(1).tab(DIM_POCKETS_TOOLS_GROUP)));
	
	private static final RegistryObject<Item> DIMENSIONAL_DEVICE_BASE  = ITEMS.register("dimensional_device_base", () -> new  CosmosItem(new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().stacksTo(16)));
	private static final RegistryObject<Item> DIMENSIONAL_EJECTOR  = ITEMS.register("dimensional_ejector", () -> new  DimensionalEjector(new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).stacksTo(4)));
	
	private static final RegistryObject<Item> DIMENSIONAL_SHIFTER  = ITEMS.register("dimensional_shifter", () -> new  DimensionalShifter(new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().stacksTo(1).rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().maxEnergyStored(5000000).maxIO(50000).maxUse(100000)));
	private static final RegistryObject<Item> DIMENSIONAL_SHIFTER_ENHANCED  = ITEMS.register("dimensional_shifter_enhanced", () -> new  DimensionalShifterEnhanced(new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().stacksTo(1).rarity(Rarity.RARE), new CosmosEnergyItem.Properties().maxEnergyStored(10000000).maxIO(100000).maxUse(50000)));
	
	private static final RegistryObject<Item> DIMENSIONAL_ENERGY_CELL  = ITEMS.register("dimensional_energy_cell", () -> new  DimensionalEnergyCell(new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().stacksTo(1).rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().maxEnergyStored(10000000).maxIO(100000)));
	private static final RegistryObject<Item> DIMENSIONAL_ENERGY_CELL_ENHANCED  = ITEMS.register("dimensional_energy_cell_enhanced", () -> new  DimensionalEnergyCellEnhanced(new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().stacksTo(1).rarity(Rarity.RARE), new CosmosEnergyItem.Properties().maxEnergyStored(50000000).maxIO(200000)));
	
	private static final RegistryObject<Item> DIMENSIONAL_SWORD  = ITEMS.register("dimensional_sword", () -> new  DimensionalSword(CoreItemTier.DIMENSIONAL, 5, -1.0F, false, new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().setStatsFromArray(DimReference.CONSTANT.ENERGY)));
	private static final RegistryObject<Item> DIMENSIONAL_PICKAXE  = ITEMS.register("dimensional_pickaxe", () -> new  DimensionalPickaxe(CoreItemTier.DIMENSIONAL, 2, -2.0F, false, new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().setStatsFromArray(DimReference.CONSTANT.ENERGY)));
	private static final RegistryObject<Item> DIMENSIONAL_AXE  = ITEMS.register("dimensional_axe", () -> new  DimensionalAxe(CoreItemTier.DIMENSIONAL, 7, -1.5F, false, new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().setStatsFromArray(DimReference.CONSTANT.ENERGY)));
	private static final RegistryObject<Item> DIMENSIONAL_SHOVEL  = ITEMS.register("dimensional_shovel", () -> new  DimensionalShovel(CoreItemTier.DIMENSIONAL,  1, -2.5F, false, new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().setStatsFromArray(DimReference.CONSTANT.ENERGY)));
	private static final RegistryObject<Item> DIMENSIONAL_HOE  = ITEMS.register("dimensional_hoe", () -> new  DimensionalHoe(CoreItemTier.DIMENSIONAL, 1, -2.5F, false, new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().setStatsFromArray(DimReference.CONSTANT.ENERGY)));
	
	private static final RegistryObject<Item> DIMENSIONAL_BOW  = ITEMS.register("dimensional_bow", () -> new  DimensionalBow(new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().maxEnergyStored(2000000).maxIO(100000).maxUse(10000), 64, 2, 1.5F, 1.4F));
	private static final RegistryObject<Item> DIMENSIONAL_TRIDENT  = ITEMS.register("dimensional_trident", () -> new  DimensionalTrident(new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().setStatsFromArray(DimReference.CONSTANT.ENERGY)));
	private static final RegistryObject<Item> DIMENSIONAL_SHIELD  = ITEMS.register("dimensional_shield", () -> new  DimensionalShield(new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().stacksTo(1).rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().setStatsFromArray(DimReference.CONSTANT.ENERGY)));
	
	private static final RegistryObject<Item> DIMENSIONAL_HELMET  = ITEMS.register("dimensional_helmet", () -> new  CosmosEnergyArmourItemColourable(CoreArmourMaterial.DIMENSIONAL, EquipmentSlot.HEAD, true, (new Item.Properties()).tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().maxEnergyStored(2000000).maxIO(100000).maxUse(10000)));
	private static final RegistryObject<Item> DIMENSIONAL_CHESTPLATE  = ITEMS.register("dimensional_chestplate", () -> new  CosmosEnergyArmourItemColourable(CoreArmourMaterial.DIMENSIONAL, EquipmentSlot.CHEST, false, (new Item.Properties()).tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().maxEnergyStored(2000000).maxIO(100000).maxUse(12000)));
	private static final RegistryObject<Item> DIMENSIONAL_LEGGINGS  = ITEMS.register("dimensional_leggings", () -> new  CosmosEnergyArmourItemColourable(CoreArmourMaterial.DIMENSIONAL, EquipmentSlot.LEGS, false, (new Item.Properties()).tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().maxEnergyStored(2000000).maxIO(100000).maxUse(10000)));
	private static final RegistryObject<Item> DIMENSIONAL_BOOTS  = ITEMS.register("dimensional_boots", () -> new  CosmosEnergyArmourItemColourable(CoreArmourMaterial.DIMENSIONAL, EquipmentSlot.FEET, false, (new Item.Properties()).tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(RARITYPOCKET), new CosmosEnergyItem.Properties().maxEnergyStored(2000000).maxIO(100000).maxUse(8000)));

	private static final RegistryObject<Item> DIMENSIONAL_SWORD_ENHANCED  = ITEMS.register("dimensional_sword_enhanced", () -> new  DimensionalSword(CoreItemTier.DIMENSIONAL_ENHANCED, 5, 0.0F, true, new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(Rarity.RARE), new CosmosEnergyItem.Properties().setStatsFromArray(DimReference.CONSTANT.ENERGY_ENHANCED)));
	private static final RegistryObject<Item> DIMENSIONAL_PICKAXE_ENHANCED  = ITEMS.register("dimensional_pickaxe_enhanced", () -> new  DimensionalPickaxe(CoreItemTier.DIMENSIONAL_ENHANCED, 2, -2.0F, true, new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(Rarity.RARE), new CosmosEnergyItem.Properties().setStatsFromArray(DimReference.CONSTANT.ENERGY_ENHANCED)));
	private static final RegistryObject<Item> DIMENSIONAL_AXE_ENHANCED  = ITEMS.register("dimensional_axe_enhanced", () -> new  DimensionalAxe(CoreItemTier.DIMENSIONAL_ENHANCED, 7, -1.5F, true, new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(Rarity.RARE), new CosmosEnergyItem.Properties().setStatsFromArray(DimReference.CONSTANT.ENERGY_ENHANCED)));
	private static final RegistryObject<Item> DIMENSIONAL_SHOVEL_ENHANCED  = ITEMS.register("dimensional_shovel_enhanced", () -> new  DimensionalShovel(CoreItemTier.DIMENSIONAL_ENHANCED, 1, -2.5F, true, new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(Rarity.RARE), new CosmosEnergyItem.Properties().setStatsFromArray(DimReference.CONSTANT.ENERGY_ENHANCED)));
	private static final RegistryObject<Item> DIMENSIONAL_HOE_ENHANCED  = ITEMS.register("dimensional_hoe_enhanced", () -> new  DimensionalHoe(CoreItemTier.DIMENSIONAL_ENHANCED, 1, -2.5F, true, new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(Rarity.RARE), new CosmosEnergyItem.Properties().setStatsFromArray(DimReference.CONSTANT.ENERGY_ENHANCED)));

	private static final RegistryObject<Item> DIMENSIONAL_BOW_ENHANCED  = ITEMS.register("dimensional_bow_enhanced", () -> new  DimensionalBow(new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(Rarity.RARE), new CosmosEnergyItem.Properties().maxEnergyStored(4000000).maxIO(200000).maxUse(20000), 64, 2, 2.25F, 2.0F));
	private static final RegistryObject<Item> DIMENSIONAL_TRIDENT_ENHANCED  = ITEMS.register("dimensional_trident_enhanced", () -> new  DimensionalTridentEnhanced(new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(Rarity.RARE), new CosmosEnergyItem.Properties().setStatsFromArray(DimReference.CONSTANT.ENERGY_ENHANCED)));
	private static final RegistryObject<Item> DIMENSIONAL_SHIELD_ENHANCED  = ITEMS.register("dimensional_shield_enhanced", () -> new  DimensionalShieldEnhanced(new Item.Properties().tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().stacksTo(1).rarity(Rarity.RARE), new CosmosEnergyItem.Properties().setStatsFromArray(DimReference.CONSTANT.ENERGY_ENHANCED)));
	
	private static final RegistryObject<Item> DIMENSIONAL_HELMET_ENHANCED  = ITEMS.register("dimensional_helmet_enhanced", () -> new  CosmosEnergyArmourItemColourable(CoreArmourMaterial.DIMENSIONAL_ENHANCED, EquipmentSlot.HEAD, true, (new Item.Properties()).tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(Rarity.RARE), new CosmosEnergyItem.Properties().maxEnergyStored(6000000).maxIO(100000).maxUse(10000)));
	private static final RegistryObject<Item> DIMENSIONAL_CHESTPLATE_ENHANCED  = ITEMS.register("dimensional_chestplate_enhanced", () -> new  CosmosEnergyArmourItemColourable(CoreArmourMaterial.DIMENSIONAL_ENHANCED, EquipmentSlot.CHEST, false, (new Item.Properties()).tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(Rarity.RARE), new CosmosEnergyItem.Properties().maxEnergyStored(6000000).maxIO(100000).maxUse(12000)));
	private static final RegistryObject<Item> DIMENSIONAL_LEGGINGS_ENHANCED  = ITEMS.register("dimensional_leggings_enhanced", () -> new  CosmosEnergyArmourItemColourable(CoreArmourMaterial.DIMENSIONAL_ENHANCED, EquipmentSlot.LEGS, false, (new Item.Properties()).tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(Rarity.RARE), new CosmosEnergyItem.Properties().maxEnergyStored(6000000).maxIO(100000).maxUse(10000)));
	private static final RegistryObject<Item> DIMENSIONAL_BOOTS_ENHANCED  = ITEMS.register("dimensional_boots_enhanced", () -> new  CosmosEnergyArmourItemColourable(CoreArmourMaterial.DIMENSIONAL_ENHANCED, EquipmentSlot.FEET, false, (new Item.Properties()).tab(DIM_POCKETS_TOOLS_GROUP).fireResistant().rarity(Rarity.RARE), new CosmosEnergyItem.Properties().maxEnergyStored(6000000).maxIO(100000).maxUse(8000)));
	
	private static final RegistryObject<Item> DIMENSIONAL_ELYTRAPLATE  = ITEMS.register("dimensional_elytraplate", () -> new  DimensionalElytraplate(CoreArmourMaterial.DIMENSIONAL_ENHANCED, EquipmentSlot.CHEST, (new Item.Properties()).tab(DIM_POCKETS_TOOLS_GROUP).rarity(Rarity.RARE).fireResistant(), false, new CosmosEnergyItem.Properties().maxEnergyStored(10000000).maxIO(200000).maxUse(6000)));
	private static final RegistryObject<MenuType<?>> CONTAINER_TYPE_ELYTRAPLATE_CONNECTOR = MENU_TYPES.register("container_elytraplate", () -> IForgeMenuType.create(ContainerElytraplateConnector::createContainerClientSide));
	private static final RegistryObject<MenuType<?>> CONTAINER_TYPE_ELYTRAPLATE_SETTINGS = MENU_TYPES.register("container_elytraplate_settings", () -> IForgeMenuType.create(ContainerElytraplateSettings::createContainerClientSide));
	private static final RegistryObject<MenuType<?>> CONTAINER_TYPE_ELYTRAPLATE_ENDER_CHEST = MENU_TYPES.register("container_elytraplate_ender_chest", () -> IForgeMenuType.create(ContainerElytraplateEnderChest::createContainerClientSide));
	
	private static final RegistryObject<Item> ARMOUR_MODULE_SCREEN  = ITEMS.register("armour_module_screen", () -> new  ItemModuleScreen(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(1).rarity(RARITY_ARMOUR)));
	private static final RegistryObject<Item> ARMOUR_MODULE_SHIFTER  = ITEMS.register("armour_module_shifter", () -> new  ItemModuleShifter(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(1).rarity(RARITY_ARMOUR)));
	private static final RegistryObject<Item> ARMOUR_MODULE_VISOR  = ITEMS.register("armour_module_visor", () -> new  ItemModuleVisor(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(1).rarity(RARITY_ARMOUR)));
	private static final RegistryObject<Item> ARMOUR_MODULE_SOLAR  = ITEMS.register("armour_module_solar", () -> new  ItemModuleSolar(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(1).rarity(RARITY_ARMOUR)));
	private static final RegistryObject<Item> ARMOUR_MODULE_BATTERY  = ITEMS.register("armour_module_battery", () -> new  ItemModuleBattery(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(1).rarity(RARITY_ARMOUR)));
	private static final RegistryObject<Item> ARMOUR_MODULE_ENDER_CHEST  = ITEMS.register("armour_module_ender_chest", () -> new  ItemModuleEnderChest(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(1).rarity(RARITY_ARMOUR)));
	
	private static final RegistryObject<Item> MODULE_BASE  = ITEMS.register("module_base", () -> new  ModuleBase(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(8).rarity(RARITY_POCKET)));
	private static final RegistryObject<Item> MODULE_CONNECTOR  = ITEMS.register("module_connector", () -> new  ModuleConnector(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(8).rarity(RARITY_POCKET)));
	private static final RegistryObject<Item> MODULE_CHARGER  = ITEMS.register("module_charger", () -> new  ModuleCharger(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(8).rarity(RARITY_POCKET)));
	private static final RegistryObject<Item> MODULE_CRAFTER  = ITEMS.register("module_crafter", () -> new  ModuleCrafter(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(8).rarity(RARITY_POCKET)));
	private static final RegistryObject<Item> MODULE_SMITHING_TABLE  = ITEMS.register("module_smithing_table", () -> new  ModuleSmithingTable(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(8).rarity(RARITY_POCKET)));
	private static final RegistryObject<Item> MODULE_FURNACE  = ITEMS.register("module_furnace", () -> new  ModuleFurnace(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(8).rarity(RARITY_POCKET)));
	private static final RegistryObject<Item> MODULE_BLAST_FURNACE  = ITEMS.register("module_blast_furnace", () -> new  ModuleBlastFurnace(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(8).rarity(RARITY_POCKET)));
	private static final RegistryObject<Item> MODULE_ENERGY_DISPLAY  = ITEMS.register("module_energy_display", () -> new  ModuleEnergyDisplay(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(8).rarity(RARITY_POCKET)));
	private static final RegistryObject<Item> MODULE_FLUID_DISPLAY  = ITEMS.register("module_fluid_display", () -> new  ModuleFluidDisplay(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(8).rarity(RARITY_POCKET)));
	private static final RegistryObject<Item> MODULE_ARMOUR_WORKBENCH  = ITEMS.register("module_armour_workbench", () -> new  ModuleArmourWorkbench(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(8).rarity(RARITY_POCKET)));
	private static final RegistryObject<Item> MODULE_GENERATOR  = ITEMS.register("module_generator", () -> new  ModuleGenerator(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(8).rarity(RARITY_POCKET)));
	private static final RegistryObject<Item> MODULE_UPGRADE_STATION  = ITEMS.register("module_upgrade_station", () -> new  ModuleUpgradeStation(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(8).rarity(RARITY_POCKET)));
	private static final RegistryObject<Item> MODULE_FOCUS  = ITEMS.register("module_focus", () -> new  ModuleFocus(new Item.Properties().tab(DIM_POCKETS_ITEMS_GROUP).stacksTo(8).rarity(RARITY_POCKET)));
	
	private static final RegistryObject<Item> MODULE_CREATIVE_ENERGY  = ITEMS.register("module_creative_energy", () -> new  ModuleZCreativeEnergy(new Item.Properties().tab(DIM_POCKETS_BLOCKS_GROUP).stacksTo(8).rarity(RARITY_CREATIVE)));
	private static final RegistryObject<Item> MODULE_CREATIVE_FLUID  = ITEMS.register("module_creative_fluid", () -> new  ModuleZCreativeFluid(new Item.Properties().tab(DIM_POCKETS_BLOCKS_GROUP).stacksTo(8).rarity(RARITY_CREATIVE)));
	
	private static final RegistryObject<Block> BLOCK_DIMENSIONAL_ORE = BLOCKS.register("block_dimensional_ore", () -> new CosmosBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(4.0F, 4.0F)));
	private static final RegistryObject<Item> ITEM_DIMENSIONAL_ORE = ITEMS.register("block_dimensional_ore", () -> new BlockItem(ObjectManager.block_dimensional_ore, new Item.Properties().tab(DIM_POCKETS_BLOCKS_GROUP)));
	
	private static final RegistryObject<Block> BLOCK_DEEPSLATE_DIMENSIONAL_ORE = BLOCKS.register("block_deepslate_dimensional_ore", () -> new CosmosBlock(Block.Properties.of(Material.STONE, MaterialColor.DEEPSLATE).requiresCorrectToolForDrops().strength(8.0F, 8.0F).sound(SoundType.DEEPSLATE)));
	private static final RegistryObject<Item> ITEM_DEEPSLATE_DIMENSIONAL_ORE = ITEMS.register("block_deepslate_dimensional_ore", () -> new BlockItem(ObjectManager.block_deepslate_dimensional_ore, new Item.Properties().tab(DIM_POCKETS_BLOCKS_GROUP)));
	
	private static final RegistryObject<Block> BLOCK_DIMENSIONAL_ORE_NETHER = BLOCKS.register("block_dimensional_ore_nether", () -> new CosmosBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(6.0F, 8.0F)));
	private static final RegistryObject<Item> ITEM_DIMENSIONAL_ORE_NETHER = ITEMS.register("block_dimensional_ore_nether", () -> new BlockItem(ObjectManager.block_dimensional_ore_nether, new Item.Properties().tab(DIM_POCKETS_BLOCKS_GROUP)));
	
	private static final RegistryObject<Block> BLOCK_DIMENSIONAL_ORE_END = BLOCKS.register("block_dimensional_ore_end", () -> new CosmosBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(8.0F, 8.0F)));
	private static final RegistryObject<Item> ITEM_DIMENSIONAL_ORE_END = ITEMS.register("block_dimensional_ore_end", () -> new BlockItem(ObjectManager.block_dimensional_ore_end, new Item.Properties().tab(DIM_POCKETS_BLOCKS_GROUP)));
	
	private static final RegistryObject<Block> BLOCK_DIMENSIONAL = BLOCKS.register("block_dimensional", () -> new CosmosBlock(Block.Properties.of(Material.HEAVY_METAL).requiresCorrectToolForDrops().strength(6.0F, 8.0F)));
	private static final RegistryObject<Item> ITEM_DIMENSIONAL = ITEMS.register("block_dimensional", () -> new BlockItem(ObjectManager.block_dimensional, new Item.Properties().tab(DIM_POCKETS_BLOCKS_GROUP)));
	
	private static final RegistryObject<Block> BLOCK_DIMENSIONAL_METAL = BLOCKS.register("block_dimensional_metal", () -> new CosmosBlock(Block.Properties.of(Material.HEAVY_METAL).requiresCorrectToolForDrops().strength(6.0F, 8.0F)));
	private static final RegistryObject<Item> ITEM_DIMENSIONAL_METAL = ITEMS.register("block_dimensional_metal", () -> new BlockItem(ObjectManager.block_dimensional_metal, new Item.Properties().tab(DIM_POCKETS_BLOCKS_GROUP)));
	
	private static final RegistryObject<Block> BLOCK_DIMENSIONAL_GEM = BLOCKS.register("block_dimensional_gem", () -> new CosmosBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(6.0F, 8.0F)));
	private static final RegistryObject<Item> ITEM_DIMENSIONAL_GEM = ITEMS.register("block_dimensional_gem", () -> new BlockItem(ObjectManager.block_dimensional_gem, new Item.Properties().tab(DIM_POCKETS_BLOCKS_GROUP)));
	
	private static final RegistryObject<Block> BLOCK_DIMENSIONAL_CORE = BLOCKS.register("block_dimensional_core", () -> new CosmosBlockModelUnplaceable(Block.Properties.of(Material.HEAVY_METAL).requiresCorrectToolForDrops().strength(6.0F, 8.0F)));
	private static final RegistryObject<Item> ITEM_DIMENSIONAL_CORE = ITEMS.register("block_dimensional_core", () -> new BlockItem(ObjectManager.block_dimensional_core, new Item.Properties().tab(DIM_POCKETS_BLOCKS_GROUP)));
	
	private static final RegistryObject<Block> BLOCK_WALL = BLOCKS.register("block_wall", () -> new BlockWallBase(Block.Properties.of(Material.HEAVY_METAL).strength(-1,3600000.0F).lightLevel((state) -> { return 15; })));
	private static final RegistryObject<Item> ITEM_WALL = ITEMS.register("block_wall", () -> new BlockItem(ObjectManager.block_wall, new Item.Properties()));
	
	private static final RegistryObject<Block> BLOCK_WALL_EDGE = BLOCKS.register("block_wall_edge", () -> new BlockWallEdge(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; })));
	private static final RegistryObject<Item> ITEM_WALL_EDGE = ITEMS.register("block_wall_edge", () -> new BlockItem(ObjectManager.block_wall_edge, new Item.Properties()));
	
	private static final RegistryObject<Block> BLOCK_WALL_DOOR = BLOCKS.register("block_wall_door", () -> new BlockWallDoor(Block.Properties.of(Material.METAL).strength(-1,3600000.0F).noOcclusion().lightLevel((state) -> { return 15; })));
	private static final RegistryObject<Item> ITEM_WALL_DOOR = ITEMS.register("block_wall_door", () -> new BlockItem(ObjectManager.block_wall_door, new Item.Properties()));
	
	private static final RegistryObject<Block> BLOCK_POCKET = BLOCKS.register("block_pocket", () -> new BlockPocket(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).noOcclusion()));
	private static final RegistryObject<Item> BLOCK_ITEM_POCKET = ITEMS.register("block_pocket", () -> new ItemBlockPocket(ObjectManager.block_pocket, new Item.Properties().stacksTo(1).setNoRepair().tab(DIM_POCKETS_BLOCKS_GROUP).rarity(RARITYPOCKET).fireResistant()));
	private static final RegistryObject<BlockEntityType<?>> BLOCK_ENTITY_TYPE_POCKET = BLOCK_ENTITY_TYPES.register("tile_entity_pocket", () -> BlockEntityType.Builder.<BlockEntityPocket>of(BlockEntityPocket::new, ObjectManager.block_pocket).build(null));
	private static final RegistryObject<MenuType<?>> CONTAINER_TYPE_POCKET = MENU_TYPES.register("container_pocket", () -> IForgeMenuType.create(ContainerPocket::createContainerClientSide));
	
	private static final RegistryObject<Block> BLOCK_WALL_CONNECTOR = BLOCKS.register("block_wall_connector", () -> new BlockWallConnector(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; })));
	private static RegistryObject<BlockEntityType<?>> BLOCK_ENTITY_TYPE_CONNECTOR = BLOCK_ENTITY_TYPES.register("tile_entity_connector", () -> BlockEntityType.Builder.<BlockEntityModuleConnector>of(BlockEntityModuleConnector::new, ObjectManager.block_wall_connector).build(null));
	private static RegistryObject<MenuType<?>> CONTAINER_TYPE_CONNECTOR = MENU_TYPES.register("container_connector", () -> IForgeMenuType.create(ContainerModuleConnector::createContainerClientSide));
	
	private static final RegistryObject<Block> BLOCK_WALL_CHARGER = BLOCKS.register("block_wall_charger", () -> new BlockWallCharger(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; })));
	private static final RegistryObject<Item> ITEM_WALL_CHARGER = ITEMS.register("block_wall_charger", () -> new BlockItem(ObjectManager.block_wall_charger, new Item.Properties()));
	private static RegistryObject<BlockEntityType<?>> BLOCK_ENTITY_TYPE_CHARGER = BLOCK_ENTITY_TYPES.register("tile_entity_charger", () -> BlockEntityType.Builder.<BlockEntityModuleCharger>of(BlockEntityModuleCharger::new, ObjectManager.block_wall_charger).build(null));
	private static RegistryObject<MenuType<?>> CONTAINER_TYPE_CHARGER = MENU_TYPES.register("container_charger", () -> IForgeMenuType.create(ContainerModuleCharger::new));
	
	private static final RegistryObject<Block> BLOCK_WALL_CRAFTER = BLOCKS.register("block_wall_crafter", () -> new BlockWallCrafter(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; })));
	private static final RegistryObject<Item> ITEM_WALL_CRAFTER = ITEMS.register("block_wall_crafter", () -> new BlockItem(ObjectManager.block_wall_crafter, new Item.Properties()));
	private static RegistryObject<BlockEntityType<?>> BLOCK_ENTITY_TYPE_CRAFTER = BLOCK_ENTITY_TYPES.register("tile_entity_crafter", () -> BlockEntityType.Builder.<BlockEntityModuleCrafter>of(BlockEntityModuleCrafter::new, ObjectManager.block_wall_crafter).build(null));
	private static RegistryObject<MenuType<?>> CONTAINER_TYPE_CRAFTER = MENU_TYPES.register("container_crafter", () -> IForgeMenuType.create(ContainerModuleCrafter::new));

	private static final RegistryObject<Block> BLOCK_WALL_SMITHING_TABLE = BLOCKS.register("block_wall_smithing_table", () -> new BlockWallSmithingTable(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; })));
	private static final RegistryObject<Item> ITEM_WALL_SMITHING_TABLE = ITEMS.register("block_wall_smithing_table", () -> new BlockItem(ObjectManager.block_wall_smithing_table, new Item.Properties()));
	private static RegistryObject<BlockEntityType<?>> BLOCK_ENTITY_TYPE_SMITHING_TABLE = BLOCK_ENTITY_TYPES.register("tile_entity_smithing_table", () -> BlockEntityType.Builder.<BlockEntityModuleSmithingTable>of(BlockEntityModuleSmithingTable::new, ObjectManager.block_wall_smithing_table).build(null));
	private static RegistryObject<MenuType<?>> CONTAINER_TYPE_SMITHING_TABLE = MENU_TYPES.register("container_smithing_table", () -> IForgeMenuType.create(ContainerModuleSmithingTable::new));

	private static final RegistryObject<Block> BLOCK_WALL_FURNACE = BLOCKS.register("block_wall_furnace", () -> new BlockWallFurnace(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; })));
	private static final RegistryObject<Item> ITEM_WALL_FURNACE = ITEMS.register("block_wall_furnace", () -> new BlockItem(ObjectManager.block_wall_furnace, new Item.Properties()));
	private static RegistryObject<BlockEntityType<?>> BLOCK_ENTITY_TYPE_FURNACE = BLOCK_ENTITY_TYPES.register("tile_entity_furnace", () -> BlockEntityType.Builder.<BlockEntityModuleFurnace>of(BlockEntityModuleFurnace::new, ObjectManager.block_wall_furnace).build(null));
	private static RegistryObject<MenuType<?>> CONTAINER_TYPE_FURNACE = MENU_TYPES.register("container_furnace", () -> IForgeMenuType.create(ContainerModuleFurnace::new));

	private static final RegistryObject<Block> BLOCK_WALL_BLAST_FURNACE = BLOCKS.register("block_wall_blast_furnace", () -> new BlockWallBlastFurnace(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; })));
	private static final RegistryObject<Item> ITEM_WALL_BLAST_FURNACE = ITEMS.register("block_wall_blast_furnace", () -> new BlockItem(ObjectManager.block_wall_blast_furnace, new Item.Properties()));
	private static RegistryObject<BlockEntityType<?>> BLOCK_ENTITY_TYPE_BLAST_FURNACE = BLOCK_ENTITY_TYPES.register("tile_entity_blast_furnace", () -> BlockEntityType.Builder.<BlockEntityModuleBlastFurnace>of(BlockEntityModuleBlastFurnace::new, ObjectManager.block_wall_blast_furnace).build(null));
	private static RegistryObject<MenuType<?>> CONTAINER_TYPE_BLAST_FURNACE = MENU_TYPES.register("container_blast_furnace", () -> IForgeMenuType.create(ContainerModuleBlastFurnace::new));

	private static final RegistryObject<Block> BLOCK_WALL_ENERGY_DISPLAY = BLOCKS.register("block_wall_energy_display", () -> new BlockWallEnergyDisplay(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; }).randomTicks()));
	private static final RegistryObject<Item> ITEM_WALL_ENERGY_DISPLAY = ITEMS.register("block_wall_energy_display", () -> new BlockItem(ObjectManager.block_wall_energy_display, new Item.Properties()));
	
	private static final RegistryObject<Block> BLOCK_WALL_FLUID_DISPLAY = BLOCKS.register("block_wall_fluid_display", () -> new BlockWallFluidDisplay(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; }).randomTicks().dynamicShape().noOcclusion()));
	private static final RegistryObject<Item> ITEM_WALL_FLUID_DISPLAY = ITEMS.register("block_wall_fluid_display", () -> new BlockItem(ObjectManager.block_wall_fluid_display, new Item.Properties()));
	private static RegistryObject<BlockEntityType<?>> BLOCK_ENTITY_TYPE_FLUID_DISPLAY = BLOCK_ENTITY_TYPES.register("tile_entity_fluid_display", () -> BlockEntityType.Builder.<BlockEntityModuleFluidDisplay>of(BlockEntityModuleFluidDisplay::new, ObjectManager.block_wall_fluid_display).build(null));

	private static final RegistryObject<Block> BLOCK_WALL_ARMOUR_WORKBENCH = BLOCKS.register("block_wall_armour_workbench", () -> new BlockWallArmourWorkbench(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; }).randomTicks()));
	private static final RegistryObject<Item> ITEM_WALL_ARMOUR_WORKBENCH = ITEMS.register("block_wall_armour_workbench", () -> new BlockItem(ObjectManager.block_wall_armour_workbench, new Item.Properties()));
	private static RegistryObject<BlockEntityType<?>> BLOCK_ENTITY_TYPE_ARMOUR_WORKBENCH = BLOCK_ENTITY_TYPES.register("tile_entity_armour_workbench", () -> BlockEntityType.Builder.<BlockEntityModuleArmourWorkbench>of(BlockEntityModuleArmourWorkbench::new, ObjectManager.block_wall_armour_workbench).build(null));
	private static RegistryObject<MenuType<?>> CONTAINER_TYPE_ARMOUR_WORKBENCH = MENU_TYPES.register("container_armour_workbench", () -> IForgeMenuType.create(ContainerModuleArmourWorkbench::new));

	private static final RegistryObject<Block> BLOCK_WALL_UPGRADE_STATION = BLOCKS.register("block_wall_upgrade_station", () -> new BlockWallUpgradeStation(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; }).randomTicks()));
	private static final RegistryObject<Item> ITEM_WALL_UPGRADE_STATION = ITEMS.register("block_wall_upgrade_station", () -> new BlockItem(ObjectManager.block_wall_upgrade_station, new Item.Properties()));
	private static RegistryObject<BlockEntityType<?>> BLOCK_ENTITY_TYPE_UPGRADE_STATION = BLOCK_ENTITY_TYPES.register("tile_entity_upgrade_station", () -> BlockEntityType.Builder.<BlockEntityModuleUpgradeStation>of(BlockEntityModuleUpgradeStation::new, ObjectManager.block_wall_upgrade_station).build(null));
	private static RegistryObject<MenuType<?>> CONTAINER_TYPE_UPGRADE_STATION = MENU_TYPES.register("container_upgrade_station", () -> IForgeMenuType.create(ContainerModuleUpgradeStation::new));

	private static final RegistryObject<Block> BLOCK_WALL_GENERATOR = BLOCKS.register("block_wall_generator", () -> new BlockWallGenerator(Block.Properties.of(Material.HEAVY_METAL).strength(-1, 3600000.0F).lightLevel((state) -> { return 15; })));
	private static final RegistryObject<Item> ITEM_WALL_GENERATOR = ITEMS.register("block_wall_generator", () -> new BlockItem(ObjectManager.block_wall_generator, new Item.Properties()));
	private static RegistryObject<BlockEntityType<?>> BLOCK_ENTITY_TYPE_GENERATOR = BLOCK_ENTITY_TYPES.register("tile_entity_generator", () -> BlockEntityType.Builder.<BlockEntityModuleGenerator>of(BlockEntityModuleGenerator::new, ObjectManager.block_wall_generator).build(null));
	private static RegistryObject<MenuType<?>> CONTAINER_TYPE_GENERATOR = MENU_TYPES.register("container_generator", () -> IForgeMenuType.create(ContainerModuleGenerator::new));

	private static final RegistryObject<Block> BLOCK_FOCUS = BLOCKS.register("block_dimensional_focus", () -> new BlockFocus(Block.Properties.of(Material.HEAVY_METAL).requiresCorrectToolForDrops().strength(-1, 3600000.0F)));
	private static final RegistryObject<Item> BLOCK_ITEM_FOCUS = ITEMS.register("block_dimensional_focus", () -> new ItemBlockFocus(ObjectManager.block_dimensional_focus, new Item.Properties().setNoRepair().tab(DIM_POCKETS_BLOCKS_GROUP).rarity(RARITYPOCKET).fireResistant()));
	private static RegistryObject<BlockEntityType<?>> BLOCK_ENTITY_TYPE_FOCUS = BLOCK_ENTITY_TYPES.register("tile_entity_focus", () -> BlockEntityType.Builder.<BlockEntityFocus>of(BlockEntityFocus::new, ObjectManager.block_dimensional_focus).build(null));
	private static RegistryObject<MenuType<?>> CONTAINER_TYPE_FOCUS = MENU_TYPES.register("container_focus", () -> IForgeMenuType.create(ContainerFocus::new));
	
	private static final RegistryObject<Block> BLOCK_WALL_CREATIVE_ENERGY = BLOCKS.register("block_wall_creative_energy", () -> new BlockWallZCreativeEnergy(Block.Properties.of(Material.HEAVY_METAL).strength(-1,3600000.0F).lightLevel((state) -> { return 15; })));
	private static final RegistryObject<Item> ITEM_WALL_CREATIVE_ENERGY = ITEMS.register("block_wall_creative_energy", () -> new BlockItem(ObjectManager.block_wall_creative_energy, new Item.Properties()));
	private static RegistryObject<BlockEntityType<?>> BLOCK_ENTITY_TYPE_CREATIVE_ENERGY = BLOCK_ENTITY_TYPES.register("tile_entity_creative_energy", () -> BlockEntityType.Builder.<BlockEntityZModuleCreativeEnergy>of(BlockEntityZModuleCreativeEnergy::new, ObjectManager.block_wall_creative_energy).build(null));

	private static final RegistryObject<Block> BLOCK_WALL_CREATIVE_FLUID = BLOCKS.register("block_wall_creative_fluid", () -> new BlockWallZCreativeFluid(Block.Properties.of(Material.HEAVY_METAL).strength(-1,3600000.0F).lightLevel((state) -> { return 15; })));
	private static final RegistryObject<Item> ITEM_WALL_CREATIVE_FLUID = ITEMS.register("block_wall_creative_fluid", () -> new BlockItem(ObjectManager.block_wall_creative_fluid, new Item.Properties()));
	private static RegistryObject<BlockEntityType<?>> BLOCK_ENTITY_TYPE_CREATIVE_FLUID = BLOCK_ENTITY_TYPES.register("tile_entity_creative_fluid", () -> BlockEntityType.Builder.<BlockEntityZModuleCreativeFluid>of(BlockEntityZModuleCreativeFluid::new, ObjectManager.block_wall_creative_fluid).build(null));

	private static RegistryObject<EntityType<?>> ENTITY_TYPE_TRIDENT = ENTITY_TYPES.register("dimensional_trident_type", () -> EntityType.Builder.<DimensionalTridentEntity>of(DimensionalTridentEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("dimensional_trident_type"));
	private static RegistryObject<EntityType<?>> ENTITY_TYPE_TRIDENT_ENHANCED = ENTITY_TYPES.register("dimensional_trident_enhanced_type", () -> EntityType.Builder.<DimensionalTridentEnhancedEntity>of(DimensionalTridentEnhancedEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("dimensional_trident_enhanced_type"));

	public static KeyMapping SUIT_SCREEN;
	public static KeyMapping SUIT_SCREEN_ENDER_CHEST;
	public static KeyMapping SUIT_SHIFT;
	public static KeyMapping SUIT_SETTINGS;
	
	public static void register(IEventBus bus) {
		ITEMS.register(bus);
		BLOCKS.register(bus);
		
		BLOCK_ENTITY_TYPES.register(bus);
		MENU_TYPES.register(bus);
		ENTITY_TYPES.register(bus);
	}
	
	@SubscribeEvent
	public static void onBlockEntityRendererRegistry(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(ObjectManager.tile_entity_fluid_display, RendererBlockEntityModuleFluidDisplay::new);
		event.registerBlockEntityRenderer(ObjectManager.tile_entity_creative_fluid, RendererBlockEntityModuleCreativeFluid::new);
		
		DimensionalPockets.CONSOLE.startup("BlockEntityRenderer Registration complete.");
	}
	
	@SuppressWarnings("unchecked")
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onEntityRenderersAddLayersEvent(final EntityRenderersEvent.AddLayers event) {
		EntityModelSet modelSet = event.getEntityModels();
		Minecraft mc = Minecraft.getInstance();
		
		mc.getEntityRenderDispatcher().renderers.forEach((entityType, entityRenderer) -> {
			if (entityRenderer instanceof LivingEntityRenderer<?, ?> && entityType != EntityType.VEX) {
				ResourceLocation type = entityType.getRegistryName();
				
				LivingEntityRenderer<LivingEntity, ?> renderer = (LivingEntityRenderer<LivingEntity, ?>) entityRenderer;
				
				if (renderer.getModel() instanceof HumanoidModel<?>) {
					LivingEntityRenderer<LivingEntity, HumanoidModel<LivingEntity>> humanRenderer = (LivingEntityRenderer<LivingEntity, HumanoidModel<LivingEntity>>) renderer;
					
					ModelLayerLocation innerArmour = ModelLayers.PLAYER_INNER_ARMOR;
					ModelLayerLocation outerArmour = ModelLayers.PLAYER_OUTER_ARMOR;
					
					List<ModelLayerLocation> locations = ModelLayers.getKnownLocations().toList();
					
					for (int i = 0; i < locations.size(); i++) {
						ModelLayerLocation location = locations.get(i);
						
						if (location.getModel().equals(type)) {
							if (location.getLayer().equals("inner_armor")) {
								innerArmour = location;
							}
							
							if (location.getLayer().equals("outer_armor")) {
								outerArmour = location;
							}
						}
					}
					
					if (innerArmour != null && outerArmour != null) {
						humanRenderer.addLayer(new CosmosLayerArmourColourable<>(humanRenderer, new HumanoidModel<>(modelSet.bakeLayer(innerArmour)), new HumanoidModel<>(modelSet.bakeLayer(outerArmour))));
						DimensionalPockets.CONSOLE.debug("LivingEntityRenderer for: { " + entityType.getRegistryName() + " } Dimensional Armour Layer added.");
					}
					
					humanRenderer.addLayer(new CosmosLayerElytra<>(humanRenderer, modelSet, new ResourceLocation(DimensionalPockets.MOD_ID, "textures/entity/dimensional_elytra_base.png")));
					DimensionalPockets.CONSOLE.debug("LivingEntityRenderer for: { " + entityType.getRegistryName() + " } Elytra Layer added.");
				}
			}
		});
		
		LivingEntityRenderer<Player, PlayerModel<Player>> playerRendererAlt = event.getSkin("default");
		LivingEntityRenderer<Player, PlayerModel<Player>> playerRendererSlim = event.getSkin("slim");

		if (playerRendererAlt != null) {
			playerRendererAlt.addLayer(new CosmosLayerElytra<>(playerRendererAlt, modelSet, new ResourceLocation(DimensionalPockets.MOD_ID, "textures/entity/dimensional_elytra_base.png")));
			playerRendererAlt.addLayer(new CosmosLayerArmourColourable<>(playerRendererAlt, new HumanoidModel<>(modelSet.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel<>(modelSet.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR))));
			DimensionalPockets.CONSOLE.debug("Player Renderer {default} Custom Layers added.");
		} else {
			DimensionalPockets.CONSOLE.fatal("Player Renderer {default} <null>!! Report this issue to the Mod Author");
		}
		
		if (playerRendererSlim != null) {
			playerRendererSlim.addLayer(new CosmosLayerElytra<>(playerRendererSlim, modelSet, new ResourceLocation(DimensionalPockets.MOD_ID, "textures/entity/dimensional_elytra_base.png")));
			playerRendererSlim.addLayer(new CosmosLayerArmourColourable<>(playerRendererSlim, new HumanoidModel<>(modelSet.bakeLayer(ModelLayers.PLAYER_SLIM_INNER_ARMOR)), new HumanoidModel<>(modelSet.bakeLayer(ModelLayers.PLAYER_SLIM_OUTER_ARMOR))));
			DimensionalPockets.CONSOLE.debug("Player Renderer {slim} Custom Layers added.");
		} else {
			DimensionalPockets.CONSOLE.fatal("Player Renderer {slim} <null>!! Report this issue to the Mod Author");
		}
		
		DimensionalPockets.CONSOLE.startup("EntityRenderer Layer Registration complete.");
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onModelRegistryEvent(ModelRegistryEvent event) {
		CosmosRuntimeHelper.registerSpecialModels(DimensionalPockets.MOD_ID, 
			"item/dimensional_elytraplate_base", 
			"item/dimensional_elytraplate_shifter",
			"item/dimensional_elytraplate_connect",
			"item/dimensional_elytraplate_visor",
			"item/dimensional_elytraplate_solar",
			"item/dimensional_elytraplate_battery"
		);
		
		DimensionalPockets.CONSOLE.startup("Model Registration complete..");
	}
	
	@SuppressWarnings("deprecation")
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onTextureStitchEventPre(TextureStitchEvent.Pre event) {
		if (event.getAtlas().location() == TextureAtlas.LOCATION_BLOCKS) {
			event.addSprite(DimReference.RESOURCE.SHIELD);
			event.addSprite(DimReference.RESOURCE.SHIELD_NO_PATTERN);

			event.addSprite(DimReference.RESOURCE.SHIELD_ENHANCED);
			event.addSprite(DimReference.RESOURCE.SHIELD_ENHANCED_NO_PATTERN);
			
			DimensionalPockets.CONSOLE.startup("Texture Stitch Pre complete..");
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	public static void registerClient(ModLoadingContext context) {
		context.registerExtensionPoint(ConfigGuiFactory.class, () -> ScreenConfiguration.getInstance());
	}
	
	@OnlyIn(Dist.CLIENT)
	public static void onFMLClientSetup(FMLClientSetupEvent event) {
		SUIT_SCREEN = new KeyMapping("dimensionalpocketsii.keybind.suit_screen", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_BACKSLASH, "dimensionalpocketsii.keybind.category");
		SUIT_SCREEN_ENDER_CHEST = new KeyMapping("dimensionalpocketsii.keybind.suit_ender_chest", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_WORLD_1, "dimensionalpocketsii.keybind.category");
		SUIT_SHIFT = new KeyMapping("dimensionalpocketsii.keybind.suit_shift", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_BRACKET, "dimensionalpocketsii.keybind.category");
		SUIT_SETTINGS = new KeyMapping("dimensionalpocketsii.keybind.suit_mode_change", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_APOSTROPHE, "dimensionalpocketsii.keybind.category");
		
		CosmosRuntimeHelper.registerKeyBindings(SUIT_SCREEN, SUIT_SCREEN_ENDER_CHEST, SUIT_SHIFT, SUIT_SETTINGS);
		
	}
}