package com.zeher.zehercraft.core.handler;

import com.zeher.zehercraft.ZCReference;
import com.zeher.zehercraft.ZeherCraft;
import com.zeher.zeherlib.api.azrf.AZRFWrench;
import com.zeher.zeherlib.core.resource.ResourceCategory;
import com.zeher.zeherlib.core.resource.ResourceType;
import com.zeher.zeherlib.mod.item.ModItem;
import com.zeher.zeherlib.mod.item.ModItemUpgradeEnergy;
import com.zeher.zeherlib.mod.item.ModItemUpgradeFluid;
import com.zeher.zeherlib.mod.item.ModToolAxe;
import com.zeher.zeherlib.mod.item.ModToolPickaxe;
import com.zeher.zeherlib.mod.item.ModToolSpade;
import com.zeher.zeherlib.mod.item.ModToolSword;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ZeherCraft.MOD_ID)
public class ItemHandler {
	
	public static final Item COPPER_INGOT = new ModItem("copper_ingot", CreativeTabHandler.TAB_ITEMS);
	public static final Item COPPER_DUST = new ModItem("copper_dust", CreativeTabHandler.TAB_ITEMS);
	public static final Item COPPER_PLATE = new ModItem("copper_plate", CreativeTabHandler.TAB_ITEMS);
	
	public static final Item TIN_INGOT = new ModItem("tin_ingot", CreativeTabHandler.TAB_ITEMS);
	public static final Item TIN_DUST = new ModItem("tin_dust", CreativeTabHandler.TAB_ITEMS);
	public static final Item TIN_PLATE = new ModItem("tin_plate", CreativeTabHandler.TAB_ITEMS);
	
	public static final Item SILVER_INGOT = new ModItem("silver_ingot", CreativeTabHandler.TAB_ITEMS);
	public static final Item SILVER_DUST = new ModItem("silver_dust", CreativeTabHandler.TAB_ITEMS);
	public static final Item SILVER_PLATE = new ModItem("silver_plate", CreativeTabHandler.TAB_ITEMS);
	
	public static final Item BRONZE_INGOT = new ModItem("bronze_ingot", CreativeTabHandler.TAB_ITEMS);
	public static final Item BRONZE_DUST = new ModItem("bronze_dust", CreativeTabHandler.TAB_ITEMS);
	public static final Item BRONZE_PLATE = new ModItem("bronze_plate", CreativeTabHandler.TAB_ITEMS);
	
	public static final Item BRASS_INGOT = new ModItem("brass_ingot", CreativeTabHandler.TAB_ITEMS);
	public static final Item BRASS_DUST = new ModItem("brass_dust", CreativeTabHandler.TAB_ITEMS);
	public static final Item BRASS_PLATE = new ModItem("brass_plate", CreativeTabHandler.TAB_ITEMS);
	
	public static final Item LAPIS_INGOT = new ModItem("lapis_ingot", CreativeTabHandler.TAB_ITEMS);
	public static final Item SILICON_INGOT = new ModItem("silicon_ingot", CreativeTabHandler.TAB_ITEMS);
	
	public static final Item STEEL_INGOT = new ModItem("steel_ingot", CreativeTabHandler.TAB_ITEMS);
	public static final Item STEEL_INGOT_UNREFINED = new ModItem("steel_ingot_unrefined", CreativeTabHandler.TAB_ITEMS);
	public static final Item STEEL_DUST = new ModItem("steel_dust", CreativeTabHandler.TAB_ITEMS);
	public static final Item STEEL_PLATE = new ModItem("steel_plate", CreativeTabHandler.TAB_ITEMS);
	public static final Item STEEL_ROD = new ModItem("steel_rod", CreativeTabHandler.TAB_ITEMS);
	
	public static final Item IRON_DUST = new ModItem("iron_dust", CreativeTabHandler.TAB_ITEMS);
	public static final Item IRON_DUST_REFINE = new ModItem("iron_dust_refine", CreativeTabHandler.TAB_ITEMS);
	public static final Item IRON_PLATE = new ModItem("iron_plate", CreativeTabHandler.TAB_ITEMS);
	
