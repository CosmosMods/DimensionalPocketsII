package com.zeher.zehercraft;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.EnumHelper;

public class ZCReference {
	
	public static class RESOURCE {
		
		/**
		 * Prefix for all ResourceLocations.
		 */
		public static final String PRE = ZeherCraft.MOD_ID + ":";
		public static final String RESOURCE = PRE + "textures/";
		
		/**
		 * ResourceLocations for Base Objects.
		 */
		public static class BASE {
			public static final String BASE = RESOURCE + "base/";
			public static final String BLOCKS = BASE + "blocks/";
			public static final String FLUID = PRE + "base/blocks/fluid/";
			public static final String ITEMS = BASE + "items/";
			public static final String GUI = BASE + "gui/";
			
			public static final ResourceLocation GUI_ALARM = new ResourceLocation(GUI + "utility/gui_alarm.png");
			
			public static final ResourceLocation FLUID_COOLANT_STILL = new ResourceLocation(FLUID + "coolant/coolant_still");
			public static final ResourceLocation FLUID_COOLANT_FLOWING = new ResourceLocation(FLUID + "coolant/coolant_flow");
			
			public static final ResourceLocation FLUID_ENERGIZED_REDSTONE_STILL= new ResourceLocation(FLUID + "energized_redstone/energized_redstone_still");
			public static final ResourceLocation FLUID_ENERGIZED_REDSTONE_FLOWING = new ResourceLocation(FLUID + "energized_redstone/energized_redstone_flow");
			
			public static final ResourceLocation FLUID_GLOWSTONE_INFUSED_MAGMA_STILL = new ResourceLocation(FLUID + "glowstone_infused_magma/glowstone_infused_magma_still");
			public static final ResourceLocation FLUID_GLOWSTONE_INFUSED_MAGMA_FLOWING = new ResourceLocation(FLUID + "glowstone_infused_magma/glowstone_infused_magma_flow");
		}
		
		/**
		 * ResourceLocations for Processing Objects.
		 */
		public static class PROCESSING {
			public static final String PROCESSING = RESOURCE + "processing/";
			public static final String BLOCKS = PROCESSING + "blocks/";
			public static final String ITEMS = PROCESSING + "items/";
			public static final String GUI = PROCESSING + "gui/";
			
			public static final String TESR = PROCESSING + "tesr/";
			
			public static final String PREFIX = "gui.processing.";
			public static final String SUFFIX = ".name";
			
			public static final String BLOCK_PREFIX = "block_";
			
			public static final ResourceLocation KILN_LOC_GUI = new ResourceLocation(GUI + "kiln/gui.png");
			public static final ResourceLocation KILN_LOC_GUI_JEI = new ResourceLocation(GUI + "kiln/kiln_jei.png");
			public static final ResourceLocation KILN_LOC_TESR = new ResourceLocation(TESR + "kiln/internals.png");
			public static final String KILN_NAME = PREFIX + "kiln" + SUFFIX;
			public static final int KILN_INDEX = 1;
			
			public static final ResourceLocation GRINDER_LOC_GUI = new ResourceLocation(GUI + "grinder/gui.png");
			public static final ResourceLocation GRINDER_LOC_GUI_JEI = new ResourceLocation(GUI + "grinder/grinder_jei.png");
			public static final ResourceLocation GRINDER_LOC_TESR = new ResourceLocation(TESR + "grinder/internals.png");
			public static final String GRINDER_NAME = PREFIX + "grinder" + SUFFIX;
			public static final int GRINDER_INDEX = 2;
			
			public static final ResourceLocation COMPACTOR_LOC_GUI = new ResourceLocation(GUI + "compactor/gui.png");
			public static final ResourceLocation COMPACTOR_LOC_GUI_JEI = new ResourceLocation(GUI + "compactor/compactor_jei.png");
			public static final ResourceLocation COMPACTOR_LOC_TESR = new ResourceLocation(TESR + "compactor/internals.png");
			public static final String COMPACTOR_NAME = PREFIX + "compactor" + SUFFIX;
			public static final int COMPACTOR_INDEX = 3;
			
			public static final ResourceLocation SEPARATOR_LOC_GUI = new ResourceLocation(GUI + "separator/gui.png");
			public static final ResourceLocation SEPARATOR_LOC_GUI_JEI = new ResourceLocation(GUI + "separator/separator_jei.png");
			public static final ResourceLocation SEPARATOR_LOC_TESR = new ResourceLocation(TESR + "separator/internals.png");
			public static final String SEPARATOR_NAME = PREFIX + "seperator" + SUFFIX;
			public static final int SEPARATOR_INDEX = 4;
			
