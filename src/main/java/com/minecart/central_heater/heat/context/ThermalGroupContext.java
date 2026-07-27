package com.minecart.central_heater.heat.context;

import com.minecart.central_heater.heat.storage.ThermalGroup;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

public record ThermalGroupContext(ServerLevel level, ChunkPos chunkPos, int sectionY, int cellIndex, BlockPos cellOrigin, ThermalGroup group) {
}
