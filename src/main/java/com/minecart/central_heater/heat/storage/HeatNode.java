package com.minecart.central_heater.heat.storage;

import com.minecart.central_heater.heat.api.HeatType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public class HeatNode {
    private final BlockPos pos;
    private int heat;
    private int capacity;
    private HeatType heatType;

    public HeatNode(BlockPos pos, int heat, int capacity, HeatType heatType) {
        this.pos = pos.immutable();
        this.heat = heat;
        this.capacity = Math.max(1, capacity);
        this.heatType = heatType;
    }

    public BlockPos pos() {
        return pos;
    }

    public int heat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }

    public int capacity() {
        return capacity;
    }

    public HeatType heatType() {
        return heatType;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putLong("pos", pos.asLong());
        tag.putInt("heat", heat);
        tag.putInt("capacity", capacity);
        tag.putString("type", heatType.name());
        return tag;
    }

    public static HeatNode load(CompoundTag tag) {
        return new HeatNode(
                BlockPos.of(tag.getLong("pos")),
                tag.getInt("heat"),
                tag.getInt("capacity"),
                HeatType.valueOf(tag.getString("type"))
        );
    }
}
