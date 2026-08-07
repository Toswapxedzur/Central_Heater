package com.minecart.central_heater.heat.api;

import com.minecart.central_heater.heat.context.HeatNodeAccess;
import com.minecart.central_heater.heat.context.HeatNodeContext;
import com.minecart.central_heater.heat.storage.HeatNode;
import net.minecraft.core.Direction;

public interface HeatBlockEntityBehavior {
    HeatNode createHeatNode();

    default void tickHeatNode(HeatNodeContext ctx, HeatNodeAccess heat) {
    }

    default HeatEmission getEmission(HeatNodeContext ctx) {
        return HeatEmission.NONE;
    }

    default HeatSink getSink(HeatNodeContext ctx) {
        return HeatSink.NONE;
    }

    default HeatPort getPort(HeatNodeContext ctx, Direction side) {
        return new HeatPort(0, 0, 0, false);
    }

    default void onHeatChanged(HeatNodeContext ctx, HeatNodeAccess heat, int oldHeat, int newHeat) {
    }
}
