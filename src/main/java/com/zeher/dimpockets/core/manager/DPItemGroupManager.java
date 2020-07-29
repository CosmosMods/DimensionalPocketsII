
package com.zeher.dimpockets.core.manager;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.zeher.dimpockets.DimensionalPockets;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public final class DPItemGroupManager {

	public static final ItemGroup DIM_POCKETS_ITEM_GROUP = new ModItemGroup(DimensionalPockets.MOD_ID, () -> new ItemStack(BusSubscriberMod.BLOCK_POCKET));

	public static final class ModItemGroup extends ItemGroup {

		@Nonnull
		private final Supplier<ItemStack> iconSupplier;

		public ModItemGroup(@Nonnull final String name, @Nonnull final Supplier<ItemStack> iconSupplier) {
			super(name);
			this.iconSupplier = iconSupplier;
		}

		@Override
		@Nonnull
		public ItemStack createIcon() {
			return iconSupplier.get();
		}
	}
}