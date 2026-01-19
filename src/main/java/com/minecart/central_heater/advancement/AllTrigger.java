package com.minecart.central_heater.advancement;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;

public class AllTrigger {
    // In 1.20.1, we store the actual instances rather than RegistryObjects/Holders
    public static MinecartSpeedTrigger MINECART_SPEED = new MinecartSpeedTrigger();
    public static BurntObjectTrigger BURNT_OBJECT = new BurntObjectTrigger();

    public static void register() {
        // Manual registration into the vanilla CriteriaTriggers system
        register(MINECART_SPEED);
        register(BURNT_OBJECT);
    }

    private static <T extends CriterionTrigger<?>> T register(T trigger) {
        return CriteriaTriggers.register(trigger);
    }
}