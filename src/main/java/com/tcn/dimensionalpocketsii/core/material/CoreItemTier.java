package com.tcn.dimensionalpocketsii.core.material;

import java.util.function.Supplier;

import com.tcn.dimensionalpocketsii.core.management.CoreModBusManager;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

public enum CoreItemTier implements IItemTier {
	
	DIMENSIONAL(4, 2500, 10.0F, 6.0F, 20, () -> {
		return Ingredient.of(CoreModBusManager.DIMENSIONAL_INGOT);
	});

	private final int harvestLevel;
	private final int maxUses;
	private final float efficiency;
	private final float attackDamage;
	private final int enchantability;
	private final LazyValue<Ingredient> repairMaterial;

	private CoreItemTier(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
		this.harvestLevel = harvestLevelIn;
		this.maxUses = maxUsesIn;
		this.efficiency = efficiencyIn;
		this.attackDamage = attackDamageIn;
		this.enchantability = enchantabilityIn;
		this.repairMaterial = new LazyValue<>(repairMaterialIn);
	}
	
	@Override
	public float getAttackDamageBonus() {
		return this.attackDamage;
	}

	@Override
	public int getEnchantmentValue() {
		return this.enchantability;
	}

	@Override
	public int getLevel() {
		return this.harvestLevel;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return this.repairMaterial.get();
	}

	@Override
	public float getSpeed() {
		return this.efficiency;
	}

	@Override
	public int getUses() {
		return this.maxUses;
	}

}