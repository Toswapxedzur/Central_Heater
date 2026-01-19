package com.minecart.central_heater.misc;

import com.minecart.central_heater.CentralHeater;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = CentralHeater.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue REPLACE_CAULDRON = BUILDER.comment("Whether to replace the existing cauldron in structures liek witch hut to the iron cauldron that the mod adds").define("replaceCauldron", true);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean replaceCauldron;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        replaceCauldron = REPLACE_CAULDRON.get();
    }
}