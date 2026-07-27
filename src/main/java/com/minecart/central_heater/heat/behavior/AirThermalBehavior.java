package com.minecart.central_heater.heat.behavior;

import com.minecart.central_heater.heat.api.HeatProfile;
import com.minecart.central_heater.heat.context.ThermalGroupContext;

public class AirThermalBehavior extends BaseThermalBehavior {
    public AirThermalBehavior() {
        super(new HeatProfile(8, 96, 0, 220, 96, 0, 0, false));
    }

    @Override
    public boolean shouldStore(ThermalGroupContext ctx, int extraHeat) {
        return Math.abs(extraHeat) > 1;
    }

}
