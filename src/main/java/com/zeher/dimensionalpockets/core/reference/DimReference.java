package com.zeher.dimensionalpockets.core.reference;

import com.zeher.dimensionalpockets.DimensionalPockets;

import net.minecraft.util.ResourceLocation;

public class DimReference {
	
	public static class RESOURCE {
		public static final String PRE = DimensionalPockets.MOD_ID + ":";

		public static final String RESOURCE = PRE + "textures/";
		public static final String GUI = RESOURCE + "gui/";

		public static final String BLOCKS = PRE + "blocks/";
		public static final String ITEMS = RESOURCE + "items/";
		
	}
	
	public static class GUI {
		public static class RESOURCE {
			public static final ResourceLocation GUI_ALLOWED_PLAYERS = new ResourceLocation(DimReference.RESOURCE.GUI + "gui_allowed_player.png");
			
		}
	}
}