package com.tcn.dimensionalpocketsii.core.item;

import com.tcn.dimensionalpocketsii.core.management.ModBusManager;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public enum CoreItemTier implements Tier {
	
	DIMENSIONAL(4, 4000, 12.0F, 8.0F, 30, Ingredient.of(ModBusManager.DIMENSIONAL_INGOT.get())),
	DIMENSIONAL_ENHANCED(4, 6000, 16.0F, 14.0F, 40, Ingredient.of(ModBusManager.DIMENSIONAL_INGOT.get()));

	private final int level;
	private final int uses;
	private final float speed;
	private final float damage;
	private final int enchantmentValue;
	private final Ingredient repairIngredient;

	private CoreItemTier(int levelIn, int usesIn, float speedIn, float damageIn, int enchantmentValueIn, Ingredient repairIngredientIn) {
		this.level = levelIn;
		this.uses = usesIn;
		this.speed = speedIn;
		this.damage = damageIn;
		this.enchantmentValue = enchantmentValueIn;
		this.repairIngredient = repairIngredientIn;
	}

	@Override
	public int getLevel() {
		return this.level;
	}

	@Override
	public int getUses() {
		return this.uses;
	}
	
	@Override
	public float getSpeed() {
		return this.speed;
	}

	@Override
	public float getAttackDamageBonus() {
		return this.damage;
	}

	@Override
	public int getEnchantmentValue() {
		return this.enchantmentValue;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return this.repairIngredient;
	}
}