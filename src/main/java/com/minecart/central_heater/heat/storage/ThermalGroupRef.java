package com.minecart.central_heater.heat.storage;

import net.minecraft.world.level.ChunkPos;

public record ThermalGroupRef(ChunkPos chunkPos, int sectionY, int cellIndex, int groupId) {
}
