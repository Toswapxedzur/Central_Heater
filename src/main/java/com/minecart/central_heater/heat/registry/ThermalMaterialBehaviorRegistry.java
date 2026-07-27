package com.minecart.central_heater.heat.registry;

import com.minecart.central_heater.heat.api.ThermalMaterial;
import com.minecart.central_heater.heat.api.ThermalMaterialBehavior;
import com.minecart.central_heater.heat.behavior.*;

import java.util.EnumMap;
import java.util.Map;

public class ThermalMaterialBehaviorRegistry {
    private static final Map<ThermalMaterial, ThermalMaterialBehavior> BEHAVIORS = new EnumMap<>(ThermalMaterial.class);

    static {
        register(ThermalMaterial.AIR, new AirThermalBehavior());
        register(ThermalMaterial.STONE, new StoneThermalBehavior());
        register(ThermalMaterial.DEEPSLATE, new StoneThermalBehavior());
        register(ThermalMaterial.BLACKSTONE, new StoneThermalBehavior());
        register(ThermalMaterial.BRICK, new BrickThermalBehavior());
        register(ThermalMaterial.MUD_BRICK, new BrickThermalBehavior());
        register(ThermalMaterial.METAL, new CopperThermalBehavior());
        register(ThermalMaterial.COPPER, new CopperThermalBehavior());
        register(ThermalMaterial.GOLD, new GoldThermalBehavior());
        register(ThermalMaterial.WOOD, new WoodThermalBehavior());
        register(ThermalMaterial.BURNT_WOOD, new BurntWoodThermalBehavior());
        register(ThermalMaterial.COAL, new ScorchedThermalBehavior());
        register(ThermalMaterial.WATER, new WaterThermalBehavior());
        register(ThermalMaterial.LAVA, new LavaThermalBehavior());
        register(ThermalMaterial.STURDY, new SturdyThermalBehavior());
        register(ThermalMaterial.SCORCHED, new ScorchedThermalBehavior());
        register(ThermalMaterial.INSULATOR, new InsulatorThermalBehavior());
    }

    private ThermalMaterialBehaviorRegistry() {
    }

    public static void register(ThermalMaterial material, ThermalMaterialBehavior behavior) {
        BEHAVIORS.put(material, behavior);
    }

    public static ThermalMaterialBehavior get(ThermalMaterial material) {
        return BEHAVIORS.getOrDefault(material, BEHAVIORS.get(ThermalMaterial.STONE));
    }
}