			public static final ResourceLocation CHARGER_LOC_GUI = new ResourceLocation(GUI + "charger/gui.png");
			public static final ResourceLocation CHARGER_LOC_TESR = new ResourceLocation(TESR + "charger/model.png");
			public static final String CHARGER_NAME = PREFIX + "charger" + SUFFIX;
			public static final int CHARGER_INDEX = 5;
			
			public static final ResourceLocation ORE_PLANT_LOC_GUI = new ResourceLocation(GUI + "ore_plant/gui.png");
			public static final ResourceLocation ORE_PLANT_LOC_TESR = new ResourceLocation(TESR + "ore_plant/model.png");
			public static final String ORE_PLANT_NAME = PREFIX + "ore_plant" + SUFFIX;
			public static final int ORE_PLANT_INDEX = 6;
			
			public static final ResourceLocation FLUID_CRAFTER_LOC_GUI = new ResourceLocation(GUI + "fluid_crafter/gui.png");
			public static final ResourceLocation FLUID_CRAFTER_LOC_TESR = new ResourceLocation(TESR + "fluid_crafter/model.png");
			public static final String FLUID_CRAFTER_NAME = PREFIX + "fluid_crafter" + SUFFIX;
			public static final int FLUID_CRAFTER_INDEX = 7;
			
			public static final ResourceLocation SYNTHESISER_LOC_GUI = new ResourceLocation(GUI + "synthesiser/synthesiser_gui.png");
			public static final ResourceLocation SYNTHESISER_LOC_GUI_JEI = new ResourceLocation(GUI + "synthesiser/synthesiser_jei.png");
			public static final ResourceLocation SYNTHESISER_LOC_GUI_JEI_LASER_2 = new ResourceLocation(GUI + "synthesiser/synthesiser_jei_laser_2.png");
			public static final ResourceLocation SYNTHESISER_LOC_GUI_JEI_LASER_4 = new ResourceLocation(GUI + "synthesiser/synthesiser_jei_laser_4.png");
			public static final ResourceLocation SYNTHESISER_LOC_GUI_JEI_LASER_8 = new ResourceLocation(GUI + "synthesiser/synthesiser_jei_laser_8.png");
			public static final ResourceLocation SYNTHESISER_LOC_TESR = new ResourceLocation(BLOCKS + "synthesiser/synthesiser.png");
			public static final String SYNTHESISER_NAME = PREFIX + "synthesiser" + SUFFIX;
			public static final int SYNTHESISER_INDEX = 8;
			
			public static final String SYNTHESISER_STAND = PREFIX + "synthesiser_stand" + SUFFIX;
			public static final int SYNTHESISER_STAND_INDEX = 9;

			public static final int[] LASER_JEI_ARRAY_X = new int[] { 0, 0, 0, 0, 60, 60, 60, 60, 120, 120, 120, 120,180, 180, 180, 180 };
			public static final int[] LASER_JEI_ARRAY_Y = new int[] { 0, 60, 120, 180, 0, 60, 120, 180, 0, 60, 120, 180, 0, 60, 120, 180 };
			
			/** Values */
			public static final int[] CAPACITY = new int[] { 40000, 50000, 60000, 70000, 80000 };
			public static final int[] MAX_INPUT = new int [] { 200, 300, 400, 500, 600 };
			
			public static final int[] RF_TICK_RATE = new int[] { 40, 60, 80, 100, 120 };
			public static final int[] SPEED_RATE = new int[] { 80, 70, 60, 50, 40 };
			
			public static final int[] CAPACITY_U = new int[] { 80000, 90000, 100000, 120000, 120000 };
			public static final int[] MAX_INPUT_U = new int[] { 600, 700, 800, 900, 1000 };
			
			public static final int[] RF_TICK_RATE_U = new int[] { 100, 120, 140, 160, 180 };
			public static final int[] SPEED_RATE_U = new int[] { 35, 30, 25, 20, 15 };
		}
		
		/**
		 * ResourceLocations for Production Objects.
		 */
		public static class PRODUCTION {
			public static final String PRODUCTION = RESOURCE + "production/";
			public static final String BLOCKS = PRODUCTION + "blocks/";
			public static final String ITEMS = PRODUCTION + "items/";
			public static final String GUI = PRODUCTION + "gui/";

