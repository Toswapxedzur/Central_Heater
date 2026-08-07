package com.minecart.central_heater.heat.behavior;

import com.minecart.central_heater.heat.api.HeatProfile;
import com.minecart.central_heater.heat.context.ThermalGroupAccess;
import com.minecart.central_heater.heat.context.ThermalGroupContext;

public class WaterThermalBehavior extends BaseThermalBehavior {
    public WaterThermalBehavior() {
        super(new HeatProfile(1200, 64, 0, 90, 120, 0, 390, true));
    }

    @Override
    public void tickGroup(ThermalGroupContext ctx, ThermalGroupAccess heat) {
        if (heat.getExtraHeat() > 0) {
            heat.setExtraHeat(Math.max(0, heat.getExtraHeat() - 4));
        }
    }
}
