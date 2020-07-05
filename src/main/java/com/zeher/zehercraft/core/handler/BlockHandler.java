package com.zeher.zehercraft.core.handler;

import com.zeher.zehercraft.ZCReference;
import com.zeher.zehercraft.ZeherCraft;
import com.zeher.zehercraft.processing.core.block.BlockCharger;
import com.zeher.zehercraft.processing.core.block.BlockCompactor;
import com.zeher.zehercraft.processing.core.block.BlockGrinder;
import com.zeher.zehercraft.processing.core.block.BlockKiln;
import com.zeher.zehercraft.processing.core.block.BlockSynthesiser;
import com.zeher.zehercraft.processing.core.block.BlockSynthesiserStand;
import com.zeher.zehercraft.processing.core.block.ItemBlockCharger;
import com.zeher.zehercraft.processing.core.block.ItemBlockCompactor;
import com.zeher.zehercraft.processing.core.block.ItemBlockGrinder;
import com.zeher.zehercraft.processing.core.block.ItemBlockKiln;
import com.zeher.zehercraft.transport.core.block.BlockEnergyChannel;
import com.zeher.zehercraft.transport.core.block.BlockEnergyChannelSurge;
import com.zeher.zehercraft.transport.core.block.BlockEnergyChannelTransparentSurge;
import com.zeher.zeherlib.mod.block.ModBlock;
import com.zeher.zeherlib.mod.block.ModBlockConnectedGlass;
import com.zeher.zeherlib.mod.block.ModBlockDropsBlock;
import com.zeher.zeherlib.mod.block.ModBlockModelUnplaceable;
import com.zeher.zeherlib.mod.block.ModItemBlock;
import com.zeher.zeherlib.mod.fluid.ModBlockFluid;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ZeherCraft.MOD_ID)
public class BlockHandler {
	
	@EventBusSubscriber(modid = ZeherCraft.MOD_ID)
	public static class PROCESSING {
		
		/** Automatically registers ItemBlocks for standard blocks. */
		private static ItemBlock[] BLOCK_ITEMS;

		public static final Block BLOCK_STRUCTURE = new ModBlockModelUnplaceable("block_processing_structure", Material.IRON, "pickaxe", 2, 5, 4, CreativeTabHandler.TAB_DEVICES);
		public static final ItemBlock ITEMBLOCK_STRUCTURE = new ModItemBlock(BLOCK_STRUCTURE, "Base block to craft machines.", "", "");
		
		public static final Block BLOCK_KILN = new BlockKiln("block_kiln", Material.IRON, "pickaxe", 2, 5, 4, CreativeTabHandler.TAB_DEVICES);
		public static final ItemBlock ITEMBLOCK_KILN = new ItemBlockKiln(BLOCK_KILN, "A machine to smelt things.", "Smelts things using RF power.", "Can be upgraded internally.");
		
		public static final Block BLOCK_GRINDER = new BlockGrinder("block_grinder", Material.IRON, "pickaxe", 2, 5, 4, CreativeTabHandler.TAB_DEVICES);
		public static final ItemBlock ITEMBLOCK_GRINDER = new ItemBlockGrinder(BLOCK_GRINDER, "A machine to grind things.", "Grinds things using RF power.", "Can be upgraded internally.");
		
		public static final Block BLOCK_COMPACTOR = new BlockCompactor("block_compactor", Material.IRON, "", 2, 5, 4, CreativeTabHandler.TAB_DEVICES);
		public static final ItemBlock ITEMBLOCK_COMPACTOR = new ItemBlockCompactor(BLOCK_COMPACTOR, "A machine to compact things.", "Compacts things using RF power.", "Can be upgraded internally.");
		
		public static final Block BLOCK_CHARGER = new BlockCharger("block_charger", Material.IRON, "pickaxe", 2, 5, 4, CreativeTabHandler.TAB_DEVICES);
		public static final ItemBlock ITEMBLOCK_CHARGER = new ItemBlockCharger(BLOCK_CHARGER, "Charges Items.", "Charges any RF enabled Item.", "Can be upgraded internally.");
		
		public static final Block BLOCK_SYNTHESISER = new BlockSynthesiser("block_synthesiser", Material.IRON, "pickaxe", 3, 8, 4, CreativeTabHandler.TAB_DEVICES);
		public static final ItemBlock ITEMBLOCK_SYNTHESISER = new ModItemBlock(BLOCK_SYNTHESISER, "Base block of the Synthesiser multiblock.", "Used in complex crafting.", "Requires Synthesiser Stands.");
		
