package com.tcn.dimensionalpocketsii.core.advancement;

import com.google.gson.JsonObject;
import com.tcn.dimensionalpocketsii.DimensionalPockets;

import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class UseShifterTrigger extends SimpleCriterionTrigger<UseShifterTrigger.TriggerInstance> {
	static final ResourceLocation ID = new ResourceLocation(DimensionalPockets.MOD_ID, "use_shifter");

	@Override
	public ResourceLocation getId() {
		return ID;
	}
	
	@Override
	public UseShifterTrigger.TriggerInstance createInstance(JsonObject jsonObjIn, EntityPredicate.Composite entityPredicateIn, DeserializationContext contextIn) {
		ItemPredicate itempredicate = ItemPredicate.fromJson(jsonObjIn.get("item"));
		return new UseShifterTrigger.TriggerInstance(entityPredicateIn, itempredicate);
	}

	public void trigger(ServerPlayer playerIn, ItemStack stackIn) {
		this.trigger(playerIn, (instance) -> {
			return instance.matches(stackIn);
		});
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {
		private final ItemPredicate item;

		public TriggerInstance(EntityPredicate.Composite entityPredicateIn, ItemPredicate itemPredicateIn) {
			super(UseShifterTrigger.ID, entityPredicateIn);
			this.item = itemPredicateIn;
		}

		public static UseShifterTrigger.TriggerInstance lookingAt(EntityPredicate.Builder entityPredicateIn, ItemPredicate.Builder itemPredicateIn) {
			return new UseShifterTrigger.TriggerInstance(EntityPredicate.Composite.wrap(entityPredicateIn.build()), itemPredicateIn.build());
		}

		public boolean matches(ItemStack stackIn) {
			return this.item.matches(stackIn);
		}

		public JsonObject serializeToJson(SerializationContext contextIn) {
			JsonObject jsonobject = super.serializeToJson(contextIn);
			jsonobject.add("item", this.item.serializeToJson());
			return jsonobject;
		}
	}
}