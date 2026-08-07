package com.minecart.central_heater.heat.sim;

import com.minecart.central_heater.heat.HeatManager;
import com.minecart.central_heater.heat.storage.ChunkHeatData;
import com.minecart.central_heater.misc.Config;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HeatScheduler {
    private static final Map<ServerLevel, LevelQueue> QUEUES = new HashMap<>();
    private static final Map<ServerLevel, HeatStats> STATS = new HashMap<>();

    private HeatScheduler() {
    }

    public static void enqueue(ServerLevel level, ChunkPos chunkPos, int sectionY, int cellIndex) {
        queue(level).add(new CellKey(chunkPos.toLong(), sectionY, cellIndex), level.getGameTime(), Config.heatTickInterval);
    }

    public static void wakeSoon(ServerLevel level, ChunkPos chunkPos, int sectionY, int cellIndex, int delayTicks) {
        queue(level).addAt(new CellKey(chunkPos.toLong(), sectionY, cellIndex), level.getGameTime() + Math.max(1, delayTicks), Config.heatTickInterval);
    }

    public static void tick(ServerLevel level) {
        if (!Config.heatSystemEnabled || Config.heatTickInterval <= 0) {
            return;
        }
        LevelQueue queue = queue(level);
        long now = level.getGameTime();
        long started = System.nanoTime();
        int attempts = 0;
        int processed = 0;
        int skippedUnloaded = 0;
        int budget = Math.max(1, Config.heatChunkBudgetPerTick * Config.heatSubchunkBudgetPerChunk);
        long maxLateness = 0L;
        CellKey key;
        while (attempts < budget && (key = queue.removeDue(now)) != null) {
            attempts++;
            maxLateness = Math.max(maxLateness, Math.max(0L, now - queue.lastRemovedDueTick()));
            int chunkX = ChunkPos.getX(key.chunkKey());
            int chunkZ = ChunkPos.getZ(key.chunkKey());
            int sectionY = key.sectionY();
            int cellIndex = key.cellIndex();
            ChunkAccess access = level.getChunk(chunkX, chunkZ, ChunkStatus.FULL, false);
            if (!(access instanceof LevelChunk chunk)) {
                skippedUnloaded++;
                continue;
            }
            ChunkHeatData data = HeatManager.getChunkData(chunk);
            int heatSteps = heatSteps(now - queue.lastRemovedLastTick());
            ChunkHeatData.HeatTickResult result = data.tickCell(level, chunk.getPos(), sectionY, cellIndex, heatSteps);
            processed++;
            data.removeColdCells();
            if (result.active()) {
                queue.addAfterTick(key, now + result.nextDelayTicks(), now);
                chunk.setUnsaved(true);
            }
        }
        STATS.put(level, new HeatStats(queue.size(), queue.hasDue(now) ? 1 : 0, processed, skippedUnloaded, queue.hasDue(now), maxLateness, System.nanoTime() - started));
    }

    private static int heatSteps(long elapsedTicks) {
        int baseInterval = Math.max(1, Config.heatTickInterval);
        return Math.max(1, Math.min(32, (int) ((Math.max(1L, elapsedTicks) + baseInterval - 1L) / baseInterval)));
    }

    public static HeatStats stats(ServerLevel level) {
        LevelQueue queue = queue(level);
        HeatStats last = STATS.getOrDefault(level, new HeatStats(queue.size(), 0, 0, 0, false, 0L, 0L));
        return new HeatStats(queue.size(), queue.countDue(level.getGameTime()), last.processedCells(), last.skippedUnloadedCells(), last.backlog(), last.maxLatenessTicks(), last.lastTickNanos());
    }

    private static LevelQueue queue(ServerLevel level) {
        return QUEUES.computeIfAbsent(level, ignored -> new LevelQueue());
    }

    public record HeatStats(int activeCells, int dueCells, int processedCells, int skippedUnloadedCells, boolean backlog, long maxLatenessTicks, long lastTickNanos) {
    }

    private record CellKey(long chunkKey, int sectionY, int cellIndex) {
    }

    private record ScheduledCell(CellKey key, long dueTick, long order) {
    }

    private record QueueState(long dueTick, long lastTick) {
    }

    private static class LevelQueue {
        private final PriorityQueue<ScheduledCell> queue = new PriorityQueue<>(Comparator
                .comparingLong(ScheduledCell::dueTick)
                .thenComparingLong(ScheduledCell::order));
        private final Map<CellKey, QueueState> states = new HashMap<>();
        private long order;
        private int spreadOffset;
        private long lastRemovedDueTick;
        private long lastRemovedLastTick;

        void add(CellKey key, long now, int interval) {
            int safeInterval = Math.max(1, interval);
            addAt(key, now + spreadOffset, interval);
            spreadOffset = (spreadOffset + 1) % safeInterval;
        }

        void addAt(CellKey key, long dueTick, int interval) {
            QueueState oldState = states.get(key);
            if (oldState != null && oldState.dueTick() <= dueTick) {
                return;
            }
            long lastTick = oldState == null ? dueTick - Math.max(1, interval) : oldState.lastTick();
            states.put(key, new QueueState(dueTick, lastTick));
            queue.add(new ScheduledCell(key, dueTick, order++));
        }

        void addAfterTick(CellKey key, long dueTick, long lastTick) {
            states.put(key, new QueueState(dueTick, lastTick));
            queue.add(new ScheduledCell(key, dueTick, order++));
        }

        CellKey removeDue(long now) {
            while (!queue.isEmpty()) {
                ScheduledCell scheduled = queue.peek();
                QueueState state = states.get(scheduled.key());
                if (state == null || state.dueTick() != scheduled.dueTick()) {
                    queue.remove();
                    continue;
                }
                if (scheduled.dueTick() > now) {
                    return null;
                }
                queue.remove();
                states.remove(scheduled.key());
                lastRemovedDueTick = scheduled.dueTick();
                lastRemovedLastTick = state.lastTick();
                return scheduled.key();
            }
            return null;
        }

        boolean hasDue(long now) {
            return nextDueTick() <= now;
        }

        int countDue(long now) {
            int count = 0;
            for (QueueState state : states.values()) {
                if (state.dueTick() <= now) {
                    count++;
                }
            }
            return count;
        }

        long lastRemovedDueTick() {
            return lastRemovedDueTick;
        }

        long lastRemovedLastTick() {
            return lastRemovedLastTick;
        }

        private long nextDueTick() {
            while (!queue.isEmpty()) {
                ScheduledCell scheduled = queue.peek();
                QueueState state = states.get(scheduled.key());
                if (state == null || state.dueTick() != scheduled.dueTick()) {
                    queue.remove();
                    continue;
                }
                return scheduled.dueTick();
            }
            return Long.MAX_VALUE;
        }

        int size() {
            return states.size();
        }
    }
}
