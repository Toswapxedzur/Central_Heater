package com.minecart.central_heater.heat.api;

public record HeatSink(int heatDemandPerUpdate, int minRequiredHeat, int maxDrainPerUpdate, boolean canCoolBelowAmbient) {
    public static final HeatSink NONE = new HeatSink(0, 0, 0, false);
}
