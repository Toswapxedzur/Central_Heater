package com.minecart.central_heater.advancement;

import com.google.gson.JsonObject;
import com.minecart.central_heater.CentralHeater;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class BurntObjectTrigger extends SimpleCriterionTrigger<BurntObjectTrigger.TriggerInstance> {
    private static final ResourceLocation ID = new ResourceLocation(CentralHeater.MODID, "burnt_object");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    protected TriggerInstance createInstance(JsonObject json, ContextAwarePredicate playerPredicate, DeserializationContext context) {
        ItemPredicate itempredicate = ItemPredicate.fromJson(json.get("item"));
        return new TriggerInstance(playerPredicate, itempredicate);
    }

    public void trigger(ServerPlayer player, ItemStack stack) {
        this.trigger(player, instance -> instance.matches(stack));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final ItemPredicate item;

        public TriggerInstance(ContextAwarePredicate player, ItemPredicate item) {
            super(BurntObjectTrigger.ID, player);
            this.item = item;
        }

        public static TriggerInstance burnt(ItemLike item) {
            return new TriggerInstance(ContextAwarePredicate.ANY, ItemPredicate.Builder.item().of(item).build());
        }

        public boolean matches(ItemStack stack) {
            return this.item.matches(stack);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject jsonobject = super.serializeToJson(context);
            if (this.item != ItemPredicate.ANY) {
                jsonobject.add("item", this.item.serializeToJson());
            }
            return jsonobject;
        }
    }
}