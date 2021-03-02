package com.tcn.dimensionalpocketsii.core.advancement;

import com.google.gson.JsonObject;

import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.NBTPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.potion.Potion;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

public class UseShifterTrigger extends AbstractCriterionTrigger<UseShifterTrigger.Instance> {

	private static ResourceLocation ID;
	
	public UseShifterTrigger(ResourceLocation location) {
		ID = location;
	}

	public ResourceLocation getId() {
		return ID;
	}

	public UseShifterTrigger.Instance deserializeTrigger(JsonObject json, EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser conditionsParser) {
		return new UseShifterTrigger.Instance(entityPredicate, ItemPredicate.deserialize(json.get("item")));
	}

	public void trigger(ServerPlayerEntity player, ItemStack item) {
		this.triggerListeners(player, (instance) -> {
			return instance.test(item);
		});
	}

	public static class Instance extends CriterionInstance {
		private final ItemPredicate item;

		public Instance(EntityPredicate.AndPredicate player, ItemPredicate item) {
			super(UseShifterTrigger.ID, player);
			this.item = item;
		}

		public static UseShifterTrigger.Instance any() {
			return new UseShifterTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, ItemPredicate.ANY);
		}

		public static UseShifterTrigger.Instance forItem(IItemProvider item) {
			return new UseShifterTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, new ItemPredicate((ITag<Item>) null, item.asItem(), MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, EnchantmentPredicate.enchantments, EnchantmentPredicate.enchantments, (Potion) null, NBTPredicate.ANY));
		}

		public boolean test(ItemStack item) {
			return this.item.test(item);
		}

		public JsonObject serialize(ConditionArraySerializer conditions) {
			JsonObject jsonobject = super.serialize(conditions);
			jsonobject.add("item", this.item.serialize());
			return jsonobject;
		}
	}
}