package com.minecart.central_heater.heat.api;

import com.minecart.central_heater.heat.context.ThermalGroupAccess;
import com.minecart.central_heater.heat.context.ThermalGroupContext;
import net.minecraft.core.Direction;

public interface ThermalMaterialBehavior {
    HeatProfile profile();

    default void tickGroup(ThermalGroupContext ctx, ThermalGroupAccess heat) {
    }

    default HeatPort getPort(ThermalGroupContext ctx, Direction side) {
        return HeatPort.fromProfile(profile());
    }

    default int modifyTransfer(ThermalGroupContext from, ThermalGroupContext to, Direction direction, int proposedTransfer) {
        return proposedTransfer;
    }

    default void onHeatChanged(ThermalGroupContext ctx, ThermalGroupAccess heat, int oldHeat, int newHeat) {
    }

    default void onThresholdCrossed(ThermalGroupContext ctx, ThermalGroupAccess heat, int oldHeat, int newHeat) {
    }

    default boolean shouldStore(ThermalGroupContext ctx, int extraHeat) {
        return profile().storesResidualHeat() && Math.abs(extraHeat) > 1;
    }
}
