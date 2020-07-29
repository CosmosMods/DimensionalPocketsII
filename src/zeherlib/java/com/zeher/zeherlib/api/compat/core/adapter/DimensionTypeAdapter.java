package com.zeher.zeherlib.api.compat.core.adapter;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;

public class DimensionTypeAdapter implements JsonSerializer<DimensionType>, JsonDeserializer<DimensionType> {
	
	public DimensionTypeAdapter() { }

	@Override
	public DimensionType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject object = json.getAsJsonObject();
		
		JsonElement nbt_1 = object.get("namespace");
		String namespace = nbt_1.getAsString();
		
		JsonElement nbt_2 = object.get("path");
		String path = nbt_2.getAsString();
		
		ResourceLocation loc = new ResourceLocation(namespace, path);
		DimensionType dim = null;
		
		try {
			dim = DimensionType.byName(loc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dim;
	}

	@Override
	public JsonElement serialize(DimensionType src, Type typeOfSrc, JsonSerializationContext context) {
		String namespace = src.getRegistryName().getNamespace();
		String path = src.getRegistryName().getPath();
		
		JsonObject object = new JsonObject();
		object.addProperty("namespace", namespace);
		object.addProperty("path", path);
		
		return object;
	}
}