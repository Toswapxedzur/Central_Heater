package com.minecart.central_heater.heat.context;

import net.minecraft.core.BlockPos;

import java.util.List;

public interface ThermalGroupAccess {
    int getExtraHeat();

    int getActualHeat();

    void setExtraHeat(int extraHeat);

    default void addExtraHeat(int delta) {
        setExtraHeat(getExtraHeat() + delta);
    }

    List<BlockPos> sampleBlocks(int maxCount);
}
