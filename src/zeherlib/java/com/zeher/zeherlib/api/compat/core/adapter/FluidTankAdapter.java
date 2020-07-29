package com.zeher.zeherlib.api.compat.core.adapter;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class FluidTankAdapter implements JsonSerializer<FluidTank>, JsonDeserializer<FluidTank> {
	
	public FluidTankAdapter() { }

	@Override
	public FluidTank deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject object = json.getAsJsonObject();
		
		JsonElement element_nbt = object.get("fluid");
		String string_nbt = element_nbt.getAsString();
		
		JsonElement element_capacity = object.get("capacity");
		String string_capacity = element_capacity.getAsString();
		
		CompoundNBT nbt = new CompoundNBT();
		
		try {
			nbt = JsonToNBT.getTagFromJson(string_nbt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		FluidTank tank = new FluidTank(Integer.parseInt(string_capacity));
		tank.readFromNBT(nbt);
		
		return tank;
	}

	@Override
	public JsonElement serialize(FluidTank src, Type typeOfSrc, JsonSerializationContext context) {
		CompoundNBT nbt = new CompoundNBT();
		src.writeToNBT(nbt);
		
		String nbt_string = nbt.toString();
		
		JsonObject object = new JsonObject();
		object.addProperty("fluid", nbt_string);
		object.addProperty("capacity", src.getCapacity());
		
		return object;
	}
}