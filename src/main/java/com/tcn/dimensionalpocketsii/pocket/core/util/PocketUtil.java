package com.tcn.dimensionalpocketsii.pocket.core.util;

import com.tcn.cosmoslibrary.math.ChunkPos;
import com.tcn.dimensionalpocketsii.DimensionalPockets;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PocketUtil {
	
	private static final String NBT_DIMENSIONAL_POCKET_KEY = "pocket_data";
	
	public static boolean isDimensionEqual(World worldIn, RegistryKey<World> dimensionIn) {
		return worldIn.getDimensionKey().equals(dimensionIn);
	}

	public static ChunkPos convertTo(BlockPos posIn) {
		return new ChunkPos(posIn.getX(), posIn.getZ());
	}

	public static BlockPos convertFrom(ChunkPos posIn) {
		return new BlockPos(posIn.getX(), 0, posIn.getZ());
	}

	public static ChunkPos scaleToChunkPos(BlockPos posIn) {
		return new ChunkPos(posIn.getX() >> 4, posIn.getZ() >> 4);
	}

	public static BlockPos scaleFromChunkPos(ChunkPos posIn) {
		return new BlockPos(posIn.getX() * 16, 0, posIn.getZ() * 16);
	}
	
	public static boolean hasPocketKey(CompoundNBT compoundIn) {
		return compoundIn.contains(NBT_DIMENSIONAL_POCKET_KEY);
	}
	
	public static CompoundNBT getPlayerPersistTag(PlayerEntity playerIn) {
		CompoundNBT tag = playerIn.serializeNBT();

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
