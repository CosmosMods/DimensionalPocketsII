package com.tcn.cosmoslibrary.impl.registry.gson;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.tcn.cosmoslibrary.impl.nbt.UtilNBT.Const;
import com.tcn.cosmoslibrary.impl.registry.object.ObjectFluidTankCustom;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.registries.ForgeRegistries;

public class GsonAdapterFluidTankCustom implements JsonSerializer<ObjectFluidTankCustom>, JsonDeserializer<ObjectFluidTankCustom> {

	private static final String NBT_FLUID_KEY = "fluid";
	private static final String NBT_FILL_LEVEL_KEY = "fill_level";
	private static final String NBT_FLUID_CAPACITY_KEY = "capacity";
	private static final String NBT_FLUID_VOLUME_KEY = "volume";
	
	public GsonAdapterFluidTankCustom() { }

	@Override
	public ObjectFluidTankCustom deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject object = json.getAsJsonObject();
		
		JsonObject fluid = object.getAsJsonObject(NBT_FLUID_KEY);
		
		int capacity = object.get(NBT_FLUID_CAPACITY_KEY).getAsInt();
		int volume = object.get(NBT_FLUID_VOLUME_KEY).getAsInt();
		int fill_level = object.get(NBT_FILL_LEVEL_KEY).getAsInt();
		
		FluidTank tank = new FluidTank(capacity);
		
		String namespace = fluid.get(Const.NBT_NAMESPACE_KEY).getAsString();
		String path = fluid.get(Const.NBT_PATH_KEY).getAsString();
		
		ResourceLocation fluidName = new ResourceLocation(namespace, path);
		tank.setFluid(new FluidStack(ForgeRegistries.FLUIDS.getValue(fluidName), volume));
		
		return new ObjectFluidTankCustom(tank, fill_level);
	}

	@Override
	public JsonElement serialize(ObjectFluidTankCustom src, Type typeOfSrc, JsonSerializationContext context) {
		ResourceLocation fluid_name = src.getFluidTank().getFluid().getFluid().getRegistryName();
		
		JsonObject object = new JsonObject();
		JsonObject fluid = new JsonObject();
		
		fluid.addProperty(Const.NBT_NAMESPACE_KEY, fluid_name.getNamespace());
		fluid.addProperty(Const.NBT_PATH_KEY, fluid_name.getPath());
		
		object.addProperty(NBT_FILL_LEVEL_KEY, src.getFillLevel());
		object.addProperty(NBT_FLUID_CAPACITY_KEY, src.getFluidTank().getCapacity());
		object.addProperty(NBT_FLUID_VOLUME_KEY, src.getFluidTank().getFluidAmount());
		object.add(NBT_FLUID_KEY, fluid);
		
		return object;
	}
}