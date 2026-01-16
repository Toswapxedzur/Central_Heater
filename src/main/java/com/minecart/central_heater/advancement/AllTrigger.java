package com.minecart.central_heater.advancement;

import com.minecart.central_heater.CentralHeater;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AllTrigger {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS =
            DeferredRegister.create(Registries.TRIGGER_TYPE, CentralHeater.MODID);

    public static final DeferredHolder<CriterionTrigger<?>, MinecartSpeedTrigger> MINECART_SPEED =
            TRIGGERS.register("minecart_speed", MinecartSpeedTrigger::new);

    public static final DeferredHolder<CriterionTrigger<?>, BurntObjectTrigger> BURNT_OBJECT =
            TRIGGERS.register("burnt_object", BurntObjectTrigger::new);

    public static void register(IEventBus eventBus) {
        TRIGGERS.register(eventBus);
    }
}
