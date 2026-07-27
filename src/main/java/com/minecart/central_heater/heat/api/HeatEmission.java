package com.minecart.central_heater.heat.api;

public record HeatEmission(int powerPerUpdate, int targetHeat, int maxHeat, HeatType heatType) {
    public static final HeatEmission NONE = new HeatEmission(0, 0, 0, HeatType.NORMAL);
}
