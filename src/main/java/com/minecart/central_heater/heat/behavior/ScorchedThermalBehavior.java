package com.minecart.central_heater.heat.behavior;

import com.minecart.central_heater.heat.api.HeatProfile;

public class ScorchedThermalBehavior extends BaseThermalBehavior {
    public ScorchedThermalBehavior() {
        super(new HeatProfile(620, 34, 36, 6, 60, 0, 1600, true));
    }
}
