package com.minecart.central_heater.temperature;

import net.minecraft.core.BlockPos;

public interface IBlockEntityHeatable {
    public int provide(BlockPos offset);
}
