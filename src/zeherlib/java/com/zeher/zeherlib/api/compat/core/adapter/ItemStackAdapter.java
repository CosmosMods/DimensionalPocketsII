package com.zeher.zeherlib.api.compat.core.adapter;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

public class ItemStackAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
	
	public ItemStackAdapter() { }

	@Override
	public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonPrimitive object = json.getAsJsonPrimitive();
		
		ItemStack item = ItemStack.EMPTY;
		String string = object.getAsString();
		
		NBTTagCompound nbt = new NBTTagCompound();
		
		try {
			nbt = JsonToNBT.getTagFromJson(string);
		} catch (NBTException e) {
			e.printStackTrace();
		}
		
		ItemStack stack = new ItemStack(nbt);
		
		return stack;
	}

	@Override
	public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject object = new JsonObject();
		NBTTagCompound nbt = new NBTTagCompound();
		src.writeToNBT(nbt);
		
		String nbt_string = nbt.toString();
		
		return new JsonPrimitive(nbt_string);
	}
}