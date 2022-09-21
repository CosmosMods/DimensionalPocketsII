package com.tcn.dimensionalpocketsii.core.item;

import com.tcn.dimensionalpocketsii.core.management.ObjectManager;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public enum CoreArmourMaterial implements ArmorMaterial {

    DIMENSIONAL("dimensionalpocketsii:base/tex", 40, new int[] { 5, 8, 10, 5 }, 30, SoundEvents.ARMOR_EQUIP_GENERIC, 6.0F, 0.4F, Ingredient.of(ObjectManager.dimensional_ingot)),
	DIMENSIONAL_ENHANCED("dimensionalpocketsii:enhanced/tex", 45, new int[] { 7, 10, 12, 7 }, 40, SoundEvents.ARMOR_EQUIP_GENERIC, 7.0F, 0.6F, Ingredient.of(ObjectManager.dimensional_ingot)),
	DIMENSIONAL_SPECIAL("dimensionalpocketsii:special/tex", 50, new int[] { 8, 11, 13, 8 }, 50, SoundEvents.ARMOR_EQUIP_GENERIC, 7.5F, 0.7F, Ingredient.of(ObjectManager.dimensional_ingot)),
	DIMENSIONAL_SPECIAL_SHIFTER("dimensionalpocketsii:shifter/tex", 50, new int[] { 8, 11, 13, 8 }, 50, SoundEvents.ARMOR_EQUIP_GENERIC, 7.5F, 0.7F, Ingredient.of(ObjectManager.dimensional_ingot)),
	DIMENSIONAL_SPECIAL_VISOR("dimensionalpocketsii:visor/tex", 50, new int[] { 8, 11, 13, 8 }, 50, SoundEvents.ARMOR_EQUIP_GENERIC, 7.5F, 0.7F, Ingredient.of(ObjectManager.dimensional_ingot));

	private static final int[] HEALTH_PER_SLOT = new int[] { 13, 15, 16, 11 };
	private final String name;
	private final int durabilityMultiplier;
	private final int[] slotProtections;
	private final int enchantmentValue;
	private final SoundEvent sound;
	private final float toughness;
	private final float knockbackResistance;
	private final Ingredient repairIngredient;

	private CoreArmourMaterial(String nameIn, int durabilityMultiplierIn, int[] slotProtectionsIn, int enchantmentValueIn, SoundEvent soundIn, float toughnessIn, float knockbackResistanceIn, Ingredient repairIngredientIn) {
		this.name = nameIn;
		this.durabilityMultiplier = durabilityMultiplierIn;
		this.slotProtections = slotProtectionsIn;
		this.enchantmentValue = enchantmentValueIn;
		this.sound = soundIn;
		this.toughness = toughnessIn;
		this.knockbackResistance = knockbackResistanceIn;
		this.repairIngredient = repairIngredientIn;
	}

	@Override
	public int getDurabilityForSlot(EquipmentSlot slotIn) {
		return HEALTH_PER_SLOT[slotIn.getIndex()] * this.durabilityMultiplier;
	}

	@Override
	public int getDefenseForSlot(EquipmentSlot slotIn) {
		return this.slotProtections[slotIn.getIndex()];
	}

	@Override
	public int getEnchantmentValue() {
		return this.enchantmentValue;
	}

	@Override
	public SoundEvent getEquipSound() {
		return this.sound;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return this.repairIngredient;
	}

	@OnlyIn(Dist.CLIENT)
	public String getName() {
		return this.name;
	}

	@Override
	public float getToughness() {
		return this.toughness;
	}
	
	@Override
	public float getKnockbackResistance() {
		return this.knockbackResistance;
	}
}