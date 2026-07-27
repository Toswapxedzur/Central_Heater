package com.minecart.central_heater.heat.behavior;

import com.minecart.central_heater.heat.api.HeatProfile;
import com.minecart.central_heater.heat.api.ThermalMaterialBehavior;

public class BaseThermalBehavior implements ThermalMaterialBehavior {
    private final HeatProfile profile;

    public BaseThermalBehavior(HeatProfile profile) {
        this.profile = profile;
    }

    @Override
    public HeatProfile profile() {
        return profile;
    }
}
