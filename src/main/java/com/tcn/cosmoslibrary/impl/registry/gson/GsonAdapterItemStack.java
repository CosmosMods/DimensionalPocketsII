package com.tcn.cosmoslibrary.impl.registry.gson;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;

public class GsonAdapterItemStack implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
	
	public GsonAdapterItemStack() { }

	@Override
	public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonPrimitive object = json.getAsJsonPrimitive();
		
		String string = object.getAsString();
		
		CompoundNBT nbt = new CompoundNBT();
		
		try {
			nbt = JsonToNBT.getTagFromJson(string);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ItemStack stack = ItemStack.read(nbt);
		
		return stack;
	}

	@Override
	public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
		CompoundNBT nbt = new CompoundNBT();
		src.write(nbt);
		
		String nbt_string = nbt.toString();
		
		return new JsonPrimitive(nbt_string);
	}
}