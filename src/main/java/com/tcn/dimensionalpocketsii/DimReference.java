package com.tcn.dimensionalpocketsii;

import java.util.ArrayList;

import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper;
import com.tcn.cosmoslibrary.common.comp.CosmosColour;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;

public class DimReference {
	
	public static class CONSTANT {
		public static final int POCKET_BIOME_ID = 99;
		
		public static final int POCKET_HELD_ITEMS_SIZE = 48;
		public static final int POCKET_RF_CAP = 50000000;
		
		public static final int DEFAULT_COLOUR = CosmosColour.POCKET_PURPLE.dec();
	}
	
	public static class CONFIG_DEFAULTS {
		public static final ArrayList<String> DISALLOWED_BLOCKS = new ArrayList<String>() { 
			private static final long serialVersionUID = 2L;
			
			{
				add("lucky:lucky_block");
				add("chancecubes:chance_cube");
				add("chancecubes:chance_icosahedron");
				add("chancecubes:giant_chance_cube");
				add("chancecubes:cube_dispenser");
				add("xreliquary:wraith_node");
			};
		};
		
		public static final ArrayList<String> DISALLOWED_ITEMS = new ArrayList<String>() {
			private static final long serialVersionUID = 2L;
			
			{
				add("minecraft:chorus_fruit");
				add("minecraft:ender_pearl");
				add("xreliquary:ender_staff");
				add("inventorypets:pet_nether_portal");
				add("inventorypets:pet_enderman");
				add("inventorypets:pet_silverfish");
			};
		};
	}
	
	public static class MESSAGES {
		public static final IFormattableTextComponent WELCOME = CosmosCompHelper.locComp(CosmosColour.CYAN, false, "dimensionalpocketsii.welcome_one")
				.append(CosmosCompHelper.locComp(CosmosColour.PURPLE, false, "dimensionalpocketsii.welcome_two"))
				.append(CosmosCompHelper.locComp(CosmosColour.CYAN, false, "dimensionalpocketsii.welcome_three"))
				.append(CosmosCompHelper.locComp(CosmosColour.GRAY, false, "dimensionalpocketsii.welcome_four"))
				.append(CosmosCompHelper.locComp(CosmosColour.GRAY, false, "dimensionalpocketsii.version"));
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
			public static final ResourceLocation POCKET_BACKGROUND_NORMAL = new ResourceLocation(DimReference.RESOURCE.GUI + "pocket/background_normal.png");
			public static final ResourceLocation POCKET_BACKGROUND_SIDE = new ResourceLocation(DimReference.RESOURCE.GUI + "pocket/background_side.png");
			public static final ResourceLocation POCKET_BASE_NORMAL = new ResourceLocation(DimReference.RESOURCE.GUI + "pocket/base_normal.png");
			public static final ResourceLocation POCKET_BASE_SIDE = new ResourceLocation(DimReference.RESOURCE.GUI + "pocket/base_side.png");
			public static final ResourceLocation POCKET_OVERLAY_NORMAL = new ResourceLocation(DimReference.RESOURCE.GUI + "pocket/overlay_normal.png");
			public static final ResourceLocation POCKET_OVERLAY_SIDE = new ResourceLocation(DimReference.RESOURCE.GUI + "pocket/overlay_side.png");
			
			public static final ResourceLocation CONNECTOR_BACKGROUND_NORMAL = new ResourceLocation(DimReference.RESOURCE.GUI + "connector/background_normal.png");
			public static final ResourceLocation CONNECTOR_BACKGROUND_SIDE = new ResourceLocation(DimReference.RESOURCE.GUI + "connector/background_side.png");
			public static final ResourceLocation CONNECTOR_BASE_NORMAL = new ResourceLocation(DimReference.RESOURCE.GUI + "connector/base_normal.png");
			public static final ResourceLocation CONNECTOR_BASE_SIDE = new ResourceLocation(DimReference.RESOURCE.GUI + "connector/base_side.png");
			public static final ResourceLocation CONNECTOR_OVERLAY_NORMAL = new ResourceLocation(DimReference.RESOURCE.GUI + "connector/overlay_normal.png");
			public static final ResourceLocation CONNECTOR_OVERLAY_SIDE = new ResourceLocation(DimReference.RESOURCE.GUI + "connector/overlay_side.png");
			
			public static final ResourceLocation CRAFTER_BACKGROUND = new ResourceLocation(DimReference.RESOURCE.GUI + "crafter/background.png");
			public static final ResourceLocation CRAFTER_BASE = new ResourceLocation(DimReference.RESOURCE.GUI + "crafter/base.png");
			public static final ResourceLocation CRAFTER_OVERLAY = new ResourceLocation(DimReference.RESOURCE.GUI + "crafter/overlay.png");
			
			public static final ResourceLocation GUI_ALLOWED_PLAYERS = new ResourceLocation(DimReference.RESOURCE.GUI + "gui_allowed_players.png");
			public static final ResourceLocation GUI_ALLOWED_PLAYERS_INFO = new ResourceLocation(DimReference.RESOURCE.GUI + "gui_allowed_players_info.png");
			
			public static final ResourceLocation GUI_ALLOWED_PLAYERS_WALL_CONNECTOR = new ResourceLocation(DimReference.RESOURCE.GUI + "gui_allowed_players_wall_connector.png");
			public static final ResourceLocation GUI_ALLOWED_PLAYERS_WALL_CONNECTOR_INFO = new ResourceLocation(DimReference.RESOURCE.GUI + "gui_allowed_players_wall_connector_info.png");
			
			public static final ResourceLocation GUI_DIMENSIONAL_BUTTON = new ResourceLocation(DimReference.RESOURCE.GUI + "gui_dimensional_button.png");
			public static final ResourceLocation GUI_DIMENSIONAL_BUTTON_0 = new ResourceLocation(DimReference.RESOURCE.GUI + "gui_dimensional_button_0.png");
			public static final ResourceLocation GUI_DIMENSIONAL_ELEMENT = new ResourceLocation(DimReference.RESOURCE.GUI + "gui_dimensional_element.png");
		}
	}
	
}