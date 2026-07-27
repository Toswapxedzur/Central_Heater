package com.minecart.central_heater.heat.sim;

import com.minecart.central_heater.misc.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class AmbientHeat {
    private AmbientHeat() {
    }

    public static int get(ServerLevel level, BlockPos pos) {
        Biome biome = level.getBiome(pos).value();
        int kelvin = Math.round(273.15f + biome.getBaseTemperature() * 18.0f);

        if (level.dimension() == Level.NETHER || level.dimensionType().ultraWarm()) {
            kelvin += 38;
        } else if (level.dimension() == Level.END) {
            kelvin -= 22;
        } else if (!level.dimensionType().natural()) {
            kelvin -= 6;
        }

        int seaLevel = level.getSeaLevel();
        if (pos.getY() > seaLevel) {
            kelvin -= (pos.getY() - seaLevel) / 14;
        } else {
            kelvin += Math.min(12, (seaLevel - pos.getY()) / 44);
        }

        boolean skyVisible = level.canSeeSkyFromBelowWater(pos);
        if (skyVisible && level.dimensionType().natural()) {
            long dayTime = level.getDayTime() % 24000L;
            boolean warmDaylight = dayTime >= 2000L && dayTime <= 10000L;
            kelvin += warmDaylight ? 4 : -3;
        }

        if (level.isRainingAt(pos)) {
            kelvin -= level.isThundering() ? 8 : 5;
        }

        return Mth.clamp(kelvin, Config.heatMinAmbientKelvin, Config.heatMaxAmbientKelvin);
    }
}
