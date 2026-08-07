package com.minecart.central_heater.heat.context;

import com.minecart.central_heater.heat.storage.HeatNode;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public record HeatNodeContext(ServerLevel level, BlockPos pos, HeatNode node) {
}
