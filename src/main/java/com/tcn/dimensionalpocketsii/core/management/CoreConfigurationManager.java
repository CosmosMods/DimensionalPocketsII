package com.tcn.dimensionalpocketsii.core.management;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.tcn.dimensionalpocketsii.DimReference.CONFIG_DEFAULTS;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class CoreConfigurationManager {
	
	public static final ForgeConfigSpec spec;
	
	static final CoreConfigurationManager INSTANCE;
	
	static {
		{
			final Pair<CoreConfigurationManager, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CoreConfigurationManager::new);
			INSTANCE = specPair.getLeft();
			spec = specPair.getRight();
		}
	}
	
	public static void save() {
		spec.save();
	}
	
	private final BooleanValue connected_textures_inside_pocket;
	private final BooleanValue keep_chunks_loaded;
	private final BooleanValue can_destroy_walls;
	private final BooleanValue should_spawn_with_book;
	private final BooleanValue debug_message;
	private final BooleanValue can_place_structures;
	private final BooleanValue can_use_items;
	
	private final BooleanValue encrypt_files;
	private final BooleanValue cancel_commands;
	
	private final IntValue internal_height;
	private final BooleanValue internal_replace;
	
	private final ConfigValue<List<? extends String>> disallowed_blocks;
	private final ConfigValue<List<? extends String>> disallowed_items;
	
	CoreConfigurationManager(final ForgeConfigSpec.Builder builder) {
		builder.push("general");
		{
			keep_chunks_loaded = builder
					.comment("Whether to keep the chunks inside the Pocket Dimension Loaded")
					.define("keep_chunks_loaded", true);
			
			can_destroy_walls = builder
					.comment("Whether the walls of Pockets can be destroyed in creative mode")
					.define("can_destroy_walls", false);
			
			cancel_commands = builder
					.comment("Whether commands are cancelled inside Pockets")
					.define("cancel_commands", true);
			
			internal_height = builder
					.comment("The Internal height of pockets. Can only be between 15 - 255")
					.defineInRange("internal_height", 15, 15, 255);
			
			internal_replace = builder
					.comment("Whether if reducing the Internal Height of Pocket that is larger will make it smaller.")
					.define("internal_replace", false);
			
			should_spawn_with_book = builder
					.comment("Whether or not any Player spawns with a book upon new spawn.")
					.define("should_spawn_with_book", true);
			
			can_place_structures = builder
					.comment("Whether or not Disallowed Blocks can be placed.")
					.define("can_place_structures", false);
			
			can_use_items = builder
					.comment("Whether or not Disallowed Items can be used.")
					.define("can_use_items", false);
		}
		builder.pop();
		
		builder.push("debug");
		{
			debug_message = builder
						.comment("Whether DimensionalPockets can send system messages.")
						.define("debug_message", false);
			
			encrypt_files = builder
					.comment("Whether pocket data files are encrypted or not")
					.define("encrpyt_files", false);
		}
		builder.pop();
		
		builder.push("visual");
		{
			connected_textures_inside_pocket = builder
					.comment("Whether or not connected textures work inside Pockets.")
					.define("connected_textures_inside_pocket", true);
		}
		builder.pop();
		
		builder.push("disallowed");
		{
			disallowed_blocks = builder
					.comment("List of disallowed Blocks inside of a Pocket.")
					.defineList("disallowed_blocks", CONFIG_DEFAULTS.DISALLOWED_BLOCKS, obj -> true );
			disallowed_items = builder
					.comment("List of disallowed Items inside of a Pocket.")
					.defineList("disallowed_items", CONFIG_DEFAULTS.DISALLOWED_ITEMS, obj -> true );
		}
		builder.pop();
		
	}
	
	public static CoreConfigurationManager getInstance() {
		return INSTANCE;
	}

	public boolean getConnectedTexturesInsidePocket() {
		return connected_textures_inside_pocket.get();
	}
	
	public void setConnectedTexturesInsidePocket(boolean value) {
		this.connected_textures_inside_pocket.set(value);
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
	
	public boolean getDebugMessage() {
		return debug_message.get();
	}
	
	public void setDebugMessage(boolean value) {
		this.debug_message.set(value);
	}
	
	public boolean getCancelCommands() {
		return cancel_commands.get();
	}
	
	public void setCancelCommands(boolean value) {
		this.cancel_commands.set(value);
	}
	
	public boolean getEncryptMessages() {
		return encrypt_files.get();
	}
	
	public void setEncryptMessages(boolean value) {
		this.encrypt_files.set(value);
	}

	public int getInternalHeight() {
		return this.internal_height.get();
	}
	
	public void setInternalHeight(int value) {
		this.internal_height.set(value);
	}

	public boolean getInternalReplace() {
		return this.internal_replace.get();
	}
	
	public void setInternalReplace(boolean value) {
		this.internal_replace.set(value);
	}

	public boolean getShouldSpawnWithBook() {
		return this.should_spawn_with_book.get();
	}
	
	public void setShouldSpawnWithBook(boolean value) {
		this.should_spawn_with_book.set(value);
	}

	public boolean getCanPlaceStructures() {
		return can_place_structures.get();
	}
	
	public void setCanPlaceStructures(boolean value) {
		this.can_place_structures.set(value);
	}

	public List<? extends String> getDisallowedBlocks() {
		return this.disallowed_blocks.get();
	}
	
	public void setDisallowedBlocks(List<? extends String> value) {
		this.disallowed_blocks.set(value);
	}

	public List<? extends String> getDisallowedItems() {
		return this.disallowed_items.get();
	}
	
	public void setDisallowedItems(List<? extends String> value) {
		this.disallowed_items.set(value);
	}

	public boolean getCanUseItems() {
		return this.can_use_items.get();
	}
	
	public void setCanUseItems(boolean value) {
		this.can_use_items.set(value);
	}
}
