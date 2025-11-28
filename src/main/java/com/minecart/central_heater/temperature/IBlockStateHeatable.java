package com.minecart.central_heater.temperature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface IBlockStateHeatable {
    public int provide(BlockPos offset, BlockState state);
}
