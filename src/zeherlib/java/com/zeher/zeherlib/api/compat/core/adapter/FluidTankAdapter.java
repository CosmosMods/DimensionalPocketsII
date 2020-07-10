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

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidTank;

public class FluidTankAdapter implements JsonSerializer<FluidTank>, JsonDeserializer<FluidTank> {
	
	public FluidTankAdapter() { }

	@Override
	public FluidTank deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject object = json.getAsJsonObject();
		
		JsonElement element_nbt = object.get("nbt_data");
		String string_nbt = element_nbt.getAsString();
		
		JsonElement element_capacity = object.get("capacity");
		String string_capacity = element_capacity.getAsString();
		
		NBTTagCompound nbt = new NBTTagCompound();
		
		try {
			nbt = JsonToNBT.getTagFromJson(string_nbt);
		} catch (NBTException e) {
			e.printStackTrace();
		}
		
		FluidTank tank = new FluidTank(Integer.parseInt(string_capacity));
		tank.readFromNBT(nbt);
		
		return tank;
	}

	@Override
	public JsonElement serialize(FluidTank src, Type typeOfSrc, JsonSerializationContext context) {
		NBTTagCompound nbt = new NBTTagCompound();
		src.writeToNBT(nbt);
		
		String nbt_string = nbt.toString();
		
		JsonObject object = new JsonObject();
		object.addProperty("nbt_data", nbt_string);
		object.addProperty("capacity", src.getCapacity());
		
		return object;
	}
}