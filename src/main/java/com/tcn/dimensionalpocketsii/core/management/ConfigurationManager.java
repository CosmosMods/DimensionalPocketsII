package com.tcn.dimensionalpocketsii.core.management;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.tcn.dimensionalpocketsii.DimReference;
import com.tcn.dimensionalpocketsii.DimReference.CONFIG_DEFAULTS;
import com.tcn.dimensionalpocketsii.DimensionalPockets;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ConfigurationManager {
	
	public static final ForgeConfigSpec spec;
	
	static final ConfigurationManager INSTANCE;
	
	static {
		{
			final Pair<ConfigurationManager, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ConfigurationManager::new);
			INSTANCE = specPair.getLeft();
			spec = specPair.getRight();
		}
	}
	
	public static void save() {
		spec.save();
	}

	private final IntValue internal_height;
	private final IntValue focus_jump_range;
	private final BooleanValue can_place_structures;
	private final BooleanValue can_use_items;
	private final BooleanValue can_use_commands;
	private final BooleanValue keep_chunks_loaded;
	private final BooleanValue can_destroy_walls;
	private final BooleanValue internal_replace;
	private final BooleanValue stop_hostile_spawns;
	
	private final BooleanValue info_message;
	private final BooleanValue debug_message;
	
	private final BooleanValue connected_textures_inside_pocket;

	private final ConfigValue<List<? extends String>> blocked_structures;
	private final ConfigValue<List<? extends String>> blocked_items;
	private final ConfigValue<List<? extends String>> blocked_commands;
	
	ConfigurationManager(final ForgeConfigSpec.Builder builder) {
		builder.push("general");
		{
			internal_height = builder.comment("Allows you to change the the internal height of Pockets").defineInRange("internal_height", 15, 15, 255);
			focus_jump_range = builder.comment("Allows you to change the the range of Dimensional Focus Jump Distance.").defineInRange("focus_jump_range", 12, 4, 32);
			can_place_structures = builder.comment("Whether or not blocked Structures can be used inside of a Pocket").define("can_place_structures", false);
			can_use_items = builder.comment("Whether or not blocked Items can be used inside of a Pocket").define("can_use_items", false);
			can_use_commands = builder.comment("Whether or not blocked Commands can be used inside of a Pocket").define("can_use_commands", false);
			keep_chunks_loaded = builder.comment("Whether to keep the Chunks inside the Pocket Dimension Loaded").define("keep_chunks_loaded", true);
			can_destroy_walls = builder.comment("Whether the walls of Pockets can be destroyed in Creative Mode").define("can_destroy_walls", false);
			internal_replace = builder.comment("Whether if reducing the Internal Height of Pocket that is larger will make it smaller").define("internal_replace", false);
			stop_hostile_spawns = builder.comment("Whether or not Hostile Mobs should be stopped from spawning").define("stop_hostile_spawns", true);
		}
		builder.pop();
		
		builder.push("console_messages");
		{
			info_message = builder.comment("Whether this mod will print [INFO] messages to the console/log").define("info", true);
			debug_message = builder.comment("Whether this mod will print [DEBUG] messages to the console/log").define("debug", false);
		}
		builder.pop();
		
		builder.push("visual");
		{
			connected_textures_inside_pocket = builder.comment("Whether or not connected textures work inside Pockets").define("connected_textures_inside_pocket", true);
		}
		builder.pop();
		
		builder.push("blocked");
		{
			this.blocked_structures = builder.comment("List of Structures that are blocked when inside a Pocket").defineList("blocked_structures", CONFIG_DEFAULTS.BLOCKED_BLOCKS, obj -> true);
			this.blocked_items = builder.comment("List of Items that are blocked when inside a Pocket").defineList("blocked_items", CONFIG_DEFAULTS.BLOCKED_ITEMS, obj -> true);
			this.blocked_commands = builder.comment("List of Commands that are blocked when inside a Pocket").defineList("blocked_commands", CONFIG_DEFAULTS.BLOCKED_COMMANDS, obj -> true);
		}
		builder.pop();
		
	}
	
	public static ConfigurationManager getInstance() {
		return INSTANCE;
	}

	public int getInternalHeight() {
		return this.internal_height.get();
	}
	
	public void setInternalHeight(int value) {
		this.internal_height.set(value);
	}

	public int getFocusJumpRange() {
		return this.focus_jump_range.get();
	}
	
	public void setFocusJumpRange(int value) {
		this.focus_jump_range.set(value);
	}

	
	public boolean getCanPlaceStructures() {
		return can_place_structures.get();
	}
	
	public void setCanPlaceStructures(boolean value) {
		this.can_place_structures.set(value);
	}
	
	
	public boolean getCanUseItems() {
		return this.can_use_items.get();
	}
	
	public void setCanUseItems(boolean value) {
		this.can_use_items.set(value);
	}

	public boolean getCanUseCommands() {
		return can_use_commands.get();
	}
	
	public void setCanUseCommands(boolean value) {
		this.can_use_commands.set(value);
	}
	
	
	public boolean getKeepChunksLoaded() {
		return keep_chunks_loaded.get();
	}
	
	public void setKeepChunksLoaded(boolean value) {
		this.keep_chunks_loaded.set(value);
	}
	
	
	public boolean getCanDestroyWalls() {
		return can_destroy_walls.get();
	}
	
	public void setCanDestroyWalls(boolean value) {
		this.can_destroy_walls.set(value);
	}
	
	
	public boolean getInternalReplace() {
		return this.internal_replace.get();
	}
	
	public void setInternalReplace(boolean value) {
		this.internal_replace.set(value);
	}
	
	public boolean getStopHostileSpawns() {
		return this.stop_hostile_spawns.get();
	}
	
	public void setStopHostileSpawns(boolean value) {
		this.stop_hostile_spawns.set(value);
	}
	
	
	/** -Messages- */
	public boolean getInfoMessage() {
		return info_message.get();
	}
	
	public void setInfoMessage(boolean value) {
		DimensionalPockets.CONSOLE.updateInfoEnabled(value);
		this.info_message.set(value);
	}
	
	public boolean getDebugMessage() {
		return debug_message.get();
	}
	
	public void setDebugMessage(boolean value) {
		DimensionalPockets.CONSOLE.updateDebugEnabled(value);
		this.debug_message.set(value);
	}
	
	
	
	/** -Visual- */
	public boolean getConnectedTexturesInsidePocket() {
		return connected_textures_inside_pocket.get();
	}
	
	public void setConnectedTexturesInsidePocket(boolean value) {
		this.connected_textures_inside_pocket.set(value);
	}

	
	/** -Blocked- */
	public List<? extends String> getBlockedStructures() {
		return this.blocked_structures.get();
	}
	
	public void setBlockedStructures(List<? extends String> value) {
		this.blocked_structures.set(value);
	}
	
	public void addBlockedStructure(String value) {
		ArrayList<String> list = DimReference.CONFIG_DEFAULTS.BLOCKED_BLOCKS;
		list.add(value);
		
		this.blocked_structures.set(list);
	}

	public void removeBlockedStructure(String value) {
		List<? extends String> list = this.blocked_structures.get();
		
		for (int i = 0; i < list.size(); i++) {
			String object = list.get(i);
			
			if (object == value) {
				list.remove(i);
			}
		}
		
		this.blocked_structures.set(list);
	}

	public List<? extends String> getBlockedItems() {
		return this.blocked_items.get();
	}
	
	public void setBlockedItems(List<? extends String> value) {
		this.blocked_items.set(value);
	}

	public void addBlockedItem(String value) {
		ArrayList<String> list = DimReference.CONFIG_DEFAULTS.BLOCKED_ITEMS;
		list.add(value);
		
		this.blocked_items.set(list);
	}

	public void removeBlockedItem(String value) {
		List<? extends String> list = this.blocked_items.get();
		
		for (int i = 0; i < list.size(); i++) {
			String object = list.get(i);
			
			if (object == value) {
				list.remove(i);
			}
		}
		
		this.blocked_items.set(list);
	}

	public List<? extends String> getBlockedCommands() {
		return this.blocked_commands.get();
	}
	
	public void setBlockedCommands(List<? extends String> value) {
		this.blocked_commands.set(value);
	}

	public void addBlockedCommand(String value) {
		ArrayList<String> list = DimReference.CONFIG_DEFAULTS.BLOCKED_COMMANDS;
		list.add(value);
		
		this.blocked_commands.set(list);
	}

	public void removeBlockedCommand(String value) {
		List<? extends String> list = this.blocked_commands.get();
		
		for (int i = 0; i < list.size(); i++) {
			String object = list.get(i);
			
			if (object == value) {
				list.remove(i);
			}
		}
		
		this.blocked_commands.set(list);
	}

}