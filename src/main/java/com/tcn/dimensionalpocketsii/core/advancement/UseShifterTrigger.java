package com.tcn.dimensionalpocketsii.core.advancement;

import com.google.gson.JsonObject;

import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.EntityPredicate.AndPredicate;
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
		return new UseShifterTrigger.Instance(entityPredicate, ItemPredicate.fromJson(json.get("item")));
	}

	public void trigger(ServerPlayerEntity player, ItemStack item) {
		this.trigger(player, (instance) -> {
			return instance.matches(item);
		});
	}

	public static class Instance extends CriterionInstance {
		private final ItemPredicate item;

		public Instance(EntityPredicate.AndPredicate player, ItemPredicate item) {
			super(UseShifterTrigger.ID, player);
			this.item = item;
		}

		public static UseShifterTrigger.Instance usedItem() {
			return new UseShifterTrigger.Instance(EntityPredicate.AndPredicate.ANY, ItemPredicate.ANY);
		}

		public static UseShifterTrigger.Instance usedItem(IItemProvider item) {
			return new UseShifterTrigger.Instance(EntityPredicate.AndPredicate.ANY, new ItemPredicate((ITag<Item>) null, item.asItem(), MinMaxBounds.IntBound.ANY, MinMaxBounds.IntBound.ANY, EnchantmentPredicate.NONE, EnchantmentPredicate.NONE, (Potion) null, NBTPredicate.ANY));
		}

		public boolean matches(ItemStack item) {
			return this.item.matches(item);
		}

		public JsonObject serializeToJson(ConditionArraySerializer conditions) {
			JsonObject jsonobject = super.serializeToJson(conditions);
			jsonobject.add("item", this.item.serializeToJson());
			return jsonobject;
		}
	}

	@Override
	protected Instance createInstance(JsonObject jsonObjectIn, AndPredicate andPredicateIn, ConditionArrayParser arrayParserIn) {
		return null;
	}
}