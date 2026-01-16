package com.minecart.central_heater.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

import java.util.Optional;

public class MinecartSpeedTrigger extends SimpleCriterionTrigger<MinecartSpeedTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, double speed) {
        this.trigger(player, instance -> instance.matches(speed));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, MinMaxBounds.Doubles speed) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                MinMaxBounds.Doubles.CODEC.optionalFieldOf("speed", MinMaxBounds.Doubles.ANY).forGetter(TriggerInstance::speed)
        ).apply(instance, TriggerInstance::new));

        public boolean matches(double currentSpeed) {
            return this.speed.matches(currentSpeed);
        }

        public static Criterion<TriggerInstance> speeding(MinMaxBounds.Doubles speedRange) {
            return AllTrigger.MINECART_SPEED.get().createCriterion(new TriggerInstance(Optional.empty(), speedRange));
        }
    }
}