		public static final Block BLOCK_SYNTHESISER_STAND = new BlockSynthesiserStand("block_synthesiser_stand", Material.IRON, "pickaxe", 3, 8, 4, CreativeTabHandler.TAB_DEVICES);
		public static final ItemBlock ITEMBLOCK_SYNTHESISER_STAND = new ModItemBlock(BLOCK_SYNTHESISER_STAND, "Support block for the Synthesiser multiblock.", "Used in complex crafting.", "Requires a Synthesiser to use.");

		@SubscribeEvent
		public static void registerB(RegistryEvent.Register<Block> event) {
			RegistryHandler.REGISTRY_EVENT.registerAllBlocks(event, 
					BLOCK_STRUCTURE, 
					BLOCK_KILN, BLOCK_GRINDER, BLOCK_COMPACTOR, BLOCK_CHARGER,
					BLOCK_SYNTHESISER, BLOCK_SYNTHESISER_STAND);
		}
		
		@SubscribeEvent
		public static void registerI(RegistryEvent.Register<Item> event) {
			RegistryHandler.REGISTRY_EVENT.registerAllItemBlocks(event,
					ITEMBLOCK_STRUCTURE, 
					ITEMBLOCK_KILN, ITEMBLOCK_GRINDER, ITEMBLOCK_COMPACTOR, ITEMBLOCK_CHARGER,
					ITEMBLOCK_SYNTHESISER, ITEMBLOCK_SYNTHESISER_STAND);
		}
		
		@SubscribeEvent 
		public static void registerModels(final ModelRegistryEvent event){
			RegistryHandler.REGISTRY_EVENT.registerAllModels(event, 
					BLOCK_STRUCTURE, 
					BLOCK_KILN, BLOCK_GRINDER, BLOCK_COMPACTOR, BLOCK_CHARGER,
					BLOCK_SYNTHESISER, BLOCK_SYNTHESISER_STAND);
		}
	}
	
	@EventBusSubscriber(modid = ZeherCraft.MOD_ID)
	public static class PRODUCER {
		
		/** Automatically registers ItemBlocks for standard blocks. */
		private static ItemBlock[] BLOCK_ITEMS;
	}
	
	@EventBusSubscriber(modid = ZeherCraft.MOD_ID)
	public static class STORAGE {
		
		/** Automatically registers ItemBlocks for standard blocks. */
		private static ItemBlock[] BLOCK_ITEMS;
	}
	
	@Mod.EventBusSubscriber(modid = ZeherCraft.MOD_ID)
	public static class TRANSPORT {
		
		private static ItemBlock[] BLOCK_ITEMS;
		
		public static final Block BLOCK_ENERGY_CHANNEL = new BlockEnergyChannel("block_energy_channel", Material.IRON, "pickaxe", 2, 5, 4, CreativeTabHandler.TAB_DEVICES);
		public static final ItemBlock ITEMBLOCK_ENERGY_CHANNEL = new ModItemBlock(BLOCK_ENERGY_CHANNEL, "Basic block for transporting energy.", "Transports RF.", "Max transfer rate: " + ZCReference.RESOURCE.TRANSPORT.ENERGY_MAX_TRANSFER + " RF/t.");
		
		public static final Block BLOCK_ENERGY_CHANNEL_TRANSPARENT = new BlockEnergyChannel("block_energy_channel_transparent", Material.IRON, "pickaxe", 2, 5, 4, CreativeTabHandler.TAB_DEVICES);
		public static final ItemBlock ITEMBLOCK_ENERGY_CHANNEL_TRANSPARENT = new ModItemBlock(BLOCK_ENERGY_CHANNEL_TRANSPARENT, "Basic block for transporting energy.", "Transports RF. Clear to be able to see energy transfer.", "Max transfer rate: " + ZCReference.RESOURCE.TRANSPORT.ENERGY_MAX_TRANSFER + " RF/t.");
		
