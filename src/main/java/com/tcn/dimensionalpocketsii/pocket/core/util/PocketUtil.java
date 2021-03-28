package com.tcn.dimensionalpocketsii.pocket.core.util;

import com.tcn.dimensionalpocketsii.DimensionalPockets;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;

public class PocketUtil {
	
	private static final String NBT_DIMENSIONAL_POCKET_KEY = "pocket_data";
	
	public static boolean isDimensionEqual(World worldIn, RegistryKey<World> dimensionIn) {
		return worldIn.dimension().equals(dimensionIn);
	}

	public static boolean hasPocketKey(CompoundNBT compoundIn) {
		return compoundIn.contains(NBT_DIMENSIONAL_POCKET_KEY);
	}
	
	public static CompoundNBT getPlayerPersistTag(PlayerEntity playerIn) {
		CompoundNBT tag = playerIn.getPersistentData();
		
		CompoundNBT pocketTag;
		if (tag.contains(DimensionalPockets.MOD_ID)) {
			pocketTag = tag.getCompound(DimensionalPockets.MOD_ID);
		} else {
			pocketTag = new CompoundNBT();
			pocketTag.putBoolean("givenInfoBook", false);
			tag.put(DimensionalPockets.MOD_ID, pocketTag);
		}
		
		return pocketTag;
	}
}
