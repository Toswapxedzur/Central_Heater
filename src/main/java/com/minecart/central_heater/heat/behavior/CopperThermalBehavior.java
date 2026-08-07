package com.minecart.central_heater.heat.behavior;

import com.minecart.central_heater.heat.api.HeatProfile;

public class CopperThermalBehavior extends BaseThermalBehavior {
    public CopperThermalBehavior() {
        super(new HeatProfile(520, 160, 4, 18, 220, 0, 1000, true));
    }
}