		public static final Block BLOCK_ENERGY_CHANNEL_SURGE = new BlockEnergyChannelSurge("block_energy_channel_surge", Material.IRON, "pickaxe", 2, 5, 4, CreativeTabHandler.TAB_DEVICES);
		public static final ItemBlock ITEMBLOCK_ENERGY_CHANNEL_SURGE = new ModItemBlock(BLOCK_ENERGY_CHANNEL_SURGE, "Advanced block for transporting energy.", "Transports RF.", "Max transfer rate: " + ZCReference.RESOURCE.TRANSPORT.ENERGY_MAX_TRANSFER_SURGE + " RF/t.");
		
		public static final Block BLOCK_ENERGY_CHANNEL_TRANSPARENT_SURGE = new BlockEnergyChannelTransparentSurge("block_energy_channel_transparent_surge", Material.IRON, "pickaxe", 2, 5, 4, CreativeTabHandler.TAB_DEVICES);
		public static final ItemBlock ITEMBLOCK_ENERGY_CHANNEL_TRANSPARENT_SURGE = new ModItemBlock(BLOCK_ENERGY_CHANNEL_TRANSPARENT_SURGE, "Advanced block for transporting energy.", "Transports RF. Clear to be able to see energy transfer.", "Max transfer rate: " + ZCReference.RESOURCE.TRANSPORT.ENERGY_MAX_TRANSFER_SURGE + " RF/t.");
		
		@SubscribeEvent
		public static void registerB(RegistryEvent.Register<Block> event) {
			RegistryHandler.REGISTRY_EVENT.registerAllBlocks(event, 
					BLOCK_ENERGY_CHANNEL, BLOCK_ENERGY_CHANNEL_TRANSPARENT, 
					BLOCK_ENERGY_CHANNEL_SURGE, BLOCK_ENERGY_CHANNEL_TRANSPARENT_SURGE);
		}

		@SubscribeEvent 
		public static void registerI(RegistryEvent.Register<Item> event){
			RegistryHandler.REGISTRY_EVENT.registerAllItemBlocks(event, 
					ITEMBLOCK_ENERGY_CHANNEL, ITEMBLOCK_ENERGY_CHANNEL_TRANSPARENT, 
					ITEMBLOCK_ENERGY_CHANNEL_SURGE, ITEMBLOCK_ENERGY_CHANNEL_TRANSPARENT_SURGE);
			
			if (BLOCK_ITEMS != null) {
				RegistryHandler.REGISTRY_EVENT.registerItemBlockArray(event, BLOCK_ITEMS);
			}
		}
		
		@SubscribeEvent
		public static void registerModels(final ModelRegistryEvent event) {
			RegistryHandler.REGISTRY_EVENT.registerAllModels(event, 
					BLOCK_ENERGY_CHANNEL, BLOCK_ENERGY_CHANNEL_TRANSPARENT, 
					BLOCK_ENERGY_CHANNEL_SURGE, BLOCK_ENERGY_CHANNEL_TRANSPARENT_SURGE);
		}	
	}
	
	@Mod.EventBusSubscriber(modid = ZeherCraft.MOD_ID)
	public static class BASE {

		private static ItemBlock[] BLOCK_ITEMS;
		
		public static final Block BLOCK_BASALTCOBBLESTONE = new ModBlock("block_basaltcobblestone", Material.ROCK, "pickaxe", 1, 2, 2, CreativeTabHandler.TAB_BLOCKS);
		public static final Block BLOCK_BASALT = new ModBlockDropsBlock("block_basalt", Material.ROCK, "pickaxe", 1, 2, 2, CreativeTabHandler.TAB_BLOCKS, BLOCK_BASALTCOBBLESTONE);
		public static final Block BLOCK_BASALTBRICK = new ModBlock("block_basaltbrick", Material.ROCK, "pickaxe", 1, 4, 4, CreativeTabHandler.TAB_BLOCKS);
		public static final Block BLOCK_BASALTSMOOTH = new ModBlock("block_basaltsmooth", Material.ROCK, "pickaxe", 1, 4, 4, CreativeTabHandler.TAB_BLOCKS);
		
