package com.minecart.central_heater.heat.behavior;

import com.minecart.central_heater.heat.api.HeatProfile;

public class InsulatorThermalBehavior extends BaseThermalBehavior {
    public InsulatorThermalBehavior() {
        super(new HeatProfile(320, 2, 180, 10, 8, 550, 800, true));
    }
}