	public static final Item GOLD_DUST = new ModItem("gold_dust", CreativeTabHandler.TAB_ITEMS);
	public static final Item GOLD_DUST_REFINE = new ModItem("gold_dust_refine", CreativeTabHandler.TAB_ITEMS);
	public static final Item GOLD_PLATE = new ModItem("gold_plate", CreativeTabHandler.TAB_ITEMS);
	
	public static final Item DIAMOND_DUST = new ModItem("diamond_dust", CreativeTabHandler.TAB_ITEMS);
	public static final Item DIAMOND_PLATE = new ModItem("diamond_plate", CreativeTabHandler.TAB_ITEMS);
	
	public static final Item STONE_DUST = new ModItem("stone_dust", CreativeTabHandler.TAB_ITEMS);
	
	public static final Item ENERGY_INGOT = new ModItem("energy_ingot", CreativeTabHandler.TAB_ITEMS);
	public static final Item ENERGY_DUST = new ModItem("energy_dust", CreativeTabHandler.TAB_ITEMS);
	public static final Item ENERGY_WAFER = new ModItem("energy_wafer", CreativeTabHandler.TAB_ITEMS);
	
	public static final Item CIRCUIT_ONE_RAW = new ModItem("circuit_one_raw", CreativeTabHandler.TAB_ITEMS);
	public static final Item CIRCUIT_ONE = new ModItem("circuit_one", CreativeTabHandler.TAB_ITEMS);
	public static final Item CIRCUIT_THREE_RAW = new ModItem("circuit_three_raw", CreativeTabHandler.TAB_ITEMS);
	public static final Item CIRCUIT_THREE = new ModItem("circuit_three", CreativeTabHandler.TAB_ITEMS);
	
	public static final Item SILICON = new ModItem("silicon", CreativeTabHandler.TAB_ITEMS);
	public static final Item SILICON_REFINED = new ModItem("silicon_refined", CreativeTabHandler.TAB_ITEMS);
	public static final Item SILICON_WAFER = new ModItem("silicon_wafer", CreativeTabHandler.TAB_ITEMS);
	
	public static final Item UPGRADE_BASE = new ModItemUpgradeEnergy("upgrade_base", CreativeTabHandler.TAB_ITEMS, 64);
	
	public static final Item UPGRADE_SPEED = new ModItemUpgradeEnergy("upgrade_speed", CreativeTabHandler.TAB_ITEMS, 8);
	public static final Item UPGRADE_CAPACITY = new ModItemUpgradeEnergy("upgrade_capacity", CreativeTabHandler.TAB_ITEMS, 8);
	public static final Item UPGRADE_EFFICIENCY = new ModItemUpgradeEnergy("upgrade_efficiency", CreativeTabHandler.TAB_ITEMS, 8);
	
	public static final Item UPGRADE_FLUID_SPEED = new ModItemUpgradeFluid("upgrade_fluid_speed", CreativeTabHandler.TAB_ITEMS, 8);
	public static final Item UPGRADE_FLUID_CAPACITY = new ModItemUpgradeFluid("upgrade_fluid_capacity", CreativeTabHandler.TAB_ITEMS, 8);
	public static final Item UPGRADE_FLUID_EFFICIENCY = new ModItemUpgradeFluid("upgrade_fluid_efficiency", CreativeTabHandler.TAB_ITEMS, 8);
	
	public static final Item RUBBER = new ModItem("rubber", CreativeTabHandler.TAB_ITEMS);
	public static final Item RUBBER_INSULATION = new ModItem("rubber_insulation", CreativeTabHandler.TAB_ITEMS);
	public static final Item TOOL_ROD = new ModItem("tool_rod", CreativeTabHandler.TAB_ITEMS);
	
