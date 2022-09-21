package com.tcn.dimensionalpocketsii.core.management;

import com.tcn.cosmoslibrary.common.runtime.CosmosRuntimeHelper;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.client.colour.ColourBlockPocket;
import com.tcn.dimensionalpocketsii.client.colour.ColourBlockWall;
import com.tcn.dimensionalpocketsii.client.colour.ColourItem;
import com.tcn.dimensionalpocketsii.client.container.ContainerElytraplateConnector;
import com.tcn.dimensionalpocketsii.client.container.ContainerElytraplateEnderChest;
import com.tcn.dimensionalpocketsii.client.container.ContainerElytraplateSettings;
import com.tcn.dimensionalpocketsii.client.renderer.RendererDimensionalTrident;
import com.tcn.dimensionalpocketsii.client.renderer.RendererDimensionalTridentEnhanced;
import com.tcn.dimensionalpocketsii.client.screen.ScreenElytraplateConnector;
import com.tcn.dimensionalpocketsii.client.screen.ScreenElytraplateEnderChest;
import com.tcn.dimensionalpocketsii.client.screen.ScreenElytraplateSettings;
import com.tcn.dimensionalpocketsii.core.crafting.UpgradeStationRecipe;
import com.tcn.dimensionalpocketsii.core.entity.DimensionalTridentEnhancedEntity;
import com.tcn.dimensionalpocketsii.core.entity.DimensionalTridentEntity;
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
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenFocus;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenModuleArmourWorkbench;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenModuleBlastFurnace;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenModuleCharger;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenModuleConnector;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenModuleCrafter;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenModuleFurnace;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenModuleGenerator;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenModuleSmithingTable;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenModuleUpgradeStation;
import com.tcn.dimensionalpocketsii.pocket.client.screen.ScreenPocket;
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

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(DimensionalPockets.MOD_ID)
public class ObjectManager {

	public static final Item dimensional_tome = null;
	
	public static final Item dimensional_shard = null;
	public static final Item dimensional_ingot = null;
	public static final Item dimensional_ingot_enhanced = null;
	public static final Item dimensional_gem = null;
	
	public static final Item dimensional_dust = null;
	public static final Item dimensional_pearl = null;
	public static final Item dimensional_thread = null;
	
	public static final Item nether_star_shard = null;
	public static final Item elytra_wing = null;
	
	public static final Item dimensional_wrench = null;
	
	public static final Item dimensional_device_base = null;
	public static final Item dimensional_ejector = null;
	
	public static final Item dimensional_shifter = null; 
	public static final Item dimensional_shifter_enhanced = null;
	
	public static final Item dimensional_energy_cell = null;
	public static final Item dimensional_energy_cell_enhanced = null;
	
	public static final Item dimensional_sword = null;
	public static final Item dimensional_pickaxe = null;
	public static final Item dimensional_axe = null;
	public static final Item dimensional_shovel = null;
	public static final Item dimensional_hoe = null;
	
	public static final Item dimensional_bow = null;
	public static final Item dimensional_trident = null;
	public static final Item dimensional_shield = null;
	
	public static final Item dimensional_helmet = null;
	public static final Item dimensional_chestplate = null;
	public static final Item dimensional_leggings = null;
	public static final Item dimensional_boots = null;

	public static final Item dimensional_sword_enhanced = null;
	public static final Item dimensional_pickaxe_enhanced = null;
	public static final Item dimensional_axe_enhanced = null;
	public static final Item dimensional_shovel_enhanced = null;
	public static final Item dimensional_hoe_enhanced = null;

	public static final Item dimensional_bow_enhanced = null;
	public static final Item dimensional_trident_enhanced = null;
	public static final Item dimensional_shield_enhanced = null;
	
	public static final Item dimensional_helmet_enhanced = null;
	public static final Item dimensional_chestplate_enhanced = null;
	public static final Item dimensional_leggings_enhanced = null;
	public static final Item dimensional_boots_enhanced = null;
	
	public static final Item dimensional_elytraplate = null;
	public static final MenuType<ContainerElytraplateConnector> container_elytraplate = null;
	public static final MenuType<ContainerElytraplateSettings> container_elytraplate_settings = null;
	public static final MenuType<ContainerElytraplateEnderChest> container_elytraplate_ender_chest = null;
	
	public static final Item armour_module_screen = null;
	public static final Item armour_module_shifter = null;
	public static final Item armour_module_visor = null;
	public static final Item armour_module_solar = null;
	public static final Item armour_module_battery = null;
	public static final Item armour_module_ender_chest = null;
	
	public static final Item module_base = null;
	public static final Item module_connector = null;
	public static final Item module_charger = null;
	public static final Item module_crafter = null;
	public static final Item module_smithing_table = null;
	public static final Item module_furnace = null;
	public static final Item module_blast_furnace = null;
	public static final Item module_energy_display = null;
	public static final Item module_fluid_display = null;
	public static final Item module_armour_workbench = null;
	public static final Item module_generator = null;
	public static final Item module_upgrade_station = null;
	public static final Item module_focus = null;
	
	public static final Item module_creative_energy = null;
	public static final Item module_creative_fluid = null;
	
