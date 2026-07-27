package com.minecart.central_heater.heat.behavior;

import com.minecart.central_heater.heat.api.HeatProfile;

public class SturdyThermalBehavior extends BaseThermalBehavior {
    public SturdyThermalBehavior() {
        super(new HeatProfile(1600, 16, 90, 2, 36, 0, 2400, true));
    }
}
