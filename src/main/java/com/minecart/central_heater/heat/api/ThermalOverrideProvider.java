package com.minecart.central_heater.heat.api;

import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface ThermalOverrideProvider {
    @Nullable
    ThermalMaterial getThermalMaterialOverride(BlockState state);

    @Nullable
    ThermalMaterialBehavior getThermalBehaviorOverride(BlockState state);
}
