package com.minecart.central_heater.heat.debug;

import com.minecart.central_heater.heat.HeatAttachments;
import com.minecart.central_heater.heat.sim.HeatScheduler;
import com.minecart.central_heater.heat.storage.ChunkHeatData;
import com.minecart.central_heater.heat.storage.ThermalCell;
import com.minecart.central_heater.network.ClientboundHeatDebugSnapshotPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class HeatDebugTempDisplay {
    private static final int CELL_RADIUS = 3;
    private static final int MAX_LABELS = 384;
    private static final int MAX_OUTLINES = 512;
    private static final Set<UUID> ENABLED_PLAYERS = new HashSet<>();

    private HeatDebugTempDisplay() {
    }

    public static int show(ServerPlayer player) {
        ENABLED_PLAYERS.add(player.getUUID());
        return sendSnapshot(player);
    }

    public static void hide(ServerPlayer player) {
        ENABLED_PLAYERS.remove(player.getUUID());
        PacketDistributor.sendToPlayer(player, new ClientboundHeatDebugSnapshotPayload(false, List.of(), List.of()));
    }

    public static boolean isEnabled(ServerPlayer player) {
        return ENABLED_PLAYERS.contains(player.getUUID());
    }

    public static void tickPlayer(ServerPlayer player) {
        if (player.tickCount % 10 == 0 && isEnabled(player)) {
            sendSnapshot(player);
        }
    }

    private static int sendSnapshot(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        BlockPos playerPos = player.blockPosition();
        int centerCellX = Math.floorDiv(playerPos.getX(), ThermalCell.SIZE);
        int centerCellY = Math.floorDiv(playerPos.getY(), ThermalCell.SIZE);
        int centerCellZ = Math.floorDiv(playerPos.getZ(), ThermalCell.SIZE);
        List<HeatDebugLabel> labels = new ArrayList<>();
        List<HeatDebugOutline> outlines = new ArrayList<>();

        for (int cellX = centerCellX - CELL_RADIUS; cellX <= centerCellX + CELL_RADIUS && (labels.size() < MAX_LABELS || outlines.size() < MAX_OUTLINES); cellX++) {
            for (int cellY = centerCellY - CELL_RADIUS; cellY <= centerCellY + CELL_RADIUS && (labels.size() < MAX_LABELS || outlines.size() < MAX_OUTLINES); cellY++) {
                int originY = cellY * ThermalCell.SIZE;
                if (originY < level.getMinBuildHeight() || originY >= level.getMaxBuildHeight()) {
                    continue;
                }
                for (int cellZ = centerCellZ - CELL_RADIUS; cellZ <= centerCellZ + CELL_RADIUS && (labels.size() < MAX_LABELS || outlines.size() < MAX_OUTLINES); cellZ++) {
                    collectCell(level, cellX, cellY, cellZ, labels, outlines);
                }
            }
        }

        PacketDistributor.sendToPlayer(player, new ClientboundHeatDebugSnapshotPayload(true, labels, outlines));
        return labels.size();
    }

    private static void collectCell(ServerLevel level, int cellX, int cellY, int cellZ, List<HeatDebugLabel> labels, List<HeatDebugOutline> outlines) {
        int originX = cellX * ThermalCell.SIZE;
        int originY = cellY * ThermalCell.SIZE;
        int originZ = cellZ * ThermalCell.SIZE;
        int chunkX = Math.floorDiv(originX, 16);
        int chunkZ = Math.floorDiv(originZ, 16);
        ChunkAccess access = level.getChunk(chunkX, chunkZ, ChunkStatus.FULL, false);
        if (!(access instanceof LevelChunk chunk)) {
            return;
        }
        ChunkHeatData data = chunk.getData(HeatAttachments.HEAT_CHUNK);
        int sectionY = SectionPos.blockToSectionCoord(originY);
        int cellIndex = Math.floorMod(cellX, 4) | (Math.floorMod(cellY, 4) << 2) | (Math.floorMod(cellZ, 4) << 4);
        ThermalCell cell = data.getOrCreateCell(sectionY, cellIndex);
        data.ensureTopology(level, chunk.getPos(), sectionY, cellIndex, cell);
        HeatScheduler.enqueue(level, chunk.getPos(), sectionY, cellIndex);
        data.collectDebugLabelsForCell(level, chunk.getPos(), sectionY, cellIndex, cell, labels, MAX_LABELS, true);
        data.collectDebugOutlinesForCell(level, chunk.getPos(), sectionY, cellIndex, cell, outlines, MAX_OUTLINES);
    }
}
