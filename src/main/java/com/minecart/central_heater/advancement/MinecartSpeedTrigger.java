package com.minecart.central_heater.advancement;

import com.google.gson.JsonObject;
import com.minecart.central_heater.CentralHeater;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class MinecartSpeedTrigger extends SimpleCriterionTrigger<MinecartSpeedTrigger.TriggerInstance> {
    private static final ResourceLocation ID = new ResourceLocation(CentralHeater.MODID, "minecart_speed");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    protected TriggerInstance createInstance(JsonObject json, ContextAwarePredicate playerPredicate, DeserializationContext context) {
        MinMaxBounds.Doubles speed = MinMaxBounds.Doubles.fromJson(json.get("speed"));
        return new TriggerInstance(playerPredicate, speed);
    }

    public void trigger(ServerPlayer player, double speed) {
        this.trigger(player, instance -> instance.matches(speed));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final MinMaxBounds.Doubles speed;

        public TriggerInstance(ContextAwarePredicate player, MinMaxBounds.Doubles speed) {
            super(MinecartSpeedTrigger.ID, player);
            this.speed = speed;
        }

        public static TriggerInstance speeding(MinMaxBounds.Doubles speedRange) {
            return new TriggerInstance(ContextAwarePredicate.ANY, speedRange);
        }

        public boolean matches(double currentSpeed) {
            return this.speed.matches(currentSpeed);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject jsonobject = super.serializeToJson(context);
            jsonobject.add("speed", this.speed.serializeToJson());
            return jsonobject;
        }
    }
}