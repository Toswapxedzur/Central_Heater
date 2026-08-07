package com.minecart.central_heater.heat.behavior;

import com.minecart.central_heater.heat.api.HeatProfile;

public class GoldThermalBehavior extends BaseThermalBehavior {
    public GoldThermalBehavior() {
        super(new HeatProfile(430, 190, 2, 20, 240, 0, 900, true));
    }
}
