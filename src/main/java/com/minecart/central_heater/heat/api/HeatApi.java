package com.minecart.central_heater.heat.api;

import com.minecart.central_heater.heat.HeatManager;
import com.minecart.central_heater.heat.sim.AmbientHeat;
import com.minecart.central_heater.heat.sim.HeatScheduler;
import com.minecart.central_heater.heat.storage.ChunkHeatData;
import com.minecart.central_heater.heat.storage.ThermalCell;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;

public class HeatApi {
    private HeatApi() {
    }

    public static int getActualHeat(ServerLevel level, BlockPos pos) {
        LevelChunk chunk = level.getChunkAt(pos);
        return HeatManager.getChunkData(chunk).getActualHeat(level, chunk.getPos(), pos);
    }

    public static int getExtraHeat(ServerLevel level, BlockPos pos) {
        return HeatManager.getChunkData(level.getChunkAt(pos)).getExtraHeat(level, pos);
    }

    public static int getAmbientHeat(ServerLevel level, BlockPos pos) {
        return AmbientHeat.get(level, pos);
    }

    public static void setActualHeat(ServerLevel level, BlockPos pos, int actualHeat) {
        LevelChunk chunk = level.getChunkAt(pos);
        ChunkHeatData data = HeatManager.getChunkData(chunk);
        data.setTemperatureKelvin(level, chunk.getPos(), pos, actualHeat);
        chunk.setUnsaved(true);
        HeatScheduler.enqueue(level, new ChunkPos(pos), SectionPos.blockToSectionCoord(pos.getY()), ThermalCell.cellIndex(pos));
    }

    public static void setExtraHeat(ServerLevel level, BlockPos pos, int extraHeat) {
        LevelChunk chunk = level.getChunkAt(pos);
        ChunkHeatData data = HeatManager.getChunkData(chunk);
        data.setExtraHeat(level, chunk.getPos(), pos, extraHeat);
        chunk.setUnsaved(true);
        HeatScheduler.enqueue(level, new ChunkPos(pos), SectionPos.blockToSectionCoord(pos.getY()), ThermalCell.cellIndex(pos));
    }

    public static void addExtraHeat(ServerLevel level, BlockPos pos, int delta) {
        setActualHeat(level, pos, getActualHeat(level, pos) + delta);
    }

    public static void markDirty(ServerLevel level, BlockPos pos) {
        LevelChunk chunk = level.getChunkAt(pos);
        HeatManager.getChunkData(chunk).markDirty(level, pos);
        chunk.setUnsaved(true);
        HeatScheduler.enqueue(level, chunk.getPos(), SectionPos.blockToSectionCoord(pos.getY()), ThermalCell.cellIndex(pos));
    }

    public static void touch(ServerLevel level, BlockPos pos) {
        LevelChunk chunk = level.getChunkAt(pos);
        ChunkHeatData data = HeatManager.getChunkData(chunk);
        data.getOrCreateCell(pos);
        data.refreshHeatNode(level, pos);
        HeatScheduler.enqueue(level, chunk.getPos(), SectionPos.blockToSectionCoord(pos.getY()), ThermalCell.cellIndex(pos));
    }
}
