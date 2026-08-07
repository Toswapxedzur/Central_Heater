package com.minecart.central_heater.heat.storage;

import com.minecart.central_heater.heat.api.ThermalMaterial;
import com.minecart.central_heater.heat.api.HeatUnits;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;

public class ThermalGroup {
    private int id;
    private ThermalMaterial material;
    private long blockMask;
    private int temperature;
    private int volume;
    private int capacity;
    private long energyRemainder;
    private long ambientRegressionRemainder;
    private byte[] faceArea = new byte[Direction.values().length];
    private boolean dirty;

    public ThermalGroup(int id, ThermalMaterial material, long blockMask, int temperature, int volume, int capacity, byte[] faceArea) {
        this.id = id;
        this.material = material;
        this.blockMask = blockMask;
        this.temperature = temperature;
        this.volume = Math.max(1, volume);
        this.capacity = Math.max(1, capacity);
        this.faceArea = faceArea.length == Direction.values().length ? faceArea : new byte[Direction.values().length];
    }

    public int id() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ThermalMaterial material() {
        return material;
    }

    public long blockMask() {
        return blockMask;
    }

    public int temperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int temperatureKelvin() {
        return HeatUnits.fixedToKelvinRounded(temperature);
    }

    public void setTemperatureKelvin(int temperatureKelvin) {
        this.temperature = HeatUnits.kelvinToFixed(temperatureKelvin);
    }

    public int extraHeat() {
        return temperatureKelvin();
    }

    public void setExtraHeat(int extraHeat) {
        setTemperatureKelvin(extraHeat);
    }

    public long energyRemainder() {
        return energyRemainder;
    }

    public void setEnergyRemainder(long energyRemainder) {
        this.energyRemainder = energyRemainder;
    }

    public void clearEnergyRemainder() {
        this.energyRemainder = 0L;
    }

    public long ambientRegressionRemainder() {
        return ambientRegressionRemainder;
    }

    public void setAmbientRegressionRemainder(long ambientRegressionRemainder) {
        this.ambientRegressionRemainder = ambientRegressionRemainder;
    }

    public void clearAmbientRegressionRemainder() {
        this.ambientRegressionRemainder = 0L;
    }

    public int volume() {
        return volume;
    }

    public int capacity() {
        return capacity;
    }

    public byte[] faceArea() {
        return faceArea;
    }

    public int faceArea(Direction direction) {
        return Byte.toUnsignedInt(faceArea[direction.ordinal()]);
    }

    public boolean dirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean containsCell(int localBlockIndex) {
        return (blockMask & (1L << localBlockIndex)) != 0L;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("id", id);
        tag.putString("material", material.serializedName());
        tag.putLong("mask", blockMask);
        tag.putInt("temperatureFx", temperature);
        tag.putLong("energyRemainder", energyRemainder);
        tag.putLong("ambientRegressionRemainder", ambientRegressionRemainder);
        tag.putInt("volume", volume);
        tag.putInt("capacity", capacity);
        tag.putByteArray("face", faceArea);
        return tag;
    }

    public static ThermalGroup load(CompoundTag tag) {
        ThermalGroup group = new ThermalGroup(
                tag.getInt("id"),
                ThermalMaterial.byName(tag.getString("material")),
                tag.getLong("mask"),
                readTemperature(tag),
                tag.getInt("volume"),
                tag.getInt("capacity"),
                tag.contains("face") ? tag.getByteArray("face") : new byte[Direction.values().length]
        );
        group.setEnergyRemainder(tag.getLong("energyRemainder"));
        group.setAmbientRegressionRemainder(tag.getLong("ambientRegressionRemainder"));
        return group;
    }

    private static int readTemperature(CompoundTag tag) {
        if (tag.contains("temperatureFx")) {
            return tag.getInt("temperatureFx");
        }
        if (tag.contains("temperature")) {
            return HeatUnits.kelvinToFixed(tag.getInt("temperature"));
        }
        return HeatUnits.kelvinToFixed(273 + tag.getInt("extra"));
    }
}
