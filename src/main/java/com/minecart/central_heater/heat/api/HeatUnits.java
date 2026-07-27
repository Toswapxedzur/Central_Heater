package com.minecart.central_heater.heat.api;

public final class HeatUnits {
    public static final int TEMPERATURE_SCALE = 4096;

    private HeatUnits() {
    }

    public static int kelvinToFixed(int kelvin) {
        return clampToInt((long) kelvin * TEMPERATURE_SCALE);
    }

    public static int kelvinDeltaToFixed(int kelvinDelta) {
        return clampToInt((long) kelvinDelta * TEMPERATURE_SCALE);
    }

    public static int fixedToKelvin(int temperature) {
        return Math.floorDiv(temperature, TEMPERATURE_SCALE);
    }

    public static int fixedToKelvinRounded(int temperature) {
        if (temperature >= 0) {
            return (int) (((long) temperature + TEMPERATURE_SCALE / 2) / TEMPERATURE_SCALE);
        }
        return (int) -((-(long) temperature + TEMPERATURE_SCALE / 2) / TEMPERATURE_SCALE);
    }

    public static int fixedToCelsiusRounded(int temperature) {
        return fixedToKelvinRounded(temperature) - 273;
    }

    public static int addKelvinDelta(int temperature, int kelvinDelta) {
        return clampToInt((long) temperature + kelvinDeltaToFixed(kelvinDelta));
    }

    public static int clampToInt(long value) {
        if (value > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (value < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int) value;
    }
}
