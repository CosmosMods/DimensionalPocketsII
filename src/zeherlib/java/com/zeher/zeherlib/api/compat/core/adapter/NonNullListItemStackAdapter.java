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

public class NonNullListItemStackAdapter implements JsonSerializer<NonNullList<ItemStack>>, JsonDeserializer<NonNullList<ItemStack>> {
	
	public NonNullListItemStackAdapter() { }

	@Override
	public NonNullList<ItemStack> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonPrimitive object = json.getAsJsonPrimitive();
		
		ItemStack item = ItemStack.EMPTY;
		String string = object.getAsString();
		
		NBTTagCompound nbt = new NBTTagCompound();
		
		String[] array = string.split("_");
		
		try {
			nbt = JsonToNBT.getTagFromJson(array[0]);
		} catch (NBTException e) {
			e.printStackTrace();
		}
		
		NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(Integer.parseInt(array[1]), ItemStack.EMPTY);
		
		ItemStackHelper.loadAllItems(nbt, stacks);
		
		return stacks;
	}

	@Override
	public JsonElement serialize(NonNullList<ItemStack> src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject object = new JsonObject();
		NBTTagCompound nbt = new NBTTagCompound();
		ItemStackHelper.saveAllItems(nbt, src);
		
		String nbt_string = nbt.toString();
		
		//object.addProperty("item", nbt_string);
		return new JsonPrimitive(nbt_string + "_" + src.size());
	}
}