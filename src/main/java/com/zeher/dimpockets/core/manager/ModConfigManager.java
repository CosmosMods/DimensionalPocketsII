package com.zeher.dimpockets.core.manager;

import com.zeher.zeherlib.core.manager.ConfigurationManager;

public class ModConfigManager {

	private static String CONFIG_FILE = "config/dimpockets/dimensionalpockets.cfg";
	
	public static String GENERAL = "general";
	
	public static String DEBUG = "debug";
	public static String VISUAL = "visual";
	
	public static String config_keep_pockets_chunkloaded = "keep_pockets_chunkloaded";
	public static boolean KEEP_POCKETS_CHUNKLOADED = true;

	public static String config_can_destroy_walls_in_creative = "can_destroy_walls_in_creative";
	public static boolean CAN_DESTROY_WALLS_IN_CREATIVE = false;
	
	public static String config_can_teleport_to_dim = "can_teleport_to_dim";
	public static boolean CAN_TELEPORT_TO_DIM = false;
	
	public static String config_dimensional_pockets_system_message = "dimensional_pockets_system_message";
	public static boolean DIMENSIONAL_POCKETS_SYSTEM_MESSAGE = false;
	
	public static String connected_textures_inside_pocket = "connected_textures_inside_pocket";
	public static boolean CONNECTED_TEXTURES_INSIDE_POCKET = false;
	
	public static void initialization() {
		
		/** - GENERAL - */
		if (ConfigurationManager.hasKey(GENERAL, CONFIG_FILE, config_keep_pockets_chunkloaded)) {
			KEEP_POCKETS_CHUNKLOADED = ConfigurationManager.getBoolean(GENERAL, CONFIG_FILE, config_keep_pockets_chunkloaded);
		} else {
			ConfigurationManager.writeConfig(GENERAL, CONFIG_FILE, config_keep_pockets_chunkloaded, true, "This sets whether or not Pockets are kept ChunkLoaded.");
		}
		
		if (ConfigurationManager.hasKey(GENERAL, CONFIG_FILE, config_can_destroy_walls_in_creative)) {
			CAN_DESTROY_WALLS_IN_CREATIVE = ConfigurationManager.getBoolean(GENERAL, CONFIG_FILE, config_can_destroy_walls_in_creative);
		} else {
			ConfigurationManager.writeConfig(GENERAL, CONFIG_FILE, config_can_destroy_walls_in_creative, false, "This allows the player to destroy Pocket Wall blocks when in Creative.");
		}
		
		/** - DEBUG - */
		if (ConfigurationManager.hasKey(DEBUG, CONFIG_FILE, config_can_teleport_to_dim)) {
			CAN_TELEPORT_TO_DIM = ConfigurationManager.getBoolean(DEBUG, CONFIG_FILE, config_can_teleport_to_dim);
		} else {
			ConfigurationManager.writeConfig(DEBUG, CONFIG_FILE, config_can_teleport_to_dim, false, "This allows the use of /dimshift to teleport to the Pocket Dimension (DEBUG).");
		}
		
		if (ConfigurationManager.hasKey(DEBUG, CONFIG_FILE, config_dimensional_pockets_system_message)) {
			DIMENSIONAL_POCKETS_SYSTEM_MESSAGE = ConfigurationManager.getBoolean(DEBUG, CONFIG_FILE, config_dimensional_pockets_system_message);
		} else {
			ConfigurationManager.writeConfig(DEBUG, CONFIG_FILE, config_dimensional_pockets_system_message, false, "This sets whether or not DimensionalPockets II will output messages to the log.");
		}
		
		/** - VISUAL - */
		if (ConfigurationManager.hasKey(VISUAL, CONFIG_FILE, connected_textures_inside_pocket)) {
			CONNECTED_TEXTURES_INSIDE_POCKET = ConfigurationManager.getBoolean(VISUAL, CONFIG_FILE, connected_textures_inside_pocket);
		} else {
			ConfigurationManager.writeConfig(VISUAL, CONFIG_FILE, connected_textures_inside_pocket, false, "This is a visual option of whether or not the Pocket wall blocks have connected textures.");
		}
	}
}