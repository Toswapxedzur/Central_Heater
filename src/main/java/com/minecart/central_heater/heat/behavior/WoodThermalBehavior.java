package com.minecart.central_heater.heat.behavior;

import com.minecart.central_heater.heat.api.HeatProfile;

public class WoodThermalBehavior extends BaseThermalBehavior {
    public WoodThermalBehavior() {
        super(new HeatProfile(220, 12, 32, 26, 24, 620, 760, true));
    }
}
