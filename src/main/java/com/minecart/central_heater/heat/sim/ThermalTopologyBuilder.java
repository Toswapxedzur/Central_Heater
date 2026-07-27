package com.minecart.central_heater.heat.sim;

import com.minecart.central_heater.heat.api.HeatProfile;
import com.minecart.central_heater.heat.api.HeatUnits;
import com.minecart.central_heater.heat.api.ThermalMaterial;
import com.minecart.central_heater.heat.registry.ThermalMaterialBehaviorRegistry;
import com.minecart.central_heater.heat.registry.ThermalMaterialRegistry;
import com.minecart.central_heater.heat.storage.ThermalCell;
import com.minecart.central_heater.heat.storage.ThermalContact;
import com.minecart.central_heater.heat.storage.ThermalGroup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class ThermalTopologyBuilder {
    private ThermalTopologyBuilder() {
    }

    public static void rebuild(ServerLevel level, ChunkPos chunkPos, int sectionY, int cellIndex, ThermalCell cell) {
        int[] oldTemperatureByBlock = oldTemperatureByBlock(cell);
        BlockPos origin = ThermalCell.origin(chunkPos, sectionY, cellIndex);
        ThermalMaterial[] materials = new ThermalMaterial[ThermalCell.BLOCK_COUNT];
        int[] initialTemperature = new int[ThermalCell.BLOCK_COUNT];
        boolean[] visited = new boolean[ThermalCell.BLOCK_COUNT];
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int z = 0; z < ThermalCell.SIZE; z++) {
            for (int y = 0; y < ThermalCell.SIZE; y++) {
                for (int x = 0; x < ThermalCell.SIZE; x++) {
                    pos.set(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
                    BlockState state = level.getBlockState(pos);
                    int blockIndex = ThermalCell.blockIndex(x, y, z);
                    materials[blockIndex] = ThermalMaterialRegistry.resolve(state);
                    initialTemperature[blockIndex] = HeatUnits.kelvinToFixed(AmbientHeat.get(level, pos) + BlockStateHeatInteractions.extraHeatFor(state));
                }
            }
        }

        List<ThermalGroup> newGroups = new ArrayList<>();
        for (int index = 0; index < ThermalCell.BLOCK_COUNT; index++) {
            if (!visited[index]) {
                newGroups.add(flood(index, visited, materials, oldTemperatureByBlock, initialTemperature, newGroups.size()));
            }
        }

        cell.groups().clear();
        cell.groups().addAll(newGroups);
        cell.setInternalContacts(buildInternalContacts(cell));
        cell.clearDirty();
    }

    private static ThermalGroup flood(int start, boolean[] visited, ThermalMaterial[] materials, int[] oldTemperatureByBlock, int[] initialTemperature, int id) {
        ThermalMaterial material = materials[start];
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        queue.add(start);
        visited[start] = true;
        long mask = 0L;
        int volume = 0;
        long temperatureSum = 0L;
        int[] faceArea = new int[Direction.values().length];

        while (!queue.isEmpty()) {
            int index = queue.removeFirst();
            mask |= 1L << index;
            volume++;
            int preservedTemperature = oldTemperatureByBlock[index];
            temperatureSum += Math.max(preservedTemperature == Integer.MIN_VALUE ? initialTemperature[index] : preservedTemperature, initialTemperature[index]);
            int x = index & 3;
            int y = (index >> 2) & 3;
            int z = (index >> 4) & 3;
            for (Direction direction : Direction.values()) {
                int nx = x + direction.getStepX();
                int ny = y + direction.getStepY();
                int nz = z + direction.getStepZ();
                if (nx < 0 || nx >= ThermalCell.SIZE || ny < 0 || ny >= ThermalCell.SIZE || nz < 0 || nz >= ThermalCell.SIZE) {
                    faceArea[direction.ordinal()]++;
                    continue;
                }
                int next = ThermalCell.blockIndex(nx, ny, nz);
                if (materials[next] != material) {
                    faceArea[direction.ordinal()]++;
                } else if (!visited[next]) {
                    visited[next] = true;
                    queue.add(next);
                }
            }
        }

        byte[] faces = new byte[Direction.values().length];
        for (Direction direction : Direction.values()) {
            faces[direction.ordinal()] = (byte) Math.min(255, faceArea[direction.ordinal()]);
        }
        HeatProfile profile = ThermalMaterialBehaviorRegistry.get(material).profile();
        return new ThermalGroup(id, material, mask, HeatUnits.clampToInt(temperatureSum / Math.max(1, volume)), volume, profile.heatCapacity() * volume, faces);
    }

    public static List<ThermalContact> buildInternalContacts(ThermalCell cell) {
        List<ThermalContact> contacts = new ArrayList<>();
        int groupCount = cell.groups().size();
        int[][] area = new int[groupCount][groupCount];
        Direction[][] directions = new Direction[groupCount][groupCount];
        for (int z = 0; z < ThermalCell.SIZE; z++) {
            for (int y = 0; y < ThermalCell.SIZE; y++) {
                for (int x = 0; x < ThermalCell.SIZE; x++) {
                    ThermalGroup first = cell.groupAtBlockIndex(ThermalCell.blockIndex(x, y, z));
                    for (Direction direction : Direction.values()) {
                        int nx = x + direction.getStepX();
                        int ny = y + direction.getStepY();
                        int nz = z + direction.getStepZ();
                        if (nx < 0 || nx >= ThermalCell.SIZE || ny < 0 || ny >= ThermalCell.SIZE || nz < 0 || nz >= ThermalCell.SIZE) {
                            continue;
                        }
                        ThermalGroup second = cell.groupAtBlockIndex(ThermalCell.blockIndex(nx, ny, nz));
                        if (first == null || second == null || first.id() == second.id()) {
                            continue;
                        }
                        int a = Math.min(first.id(), second.id());
                        int b = Math.max(first.id(), second.id());
                        area[a][b]++;
                        directions[a][b] = first.id() <= second.id() ? direction : direction.getOpposite();
                    }
                }
            }
        }
        for (int a = 0; a < groupCount; a++) {
            for (int b = a + 1; b < groupCount; b++) {
                if (area[a][b] > 0) {
                    contacts.add(new ThermalContact(a, b, directions[a][b] == null ? Direction.NORTH : directions[a][b], area[a][b] / 2));
                }
            }
        }
        return contacts;
    }

    private static int[] oldTemperatureByBlock(ThermalCell cell) {
        int[] heat = new int[ThermalCell.BLOCK_COUNT];
        java.util.Arrays.fill(heat, Integer.MIN_VALUE);
        for (ThermalGroup group : cell.groups()) {
            long mask = group.blockMask();
            while (mask != 0L) {
                int index = Long.numberOfTrailingZeros(mask);
                heat[index] = group.temperature();
                mask &= mask - 1L;
            }
        }
        return heat;
    }
}
