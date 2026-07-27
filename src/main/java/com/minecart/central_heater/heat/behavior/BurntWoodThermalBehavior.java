package com.minecart.central_heater.heat.behavior;

import com.minecart.central_heater.heat.api.HeatProfile;

public class BurntWoodThermalBehavior extends BaseThermalBehavior {
    public BurntWoodThermalBehavior() {
        super(new HeatProfile(300, 16, 28, 22, 28, 500, 700, true));
    }
}
