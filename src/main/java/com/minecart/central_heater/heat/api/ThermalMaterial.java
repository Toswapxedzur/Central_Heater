package com.minecart.central_heater.heat.api;

import com.mojang.serialization.Codec;

import java.util.Locale;

public enum ThermalMaterial {
    AIR,
    STONE,
    DEEPSLATE,
    BRICK,
    MUD_BRICK,
    BLACKSTONE,
    METAL,
    COPPER,
    GOLD,
    WOOD,
    BURNT_WOOD,
    COAL,
    WATER,
    LAVA,
    STURDY,
    SCORCHED,
    INSULATOR;

    public static final Codec<ThermalMaterial> CODEC = Codec.STRING.xmap(ThermalMaterial::byName, ThermalMaterial::serializedName);

    public static ThermalMaterial byName(String name) {
        try {
            return ThermalMaterial.valueOf(name.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
            return STONE;
        }
    }

    public String serializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
