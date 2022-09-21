package com.tcn.dimensionalpocketsii.core.management;

import com.tcn.dimensionalpocketsii.client.container.ContainerElytraplateConnector;
import com.tcn.dimensionalpocketsii.client.container.ContainerElytraplateEnderChest;
import com.tcn.dimensionalpocketsii.client.container.ContainerElytraplateSettings;
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

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class ObjectManager {

	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_tome")
	public static final Item dimensional_tome = null;
	
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_shard")
	public static final Item dimensional_shard = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_ingot")
	public static final Item dimensional_ingot = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_ingot_enhanced")
	public static final Item dimensional_ingot_enhanced = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_gem")
	public static final Item dimensional_gem = null;
	
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_dust")
	public static final Item dimensional_dust = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_pearl")
	public static final Item dimensional_pearl = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_thread")
	public static final Item dimensional_thread = null;
	
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:nether_star_shard")
	public static final Item nether_star_shard = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:elytra_wing")
	public static final Item elytra_wing = null;
	
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_wrench")
	public static final Item dimensional_wrench = null;
	
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_device_base")
	public static final Item dimensional_device_base = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_ejector")
	public static final Item dimensional_ejector = null;
	
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_shifter")
	public static final Item dimensional_shifter = null; 
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_shifter_enhanced")
	public static final Item dimensional_shifter_enhanced = null;
	
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_energy_cell")
	public static final Item dimensional_energy_cell = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_energy_cell_enhanced")
	public static final Item dimensional_energy_cell_enhanced = null;
	
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_sword")
	public static final Item dimensional_sword = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_pickaxe")
	public static final Item dimensional_pickaxe = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_axe")
	public static final Item dimensional_axe = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_shovel")
	public static final Item dimensional_shovel = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_hoe")
	public static final Item dimensional_hoe = null;
	
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_bow")
	public static final Item dimensional_bow = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_trident")
	public static final Item dimensional_trident = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_shield")
	public static final Item dimensional_shield = null;
	
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_helmet")
	public static final Item dimensional_helmet = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_chestplate")
	public static final Item dimensional_chestplate = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_leggings")
	public static final Item dimensional_leggings = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_boots")
	public static final Item dimensional_boots = null;

	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_sword_enhanced")
	public static final Item dimensional_sword_enhanced = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_pickaxe_enhanced")
	public static final Item dimensional_pickaxe_enhanced = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_axe_enhanced")
	public static final Item dimensional_axe_enhanced = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_shovel_enhanced")
	public static final Item dimensional_shovel_enhanced = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_hoe_enhanced")
	public static final Item dimensional_hoe_enhanced = null;

	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_bow_enhanced")
	public static final Item dimensional_bow_enhanced = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_trident_enhanced")
	public static final Item dimensional_trident_enhanced = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_shield_enhanced")
	public static final Item dimensional_shield_enhanced = null;
	
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_helmet_enhanced")
	public static final Item dimensional_helmet_enhanced = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_chestplate_enhanced")
	public static final Item dimensional_chestplate_enhanced = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_leggings_enhanced")
	public static final Item dimensional_leggings_enhanced = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_boots_enhanced")
	public static final Item dimensional_boots_enhanced = null;
	
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:dimensional_elytraplate")
	public static final Item dimensional_elytraplate = null;
	@ObjectHolder(registryName = "minecraft:menu", value = "dimensionalpocketsii:container_elytraplate")
	public static final MenuType<ContainerElytraplateConnector> container_elytraplate = null;
	@ObjectHolder(registryName = "minecraft:menu", value = "dimensionalpocketsii:container_elytraplate_settings")
	public static final MenuType<ContainerElytraplateSettings> container_elytraplate_settings = null;
	@ObjectHolder(registryName = "minecraft:menu", value = "dimensionalpocketsii:container_elytraplate_ender_chest")
	public static final MenuType<ContainerElytraplateEnderChest> container_elytraplate_ender_chest = null;
	
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:armour_module_screen")
	public static final Item armour_module_screen = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:armour_module_shifter")
	public static final Item armour_module_shifter = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:armour_module_visor")
	public static final Item armour_module_visor = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:armour_module_solar")
	public static final Item armour_module_solar = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:armour_module_battery")
	public static final Item armour_module_battery = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:armour_module_ender_chest")
	public static final Item armour_module_ender_chest = null;

	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:module_base")
	public static final Item module_base = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:module_connector")
	public static final Item module_connector = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:module_charger")
	public static final Item module_charger = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:module_crafter")
	public static final Item module_crafter = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:module_smithing_table")
	public static final Item module_smithing_table = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:module_furnace")
	public static final Item module_furnace = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:module_blast_furnace")
	public static final Item module_blast_furnace = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:module_energy_display")
	public static final Item module_energy_display = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:module_fluid_display")
	public static final Item module_fluid_display = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:module_armour_workbench")
	public static final Item module_armour_workbench = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:module_generator")
	public static final Item module_generator = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:module_upgrade_station")
	public static final Item module_upgrade_station = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:module_focus")
	public static final Item module_focus = null;
	
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:module_creative_energy")
	public static final Item module_creative_energy = null;
	@ObjectHolder(registryName = "minecraft:item", value = "dimensionalpocketsii:module_creative_fluid")
	public static final Item module_creative_fluid = null;
	
	@ObjectHolder(registryName = "minecraft:block", value = "dimensionalpocketsii:block_dimensional_ore")
	public static final Block block_dimensional_ore = null;
	@ObjectHolder(registryName = "minecraft:block", value = "dimensionalpocketsii:block_deepslate_dimensional_ore")
	public static final Block block_deepslate_dimensional_ore = null;
	@ObjectHolder(registryName = "minecraft:block", value = "dimensionalpocketsii:block_dimensional_ore_nether")
	public static final Block block_dimensional_ore_nether = null;
	@ObjectHolder(registryName = "minecraft:block", value = "dimensionalpocketsii:block_dimensional_ore_end")
	public static final Block block_dimensional_ore_end = null;

	@ObjectHolder(registryName = "minecraft:block", value = "dimensionalpocketsii:block_dimensional")
	public static final Block block_dimensional = null;
	@ObjectHolder(registryName = "minecraft:block", value = "dimensionalpocketsii:block_dimensional_metal")
	public static final Block block_dimensional_metal = null;
	@ObjectHolder(registryName = "minecraft:block", value = "dimensionalpocketsii:block_dimensional_gem")
	public static final Block block_dimensional_gem = null;

	@ObjectHolder(registryName = "minecraft:block", value = "dimensionalpocketsii:block_dimensional_core")
	public static final Block block_dimensional_core = null;

	@ObjectHolder(registryName = "minecraft:block", value = "dimensionalpocketsii:block_wall")
	public static final Block block_wall = null;
	@ObjectHolder(registryName = "minecraft:block", value = "dimensionalpocketsii:block_wall_edge")
	public static final Block block_wall_edge = null;

	@ObjectHolder(registryName = "minecraft:block", value = "dimensionalpocketsii:block_wall_door")
	public static final Block block_wall_door = null;

	@ObjectHolder(registryName = "minecraft:block", value = "dimensionalpocketsii:block_pocket")
	public static final Block block_pocket = null;
	@ObjectHolder(registryName = "minecraft:block_entity_type", value = "dimensionalpocketsii:tile_entity_pocket")
	public static final BlockEntityType<BlockEntityPocket> tile_entity_pocket = null;
	@ObjectHolder(registryName = "minecraft:menu", value = "dimensionalpocketsii:container_pocket")
	public static final MenuType<ContainerPocket> container_pocket = null;

	@ObjectHolder(registryName = "minecraft:block", value = "dimensionalpocketsii:block_wall_connector")
	public static final Block block_wall_connector = null;
	@ObjectHolder(registryName = "minecraft:block_entity_type", value = "dimensionalpocketsii:tile_entity_connector")
	public static final BlockEntityType<BlockEntityModuleConnector> tile_entity_connector = null;
	@ObjectHolder(registryName = "minecraft:menu", value = "dimensionalpocketsii:container_connector")
	public static final MenuType<ContainerModuleConnector> container_connector = null;

	@ObjectHolder(registryName = "minecraft:block", value = "dimensionalpocketsii:block_wall_charger")
	public static final Block block_wall_charger = null;
	@ObjectHolder(registryName = "minecraft:block_entity_type", value = "dimensionalpocketsii:tile_entity_charger")
	public static final BlockEntityType<BlockEntityModuleCharger> tile_entity_charger = null;
	@ObjectHolder(registryName = "minecraft:menu", value = "dimensionalpocketsii:container_charger")
	public static final MenuType<ContainerModuleCharger> container_charger = null;

	@ObjectHolder(registryName = "minecraft:block", value = "dimensionalpocketsii:block_wall_crafter")
	public static final Block block_wall_crafter = null;
	@ObjectHolder(registryName = "minecraft:block_entity_type", value = "dimensionalpocketsii:tile_entity_crafter")
	public static final BlockEntityType<BlockEntityModuleCrafter> tile_entity_crafter = null;
	@ObjectHolder(registryName = "minecraft:menu", value = "dimensionalpocketsii:container_crafter")
	public static final MenuType<ContainerModuleCrafter> container_crafter = null;

	@ObjectHolder(registryName = "minecraft:block", value = "dimensionalpocketsii:block_wall_smithing_table")
	public static final Block block_wall_smithing_table = null;
	@ObjectHolder(registryName = "minecraft:block_entity_type", value = "dimensionalpocketsii:tile_entity_smithing_table")
	public static final BlockEntityType<BlockEntityModuleSmithingTable> tile_entity_smithing_table = null;
	@ObjectHolder(registryName = "minecraft:menu", value = "dimensionalpocketsii:container_smithing_table")
	public static final MenuType<ContainerModuleSmithingTable> container_smithing_table = null;

	@ObjectHolder(registryName = "minecraft:block", value = "dimensionalpocketsii:block_wall_furnace")
	public static final Block block_wall_furnace = null;
	@ObjectHolder(registryName = "minecraft:block_entity_type", value = "dimensionalpocketsii:tile_entity_furnace")
	public static final BlockEntityType<BlockEntityModuleFurnace> tile_entity_furnace = null;
	@ObjectHolder(registryName = "minecraft:menu", value = "dimensionalpocketsii:container_furnace")
	public static final MenuType<ContainerModuleFurnace> container_furnace = null;

	@ObjectHolder(registryName = "minecraft:block", value = "dimensionalpocketsii:block_wall_blast_furnace")
	public static final Block block_wall_blast_furnace = null;
	@ObjectHolder(registryName = "minecraft:block_entity_type", value = "dimensionalpocketsii:tile_entity_blast_furnace")
	public static final BlockEntityType<BlockEntityModuleBlastFurnace> tile_entity_blast_furnace = null;
	@ObjectHolder(registryName = "minecraft:menu", value = "dimensionalpocketsii:container_blast_furnace")
	public static final MenuType<ContainerModuleBlastFurnace> container_blast_furnace = null;

	@ObjectHolder(registryName = "minecraft:block", value = "dimensionalpocketsii:block_wall_energy_display")
	public static final Block block_wall_energy_display = null;

	@ObjectHolder(registryName = "minecraft:block", value = "dimensionalpocketsii:block_wall_fluid_display")
	public static final Block block_wall_fluid_display = null;
	@ObjectHolder(registryName = "minecraft:block_entity_type", value = "dimensionalpocketsii:tile_entity_fluid_display")
	public static final BlockEntityType<BlockEntityModuleFluidDisplay> tile_entity_fluid_display = null;

	@ObjectHolder(registryName = "minecraft:block", value = "dimensionalpocketsii:block_wall_armour_workbench")
	public static final Block block_wall_armour_workbench = null;
	@ObjectHolder(registryName = "minecraft:block_entity_type", value = "dimensionalpocketsii:tile_entity_armour_workbench")
	public static final BlockEntityType<BlockEntityModuleArmourWorkbench> tile_entity_armour_workbench = null;
	@ObjectHolder(registryName = "minecraft:menu", value = "dimensionalpocketsii:container_armour_workbench")
	public static final MenuType<ContainerModuleArmourWorkbench> container_armour_workbench = null;

	@ObjectHolder(registryName = "minecraft:block", value = "dimensionalpocketsii:block_wall_upgrade_station")
	public static final Block block_wall_upgrade_station = null;
	@ObjectHolder(registryName = "minecraft:block_entity_type", value = "dimensionalpocketsii:tile_entity_upgrade_station")
	public static final BlockEntityType<BlockEntityModuleUpgradeStation> tile_entity_upgrade_station = null;
	@ObjectHolder(registryName = "minecraft:menu", value = "dimensionalpocketsii:container_upgrade_station")
	public static final MenuType<ContainerModuleUpgradeStation> container_upgrade_station = null;

	@ObjectHolder(registryName = "minecraft:block", value = "dimensionalpocketsii:block_wall_generator")
	public static final Block block_wall_generator = null;
	@ObjectHolder(registryName = "minecraft:block_entity_type", value = "dimensionalpocketsii:tile_entity_generator")
	public static final BlockEntityType<BlockEntityModuleGenerator> tile_entity_generator = null;
	@ObjectHolder(registryName = "minecraft:menu", value = "dimensionalpocketsii:container_generator")
	public static final MenuType<ContainerModuleGenerator> container_generator = null;

	@ObjectHolder(registryName = "minecraft:block", value = "dimensionalpocketsii:block_dimensional_focus")
	public static final Block block_dimensional_focus = null;
	@ObjectHolder(registryName = "minecraft:block_entity_type", value = "dimensionalpocketsii:tile_entity_focus")
	public static final BlockEntityType<BlockEntityFocus> tile_entity_focus = null;
	@ObjectHolder(registryName = "minecraft:menu", value = "dimensionalpocketsii:container_focus")
	public static final MenuType<ContainerFocus> container_focus = null;

	@ObjectHolder(registryName = "minecraft:block", value = "dimensionalpocketsii:block_wall_creative_energy")
	public static final Block block_wall_creative_energy = null;
	@ObjectHolder(registryName = "minecraft:block_entity_type", value = "dimensionalpocketsii:tile_entity_creative_energy")
	public static final BlockEntityType<BlockEntityZModuleCreativeEnergy> tile_entity_creative_energy = null;

	@ObjectHolder(registryName = "minecraft:block", value = "dimensionalpocketsii:block_wall_creative_fluid")
	public static final Block block_wall_creative_fluid = null;
	@ObjectHolder(registryName = "minecraft:block_entity_type", value = "dimensionalpocketsii:tile_entity_creative_fluid")
	public static final BlockEntityType<BlockEntityZModuleCreativeFluid> tile_entity_creative_fluid = null;

	@ObjectHolder(registryName = "minecraft:entity_type", value = "dimensionalpocketsii:dimensional_trident_type")
	public static final EntityType<DimensionalTridentEntity> dimensional_trident_type = null;
	@ObjectHolder(registryName = "minecraft:entity_type", value = "dimensionalpocketsii:dimensional_trident_enhanced_type")
	public static final EntityType<DimensionalTridentEnhancedEntity> dimensional_trident_enhanced_type = null;

	@ObjectHolder(registryName = "minecraft:recipe_serializer", value = "dimensionalpocketsii:upgrading")
	public static final RecipeSerializer<UpgradeStationRecipe> upgrading = null;
	
	@ObjectHolder(registryName = "minecraft:sound_event", value = "dimensionalpocketsii:sound_portal_in")
	public static final SoundEvent sound_portal_in = null;

	@ObjectHolder(registryName = "minecraft:sound_event", value = "dimensionalpocketsii:sound_portal_out")
	public static final SoundEvent sound_portal_out = null;
	
	@ObjectHolder(registryName = "minecraft:sound_event", value = "dimensionalpocketsii:sound_woosh")
	public static final SoundEvent sound_woosh = null;
	
	@ObjectHolder(registryName = "minecraft:sound_event", value = "dimensionalpocketsii:sound_tink")
	public static final SoundEvent sound_tink = null;
}