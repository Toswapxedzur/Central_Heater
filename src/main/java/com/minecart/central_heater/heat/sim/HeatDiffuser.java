package com.minecart.central_heater.heat.sim;

import com.minecart.central_heater.heat.HeatManager;
import com.minecart.central_heater.heat.api.HeatPort;
import com.minecart.central_heater.heat.api.HeatProfile;
import com.minecart.central_heater.heat.api.ThermalMaterial;
import com.minecart.central_heater.heat.api.ThermalMaterialBehavior;
import com.minecart.central_heater.heat.context.ThermalGroupContext;
import com.minecart.central_heater.heat.registry.ThermalMaterialBehaviorRegistry;
import com.minecart.central_heater.heat.storage.ChunkHeatData;
import com.minecart.central_heater.heat.storage.ThermalCell;
import com.minecart.central_heater.heat.storage.ThermalContact;
import com.minecart.central_heater.heat.storage.ThermalGroup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;

public class HeatDiffuser {
    private HeatDiffuser() {
    }

    public static boolean exchangeInsideCell(ServerLevel level, ChunkPos chunkPos, int sectionY, int cellIndex, ThermalCell cell, int heatSteps) {
        long[] energyDeltas = new long[cell.groups().size()];
        for (ThermalContact contact : cell.internalContacts()) {
            ThermalGroup a = cell.groups().get(contact.groupA());
            ThermalGroup b = cell.groups().get(contact.groupB());
            applyPair(level, chunkPos, sectionY, cellIndex, cell, a, b, contact.direction(), contact.contactArea(), heatSteps, energyDeltas);
        }
        return applyEnergyDeltas(cell, energyDeltas);
    }

