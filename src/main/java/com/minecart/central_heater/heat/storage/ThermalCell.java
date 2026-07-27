package com.minecart.central_heater.heat.storage;

import com.minecart.central_heater.heat.api.HeatUnits;
import com.minecart.central_heater.heat.api.ThermalMaterialBehavior;
import com.minecart.central_heater.heat.context.ThermalGroupAccess;
import com.minecart.central_heater.heat.context.ThermalGroupContext;
import com.minecart.central_heater.heat.registry.ThermalMaterialBehaviorRegistry;
import com.minecart.central_heater.heat.sim.AmbientHeat;
import com.minecart.central_heater.heat.sim.BlockStateHeatInteractions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThermalCell {
    public static final int SIZE = 4;
    public static final int BLOCK_COUNT = 64;
    private static final long AMBIENT_REGRESSION_DENOMINATOR = 524288L;

    private final List<ThermalGroup> groups = new ArrayList<>();
    private final List<ThermalContact> internalContacts = new ArrayList<>();
    private boolean dirtyTopology = true;

    public static int cellIndex(BlockPos pos) {
        int x = (pos.getX() & 15) >> 2;
        int y = (pos.getY() & 15) >> 2;
        int z = (pos.getZ() & 15) >> 2;
        return x | (y << 2) | (z << 4);
    }

    public static int blockIndex(int x, int y, int z) {
        return (x & 3) | ((y & 3) << 2) | ((z & 3) << 4);
    }

    public static BlockPos origin(ChunkPos chunkPos, int sectionY, int cellIndex) {
        int x = cellIndex & 3;
        int y = (cellIndex >> 2) & 3;
        int z = (cellIndex >> 4) & 3;
        return new BlockPos(chunkPos.getMinBlockX() + x * SIZE, (sectionY << 4) + y * SIZE, chunkPos.getMinBlockZ() + z * SIZE);
    }

    public List<ThermalGroup> groups() {
        return groups;
    }

    public List<ThermalContact> internalContacts() {
        return Collections.unmodifiableList(internalContacts);
    }

    public void setInternalContacts(List<ThermalContact> contacts) {
        internalContacts.clear();
        internalContacts.addAll(contacts);
    }

    public boolean dirtyTopology() {
        return dirtyTopology;
    }

    public void markDirty() {
        dirtyTopology = true;
    }

    public void clearDirty() {
        dirtyTopology = false;
    }

    public ThermalGroup groupAtBlockIndex(int blockIndex) {
        for (ThermalGroup group : groups) {
            if (group.containsCell(blockIndex)) {
                return group;
            }
        }
        return null;
    }

    public int getActualHeat(ServerLevel level, ChunkPos chunkPos, int sectionY, int cellIndex, int localX, int localY, int localZ, int ambientHeat) {
        ThermalGroup group = groupAtBlockIndex(blockIndex(localX, localY, localZ));
        return group == null ? ambientHeat : group.temperatureKelvin();
    }

    public int getTemperatureKelvin(int localX, int localY, int localZ, int ambientHeat) {
        ThermalGroup group = groupAtBlockIndex(blockIndex(localX, localY, localZ));
        return group == null ? ambientHeat : group.temperatureKelvin();
    }

    public int getExtraHeat(int localX, int localY, int localZ, int ambientHeat) {
        return getTemperatureKelvin(localX, localY, localZ, ambientHeat) - ambientHeat;
    }

    public void setTemperatureKelvin(int localX, int localY, int localZ, int temperatureKelvin) {
        ThermalGroup group = groupAtBlockIndex(blockIndex(localX, localY, localZ));
        if (group != null) {
            group.setTemperatureKelvin(temperatureKelvin);
        }
    }

    public boolean tickGroups(ServerLevel level, ChunkPos chunkPos, int sectionY, int cellIndex, int ambientHeat, int heatSteps) {
        boolean active = dirtyTopology;
        BlockPos origin = origin(chunkPos, sectionY, cellIndex);
        int ambientTemperature = HeatUnits.kelvinToFixed(ambientHeat);
        for (ThermalGroup group : groups) {
            ThermalMaterialBehavior behavior = ThermalMaterialBehaviorRegistry.get(group.material());
            int oldHeat = group.temperatureKelvin();
            GroupAccess access = new GroupAccess(group, ambientHeat, origin);
            behavior.tickGroup(new ThermalGroupContext(level, chunkPos, sectionY, cellIndex, origin, group), access);
            applyBlockStateHeatSources(level, origin, group, heatSteps);
            if (oldHeat != group.temperatureKelvin()) {
                behavior.onHeatChanged(new ThermalGroupContext(level, chunkPos, sectionY, cellIndex, origin, group), access, oldHeat, group.temperatureKelvin());
            }
            if (group.temperature() != ambientTemperature) {
                active = true;
            }
        }
        return active;
    }

    public void coolAndCull(ServerLevel level, ChunkPos chunkPos, int sectionY, int cellIndex, int heatSteps) {
        BlockPos origin = origin(chunkPos, sectionY, cellIndex);
        for (ThermalGroup group : groups) {
            ThermalMaterialBehavior behavior = ThermalMaterialBehaviorRegistry.get(group.material());
            int oldHeat = group.temperatureKelvin();
            int ambientHeat = AmbientHeat.get(level, origin);
            int cooled = regressTowardAmbient(group, ambientHeat, behavior.profile().coolingRate(), heatSteps);
            group.setTemperature(cooled);
            if (cooled == HeatUnits.kelvinToFixed(ambientHeat)) {
                group.clearEnergyRemainder();
                group.clearAmbientRegressionRemainder();
            }
            ThermalGroupContext ctx = new ThermalGroupContext(level, chunkPos, sectionY, cellIndex, origin, group);
            GroupAccess access = new GroupAccess(group, ambientHeat, origin);
            int newHeat = group.temperatureKelvin();
            if (oldHeat != newHeat) {
                behavior.onHeatChanged(ctx, access, oldHeat, newHeat);
            }
        }
    }

    private static void applyBlockStateHeatSources(ServerLevel level, BlockPos origin, ThermalGroup group, int heatSteps) {
        int targetTemperature = Integer.MIN_VALUE;
        int heatingPower = 0;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        long mask = group.blockMask();
        while (mask != 0L) {
            int bit = Long.numberOfTrailingZeros(mask);
            pos.set(origin.getX() + (bit & 3), origin.getY() + ((bit >> 2) & 3), origin.getZ() + ((bit >> 4) & 3));
            int extraHeat = BlockStateHeatInteractions.extraHeatFor(level.getBlockState(pos));
            if (extraHeat > 0) {
                targetTemperature = Math.max(targetTemperature, HeatUnits.kelvinToFixed(AmbientHeat.get(level, pos) + extraHeat));
                heatingPower += BlockStateHeatInteractions.sourceHeatingPowerFor(level.getBlockState(pos));
            }
            mask &= mask - 1L;
        }
        if (targetTemperature != Integer.MIN_VALUE && group.temperature() < targetTemperature) {
            int scaledHeatingPower = Math.max(1, heatingPower) * Math.max(1, heatSteps);
            group.setTemperature(Math.min(targetTemperature, HeatUnits.addKelvinDelta(group.temperature(), scaledHeatingPower)));
        }
    }

    public boolean shouldSave() {
        return !groups.isEmpty() || dirtyTopology;
    }

    public CompoundTag save(int index) {
        CompoundTag tag = new CompoundTag();
        tag.putByte("i", (byte) index);
        ListTag groupList = new ListTag();
        for (ThermalGroup group : groups) {
            groupList.add(group.save());
        }
        tag.put("groups", groupList);
        return tag;
    }

    public static ThermalCell load(CompoundTag tag) {
        ThermalCell cell = new ThermalCell();
        cell.dirtyTopology = true;
        ListTag groupList = tag.getList("groups", Tag.TAG_COMPOUND);
        for (int i = 0; i < groupList.size(); i++) {
            cell.groups.add(ThermalGroup.load(groupList.getCompound(i)));
        }
        return cell;
    }

    private static int regressTowardAmbient(ThermalGroup group, int ambientKelvin, int coolingRate, int heatSteps) {
        int temperature = group.temperature();
        int ambientTemperature = HeatUnits.kelvinToFixed(ambientKelvin);
        int diff = temperature - ambientTemperature;
        if (diff == 0 || coolingRate <= 0) {
            return temperature;
        }
        long signedNumerator = -(long) diff * Math.max(1, group.capacity()) * coolingRate * Math.max(1, heatSteps) + group.ambientRegressionRemainder();
        long energyDelta = signedNumerator / AMBIENT_REGRESSION_DENOMINATOR;
        long remainder = signedNumerator % AMBIENT_REGRESSION_DENOMINATOR;
        group.setAmbientRegressionRemainder(remainder);
        if (energyDelta == 0L) {
            return temperature;
        }
        long totalEnergy = group.energyRemainder() + energyDelta;
        long temperatureDelta = totalEnergy / Math.max(1, group.capacity());
        group.setEnergyRemainder(totalEnergy % Math.max(1, group.capacity()));
        if (temperatureDelta == 0L) {
            return temperature;
        }
        long next = (long) temperature + temperatureDelta;
        return HeatUnits.clampToInt(diff > 0 ? Math.max(ambientTemperature, next) : Math.min(ambientTemperature, next));
    }

    private static class GroupAccess implements ThermalGroupAccess {
        private final ThermalGroup group;
        private final int ambientHeat;
        private final BlockPos origin;

        private GroupAccess(ThermalGroup group, int ambientHeat, BlockPos origin) {
            this.group = group;
            this.ambientHeat = ambientHeat;
            this.origin = origin;
        }

        @Override
        public int getExtraHeat() {
            return group.temperatureKelvin() - ambientHeat;
        }

        @Override
        public int getActualHeat() {
            return group.temperatureKelvin();
        }

        @Override
        public void setExtraHeat(int extraHeat) {
            group.setTemperatureKelvin(ambientHeat + extraHeat);
        }

        @Override
        public List<BlockPos> sampleBlocks(int maxCount) {
            List<BlockPos> blocks = new ArrayList<>();
            long mask = group.blockMask();
            while (mask != 0L && blocks.size() < maxCount) {
                int bit = Long.numberOfTrailingZeros(mask);
                blocks.add(origin.offset(bit & 3, (bit >> 2) & 3, (bit >> 4) & 3));
                mask &= mask - 1L;
            }
            return blocks;
        }
    }
}