	public static final Block block_dimensional_ore = null;
	public static final Block block_deepslate_dimensional_ore = null;
	public static final Block block_dimensional_ore_nether = null;
	public static final Block block_dimensional_ore_end = null;
	
	public static final Block block_dimensional = null;
	public static final Block block_dimensional_metal = null;
	public static final Block block_dimensional_gem = null;
	
	public static final Block block_dimensional_core = null;

	public static final Block block_wall = null;
	public static final Block block_wall_edge = null;

	public static final Block block_wall_door = null;
	
	public static final Block block_pocket = null;
	public static final BlockEntityType<BlockEntityPocket> tile_entity_pocket = null;
	public static final MenuType<ContainerPocket> container_pocket = null;
	
	public static final Block block_wall_connector = null;
	public static final BlockEntityType<BlockEntityModuleConnector> tile_entity_connector = null;
	public static final MenuType<ContainerModuleConnector> container_connector = null;
	
	public static final Block block_wall_charger = null;
	public static final BlockEntityType<BlockEntityModuleCharger> tile_entity_charger = null;
	public static final MenuType<ContainerModuleCharger> container_charger = null;
	
	public static final Block block_wall_crafter = null;
	public static final BlockEntityType<BlockEntityModuleCrafter> tile_entity_crafter = null;
	public static final MenuType<ContainerModuleCrafter> container_crafter = null;

	public static final Block block_wall_smithing_table = null;
	public static final BlockEntityType<BlockEntityModuleSmithingTable> tile_entity_smithing_table = null;
	public static final MenuType<ContainerModuleSmithingTable> container_smithing_table = null;

	public static final Block block_wall_furnace = null;
	public static final BlockEntityType<BlockEntityModuleFurnace> tile_entity_furnace = null;
	public static final MenuType<ContainerModuleFurnace> container_furnace = null;

	public static final Block block_wall_blast_furnace = null;
	public static final BlockEntityType<BlockEntityModuleBlastFurnace> tile_entity_blast_furnace = null;
	public static final MenuType<ContainerModuleBlastFurnace> container_blast_furnace = null;

	public static final Block block_wall_energy_display = null;
	
	public static final Block block_wall_fluid_display = null;
	public static final BlockEntityType<BlockEntityModuleFluidDisplay> tile_entity_fluid_display = null;

	public static final Block block_wall_armour_workbench = null;
	public static final BlockEntityType<BlockEntityModuleArmourWorkbench> tile_entity_armour_workbench = null;
	public static final MenuType<ContainerModuleArmourWorkbench> container_armour_workbench = null;

	public static final Block block_wall_upgrade_station = null;
	public static final BlockEntityType<BlockEntityModuleUpgradeStation> tile_entity_upgrade_station = null;
	public static final MenuType<ContainerModuleUpgradeStation> container_upgrade_station = null;

	public static final Block block_wall_generator = null;
	public static final BlockEntityType<BlockEntityModuleGenerator> tile_entity_generator = null;
	public static final MenuType<ContainerModuleGenerator> container_generator = null;

	public static final Block block_dimensional_focus = null;
	public static final BlockEntityType<BlockEntityFocus> tile_entity_focus = null;
	public static final MenuType<ContainerFocus> container_focus = null;
	
	public static final Block block_wall_creative_energy = null;
	public static final BlockEntityType<BlockEntityZModuleCreativeEnergy> tile_entity_creative_energy = null;

	public static final Block block_wall_creative_fluid = null;
	public static final BlockEntityType<BlockEntityZModuleCreativeFluid> tile_entity_creative_fluid = null;

	public static final EntityType<DimensionalTridentEntity> dimensional_trident_type = null;
	public static final EntityType<DimensionalTridentEnhancedEntity> dimensional_trident_enhanced_type = null;
	
	public static final RecipeSerializer<UpgradeStationRecipe> upgrading = null;
	//public static final RecipeType<UpgradeStationRecipe> recipe_type_upgrading = null;
	
