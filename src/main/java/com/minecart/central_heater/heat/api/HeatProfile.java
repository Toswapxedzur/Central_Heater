package com.minecart.central_heater.heat.api;

public record HeatProfile(
        int heatCapacity,
        int conductivity,
        int insulation,
        int coolingRate,
        int maxTransfer,
        int ignitionHeat,
        int damageHeat,
        boolean storesResidualHeat
) {
    public HeatProfile {
        heatCapacity = Math.max(1, heatCapacity);
        conductivity = Math.max(0, conductivity);
        insulation = Math.max(0, insulation);
        coolingRate = Math.max(0, coolingRate);
        maxTransfer = Math.max(0, maxTransfer);
    }
}