    public static boolean exchangeWithNeighbors(ServerLevel level, ChunkPos chunkPos, int sectionY, int cellIndex, ThermalCell cell, int heatSteps) {
        boolean changed = false;
        BlockPos origin = ThermalCell.origin(chunkPos, sectionY, cellIndex);
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = origin.relative(direction, ThermalCell.SIZE);
            ChunkAccess access = level.getChunk(SectionPos.blockToSectionCoord(neighborPos.getX()), SectionPos.blockToSectionCoord(neighborPos.getZ()), ChunkStatus.FULL, false);
            if (!(access instanceof LevelChunk neighborChunk)) {
                continue;
            }
            ChunkHeatData neighborData = HeatManager.getChunkData(neighborChunk);
            int neighborSectionY = SectionPos.blockToSectionCoord(neighborPos.getY());
            int neighborCellIndex = ThermalCell.cellIndex(neighborPos);
            ThermalCell neighborCell = neighborData.getOrCreateCell(neighborSectionY, neighborCellIndex);
            neighborData.ensureTopology(level, neighborChunk.getPos(), neighborSectionY, neighborCellIndex, neighborCell);
            if (exchangeFaces(level, chunkPos, sectionY, cellIndex, cell, neighborChunk.getPos(), neighborSectionY, neighborCellIndex, neighborCell, direction, heatSteps)) {
                changed = true;
                neighborChunk.setUnsaved(true);
                HeatScheduler.wakeSoon(level, neighborChunk.getPos(), neighborSectionY, neighborCellIndex, 1);
            }
        }
        return changed;
    }

    private static boolean exchangeFaces(ServerLevel level, ChunkPos aChunk, int aSectionY, int aCellIndex, ThermalCell aCell, ChunkPos bChunk, int bSectionY, int bCellIndex, ThermalCell bCell, Direction direction, int heatSteps) {
        long[] aEnergyDeltas = new long[aCell.groups().size()];
        long[] bEnergyDeltas = new long[bCell.groups().size()];
        boolean changed = false;
        for (int u = 0; u < ThermalCell.SIZE; u++) {
            for (int v = 0; v < ThermalCell.SIZE; v++) {
                int aIndex = faceIndex(direction, u, v, true);
                int bIndex = faceIndex(direction, u, v, false);
                ThermalGroup a = aCell.groupAtBlockIndex(aIndex);
                ThermalGroup b = bCell.groupAtBlockIndex(bIndex);
                if (a == null || b == null) {
                    continue;
                }
                long transferEnergy = proposedEnergy(level, aChunk, aSectionY, aCellIndex, a, bChunk, bSectionY, bCellIndex, b, direction, 1, heatSteps);
                if (transferEnergy > 0L) {
                    changed = true;
                    aEnergyDeltas[a.id()] -= transferEnergy;
                    bEnergyDeltas[b.id()] += transferEnergy;
                } else if (transferEnergy < 0L) {
                    changed = true;
                    aEnergyDeltas[a.id()] -= transferEnergy;
                    bEnergyDeltas[b.id()] += transferEnergy;
                }
            }
        }
        changed |= applyEnergyDeltas(aCell, aEnergyDeltas);
        changed |= applyEnergyDeltas(bCell, bEnergyDeltas);
        return changed;
    }

    private static void applyPair(ServerLevel level, ChunkPos chunkPos, int sectionY, int cellIndex, ThermalCell cell, ThermalGroup a, ThermalGroup b, Direction direction, int contactArea, int heatSteps, long[] energyDeltas) {
        long transferEnergy = proposedEnergy(level, chunkPos, sectionY, cellIndex, a, chunkPos, sectionY, cellIndex, b, direction, contactArea, heatSteps);
        if (transferEnergy > 0L) {
            energyDeltas[a.id()] -= transferEnergy;
            energyDeltas[b.id()] += transferEnergy;
        } else if (transferEnergy < 0L) {
            energyDeltas[a.id()] -= transferEnergy;
            energyDeltas[b.id()] += transferEnergy;
        }
    }

    private static long proposedEnergy(ServerLevel level, ChunkPos aChunk, int aSectionY, int aCellIndex, ThermalGroup a, ChunkPos bChunk, int bSectionY, int bCellIndex, ThermalGroup b, Direction direction, int contactArea, int heatSteps) {
        ThermalMaterialBehavior aBehavior = ThermalMaterialBehaviorRegistry.get(a.material());
        ThermalMaterialBehavior bBehavior = ThermalMaterialBehaviorRegistry.get(b.material());
        HeatProfile aProfile = aBehavior.profile();
        HeatProfile bProfile = bBehavior.profile();
        BlockPos aOrigin = ThermalCell.origin(aChunk, aSectionY, aCellIndex);
        BlockPos bOrigin = ThermalCell.origin(bChunk, bSectionY, bCellIndex);
        ThermalGroupContext aCtx = new ThermalGroupContext(level, aChunk, aSectionY, aCellIndex, aOrigin, a);
        ThermalGroupContext bCtx = new ThermalGroupContext(level, bChunk, bSectionY, bCellIndex, bOrigin, b);
        HeatPort aPort = aBehavior.getPort(aCtx, direction);
        HeatPort bPort = bBehavior.getPort(bCtx, direction.getOpposite());
        int aActual = a.temperature();
        int bActual = b.temperature();
        if (aActual >= bActual) {
            return HeatTransferMath.proposeTransferEnergy(aActual, bActual, a, b, aProfile, bProfile, aPort, bPort, contactArea, transferBoost(level, a, aOrigin, b, direction), heatSteps);
        }
        return -HeatTransferMath.proposeTransferEnergy(bActual, aActual, b, a, bProfile, aProfile, bPort, aPort, contactArea, transferBoost(level, b, bOrigin, a, direction.getOpposite()), heatSteps);
    }

    private static int transferBoost(ServerLevel level, ThermalGroup fromHot, BlockPos hotOrigin, ThermalGroup toCold, Direction hotToCold) {
        if (fromHot.material() == ThermalMaterial.LAVA && toCold.material() == ThermalMaterial.AIR) {
            return lavaToAirBoost(level, hotOrigin, fromHot, hotToCold);
        }
        if (fromHot.material() == ThermalMaterial.SCORCHED && toCold.material() == ThermalMaterial.AIR) {
            return hotToCold == Direction.UP ? 16 : 8;
        }
        return 1;
    }

    private static int lavaToAirBoost(ServerLevel level, BlockPos origin, ThermalGroup lava, Direction hotToCold) {
        boolean flowing = containsFlowingLava(level, origin, lava);
        if (flowing) {
            return switch (hotToCold) {
                case DOWN -> 96;
                case NORTH, SOUTH, EAST, WEST -> 36;
                case UP -> 16;
            };
        }
        return switch (hotToCold) {
            case UP -> 64;
            case NORTH, SOUTH, EAST, WEST -> 24;
            case DOWN -> 12;
        };
    }

    private static boolean containsFlowingLava(ServerLevel level, BlockPos origin, ThermalGroup lava) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        long mask = lava.blockMask();
        while (mask != 0L) {
            int bit = Long.numberOfTrailingZeros(mask);
            pos.set(origin.getX() + (bit & 3), origin.getY() + ((bit >> 2) & 3), origin.getZ() + ((bit >> 4) & 3));
            BlockState state = level.getBlockState(pos);
            if (state.is(Blocks.LAVA) && state.hasProperty(LiquidBlock.LEVEL) && state.getValue(LiquidBlock.LEVEL) > 0) {
                return true;
            }
            mask &= mask - 1L;
        }
        return false;
    }

    private static boolean applyEnergyDeltas(ThermalCell cell, long[] energyDeltas) {
        boolean changed = false;
        for (ThermalGroup group : cell.groups()) {
            if (group.id() < energyDeltas.length && energyDeltas[group.id()] != 0L) {
                applyEnergy(group, energyDeltas[group.id()]);
                changed = true;
            }
        }
        return changed;
    }

    private static void applyEnergy(ThermalGroup group, long energyDelta) {
        long totalEnergy = group.energyRemainder() + energyDelta;
        long temperatureDelta = totalEnergy / Math.max(1, group.capacity());
        if (temperatureDelta != 0L) {
            group.setTemperature(clampToInt((long) group.temperature() + temperatureDelta));
            totalEnergy -= temperatureDelta * group.capacity();
        }
        group.setEnergyRemainder(totalEnergy);
    }

    private static int clampToInt(long value) {
        if (value > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (value < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int) value;
    }

    private static int faceIndex(Direction direction, int u, int v, boolean thisSide) {
        return switch (direction) {
            case EAST -> ThermalCell.blockIndex(thisSide ? 3 : 0, u, v);
            case WEST -> ThermalCell.blockIndex(thisSide ? 0 : 3, u, v);
            case UP -> ThermalCell.blockIndex(u, thisSide ? 3 : 0, v);
            case DOWN -> ThermalCell.blockIndex(u, thisSide ? 0 : 3, v);
            case SOUTH -> ThermalCell.blockIndex(u, v, thisSide ? 3 : 0);
            case NORTH -> ThermalCell.blockIndex(u, v, thisSide ? 0 : 3);
        };
    }
}
