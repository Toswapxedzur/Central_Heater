package com.minecart.central_heater.heat.behavior;

import com.minecart.central_heater.heat.api.HeatProfile;

public class StoneThermalBehavior extends BaseThermalBehavior {
    public StoneThermalBehavior() {
        super(new HeatProfile(480, 18, 16, 8, 32, 0, 900, true));
    }
}
