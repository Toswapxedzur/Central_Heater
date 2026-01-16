package com.minecart.central_heater.misc;

import com.minecart.central_heater.CentralHeater;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = CentralHeater.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue REPLACE_CAULDRON = BUILDER.comment("Whether to replace the existing cauldron in structures liek witch hut to the iron cauldron that the mod adds").define("replaceCauldron", true);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean replaceCauldron;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        replaceCauldron = REPLACE_CAULDRON.get();
    }
}