		public static final Block BLOCK_MARBLECOBBLESTONE = new ModBlock("block_marblecobblestone", Material.ROCK, "pickaxe", 1, 2, 2, CreativeTabHandler.TAB_BLOCKS);
		public static final Block BLOCK_MARBLE = new ModBlockDropsBlock("block_marble", Material.ROCK, "pickaxe", 1, 2, 2, CreativeTabHandler.TAB_BLOCKS, BLOCK_MARBLECOBBLESTONE);
		public static final Block BLOCK_MARBLEBRICK = new ModBlock("block_marblebrick", Material.ROCK, "pickaxe", 1, 4, 4, CreativeTabHandler.TAB_BLOCKS);
		public static final Block BLOCK_MARBLESMOOTH = new ModBlock("block_marblesmooth", Material.ROCK, "pickaxe", 1, 4, 4, CreativeTabHandler.TAB_BLOCKS);
		
		public static final Block BLOCK_GLASS_BLACK = new ModBlockConnectedGlass("block_glass_black", "pickaxe", 3, 1, 1, CreativeTabHandler.TAB_BLOCKS);
		
		public static final Block BLOCK_REINFORCEDSTONE = new ModBlock("block_reinforcedstone", Material.ROCK, "pickaxe", 3, 20, 45, CreativeTabHandler.TAB_BLOCKS);
		public static final Block BLOCK_REINFORCEDBRICK = new ModBlock("block_reinforcedbrick", Material.ROCK, "pickaxe", 3, 20, 45, CreativeTabHandler.TAB_BLOCKS);
		public static final Block BLOCK_REINFORCEDGLASS = new ModBlockConnectedGlass("block_reinforcedglass", "pickaxe", 3, 20, 45, CreativeTabHandler.TAB_BLOCKS);
		
		public static final Block BLOCK_WITHERSTONE = new ModBlock("block_witherstone", Material.ROCK, "pickaxe", 3, 40, 90, CreativeTabHandler.TAB_BLOCKS);
		public static final Block BLOCK_WITHERBRICK = new ModBlock("block_witherbrick", Material.ROCK, "pickaxe", 3, 40, 90, CreativeTabHandler.TAB_BLOCKS);
		public static final Block BLOCK_WITHERGLASS = new ModBlockConnectedGlass("block_witherglass", "pickaxe", 3, 40, 90, CreativeTabHandler.TAB_BLOCKS);
		
		public static final Block BLOCK_ORE_COPPER = new ModBlock("block_ore_copper", Material.ROCK, "pickaxe", 1, 3, 8, CreativeTabHandler.TAB_BLOCKS);
		public static final Block BLOCK_ORE_TIN = new ModBlock("block_ore_tin", Material.ROCK, "pickaxe", 1, 3, 8, CreativeTabHandler.TAB_BLOCKS);
		public static final Block BLOCK_ORE_SILVER = new ModBlock("block_ore_silver", Material.ROCK, "pickaxe", 2, 5, 8, CreativeTabHandler.TAB_BLOCKS);
		public static final Block BLOCK_ORE_SILICON = new ModBlock("block_ore_silicon", Material.ROCK, "pickaxe", 2, 4, 3, CreativeTabHandler.TAB_BLOCKS);
		
		public static final Block BLOCK_COPPER = new ModBlock("block_copper", Material.IRON, "pickaxe", 1, 3, 8, CreativeTabHandler.TAB_BLOCKS);
		public static final Block BLOCK_TIN = new ModBlock("block_tin", Material.IRON, "pickaxe", 1, 3, 8, CreativeTabHandler.TAB_BLOCKS);
		public static final Block BLOCK_SILVER = new ModBlock("block_silver", Material.IRON, "pickaxe", 1, 5, 8, CreativeTabHandler.TAB_BLOCKS);
		public static final Block BLOCK_SILICON = new ModBlock("block_silicon", Material.IRON, "pickaxe", 2, 4, 5, CreativeTabHandler.TAB_BLOCKS);
		
		public static final Block BLOCK_STEEL = new ModBlock("block_steel", Material.IRON, "pickaxe", 2, 5, 10, CreativeTabHandler.TAB_BLOCKS);
		public static final Block BLOCK_BRONZE = new ModBlock("block_bronze", Material.IRON, "pickaxe", 2, 4, 8, CreativeTabHandler.TAB_BLOCKS);
		public static final Block BLOCK_BRASS = new ModBlock("block_brass", Material.IRON, "pickaxe", 2, 4, 6, CreativeTabHandler.TAB_BLOCKS);