	public static final Item MACHINE_WRENCH = new AZRFWrench("machine_wrench", CreativeTabHandler.TAB_ITEMS);
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(
				COPPER_INGOT, COPPER_DUST, COPPER_PLATE,
				TIN_INGOT, TIN_DUST, TIN_PLATE,
				SILVER_INGOT, SILVER_DUST, SILVER_PLATE,
				BRONZE_INGOT, BRONZE_DUST, BRONZE_PLATE,
				BRASS_INGOT, BRASS_DUST, BRASS_PLATE,
				
				LAPIS_INGOT, SILICON_INGOT,
				STEEL_INGOT, STEEL_INGOT_UNREFINED, STEEL_DUST, STEEL_PLATE, STEEL_ROD,
				
				IRON_DUST, IRON_DUST_REFINE, IRON_PLATE,
				
				GOLD_DUST, GOLD_DUST_REFINE, GOLD_PLATE,
				
				DIAMOND_DUST, DIAMOND_PLATE,
				
				STONE_DUST,
				
				ENERGY_INGOT, ENERGY_DUST, ENERGY_WAFER,
				
				CIRCUIT_ONE_RAW, CIRCUIT_ONE,
				CIRCUIT_THREE_RAW, CIRCUIT_THREE,
				
				SILICON, SILICON_REFINED, SILICON_WAFER,
				
				UPGRADE_BASE, UPGRADE_SPEED, UPGRADE_CAPACITY, UPGRADE_EFFICIENCY,
				UPGRADE_FLUID_SPEED, UPGRADE_FLUID_CAPACITY, UPGRADE_FLUID_EFFICIENCY,
				
				RUBBER, RUBBER_INSULATION, TOOL_ROD,
				
				MACHINE_WRENCH);
	}
	
	@SubscribeEvent
	public static void registerModelLocations(final ModelRegistryEvent event) {
		registerItemModels(ResourceCategory.MATERIAL, 
				COPPER_INGOT, COPPER_DUST, COPPER_PLATE,
				TIN_INGOT, TIN_DUST, TIN_PLATE,
				SILVER_INGOT, SILVER_DUST, SILVER_PLATE,
				BRONZE_INGOT, BRONZE_DUST, BRONZE_PLATE,
				BRASS_INGOT, BRASS_DUST, BRASS_PLATE,
				
				LAPIS_INGOT, SILICON_INGOT,
				STEEL_INGOT, STEEL_INGOT_UNREFINED, STEEL_DUST, STEEL_PLATE, STEEL_ROD,
				
				IRON_DUST, IRON_DUST_REFINE, IRON_PLATE,
				
				GOLD_DUST, GOLD_DUST_REFINE, GOLD_PLATE,
				
				DIAMOND_DUST, DIAMOND_PLATE,
				
				STONE_DUST,
				
				RUBBER, RUBBER_INSULATION, 
				TOOL_ROD);
				
		registerItemModels(ResourceCategory.ENERGY,
				
				ENERGY_INGOT, ENERGY_DUST, ENERGY_WAFER,
				
				CIRCUIT_ONE_RAW, CIRCUIT_ONE,
				CIRCUIT_THREE_RAW, CIRCUIT_THREE,
				
				SILICON, SILICON_REFINED, SILICON_WAFER,
				
				UPGRADE_BASE, UPGRADE_SPEED, UPGRADE_CAPACITY, UPGRADE_EFFICIENCY,
				UPGRADE_FLUID_SPEED, UPGRADE_FLUID_CAPACITY, UPGRADE_FLUID_EFFICIENCY);
				
		registerItemModels(ResourceCategory.TOOL,
				MACHINE_WRENCH);
	}
	
	private static void registerItemModels(ResourceCategory category, Item... items) {
		for (int i = 0; i < items.length; i++) {
			registerItemModelLocation(items[i], category);
		}
	}
	
	public static void registerItemModelLocation(Item item, ResourceCategory category){
		String name = "zehercraft:" /**"models/" + ResourceType.ITEM */+ category.getName();
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(name + item.getUnlocalizedName().split("\\.")[1].toString(), "inventory"));
	}
}