			public static final String TESR = PRODUCTION + "tesr/";
			
			/** Gui */
			public static final ResourceLocation GUI_SCALED_ELEMENTS = new ResourceLocation(GUI + "scaled_elements.png");
			
			public static final ResourceLocation GUI_SOLIDFUEL = new ResourceLocation(GUI + "gui_solid_fuel.png");
			public static final ResourceLocation GUI_HEATEDFLUID = new ResourceLocation(GUI + "gui_heatedfluid.png");
			public static final ResourceLocation GUI_PELTIER = new ResourceLocation(GUI + "gui_peltier.png");
			public static final ResourceLocation GUI_SOLARPANEL = new ResourceLocation(GUI + "gui_solarpanel.png");
			
			/** Localised Names */
			public static final String SOLIDFUEL = "gui.production.solid_fuel.name";
			public static final String SOLARPANEL = "gui.production.solar.name";
			public static final String HEATEDFLUID = "gui.production.heated_fluid.name";
			public static final String PELTIER = "gui.production.peltier.name";
			
			/** Values */
			public static final int CAPACITY = 60000;
			public static final int MAX_OUTPUT = 500;
			
			public static final int[] RF_TICK_RATE = new int[] { 200, 300, 400, 500 };
			public static final int[] SPEED_RATE = new int[] { 100, 150, 200, 250 };
			
			public static final int CAPACITY_U = 120000;
			public static final int MAX_OUTPUT_U = 1000;
			
			public static final int[] RF_TICK_RATE_U = new int[] { 250, 500, 750, 1000 };
			public static final int[] SPEED_RATE_U = new int[] { 100, 150, 200, 250 };
		}
		
		/**
		 * ResourceLocations for Storage Objects.
		 */
		public static class STORAGE {
			public static final String STORAGE = RESOURCE + "storage/";
			public static final String BLOCKS = STORAGE + "blocks/";
			public static final String ITEMS = STORAGE + "items/";
			public static final String GUI = STORAGE + "gui/";

			/** Gui */
			public static final ResourceLocation GUI_CAPACITOR = new ResourceLocation(GUI + "storage/capacitor/gui_capacitor.png");
			
			/** Localised Names */
			public static final String P_CAPACITOR = "gui.storage.powered.capacitor.name";
			public static final String E_CAPACITOR = "gui.storage.energized.capacitor.name";
			
			public static final String MECHANISEDSTORAGESMALL = "gui.storage.mechanisedstoragesmall.name";
			public static final String MECHANISEDSTORAGELARGE = "gui.storage.mechanisedstoragelarge.name";
			
			/** Values */
			public static final int CAPACITY = 500000;
			public static final int MAX_INPUT = 1000;
			public static final int MAX_OUTPUT = 1000;
			
			public static final int CAPACITY_U = 1000000;
			public static final int MAX_INPUT_U = 2000;
			public static final int MAX_OUTPUT_U = 2000;
		}
		
		/**
		 * ResourceLocation for Transport Objects.
		 */
		public static class TRANSPORT {
			
