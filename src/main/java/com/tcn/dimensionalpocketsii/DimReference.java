package com.tcn.dimensionalpocketsii;

import net.minecraft.util.ResourceLocation;

public class DimReference {
	
	public static class CONSTANT {
		//public static final int POCKET_DIMENSION_ID = 98;
		public static final int POCKET_BIOME_ID = 99;
		
		public static final int POCKET_HELD_ITEMS_SIZE = 48;
		public static final int POCKET_RF_CAP = 50000000;
	}
	
	public static class RESOURCE {
		public static final String PRE = DimensionalPockets.MOD_ID + ":";

		public static final String RESOURCE = PRE + "textures/";
		public static final String GUI = RESOURCE + "gui/";

		public static final String BLOCKS = PRE + "blocks/";
		public static final String ITEMS = RESOURCE + "items/";
		
	}
	
	public static class GUI {
		public static class RESOURCE {
			public static final ResourceLocation GUI_ALLOWED_PLAYERS = new ResourceLocation(DimReference.RESOURCE.GUI + "gui_allowed_players.png");
			public static final ResourceLocation GUI_ALLOWED_PLAYERS_INFO = new ResourceLocation(DimReference.RESOURCE.GUI + "gui_allowed_players_info.png");
			
			public static final ResourceLocation GUI_ALLOWED_PLAYERS_WALL_CONNECTOR = new ResourceLocation(DimReference.RESOURCE.GUI + "gui_allowed_players_wall_connector.png");
			public static final ResourceLocation GUI_ALLOWED_PLAYERS_WALL_CONNECTOR_INFO = new ResourceLocation(DimReference.RESOURCE.GUI + "gui_allowed_players_wall_connector_info.png");
			
			public static final ResourceLocation GUI_DIMENSIONAL_BUTTON = new ResourceLocation(DimReference.RESOURCE.GUI + "gui_dimensional_button.png");
			public static final ResourceLocation GUI_DIMENSIONAL_ELEMENT = new ResourceLocation(DimReference.RESOURCE.GUI + "gui_dimensional_element.png");
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
		public static final String ZEHERLIB_DEP = "required-after:" + "zeherlib" + "@[" + ZEHERLIB_REQ + "," + ZEHERLIB_REQ_MAX + "];";

		private static final String REDSTONE_REQ = "2.1.0";
		private static final String REDSTONE_REQ_MAX = "2.2.0";
		public static final String REDSTONE_DEP = "required-after:" + "redstoneflux" + "@[" + REDSTONE_REQ + "," + REDSTONE_REQ_MAX + "];";
		
		public static final String FORGE_DEP = "required-after:" + "forge" +  "@[" + FORGE_REQ + "," + FORGE_REQ_MAX + "];";
		
		public static final String DOWN_URL = "";
		
	}
}