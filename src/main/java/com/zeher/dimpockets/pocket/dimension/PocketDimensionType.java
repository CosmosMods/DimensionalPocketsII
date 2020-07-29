package com.zeher.dimpockets.pocket.dimension;

import java.util.function.BiFunction;

import com.zeher.dimpockets.DimensionalPockets;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ModDimension;

public class PocketDimensionType extends ModDimension {
	
	public PocketDimensionType(final ResourceLocation registryName) {
		this.setRegistryName(registryName);
	}

	public DimensionType getDimensionType() {
		return DimensionType.byName(new ResourceLocation(DimensionalPockets.MOD_ID, "pocket_dimension"));
	}
	
	@Override
	public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
		return PocketDimension::new;
	}
}