			//Bounding Boxes for "standard" pipes.
			public static final AxisAlignedBB[] BOUNDING_BOXES_STANDARD = new AxisAlignedBB[] {
					new AxisAlignedBB(0.3D, 0.3D, 0.3D, 0.7D, 0.7D, 0.7D), // BASE 0
					new AxisAlignedBB(0.3D, 0.0D, 0.3D, 0.7D, 0.7D, 0.7D), // DOWN 1

					new AxisAlignedBB(0.3D, 0.3D, 0.3D, 0.7D, 1.0D, 0.7D), // UP 2
					new AxisAlignedBB(0.3D, 0.0D, 0.3D, 0.7D, 1.0D, 0.7D), // UP-DOWN 3

					new AxisAlignedBB(0.3D, 0.3D, 0.0D, 0.7D, 0.7D, 0.7D), // NORTH 4
					new AxisAlignedBB(0.3D, 0.0D, 0.0D, 0.7D, 0.7D, 0.7D), // NORTH-DOWN 5
					new AxisAlignedBB(0.3D, 0.3D, 0.0D, 0.7D, 1.0D, 0.7D), // NORTH-UP 6
					new AxisAlignedBB(0.3D, 0.0D, 0.0D, 0.7D, 1.0D, 0.7D), // NORTH-UP-DOWN 7

					new AxisAlignedBB(0.3D, 0.3D, 0.3D, 0.7D, 0.7D, 1.0D), // SOUTH 8
					new AxisAlignedBB(0.3D, 0.0D, 0.3D, 0.7D, 0.7D, 1.0D), // SOUTH-DOWN 9
					new AxisAlignedBB(0.3D, 0.3D, 0.3D, 0.7D, 1.0D, 1.0D), // SOUTH-UP 10
					new AxisAlignedBB(0.3D, 0.0D, 0.3D, 0.7D, 1.0D, 1.0D), // SOUTH-UP-DOWN 11

					new AxisAlignedBB(0.3D, 0.3D, 0.0D, 0.7D, 0.7D, 1.0D), // NORTH-SOUTH 12
					new AxisAlignedBB(0.3D, 0.0D, 0.0D, 0.7D, 0.7D, 1.0D), // NORTH-SOUTH-DOWN 13
					new AxisAlignedBB(0.3D, 0.3D, 0.0D, 0.7D, 1.0D, 1.0D), // NORTH-SOUTH-UP 14
					new AxisAlignedBB(0.3D, 0.0D, 0.0D, 0.7D, 1.0D, 1.0D), // NORTH-SOUTH-UP-DOWN 15

					new AxisAlignedBB(0.0D, 0.3D, 0.3D, 0.7D, 0.7D, 0.7D), // WEST 16
					new AxisAlignedBB(0.0D, 0.0D, 0.3D, 0.7D, 0.7D, 0.7D), // WEST-DOWN 17
					new AxisAlignedBB(0.0D, 0.3D, 0.3D, 0.7D, 1.0D, 0.7D), // WEST-UP 18
					new AxisAlignedBB(0.0D, 0.0D, 0.3D, 0.7D, 1.0D, 0.7D), // WEST-UP-DOWN 19

					new AxisAlignedBB(0.0D, 0.3D, 0.0D, 0.7D, 0.7D, 0.7D), // NORTH-WEST 20
					new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.7D, 0.7D, 0.7D), // NORTH-WEST-DOWN 21
					new AxisAlignedBB(0.0D, 0.3D, 0.0D, 0.7D, 1.0D, 0.7D), // NORTH-WEST-UP 22
					new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.7D, 1.0D, 0.7D), // NORTH-WEST-UP-DOWN 23

					new AxisAlignedBB(0.0D, 0.3D, 0.3D, 0.7D, 0.7D, 1.0D), // SOUTH-WEST 24
					new AxisAlignedBB(0.0D, 0.0D, 0.3D, 0.7D, 0.7D, 1.0D), // SOUTH-WEST-DOWN 25
					new AxisAlignedBB(0.0D, 0.3D, 0.3D, 0.7D, 1.0D, 1.0D), // SOUTH-WEST-UP 26
					new AxisAlignedBB(0.0D, 0.0D, 0.3D, 0.7D, 1.0D, 1.0D), // SOUTH-WEST-UP-DOWN 27

