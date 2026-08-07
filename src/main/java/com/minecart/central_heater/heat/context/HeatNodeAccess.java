package com.minecart.central_heater.heat.context;

public interface HeatNodeAccess {
    int getHeat();

    void setHeat(int heat);

    default void addHeat(int delta) {
        setHeat(getHeat() + delta);
    }
}
