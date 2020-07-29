package com.zeher.dimpockets.core.manager;

public class DimConfigManager {

	//private static String CONFIG_FILE = "config/dimpockets/dimensionalpockets.cfg";
	
	public static String config_category_general = "general";
	
	public static String config_keep_pockets_chunkloaded = "keep_pockets_chunkloaded";
	public static boolean KEEP_POCKETS_CHUNKLOADED = true;

	public static String config_can_destroy_walls_in_creative = "can_destroy_walls_in_creative";
	public static boolean CAN_DESTROY_WALLS_IN_CREATIVE = false;
	
	public static String config_can_teleport_to_dim = "can_teleport_to_dim";
	public static boolean CAN_TELEPORT_TO_DIM = false;
	
	public static String config_dimensional_pockets_system_message = "dimensional_pockets_system_message";
	public static boolean DIMENSIONAL_POCKETS_SYSTEM_MESSAGE = false;
	
	/**
	public static void initialization() {
		if (ConfigurationManager.hasKey(config_category_general, CONFIG_FILE, config_can_destroy_walls_in_creative)) {
			KEEP_POCKETS_CHUNKLOADED = ConfigurationManager.getBoolean(config_category_general, CONFIG_FILE, config_can_destroy_walls_in_creative);
		} else {
			ConfigurationManager.writeConfig(config_category_general, CONFIG_FILE, config_keep_pockets_chunkloaded, true);
		}
		
		if (ConfigurationManager.hasKey(config_category_general, CONFIG_FILE, config_can_destroy_walls_in_creative)) {
			CAN_DESTROY_WALLS_IN_CREATIVE = ConfigurationManager.getBoolean(config_category_general, CONFIG_FILE, config_can_destroy_walls_in_creative);
		} else {
			ConfigurationManager.writeConfig(config_category_general, CONFIG_FILE, config_can_destroy_walls_in_creative, false);
		}
		
		if (ConfigurationManager.hasKey(config_category_general, CONFIG_FILE, config_can_teleport_to_dim)) {
			CAN_TELEPORT_TO_DIM = ConfigurationManager.getBoolean(config_category_general, CONFIG_FILE, config_can_teleport_to_dim);
		} else {
			ConfigurationManager.writeConfig(config_category_general, CONFIG_FILE, config_can_teleport_to_dim, false);
		}
		
		if (ConfigurationManager.hasKey(config_category_general, CONFIG_FILE, config_dimensional_pockets_system_message)) {
			DIMENSIONAL_POCKETS_SYSTEM_MESSAGE = ConfigurationManager.getBoolean(config_category_general, CONFIG_FILE, config_dimensional_pockets_system_message);
		} else {
			ConfigurationManager.writeConfig(config_category_general, CONFIG_FILE, config_dimensional_pockets_system_message, false);
		}
	}
	*/
}
