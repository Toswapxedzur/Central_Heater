package com.minecart.central_heater.heat;

import com.minecart.central_heater.heat.sim.HeatScheduler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

public class HeatTicker {
    private HeatTicker() {
    }

    public static void enqueue(ServerLevel level, ChunkPos chunkPos) {
        HeatManager.getChunkData(level.getChunk(chunkPos.x, chunkPos.z)).enqueueActiveCells(level, chunkPos);
    }

    public static void tick(ServerLevel level) {
        HeatScheduler.tick(level);
    }

    public static HeatStats stats(ServerLevel level) {
        HeatScheduler.HeatStats stats = HeatScheduler.stats(level);
        return new HeatStats(stats.activeCells(), stats.dueCells(), stats.processedCells(), stats.skippedUnloadedCells(), stats.backlog(), stats.maxLatenessTicks(), stats.lastTickNanos());
    }

    public record HeatStats(int activeCells, int dueCells, int processedCells, int skippedUnloadedCells, boolean backlog, long maxLatenessTicks, long lastTickNanos) {
    }
}
