package com.tcn.cosmoslibrary.impl.nbt;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class UtilNBT {

	public static void writeDimensionToNBT(RegistryKey<World> dimension, CompoundNBT compound) {
		CompoundNBT dimension_tag = new CompoundNBT();
		
		ResourceLocation location = dimension.getLocation();
		
		dimension_tag.putString("namespace", location.getNamespace());
		dimension_tag.putString("path", location.getPath());
		
		compound.put("block_dimension", dimension_tag);
	}
	
	public static ResourceLocation readDimensionFromNBT(CompoundNBT compound) {
		if (compound.contains("block_dimension")) {
			CompoundNBT dimension = compound.getCompound("block_dimension");
			
			String namespace = dimension.getString("namespace");
			String path = dimension.getString("path");
			
			return new ResourceLocation(namespace, path);
		}
		
		return null;
	}
	
	public static class Const {
		public static final String NBT_NAMESPACE_KEY = "namespace";
		public static final String NBT_PATH_KEY = "path";
		
		public static final String NBT_POS_KEY = "pos";
		
		public static final String NBT_POS_X_KEY = "x";
		public static final String NBT_POS_Y_KEY = "y";
		public static final String NBT_POS_Z_KEY = "z";
		
		public static final String NBT_POS_YAW_KEY = "yaw";
		public static final String NBT_POS_PITCH_KEY = "pitch";
	}
}
