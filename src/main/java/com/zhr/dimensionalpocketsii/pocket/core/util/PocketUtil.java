package com.zhr.dimensionalpocketsii.pocket.core.util;

import com.zhr.dimensionalpocketsii.DimensionalPockets;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

public class PocketUtil {
	
	private static final String NBT_DIMENSIONAL_POCKET_KEY = "pocket_data";

	public static BlockPos getChunkPos(BlockPos pos) {
		return new BlockPos(new BlockPos(pos.getX() >> 4, pos.getY() >> 4, pos.getZ() >> 4));
	}
	
	public static boolean hasPocketKey(CompoundNBT compound) {
		return compound.contains(NBT_DIMENSIONAL_POCKET_KEY);
	}
	
	public static CompoundNBT getPlayerPersistTag(PlayerEntity player) {
		CompoundNBT tag = player.serializeNBT();

		CompoundNBT persistTag;
		if (tag.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
			persistTag = (CompoundNBT) tag.get(PlayerEntity.PERSISTED_NBT_TAG);
		} else {
			persistTag = new CompoundNBT();
			tag.put(PlayerEntity.PERSISTED_NBT_TAG, persistTag);
		}

		CompoundNBT modTag;
		String modID = DimensionalPockets.MOD_ID;

		if (persistTag.contains(modID)) {
			modTag = (CompoundNBT) persistTag.get(modID);
		} else {
			modTag = new CompoundNBT();
			persistTag.put(modID, modTag);
		}

		return modTag;
	}
}
