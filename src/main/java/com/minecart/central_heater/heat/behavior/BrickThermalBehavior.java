package com.minecart.central_heater.heat.behavior;

import com.minecart.central_heater.heat.api.HeatProfile;

public class BrickThermalBehavior extends BaseThermalBehavior {
    public BrickThermalBehavior() {
        super(new HeatProfile(720, 28, 24, 5, 48, 0, 1100, true));
    }
}
