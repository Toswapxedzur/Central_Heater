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
    private static final ModConfigSpec.BooleanValue HEAT_SYSTEM_ENABLED = BUILDER.comment("Whether the central heater simulation core should tick on the server.").define("heatSystemEnabled", true);
    private static final ModConfigSpec.IntValue HEAT_TICK_INTERVAL = BUILDER.comment("Target maximum game ticks between updates for each active heat subchunk. 50 is 2.5 seconds.").defineInRange("heatTickInterval", 50, 1, 20 * 60);
    private static final ModConfigSpec.IntValue HEAT_CHUNK_BUDGET_PER_TICK = BUILDER.comment("Legacy heat budget knob kept for existing configs; active heat subchunks are now cadence-scheduled by heatTickInterval.").defineInRange("heatChunkBudgetPerTick", 32, 1, 4096);
    private static final ModConfigSpec.IntValue HEAT_SUBCHUNK_BUDGET_PER_CHUNK = BUILDER.comment("Legacy heat budget knob kept for existing configs; active heat subchunks are now cadence-scheduled by heatTickInterval.").defineInRange("heatSubchunkBudgetPerChunk", 16, 1, 1024);
    private static final ModConfigSpec.IntValue HEAT_MIN_AMBIENT_KELVIN = BUILDER.comment("Minimum ambient temperature used by the heat simulation, in Kelvin.").defineInRange("heatMinAmbientKelvin", 180, Short.MIN_VALUE, Short.MAX_VALUE);
    private static final ModConfigSpec.IntValue HEAT_MAX_AMBIENT_KELVIN = BUILDER.comment("Maximum ambient temperature used by the heat simulation, in Kelvin.").defineInRange("heatMaxAmbientKelvin", 330, Short.MIN_VALUE, Short.MAX_VALUE);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean replaceCauldron;
    public static boolean heatSystemEnabled;
    public static int heatTickInterval;
    public static int heatChunkBudgetPerTick;
    public static int heatSubchunkBudgetPerChunk;
    public static int heatMinAmbientKelvin;
    public static int heatMaxAmbientKelvin;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        replaceCauldron = REPLACE_CAULDRON.get();
        heatSystemEnabled = HEAT_SYSTEM_ENABLED.get();
        heatTickInterval = HEAT_TICK_INTERVAL.get();
        heatChunkBudgetPerTick = HEAT_CHUNK_BUDGET_PER_TICK.get();
        heatSubchunkBudgetPerChunk = HEAT_SUBCHUNK_BUDGET_PER_CHUNK.get();
        heatMinAmbientKelvin = HEAT_MIN_AMBIENT_KELVIN.get();
        heatMaxAmbientKelvin = HEAT_MAX_AMBIENT_KELVIN.get();
    }
}
