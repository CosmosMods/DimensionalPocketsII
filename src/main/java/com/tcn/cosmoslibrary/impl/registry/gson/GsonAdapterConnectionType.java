package com.tcn.cosmoslibrary.impl.registry.gson;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.tcn.cosmoslibrary.impl.enums.EnumConnectionType;
import com.tcn.cosmoslibrary.impl.nbt.UtilNBT.Const;
import com.tcn.cosmoslibrary.impl.registry.object.ObjectConnectionType;

import net.minecraft.util.math.BlockPos;

public class GsonAdapterConnectionType implements JsonSerializer<ObjectConnectionType>, JsonDeserializer<ObjectConnectionType> {

	private static final String NBT_BLOCKPOS_KEY = "block_pos";
	private static final String NBT_TYPE_KEY = "type";
	
	public GsonAdapterConnectionType() { }

	@Override
	public ObjectConnectionType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject object = json.getAsJsonObject();
		JsonObject block_pos = object.getAsJsonObject(NBT_BLOCKPOS_KEY);
		
		int x = block_pos.get(Const.NBT_POS_X_KEY).getAsInt();
		int y = block_pos.get(Const.NBT_POS_Y_KEY).getAsInt();
		int z = block_pos.get(Const.NBT_POS_Z_KEY).getAsInt();
		
		BlockPos pos = new BlockPos(x, y, z);
		String name = object.get(NBT_TYPE_KEY).getAsString();
		
		EnumConnectionType type = EnumConnectionType.getStateFromName(name);
		
		return new ObjectConnectionType(pos, type);
	}

	@Override
	public JsonElement serialize(ObjectConnectionType src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject object = new JsonObject();
		JsonObject block_pos = new JsonObject();
		
		BlockPos pos = src.getPos();
		
		block_pos.addProperty(Const.NBT_POS_X_KEY, pos.getX());
		block_pos.addProperty(Const.NBT_POS_Y_KEY, pos.getY());
		block_pos.addProperty(Const.NBT_POS_Z_KEY, pos.getZ());
		
		object.addProperty(NBT_TYPE_KEY, src.getType().getName());
		object.add(NBT_BLOCKPOS_KEY, block_pos);
		
		return object;
	}
}