	@OnlyIn(Dist.CLIENT)
	public static void onFMLClientSetup(FMLClientSetupEvent event) {
		MenuScreens.register(container_pocket, ScreenPocket::new);
		MenuScreens.register(container_connector, ScreenModuleConnector::new);
		MenuScreens.register(container_charger, ScreenModuleCharger::new);
		MenuScreens.register(container_crafter, ScreenModuleCrafter::new);
		MenuScreens.register(container_smithing_table, ScreenModuleSmithingTable::new);
		MenuScreens.register(container_furnace, ScreenModuleFurnace::new);
		MenuScreens.register(container_blast_furnace, ScreenModuleBlastFurnace::new);
		MenuScreens.register(container_armour_workbench, ScreenModuleArmourWorkbench::new);
		MenuScreens.register(container_upgrade_station, ScreenModuleUpgradeStation::new);
		MenuScreens.register(container_generator, ScreenModuleGenerator::new);
		MenuScreens.register(container_focus, ScreenFocus::new);
		
		MenuScreens.register(container_elytraplate, ScreenElytraplateConnector::new);
		MenuScreens.register(container_elytraplate_settings, ScreenElytraplateSettings::new);
		MenuScreens.register(container_elytraplate_ender_chest, ScreenElytraplateEnderChest::new);
		

		CosmosRuntimeHelper.setRenderLayers(RenderType.cutoutMipped(),
			block_pocket,
			
			block_wall_charger, block_wall_connector, block_wall_crafter, block_wall_smithing_table, block_wall_furnace, block_wall_blast_furnace, 
			block_wall_energy_display, block_wall_fluid_display, block_wall_armour_workbench, block_wall_upgrade_station, block_wall_generator,
			block_dimensional_core, block_dimensional_focus,
			
			block_wall_creative_energy, block_wall_creative_fluid
		);
		
		CosmosRuntimeHelper.registerBlockColours(new ColourBlockPocket(), block_pocket);
		
		CosmosRuntimeHelper.registerBlockColours(new ColourBlockWall(), 
			block_wall, block_wall_edge, block_wall_door,
			
			block_wall_connector, block_wall_charger, block_wall_crafter, block_wall_smithing_table, block_dimensional_focus,
			block_wall_furnace, block_wall_blast_furnace, block_wall_energy_display, block_wall_fluid_display, block_wall_armour_workbench, block_wall_upgrade_station, block_wall_generator
		);
		
		CosmosRuntimeHelper.registerItemColours(new ColourItem(),
			block_pocket, block_dimensional_core, block_wall, block_wall_edge,
			
			dimensional_device_base, dimensional_shifter, dimensional_shifter_enhanced, dimensional_ejector,
			dimensional_energy_cell, dimensional_energy_cell_enhanced,
			
			dimensional_helmet, dimensional_chestplate, dimensional_leggings, dimensional_boots,
			dimensional_helmet_enhanced, dimensional_chestplate_enhanced, dimensional_leggings_enhanced, dimensional_boots_enhanced,
			
			dimensional_elytraplate,
			
			block_wall_connector, block_wall_charger, block_wall_crafter, block_wall_smithing_table, block_dimensional_focus,
			block_wall_furnace, block_wall_blast_furnace, block_wall_energy_display, block_wall_fluid_display, block_wall_armour_workbench, block_wall_upgrade_station, block_wall_generator
		);

		ItemProperties.register(dimensional_bow, new ResourceLocation("pull"), (stackIn, clientWorldIn, livingEntityIn, o) -> { if (livingEntityIn == null) { return 0.0F; } else { return livingEntityIn.getUseItem() != stackIn ? 0.0F : (float) (stackIn.getUseDuration() - livingEntityIn.getUseItemRemainingTicks()) / 20.0F; }});
		ItemProperties.register(dimensional_bow, new ResourceLocation("pulling"), (stackIn, clientWorldIn, livingEntityIn, o) -> { return livingEntityIn != null && livingEntityIn.isUsingItem() && livingEntityIn.getUseItem() == stackIn ? 1.0F : 0.0F; });
		
		ItemProperties.register(dimensional_bow_enhanced, new ResourceLocation("pull"), (stackIn, clientWorldIn, livingEntityIn, o) -> { if (livingEntityIn == null) { return 0.0F; } else { return livingEntityIn.getUseItem() != stackIn ? 0.0F : (float) (stackIn.getUseDuration() - livingEntityIn.getUseItemRemainingTicks()) / 20.0F; }});
		ItemProperties.register(dimensional_bow_enhanced, new ResourceLocation("pulling"), (stackIn, clientWorldIn, livingEntityIn, o) -> { return livingEntityIn != null && livingEntityIn.isUsingItem() && livingEntityIn.getUseItem() == stackIn ? 1.0F : 0.0F; });
		
		ItemProperties.register(dimensional_trident, new ResourceLocation("throwing"), (stackIn, clientWorldIn, livingEntityIn, o) -> { return livingEntityIn != null && livingEntityIn.isUsingItem() && livingEntityIn.getUseItem() == stackIn ? 1.0F : 0.0F; });
		ItemProperties.register(dimensional_trident_enhanced, new ResourceLocation("throwing"), (stackIn, clientWorldIn, livingEntityIn, o) -> { return livingEntityIn != null && livingEntityIn.isUsingItem() && livingEntityIn.getUseItem() == stackIn ? 1.0F : 0.0F; });
		
		ItemProperties.register(dimensional_shield, new ResourceLocation("blocking"), (stackIn, clientWorldIn, livingEntityIn, o) -> { return livingEntityIn != null && livingEntityIn.isUsingItem() && livingEntityIn.getUseItem() == stackIn ? 1.0F : 0.0F; });
		ItemProperties.register(dimensional_shield_enhanced, new ResourceLocation("blocking"), (stackIn, clientWorldIn, livingEntityIn, o) -> { return livingEntityIn != null && livingEntityIn.isUsingItem() && livingEntityIn.getUseItem() == stackIn ? 1.0F : 0.0F; });
		
		EntityRenderers.register(dimensional_trident_type, RendererDimensionalTrident::new);
		EntityRenderers.register(dimensional_trident_enhanced_type, RendererDimensionalTridentEnhanced::new);
	}
}