					new AxisAlignedBB(0.0D, 0.3D, 0.0D, 0.7D, 0.7D, 1.0D), // NORTH-SOUTH-WEST 28
					new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.7D, 0.7D, 1.0D), // NORTH-SOUTH-WEST-DOWN 29
					new AxisAlignedBB(0.0D, 0.3D, 0.0D, 0.7D, 1.0D, 1.0D), // NORTH-SOUTH-WEST-UP 30
					new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.7D, 1.0D, 1.0D), // NORTH-SOUTH-WEST-UP-DOWN 31

					new AxisAlignedBB(0.3D, 0.3D, 0.3D, 1.0D, 0.7D, 0.7D), // EAST 32
					new AxisAlignedBB(0.3D, 0.0D, 0.3D, 1.0D, 0.7D, 0.7D), // EAST-DOWN 33
					new AxisAlignedBB(0.3D, 0.3D, 0.3D, 1.0D, 1.0D, 0.7D), // EAST-UP 34
					new AxisAlignedBB(0.3D, 0.0D, 0.3D, 1.0D, 1.0D, 0.7D), // EAST-UP-DOWN 35

					new AxisAlignedBB(0.3D, 0.3D, 0.0D, 1.0D, 0.7D, 0.7D), // NORTH-EAST 36
					new AxisAlignedBB(0.3D, 0.0D, 0.0D, 1.0D, 0.7D, 0.7D), // NORTH-EAST-DOWN 37
					new AxisAlignedBB(0.3D, 0.3D, 0.0D, 1.0D, 1.0D, 0.7D), // NORTH-EAST-UP 38
					new AxisAlignedBB(0.3D, 0.0D, 0.0D, 1.0D, 1.0D, 0.7D), // NORTH-EAST-UP-DOWN 39

					new AxisAlignedBB(0.3D, 0.3D, 0.3D, 1.0D, 0.7D, 1.0D), // SOUTH-EAST 40
					new AxisAlignedBB(0.3D, 0.0D, 0.3D, 1.0D, 0.7D, 1.0D), // SOUTH-EAST-DOWN 41
					new AxisAlignedBB(0.3D, 0.3D, 0.3D, 1.0D, 1.0D, 1.0D), // SOUTH-EAST-UP 42
					new AxisAlignedBB(0.3D, 0.0D, 0.3D, 1.0D, 1.0D, 1.0D), // SOUTH-EAST-UP-DOWN 43

					new AxisAlignedBB(0.3D, 0.3D, 0.0D, 1.0D, 0.7D, 1.0D), // NORTH-SOUTH-EAST 44
					new AxisAlignedBB(0.3D, 0.0D, 0.0D, 1.0D, 0.7D, 1.0D), // NORTH-SOUTH-EAST-DOWN 45
					new AxisAlignedBB(0.3D, 0.3D, 0.0D, 1.0D, 1.0D, 1.0D), // NORTH-SOUTH-EAST-UP 46
					new AxisAlignedBB(0.3D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), // NORTH-SOUTH-EAAT-UP-DOWN 47

					new AxisAlignedBB(0.0D, 0.3D, 0.3D, 1.0D, 0.7D, 0.7D), // EAST-WEST 48
					new AxisAlignedBB(0.0D, 0.0D, 0.3D, 1.0D, 0.7D, 0.7D), // EAST-WEST-DOWN 49
					new AxisAlignedBB(0.0D, 0.3D, 0.3D, 1.0D, 1.0D, 0.7D), // EAST-WEST-UP 50
					new AxisAlignedBB(0.0D, 0.0D, 0.3D, 1.0D, 1.0D, 0.7D), // EAST-WEST-UP=DOWN 51

					new AxisAlignedBB(0.0D, 0.3D, 0.0D, 1.0D, 0.7D, 0.7D), // EAST-WEST-NORTH 52
					new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.7D, 0.7D), // EAST-WEST-NORTH-DOWN 53
					new AxisAlignedBB(0.0D, 0.3D, 0.0D, 1.0D, 1.0D, 0.7D), // EAST-WEST-NORTH-UP 54
					new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.7D), // EAST-WEST-NORTH-UP-DOWN 55

					new AxisAlignedBB(0.0D, 0.3D, 0.3D, 1.0D, 0.7D, 1.0D), // SOUTH-EAST-WEST 56
					new AxisAlignedBB(0.0D, 0.0D, 0.3D, 1.0D, 0.7D, 1.0D), // SOUTH-EAST-WEST-DOWN 57
					new AxisAlignedBB(0.0D, 0.3D, 0.3D, 1.0D, 1.0D, 1.0D), // SOUTH-EAST-WEST-UP 58
					new AxisAlignedBB(0.0D, 0.0D, 0.3D, 1.0D, 1.0D, 1.0D), // SOUTH-EAST-WEST-UP-DOWN 59

					new AxisAlignedBB(0.0D, 0.3D, 0.0D, 1.0D, 0.7D, 1.0D), // SOUTH-NORTH-EAST-WEST 60
					new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.7D, 1.0D), // SOUTH-NORTH-EAST-WEST-DOWN 61
					new AxisAlignedBB(0.0D, 0.3D, 0.0D, 1.0D, 1.0D, 1.0D), // SOUTH-NORTH-EAST-WEST-UP 62
					new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)  /* SOUTH-NORTH-EAST-WEST-UP-DOWN 63*/ };
			
			//Bounding Boxes for "surge" pipes.
			public static final AxisAlignedBB[] BOUNDING_BOXES_STANDARD_SURGE = new AxisAlignedBB[] {
						new AxisAlignedBB(0.25D, 0.25D, 0.25D, 0.75D, 0.75D, 0.75D), // BASE 0
						new AxisAlignedBB(0.25D, 0.00D, 0.25D, 0.75D, 0.75D, 0.75D), // DOWN 1
						new AxisAlignedBB(0.25D, 0.25D, 0.25D, 0.75D, 1.00D, 0.75D), // UP 2
						new AxisAlignedBB(0.25D, 0.00D, 0.25D, 0.75D, 1.00D, 0.75D), // UP-DOWN 3
						new AxisAlignedBB(0.25D, 0.25D, 0.00D, 0.75D, 0.75D, 0.75D), // NORTH 4
						new AxisAlignedBB(0.25D, 0.00D, 0.00D, 0.75D, 0.75D, 0.75D), // NORTH-DOWN 5
						new AxisAlignedBB(0.25D, 0.25D, 0.00D, 0.75D, 1.00D, 0.75D), // NORTH-UP 6
						new AxisAlignedBB(0.25D, 0.00D, 0.00D, 0.75D, 1.00D, 0.75D), // NORTH-UP-DOWN 7
						new AxisAlignedBB(0.25D, 0.25D, 0.25D, 0.75D, 0.75D, 1.00D), // SOUTH 8
						new AxisAlignedBB(0.25D, 0.00D, 0.25D, 0.75D, 0.75D, 1.00D), // SOUTH-DOWN 9
						new AxisAlignedBB(0.25D, 0.25D, 0.25D, 0.75D, 1.00D, 1.00D), // SOUTH-UP 10
						new AxisAlignedBB(0.25D, 0.00D, 0.25D, 0.75D, 1.00D, 1.00D), // SOUTH-UP-DOWN 11
						new AxisAlignedBB(0.25D, 0.25D, 0.00D, 0.75D, 0.75D, 1.00D), // NORTH-SOUTH 12
						new AxisAlignedBB(0.25D, 0.00D, 0.00D, 0.75D, 0.75D, 1.00D), // NORTH-SOUTH-DOWN 13
						new AxisAlignedBB(0.25D, 0.25D, 0.00D, 0.75D, 1.00D, 1.00D), // NORTH-SOUTH-UP 14
						new AxisAlignedBB(0.25D, 0.00D, 0.00D, 0.75D, 1.00D, 1.00D), // NORTH-SOUTH-UP-DOWN 15
						new AxisAlignedBB(0.00D, 0.25D, 0.25D, 0.75D, 0.75D, 0.75D), // WEST 16
						new AxisAlignedBB(0.00D, 0.00D, 0.25D, 0.75D, 0.75D, 0.75D), // WEST-DOWN 17
						new AxisAlignedBB(0.00D, 0.25D, 0.25D, 0.75D, 1.00D, 0.75D), // WEST-UP 18
						new AxisAlignedBB(0.00D, 0.00D, 0.25D, 0.75D, 1.00D, 0.75D), // WEST-UP-DOWN 19
						new AxisAlignedBB(0.00D, 0.25D, 0.00D, 0.75D, 0.75D, 0.75D), // NORTH-WEST 20
						new AxisAlignedBB(0.00D, 0.00D, 0.00D, 0.75D, 0.75D, 0.75D), // NORTH-WEST-DOWN 21
						new AxisAlignedBB(0.00D, 0.25D, 0.00D, 0.75D, 1.00D, 0.75D), // NORTH-WEST-UP 22
						new AxisAlignedBB(0.00D, 0.00D, 0.00D, 0.75D, 1.00D, 0.75D), // NORTH-WEST-UP-DOWN 23
						new AxisAlignedBB(0.00D, 0.25D, 0.25D, 0.75D, 0.75D, 1.00D), // SOUTH-WEST 24
						new AxisAlignedBB(0.00D, 0.00D, 0.25D, 0.75D, 0.75D, 1.00D), // SOUTH-WEST-DOWN 25
						new AxisAlignedBB(0.00D, 0.25D, 0.25D, 0.75D, 1.00D, 1.00D), // SOUTH-WEST-UP 26
						new AxisAlignedBB(0.00D, 0.00D, 0.25D, 0.75D, 1.00D, 1.00D), // SOUTH-WEST-UP-DOWN 27
						new AxisAlignedBB(0.00D, 0.25D, 0.00D, 0.75D, 0.75D, 1.00D), // NORTH-SOUTH-WEST 28
						new AxisAlignedBB(0.00D, 0.00D, 0.00D, 0.75D, 0.75D, 1.00D), // NORTH-SOUTH-WEST-DOWN 29
						new AxisAlignedBB(0.00D, 0.25D, 0.00D, 0.75D, 1.00D, 1.00D), // NORTH-SOUTH-WEST-UP 30
						new AxisAlignedBB(0.00D, 0.00D, 0.00D, 0.75D, 1.00D, 1.00D), // NORTH-SOUTH-WEST-UP-DOWN 31
						new AxisAlignedBB(0.25D, 0.25D, 0.25D, 1.00D, 0.75D, 0.75D), // EAST 32
						new AxisAlignedBB(0.25D, 0.00D, 0.25D, 1.00D, 0.75D, 0.75D), // EAST-DOWN 33
						new AxisAlignedBB(0.25D, 0.25D, 0.25D, 1.00D, 1.00D, 0.75D), // EAST-UP 34
						new AxisAlignedBB(0.25D, 0.00D, 0.25D, 1.00D, 1.00D, 0.75D), // EAST-UP-DOWN 35
						new AxisAlignedBB(0.25D, 0.25D, 0.00D, 1.00D, 0.75D, 0.75D), // NORTH-EAST 36
						new AxisAlignedBB(0.25D, 0.00D, 0.00D, 1.00D, 0.75D, 0.75D), // NORTH-EAST-DOWN 37
						new AxisAlignedBB(0.25D, 0.25D, 0.00D, 1.00D, 1.00D, 0.75D), // NORTH-EAST-UP 38
						new AxisAlignedBB(0.25D, 0.00D, 0.00D, 1.00D, 1.00D, 0.75D), // NORTH-EAST-UP-DOWN 39
						new AxisAlignedBB(0.25D, 0.25D, 0.25D, 1.00D, 0.75D, 1.00D), // SOUTH-EAST 40
						new AxisAlignedBB(0.25D, 0.00D, 0.25D, 1.00D, 0.75D, 1.00D), // SOUTH-EAST-DOWN 41
						new AxisAlignedBB(0.25D, 0.25D, 0.25D, 1.00D, 1.00D, 1.00D), // SOUTH-EAST-UP 42
						new AxisAlignedBB(0.25D, 0.00D, 0.25D, 1.00D, 1.00D, 1.00D), // SOUTH-EAST-UP-DOWN 43
						new AxisAlignedBB(0.25D, 0.25D, 0.00D, 1.00D, 0.75D, 1.00D), // NORTH-SOUTH-EAST 44
						new AxisAlignedBB(0.25D, 0.00D, 0.00D, 1.00D, 0.75D, 1.00D), // NORTH-SOUTH-EAST-DOWN 45
						new AxisAlignedBB(0.25D, 0.25D, 0.00D, 1.00D, 1.00D, 1.00D), // NORTH-SOUTH-EAST-UP 46
						new AxisAlignedBB(0.25D, 0.00D, 0.00D, 1.00D, 1.00D, 1.00D), // NORTH-SOUTH-EAAT-UP-DOWN 47
						new AxisAlignedBB(0.00D, 0.25D, 0.25D, 1.00D, 0.75D, 0.75D), // EAST-WEST 48
						new AxisAlignedBB(0.00D, 0.00D, 0.25D, 1.00D, 0.75D, 0.75D), // EAST-WEST-DOWN 49
						new AxisAlignedBB(0.00D, 0.25D, 0.25D, 1.00D, 1.00D, 0.75D), // EAST-WEST-UP 50
						new AxisAlignedBB(0.00D, 0.00D, 0.25D, 1.00D, 1.00D, 0.75D), // EAST-WEST-UP=DOWN 51
						new AxisAlignedBB(0.00D, 0.25D, 0.00D, 1.00D, 0.75D, 0.75D), // EAST-WEST-NORTH 52
						new AxisAlignedBB(0.00D, 0.00D, 0.00D, 1.00D, 0.75D, 0.75D), // EAST-WEST-NORTH-DOWN 53
						new AxisAlignedBB(0.00D, 0.25D, 0.00D, 1.00D, 1.00D, 0.75D), // EAST-WEST-NORTH-UP 54
						new AxisAlignedBB(0.00D, 0.00D, 0.00D, 1.00D, 1.00D, 0.75D), // EAST-WEST-NORTH-UP-DOWN 55
						new AxisAlignedBB(0.00D, 0.25D, 0.25D, 1.00D, 0.75D, 1.00D), // SOUTH-EAST-WEST 56
						new AxisAlignedBB(0.00D, 0.00D, 0.25D, 1.00D, 0.75D, 1.00D), // SOUTH-EAST-WEST-DOWN 57
						new AxisAlignedBB(0.00D, 0.25D, 0.25D, 1.00D, 1.00D, 1.00D), // SOUTH-EAST-WEST-UP 58
						new AxisAlignedBB(0.00D, 0.00D, 0.25D, 1.00D, 1.00D, 1.00D), // SOUTH-EAST-WEST-UP-DOWN 59
						new AxisAlignedBB(0.00D, 0.25D, 0.00D, 1.00D, 0.75D, 1.00D), // SOUTH-NORTH-EAST-WEST 60
						new AxisAlignedBB(0.00D, 0.00D, 0.00D, 1.00D, 0.75D, 1.00D), // SOUTH-NORTH-EAST-WEST-DOWN 61
						new AxisAlignedBB(0.00D, 0.25D, 0.00D, 1.00D, 1.00D, 1.00D), // SOUTH-NORTH-EAST-WEST-UP 62
						new AxisAlignedBB(0.00D, 0.00D, 0.00D, 1.00D, 1.00D, 1.00D)  /* SOUTH-NORTH-EAST-WEST-UP-DOWN 63*/ };
				
			//[WIP] Bounding Boxes for "thin" pipes.
			public static final AxisAlignedBB[] BOUNDING_BOXES_THIN = new AxisAlignedBB[] {};
			
			public static final String TRANSPORT = RESOURCE + "transport/";
			public static final String BLOCKS = TRANSPORT + "blocks/";
			public static final String ITEMS = TRANSPORT + "items/";
			public static final String GUI = TRANSPORT + "gui/";
			public static final String TESR = TRANSPORT + "tesr/";
			
			/** Values */
			public static final int ENERGY_CAPACITY = 800;
			public static final int ENERGY_MAX_TRANSFER = 400;
			
			public static final int ENERGY_CAPACITY_SURGE = 1600;
			public static final int ENERGY_MAX_TRANSFER_SURGE = 800;
			
			public static final ResourceLocation ENERGY_CHANNEL_LOC_TESR = new ResourceLocation(BLOCKS + "channel/energy/channel.png");
			public static final ResourceLocation ENERGY_CHANNEL_TRANSPARENT_LOC_TESR = new ResourceLocation(BLOCKS + "channel/energy/channel_transparent.png");
			
			public static final ResourceLocation ENERGY_CHANNEL_SURGE_LOC_TESR = new ResourceLocation(BLOCKS + "channel/energy/channel_surge.png");
			public static final ResourceLocation ENERGY_CHANNEL_TRANSPARENT_SURGE_LOC_TESR = new ResourceLocation(BLOCKS + "channel/energy/channel_transparent_surge.png");
		}
	}
	
	/**
	 * Dependencies.
	 */
	public static class DEPENDENCY {
		private static final String FORGE_BUILD = "2838";
		private static final String FORGE_REQ = "14.23.5." + FORGE_BUILD;
		private static final String FORGE_REQ_MAX = "14.24.0";
		
		private static final String ZEHERLIB_REQ = "7.0.14";
		private static final String ZEHERLIB_REQ_MAX = "7.1.0";
		public static final String ZEHERLIB_DEP = "required-after:" + "redstoneflux" + "@[" + ZEHERLIB_REQ + "," + ZEHERLIB_REQ_MAX + "];";

		private static final String REDSTONE_REQ = "2.1.0";
		private static final String REDSTONE_REQ_MAX = "2.2.0";
		public static final String REDSTONE_DEP = "required-after:" + "redstoneflux" + "@[" + REDSTONE_REQ + "," + REDSTONE_REQ_MAX + "];";
		
		public static final String FORGE_DEP = "required-after:" + "forge" +  "@[" + FORGE_REQ + "," + FORGE_REQ_MAX + "];";
		
		public static final String DOWN_URL = "";
		
	}
	
	/**
	 * JEI Integration.
	 */
	public static class JEI {
		public static final String KILN_UNLOC = "jei.recipe.kiln";
		public static final String GRINDER_UNLOC = "jei.recipe.grinder";
		public static final String COMPACTOR_UNLOC = "jei.recipe.compactor";
		public static final String SEPARATOR_UNLOC = "jei.recipe.separator";
		public static final String SYNTHESISER_UNLOC = "jei.recipe.synthesiser";
	}
}