		@SubscribeEvent
		public static void registerB(RegistryEvent.Register<Block> event) {
			BLOCK_ITEMS = RegistryHandler.REGISTRY_EVENT.getItemBlocks(event, 
					BLOCK_BASALTCOBBLESTONE, BLOCK_BASALT, BLOCK_BASALTBRICK, BLOCK_BASALTSMOOTH,
					BLOCK_MARBLECOBBLESTONE, BLOCK_MARBLE, BLOCK_MARBLEBRICK, BLOCK_MARBLESMOOTH,
					
					BLOCK_GLASS_BLACK,
					
					BLOCK_REINFORCEDSTONE, BLOCK_REINFORCEDBRICK, BLOCK_REINFORCEDGLASS,
					BLOCK_WITHERSTONE, BLOCK_WITHERBRICK, BLOCK_WITHERGLASS,
					
					BLOCK_ORE_COPPER, BLOCK_ORE_TIN, BLOCK_ORE_SILVER, BLOCK_ORE_SILICON,
					BLOCK_COPPER, BLOCK_TIN, BLOCK_SILVER, BLOCK_SILICON,
					BLOCK_STEEL, BLOCK_BRONZE, BLOCK_BRASS);
		}
		
		@SubscribeEvent
		public static void registerI(RegistryEvent.Register<Item> event) {
			if (BLOCK_ITEMS != null) {
				RegistryHandler.REGISTRY_EVENT.registerAllItemBlocks(event, BLOCK_ITEMS);
			}
		}
		
		@SubscribeEvent
		public static void registerModels(final ModelRegistryEvent event) {
			RegistryHandler.REGISTRY_EVENT.registerAllModels(event, 
					BLOCK_BASALTCOBBLESTONE, BLOCK_BASALT, BLOCK_BASALTBRICK, BLOCK_BASALTSMOOTH,
					BLOCK_MARBLECOBBLESTONE, BLOCK_MARBLE, BLOCK_MARBLEBRICK, BLOCK_MARBLESMOOTH,
					
					BLOCK_GLASS_BLACK,
					
					BLOCK_REINFORCEDSTONE, BLOCK_REINFORCEDBRICK, BLOCK_REINFORCEDGLASS,
					BLOCK_WITHERSTONE, BLOCK_WITHERBRICK, BLOCK_WITHERGLASS,
					
					BLOCK_ORE_COPPER, BLOCK_ORE_TIN, BLOCK_ORE_SILVER, BLOCK_ORE_SILICON,
					BLOCK_COPPER, BLOCK_TIN, BLOCK_SILVER, BLOCK_SILICON,
					BLOCK_STEEL, BLOCK_BRONZE, BLOCK_BRASS);
		}
	}
	
	@Mod.EventBusSubscriber(modid = ZeherCraft.MOD_ID)
	public static class FLUID {

		public static ModBlockFluid BLOCK_FLUID_COOLANT;
		public static ModBlockFluid BLOCK_FLUID_ENERGIZED_REDSTONE;
		public static ModBlockFluid BLOCK_FLUID_GLOWSTONE_INFUSED_MAGMA;
		
		/**
		 * Define all Fluid Blocks
		 */
		public static void preInitialization() {
			BLOCK_FLUID_COOLANT = new ModBlockFluid(FluidRegistry.getFluid("coolant"), Material.WATER, "coolant");
			BLOCK_FLUID_ENERGIZED_REDSTONE = new ModBlockFluid(FluidRegistry.getFluid("energized_redstone"), Material.WATER, "energized_redstone");
			BLOCK_FLUID_GLOWSTONE_INFUSED_MAGMA = new ModBlockFluid(FluidRegistry.getFluid("glowstone_infused_magma"), Material.LAVA, "glowstoneinfused_magma");
		}
		
		@SubscribeEvent
		public static void registerB(RegistryEvent.Register<Block> event) {
			RegistryHandler.REGISTRY_EVENT.registerAllBlocks(event, 
					BLOCK_FLUID_COOLANT, 
					BLOCK_FLUID_ENERGIZED_REDSTONE, 
					BLOCK_FLUID_GLOWSTONE_INFUSED_MAGMA);
		}
		
		@SubscribeEvent
		public static void registerModels(final ModelRegistryEvent event) {
			RegistryHandler.REGISTRY_EVENT.registerAllFluidModels(event, 
					BLOCK_FLUID_COOLANT, 
					BLOCK_FLUID_ENERGIZED_REDSTONE, 
					BLOCK_FLUID_GLOWSTONE_INFUSED_MAGMA);
		}
	}
}