package com.tcn.cosmoslibrary.impl.registry.object;

import java.util.UUID;

import com.google.gson.annotations.SerializedName;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class ObjectPlayerInformation {
	
	private static final String NBT_PLAYER_NAME_KEY = "player_name";
	private static final String NBT_PLAYER_UUID_KEY = "player_uuid";
	
	@SerializedName(NBT_PLAYER_NAME_KEY)
	private String player_name;
	
	@SerializedName(NBT_PLAYER_UUID_KEY)
	private UUID player_uuid;
	
	public ObjectPlayerInformation(String player_name, UUID player_uuid) {
		this.player_name = player_name;
		this.player_uuid = player_uuid;
	}
	
	public ObjectPlayerInformation(PlayerEntity player) {
		this.player_name = player.getDisplayName().getString();
		this.player_uuid = player.getUniqueID();
	}
	
	public String getPlayerName() {
		return this.player_name;
	}
	
	public void setPlayerName(String player_name) {
		this.player_name = player_name;
	}
	
	public UUID getPlayerUUID() {
		return this.player_uuid;
	}
	
	public void setPlayerUUID(UUID player_uuid) {
		this.player_uuid = player_uuid;
	}
	
	public static ObjectPlayerInformation readFromNBT(CompoundNBT compound, String key) {
		if (compound.contains(key)) {
			CompoundNBT nbt = compound.getCompound(key);
			
			String name = nbt.getString(NBT_PLAYER_NAME_KEY);
			UUID id = nbt.getUniqueId(NBT_PLAYER_UUID_KEY);
			
			return new ObjectPlayerInformation(name, id);
		}
		
		return null;
	}
	
	public void writeToNBT(CompoundNBT compound, String key) {
		CompoundNBT tag = new CompoundNBT();
		
		tag.putString(NBT_PLAYER_NAME_KEY, this.getPlayerName());
		tag.putUniqueId(NBT_PLAYER_UUID_KEY, this.getPlayerUUID());
		
		compound.put(key, tag);
	}
	
	public static ObjectPlayerInformation readFromNBT(CompoundNBT compound) {
		String name = compound.getString(NBT_PLAYER_NAME_KEY);
		UUID id = compound.getUniqueId(NBT_PLAYER_UUID_KEY);
		
		return new ObjectPlayerInformation(name, id);
	}
	
	public void writeToNBT(CompoundNBT compound) {
		compound.putString(NBT_PLAYER_NAME_KEY, this.getPlayerName());
		compound.putUniqueId(NBT_PLAYER_UUID_KEY, this.getPlayerUUID());
	}
}
