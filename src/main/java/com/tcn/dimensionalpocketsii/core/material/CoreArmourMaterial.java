package com.tcn.dimensionalpocketsii.core.material;

import java.util.function.Supplier;

import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.core.management.CoreModBusManager;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public enum CoreArmourMaterial implements IArmorMaterial {
	DIMENSIONAL("dimensional", 40, new int[] { 3, 6, 8, 3 }, 20, SoundEvents.ARMOR_EQUIP_GENERIC, 4.0F, 0.2F, () -> { return Ingredient.of(CoreModBusManager.DIMENSIONAL_INGOT); }),
	DIMENSIONAL_ENHANCED("dimensional_enhanced", 45, new int[] { 3, 6, 8, 3 }, 25, SoundEvents.ARMOR_EQUIP_GENERIC, 5.0F, 0.4F, () -> { return Ingredient.of(CoreModBusManager.DIMENSIONAL_INGOT); });

   private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
   private final String name;
   private final int maxDamageFactor;
   private final int[] damageReductionAmountArray;
   private final int enchantability;
   private final SoundEvent soundEvent;
   private final float toughness;
   private final float knockbackResistance;
   private final LazyValue<Ingredient> repairMaterial;

   private CoreArmourMaterial(String name, int maxDamageFactor, int[] damageReductionAmountArray, int enchantability, SoundEvent soundEvent, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterial) {
      this.name = DimensionalPockets.MOD_ID + ":" + name;
      this.maxDamageFactor = maxDamageFactor;
      this.damageReductionAmountArray = damageReductionAmountArray;
      this.enchantability = enchantability;
      this.soundEvent = soundEvent;
      this.toughness = toughness;
      this.knockbackResistance = knockbackResistance;
      this.repairMaterial = new LazyValue<>(repairMaterial);
   }

   @Override
   public int getDurabilityForSlot(EquipmentSlotType slotIn) {
      return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
   }

   @Override
   public int getDefenseForSlot(EquipmentSlotType slotIn) {
      return this.damageReductionAmountArray[slotIn.getIndex()];
   }

   @Override
   public int getEnchantmentValue() {
      return this.enchantability;
   }

   @Override
   public SoundEvent getEquipSound() {
      return this.soundEvent;
   }

   @Override
   public Ingredient getRepairIngredient() {
      return this.repairMaterial.get();
   }

   @OnlyIn(Dist.CLIENT)
   public String getName() {
      return this.name;
   }

   @Override
   public float getToughness() {
      return this.toughness;
   }

   /**
    * Gets the percentage of knockback resistance provided by armor of the material. 
    */
   public float getKnockbackResistance() {
      return this.knockbackResistance;
   }
}