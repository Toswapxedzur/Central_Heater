package com.minecart.central_heater.heat.behavior;

import com.minecart.central_heater.heat.api.HeatProfile;
import com.minecart.central_heater.heat.context.ThermalGroupAccess;
import com.minecart.central_heater.heat.context.ThermalGroupContext;

public class LavaThermalBehavior extends BaseThermalBehavior {
    public LavaThermalBehavior() {
        super(new HeatProfile(1600, 80, 0, 2, 180, 0, 1800, true));
    }

    @Override
    public void tickGroup(ThermalGroupContext ctx, ThermalGroupAccess heat) {
        heat.setExtraHeat(Math.max(500, heat.getExtraHeat()));
    }
}
