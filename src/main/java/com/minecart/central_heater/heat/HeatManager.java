package com.minecart.central_heater.heat;

import com.minecart.central_heater.heat.api.HeatApi;
import com.minecart.central_heater.heat.storage.ChunkHeatData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.chunk.LevelChunk;

public class HeatManager {
    private HeatManager() {
    }

    public static short getTemperature(ServerLevel level, BlockPos pos) {
        return clampKelvin(HeatApi.getActualHeat(level, pos));
    }

    public static void setTemperature(ServerLevel level, BlockPos pos, short kelvin) {
        HeatApi.setActualHeat(level, pos, kelvin);
    }

    public static void addHeat(ServerLevel level, BlockPos pos, int kelvinDeltaOrEnergyUnits) {
        HeatApi.addExtraHeat(level, pos, kelvinDeltaOrEnergyUnits);
    }

    public static short getAmbientTemperature(ServerLevel level, BlockPos pos) {
        return clampKelvin(HeatApi.getAmbientHeat(level, pos));
    }

    public static void markDirty(ServerLevel level, BlockPos pos) {
        HeatApi.markDirty(level, pos);
    }

    public static ChunkHeatData getChunkData(LevelChunk chunk) {
        return chunk.getData(HeatAttachments.HEAT_CHUNK);
    }

    public static short clampKelvin(int kelvin) {
        return (short) Mth.clamp(kelvin, Short.MIN_VALUE, Short.MAX_VALUE);
    }

}
