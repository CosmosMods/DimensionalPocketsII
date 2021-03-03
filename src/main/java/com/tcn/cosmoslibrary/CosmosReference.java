package com.tcn.cosmoslibrary;

import net.minecraft.util.ResourceLocation;

public class CosmosReference {
	/**
	 * Static access to standard values required by multiple classes.
	 */
	public static class RESOURCE {
		
		/**
		 * Prefix for all ResourceLocations.
		 */
		public static final String PRE = CosmosLibrary.MOD_ID + ":";
		public static final String RESOURCE = PRE + "textures/";
		
		/**
		 * ResourceLocations for Base Objects
		 */
		public static class BASE {
			public static final String BASE = RESOURCE + "base/";
			public static final String BLOCKS = BASE + "blocks/";
			public static final String ITEMS = BASE + "items/";
			public static final String GUI = BASE + "gui/";
			
			/** Gui */
			public static final ResourceLocation GUI_SLOT_LOC = new ResourceLocation(GUI + "gui_slot.png");
			public static final ResourceLocation GUI_ENERGY_BAR_LOC = new ResourceLocation(GUI + "gui_energy_bar.png");
			public static final ResourceLocation GUI_DIRECTION_LOC = new ResourceLocation(GUI + "gui_direction.png");
			
			public static final ResourceLocation GUI_ICON_BUTTON_LOC = new ResourceLocation(GUI + "button/gui_icon_button.png");
			public static final ResourceLocation GUI_ELEMENT_MISC_LOC = new ResourceLocation(GUI + "gui_element_misc.png");
			
			public static final ResourceLocation GUI_ENERGY_BUTTON_CUSTOM_LOC = new ResourceLocation(GUI + "button/gui_energy_button_custom.png");
			public static final ResourceLocation GUI_ENERGY_BUTTON_LOC = new ResourceLocation(GUI + "button/gui_energy_button.png");
			
			public static final ResourceLocation GUI_FLUID_BUTTON_LOC = new ResourceLocation(GUI + "button/gui_fluid_button.png");
			public static final ResourceLocation GUI_FLUID_BUTTON_CUSTOM_LOC = new ResourceLocation(GUI + "button/gui_fluid_button_custom.png");
			
			public static final ResourceLocation GUI_ITEM_BUTTON_LOC = new ResourceLocation(GUI + "button/gui_item_button.png");
			public static final ResourceLocation GUI_ITEM_BUTTON_CUSTOM_LOC = new ResourceLocation(GUI + "button/gui_item_button_custom.png");
			
			public static final ResourceLocation GUI_STORAGE_BUTTON_LOC = new ResourceLocation(GUI + "button/gui_storage_button.png");
			public static final ResourceLocation GUI_STORAGE_BUTTON_CUSTOM_LOC = new ResourceLocation(GUI + "button/gui_storage_button_custom.png");
			
			/** ToolTip */
			public static final String TOOLTIP_HOLD = "info.hold.name";
			public static final String TOOLTIP_SHIFT = "info.shift.name";
			public static final String TOOLTIP_FOR_DETAILS = "info.fordetails.name";
			
			public static final String TOOLTIP_ENERGYITEM = "info.tooltip.energy.name";
			public static final String TOOLTIP_FLUIDITEM = "info.tooltip.fluid.name";
			
			public static final String TOOLTIP_CURRENT_POWER = "info.currentpower.name";
			public static final String TOOLTIP_MAX_POWER = "info.maxpower.name";
			
			public static final String TOOLTIP_RELEASE = "info.release.name";
			public static final String TOOLTIP_LESS_DETAILS = "info.lessdetails.name";
			
			public static final String TOOLTIP_CTRL = "info.ctrl.name";
			public static final String TOOLTIP_NBT = "info.nbt.name";
			public static final String TOOLTIP_NBT_LESS_DETAILS = "info.nbt.lessdetails.name";
		}
		
		public static class INFO {
			/** Button Spacing [-X-] */
			public static final int BUTTON_X_SPACING = 44;
			public static final int BUTTON_X_SPACING_SMALL = 67;
			
			/** Locations [-X-] for each button in the button array. */
			public static final int[] BUTTON_STATE_X = { 0, 38, 76, 114, 152, 190 };
			public static final int[] BUTTON_STATE_X_SMALL = { 20, 58, 96, 134, 172, 210 };
			
			/** Button Spacing [-Y-] */
			public static final int BUTTON_Y_SPACING = 23;
			/** Locations [-Y-] for each button in the button array. */
			public static final int[] BUTTON_STATE_Y = { 0, 20, 40, 60, 80, 100, 120, 140, 160, 180, 200, 220 };
			public static final int[] BUTTON_STATE_Y_SMALL = { 0, 20, 40, 60, 80, 100, 120, 140, 160, 180, 200, 220 };
			
			/** Slots in the format: [ texture-x, texture-y, width, height ] */
			public static final int[] SLOT_REGULAR_SMALL = new int[] { 0, 0, 18, 18 };
			public static final int[] SLOT_OUTPUT_SMALL = new int[] { 18, 0, 18, 18 };
			public static final int[] SLOT_INPUT_SMALL = new int[] { 36, 0, 18, 18 };
			
			/** Energy-bar in the format: [ texture-x, texture-y, texture-height, texture-width ]*/
			public static final int[] ENERGY_BAR = new int[] { 72, 61, 62, 18};
			public static final int[] ENERGY_BAR_SMALL = new int[] { 72, 102, 40, 18 };
			
			@Deprecated
			public static final int[] POWERED_BAR = new int[] { 18, 61, 62, 18 };
			@Deprecated
			public static final int[] POWERED_BAR_SMALL = new int[] { 18, 102, 40, 18 };
			
			@Deprecated
			public static final int[] ENERGIZED_BAR = new int[] { 36, 61, 62, 18 };
			@Deprecated
			public static final int[] ENERGIZED_BAR_SMALL = new int[] { 36, 102, 40, 18 };
			
			@Deprecated
			public static final int[] CREATIVE_BAR = new int[] { 54, 61, 62, 18	};
			@Deprecated
			public static final int[] CREATIVE_BAR_SMALL = new int[] { 54, 102, 40, 18 };
		}
	}
	
	/**
	 * Dependencies.
	 */
	public static class DEPENDENCY {
		private static final String FORGE_BUILD = "2838";
		private static final String FORGE_REQ = "14.23.5." + FORGE_BUILD;
		private static final String FORGE_REQ_MAX = "14.24.0";

		private static final String REDSTONE_REQ = "2.1.0";
		private static final String REDSTONE_REQ_MAX = "2.2.0";
		public static final String REDSTONE_DEP = "required-after:" + "redstoneflux" + "@[" + REDSTONE_REQ + "," + REDSTONE_REQ_MAX + "];";
		
		public static final String FORGE_DEP = "required-after:" + "forge" +  "@[" + FORGE_REQ + "," + FORGE_REQ_MAX + "];";
		
		public static final String DOWN_URL = "";
		
	}
	
	public static class JEI {
		public static final String GRINDER_UID = CosmosLibrary.MOD_ID + ":grinder";
		
		public static final String COMPACTOR_UID = CosmosLibrary.MOD_ID + ":compactor";
		
		public static final String SEPARATOR_UID = CosmosLibrary.MOD_ID + ":separator";
		
		public static final String SYNTHESISER_UID = CosmosLibrary.MOD_ID + ":synthesiser";
		
		public static final String EXPERIENCE = "jei.experience";
	}
}