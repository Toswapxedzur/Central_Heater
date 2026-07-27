package com.minecart.central_heater.heat.storage;

import com.minecart.central_heater.heat.api.HeatBlockEntityBehavior;
import com.minecart.central_heater.heat.api.HeatEmission;
import com.minecart.central_heater.heat.api.HeatSink;
import com.minecart.central_heater.heat.api.HeatUnits;
import com.minecart.central_heater.heat.HeatManager;
import com.minecart.central_heater.heat.context.HeatNodeAccess;
import com.minecart.central_heater.heat.context.HeatNodeContext;
import com.minecart.central_heater.heat.debug.HeatDebugLabel;
import com.minecart.central_heater.heat.debug.HeatDebugOutline;
import com.minecart.central_heater.heat.sim.AmbientHeat;
import com.minecart.central_heater.heat.sim.BlockStateHeatInteractions;
import com.minecart.central_heater.heat.sim.HeatDiffuser;
import com.minecart.central_heater.heat.sim.HeatScheduler;
import com.minecart.central_heater.heat.sim.ThermalTopologyBuilder;
import com.minecart.central_heater.misc.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChunkHeatData implements INBTSerializable<CompoundTag> {
    private static final int VERSION = 2;
    private static final int OUTLINE_PURPLE = 0xE0B050FF;
    private static final int OUTLINE_BLUE = 0xE050B8FF;
    private static final int MAX_ADAPTIVE_TICK_DELAY = 20 * 60;
    private static final long FACE_PRESSURE_BASE = 5L * HeatUnits.TEMPERATURE_SCALE;

    private final Map<Integer, ThermalCell[]> cellsBySection = new HashMap<>();
    private final Map<Long, HeatNode> heatNodes = new HashMap<>();

    public ThermalCell getCell(BlockPos pos) {
        ThermalCell[] cells = cellsBySection.get(SectionPos.blockToSectionCoord(pos.getY()));
        return cells == null ? null : cells[ThermalCell.cellIndex(pos)];
    }

    public ThermalCell getOrCreateCell(BlockPos pos) {
        int sectionY = SectionPos.blockToSectionCoord(pos.getY());
        ThermalCell[] cells = cellsBySection.computeIfAbsent(sectionY, ignored -> new ThermalCell[64]);
        int index = ThermalCell.cellIndex(pos);
        ThermalCell cell = cells[index];
        if (cell == null) {
            cell = new ThermalCell();
            cells[index] = cell;
        }
        return cell;
    }

    public void markDirty(ServerLevel level, BlockPos pos) {
        getOrCreateCell(pos).markDirty();
        refreshHeatNode(level, pos);
        markNeighborIfPresent(pos, pos.west(), (pos.getX() & 3) == 0);
        markNeighborIfPresent(pos, pos.east(), (pos.getX() & 3) == 3);
        markNeighborIfPresent(pos, pos.below(), (pos.getY() & 3) == 0);
        markNeighborIfPresent(pos, pos.above(), (pos.getY() & 3) == 3);
        markNeighborIfPresent(pos, pos.north(), (pos.getZ() & 3) == 0);
        markNeighborIfPresent(pos, pos.south(), (pos.getZ() & 3) == 3);
    }

    public int getExtraHeat(BlockPos pos) {
        ThermalCell cell = getCell(pos);
        return cell == null ? 0 : cell.getExtraHeat(pos.getX() & 3, pos.getY() & 3, pos.getZ() & 3, 0);
    }

    public int getExtraHeat(ServerLevel level, BlockPos pos) {
        int ambient = AmbientHeat.get(level, pos);
        ThermalCell cell = getCell(pos);
        return cell == null ? 0 : cell.getExtraHeat(pos.getX() & 3, pos.getY() & 3, pos.getZ() & 3, ambient);
    }

    public int getActualHeat(ServerLevel level, ChunkPos chunkPos, BlockPos pos) {
        ThermalCell cell = getCell(pos);
        int ambient = AmbientHeat.get(level, pos);
        if (cell == null) {
            return ambient;
        }
        ensureTopology(level, chunkPos, SectionPos.blockToSectionCoord(pos.getY()), ThermalCell.cellIndex(pos), cell);
        return cell.getActualHeat(level, chunkPos, SectionPos.blockToSectionCoord(pos.getY()), ThermalCell.cellIndex(pos), pos.getX() & 3, pos.getY() & 3, pos.getZ() & 3, ambient);
    }

    public void setTemperatureKelvin(ServerLevel level, ChunkPos chunkPos, BlockPos pos, int temperatureKelvin) {
        ThermalCell cell = getOrCreateCell(pos);
        int sectionY = SectionPos.blockToSectionCoord(pos.getY());
        int cellIndex = ThermalCell.cellIndex(pos);
        ensureTopology(level, chunkPos, sectionY, cellIndex, cell);
        cell.setTemperatureKelvin(pos.getX() & 3, pos.getY() & 3, pos.getZ() & 3, temperatureKelvin);
    }

    public void setExtraHeat(ServerLevel level, ChunkPos chunkPos, BlockPos pos, int extraHeat) {
        setTemperatureKelvin(level, chunkPos, pos, AmbientHeat.get(level, pos) + extraHeat);
    }

    public HeatTickResult tickCell(ServerLevel level, ChunkPos chunkPos, int sectionY, int cellIndex, int heatSteps) {
        ThermalCell cell = cell(sectionY, cellIndex);
        if (cell == null) {
            return HeatTickResult.inactive();
        }
        boolean wasDirty = cell.dirtyTopology();
        ensureTopology(level, chunkPos, sectionY, cellIndex, cell);
        BlockPos origin = ThermalCell.origin(chunkPos, sectionY, cellIndex);
        int ambient = AmbientHeat.get(level, origin);
        boolean blockHeatSource = hasBlockHeatSource(level, origin, cell);
        boolean active = cell.tickGroups(level, chunkPos, sectionY, cellIndex, ambient, heatSteps);
        boolean heatNodeActive = tickHeatNodes(level, sectionY, cellIndex);
        active |= heatNodeActive;
        active |= HeatDiffuser.exchangeInsideCell(level, chunkPos, sectionY, cellIndex, cell, heatSteps);
        active |= HeatDiffuser.exchangeWithNeighbors(level, chunkPos, sectionY, cellIndex, cell, heatSteps);
        cell.coolAndCull(level, chunkPos, sectionY, cellIndex, heatSteps);
        active |= blockHeatSource;
        long facePressure = facePressure(level, chunkPos, sectionY, cellIndex, cell, ambient);
        if (!active) {
            return HeatTickResult.inactive();
        }
        return new HeatTickResult(true, adaptiveDelayTicks(facePressure, wasDirty || blockHeatSource || heatNodeActive));
    }

    public record HeatTickResult(boolean active, int nextDelayTicks) {
        private static HeatTickResult inactive() {
            return new HeatTickResult(false, 0);
        }
    }

    private static int adaptiveDelayTicks(long facePressure, boolean priority) {
        int baseDelay = Math.max(1, Config.heatTickInterval);
        int tier = pressureTier(facePressure);
        if (priority) {
            tier = Math.max(tier, 5);
        }
        if (tier == 5) {
            return baseDelay;
        }
        if (tier < 5) {
            return Math.min(MAX_ADAPTIVE_TICK_DELAY, baseDelay << Math.min(8, 5 - tier));
        }
        return Math.max(1, baseDelay >> Math.min(8, tier - 5));
    }

    private static int pressureTier(long facePressure) {
        if (facePressure <= 0L) {
            return 0;
        }
        long scaled = Math.max(1L, facePressure / FACE_PRESSURE_BASE);
        return Math.min(12, 64 - Long.numberOfLeadingZeros(scaled));
    }

    private static long facePressure(ServerLevel level, ChunkPos chunkPos, int sectionY, int cellIndex, ThermalCell cell, int ambient) {
        long pressure = internalFacePressure(cell);
        pressure += externalFacePressure(level, chunkPos, sectionY, cellIndex, cell, HeatUnits.kelvinToFixed(ambient));
        return pressure;
    }

    private static long internalFacePressure(ThermalCell cell) {
        long pressure = 0L;
        for (ThermalContact contact : cell.internalContacts()) {
            if (contact.groupA() >= cell.groups().size() || contact.groupB() >= cell.groups().size()) {
                continue;
            }
            ThermalGroup first = cell.groups().get(contact.groupA());
            ThermalGroup second = cell.groups().get(contact.groupB());
            pressure += (long) fixedAbsDiff(first.temperature(), second.temperature()) * Math.max(1, contact.contactArea());
        }
        return pressure;
    }

    private static long externalFacePressure(ServerLevel level, ChunkPos chunkPos, int sectionY, int cellIndex, ThermalCell cell, int ambientTemperature) {
        long pressure = 0L;
        BlockPos origin = ThermalCell.origin(chunkPos, sectionY, cellIndex);
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = origin.relative(direction, ThermalCell.SIZE);
            net.minecraft.world.level.chunk.ChunkAccess access = level.getChunk(SectionPos.blockToSectionCoord(neighborPos.getX()), SectionPos.blockToSectionCoord(neighborPos.getZ()), net.minecraft.world.level.chunk.status.ChunkStatus.FULL, false);
            ThermalCell neighborCell = null;
            int neighborSectionY = SectionPos.blockToSectionCoord(neighborPos.getY());
            int neighborCellIndex = ThermalCell.cellIndex(neighborPos);
            if (access instanceof net.minecraft.world.level.chunk.LevelChunk neighborChunk) {
                ChunkHeatData neighborData = HeatManager.getChunkData(neighborChunk);
                neighborCell = neighborData.cell(neighborSectionY, neighborCellIndex);
                if (neighborCell != null) {
                    neighborData.ensureTopology(level, neighborChunk.getPos(), neighborSectionY, neighborCellIndex, neighborCell);
                }
            }
            for (int u = 0; u < ThermalCell.SIZE; u++) {
                for (int v = 0; v < ThermalCell.SIZE; v++) {
                    ThermalGroup local = cell.groupAtBlockIndex(faceIndex(direction, u, v, true));
                    if (local == null) {
                        continue;
                    }
                    int neighborTemperature = ambientTemperature;
                    if (neighborCell != null) {
                        ThermalGroup neighbor = neighborCell.groupAtBlockIndex(faceIndex(direction, u, v, false));
                        if (neighbor != null) {
                            neighborTemperature = neighbor.temperature();
                        }
                    }
                    pressure += fixedAbsDiff(local.temperature(), neighborTemperature);
                }
            }
        }
        return pressure;
    }

    private static int fixedAbsDiff(int first, int second) {
        return HeatUnits.clampToInt(Math.abs((long) first - second));
    }

    private static boolean hasBlockHeatSource(ServerLevel level, BlockPos origin, ThermalCell cell) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (ThermalGroup group : cell.groups()) {
            long mask = group.blockMask();
            while (mask != 0L) {
                int bit = Long.numberOfTrailingZeros(mask);
                pos.set(origin.getX() + (bit & 3), origin.getY() + ((bit >> 2) & 3), origin.getZ() + ((bit >> 4) & 3));
                if (BlockStateHeatInteractions.extraHeatFor(level.getBlockState(pos)) > 0) {
                    return true;
                }
                mask &= mask - 1L;
            }
        }
        return false;
    }

    public void ensureTopology(ServerLevel level, ChunkPos chunkPos, int sectionY, int cellIndex, ThermalCell cell) {
        if (cell.dirtyTopology()) {
            ThermalTopologyBuilder.rebuild(level, chunkPos, sectionY, cellIndex, cell);
        }
    }

    public ThermalCell cell(int sectionY, int cellIndex) {
        ThermalCell[] cells = cellsBySection.get(sectionY);
        return cells == null ? null : cells[cellIndex];
    }

    public ThermalCell getOrCreateCell(int sectionY, int cellIndex) {
        ThermalCell[] cells = cellsBySection.computeIfAbsent(sectionY, ignored -> new ThermalCell[64]);
        ThermalCell cell = cells[cellIndex];
        if (cell == null) {
            cell = new ThermalCell();
            cells[cellIndex] = cell;
        }
        return cell;
    }

    public void setCell(int sectionY, int cellIndex, ThermalCell cell) {
        cellsBySection.computeIfAbsent(sectionY, ignored -> new ThermalCell[64])[cellIndex] = cell;
    }

    public Map<Long, HeatNode> heatNodes() {
        return heatNodes;
    }

    public void removeColdCells() {
        // Temperatures are stored as fixed-point actual Kelvin values, so neutral cells still carry state.
    }

    public int populatedCellCount() {
        int count = 0;
        for (ThermalCell[] cells : cellsBySection.values()) {
            for (ThermalCell cell : cells) {
                if (cell != null) {
                    count++;
                }
            }
        }
        return count;
    }

    public int dirtyCellCount() {
        int count = 0;
        for (ThermalCell[] cells : cellsBySection.values()) {
            for (ThermalCell cell : cells) {
                if (cell != null && cell.dirtyTopology()) {
                    count++;
                }
            }
        }
        return count;
    }

    public void enqueueActiveCells(ServerLevel level, ChunkPos chunkPos) {
        for (Map.Entry<Integer, ThermalCell[]> entry : cellsBySection.entrySet()) {
            ThermalCell[] cells = entry.getValue();
            for (int i = 0; i < cells.length; i++) {
                if (cells[i] != null && (cells[i].dirtyTopology() || cells[i].shouldSave())) {
                    HeatScheduler.enqueue(level, chunkPos, entry.getKey(), i);
                }
            }
        }
    }

    public void collectDebugLabels(ServerLevel level, ChunkPos chunkPos, List<HeatDebugLabel> labels, int maxLabels) {
        for (Map.Entry<Integer, ThermalCell[]> entry : cellsBySection.entrySet()) {
            int sectionY = entry.getKey();
            ThermalCell[] cells = entry.getValue();
            for (int cellIndex = 0; cellIndex < cells.length && labels.size() < maxLabels; cellIndex++) {
                ThermalCell cell = cells[cellIndex];
                if (cell == null || cell.dirtyTopology()) {
                    continue;
                }
                collectDebugLabelsForCell(level, chunkPos, sectionY, cellIndex, cell, labels, maxLabels, false);
            }
        }
    }

    public void collectDebugLabelsForCell(ServerLevel level, ChunkPos chunkPos, int sectionY, int cellIndex, ThermalCell cell, List<HeatDebugLabel> labels, int maxLabels, boolean includeAmbient) {
        BlockPos origin = ThermalCell.origin(chunkPos, sectionY, cellIndex);
        int ambient = AmbientHeat.get(level, origin);
        if (includeAmbient && cell.groups().isEmpty() && labels.size() < maxLabels) {
            labels.add(new HeatDebugLabel(origin.getX() + 2.0D, origin.getY() + 2.0D, origin.getZ() + 2.0D, ambient - 273));
            return;
        }
        for (ThermalGroup group : cell.groups()) {
            if (labels.size() >= maxLabels) {
                return;
            }
            if (includeAmbient || group.temperature() != HeatUnits.kelvinToFixed(ambient)) {
                labels.add(debugLabel(origin, group, ambient));
            }
        }
    }

    public void collectDebugOutlinesForCell(ServerLevel level, ChunkPos chunkPos, int sectionY, int cellIndex, ThermalCell cell, List<HeatDebugOutline> outlines, int maxOutlines) {
        if (outlines.size() >= maxOutlines || cell.groups().isEmpty()) {
            return;
        }
        BlockPos origin = ThermalCell.origin(chunkPos, sectionY, cellIndex);
        Set<Integer> sourceGroups = new HashSet<>();
        Set<Integer> adjacentGroups = new HashSet<>();
        for (ThermalGroup group : cell.groups()) {
            if (containsScorchedBrick(level, origin, group)) {
                sourceGroups.add(group.id());
            }
        }
        if (sourceGroups.isEmpty()) {
            return;
        }
        for (ThermalContact contact : cell.internalContacts()) {
            if (sourceGroups.contains(contact.groupA())) {
                adjacentGroups.add(contact.groupB());
            }
            if (sourceGroups.contains(contact.groupB())) {
                adjacentGroups.add(contact.groupA());
            }
        }
        collectCrossCellAdjacentOutlines(level, chunkPos, sectionY, cellIndex, cell, sourceGroups, outlines, maxOutlines);
        for (ThermalGroup group : cell.groups()) {
            if (outlines.size() >= maxOutlines) {
                return;
            }
            if (sourceGroups.contains(group.id())) {
                outlines.add(outline(origin, group, OUTLINE_PURPLE));
            } else if (adjacentGroups.contains(group.id())) {
                outlines.add(outline(origin, group, OUTLINE_BLUE));
            }
        }
    }

    private static void collectCrossCellAdjacentOutlines(ServerLevel level, ChunkPos chunkPos, int sectionY, int cellIndex, ThermalCell cell, Set<Integer> sourceGroups, List<HeatDebugOutline> outlines, int maxOutlines) {
        BlockPos origin = ThermalCell.origin(chunkPos, sectionY, cellIndex);
        Set<HeatDebugOutline> emitted = new HashSet<>();
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = origin.relative(direction, ThermalCell.SIZE);
            ChunkHeatData neighborData = null;
            ThermalCell neighborCell = null;
            ChunkPos neighborChunkPos = null;
            int neighborSectionY = SectionPos.blockToSectionCoord(neighborPos.getY());
            int neighborCellIndex = ThermalCell.cellIndex(neighborPos);
            for (int u = 0; u < ThermalCell.SIZE; u++) {
                for (int v = 0; v < ThermalCell.SIZE; v++) {
                    int localIndex = faceIndex(direction, u, v, true);
                    ThermalGroup source = cell.groupAtBlockIndex(localIndex);
                    if (source == null || !sourceGroups.contains(source.id())) {
                        continue;
                    }
                    if (neighborCell == null) {
                        net.minecraft.world.level.chunk.ChunkAccess access = level.getChunk(SectionPos.blockToSectionCoord(neighborPos.getX()), SectionPos.blockToSectionCoord(neighborPos.getZ()), net.minecraft.world.level.chunk.status.ChunkStatus.FULL, false);
                        if (!(access instanceof net.minecraft.world.level.chunk.LevelChunk neighborChunk)) {
                            return;
                        }
                        neighborChunkPos = neighborChunk.getPos();
                        neighborData = neighborChunk.getData(com.minecart.central_heater.heat.HeatAttachments.HEAT_CHUNK);
                        neighborCell = neighborData.getOrCreateCell(neighborSectionY, neighborCellIndex);
                        neighborData.ensureTopology(level, neighborChunk.getPos(), neighborSectionY, neighborCellIndex, neighborCell);
                    }
                    ThermalGroup adjacent = neighborCell.groupAtBlockIndex(faceIndex(direction, u, v, false));
                    if (adjacent != null && outlines.size() < maxOutlines) {
                        HeatDebugOutline adjacentOutline = outline(ThermalCell.origin(neighborChunkPos, neighborSectionY, neighborCellIndex), adjacent, OUTLINE_BLUE);
                        if (emitted.add(adjacentOutline)) {
                            outlines.add(adjacentOutline);
                        }
                    }
                }
            }
        }
    }

    private static boolean containsScorchedBrick(ServerLevel level, BlockPos origin, ThermalGroup group) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        long mask = group.blockMask();
        while (mask != 0L) {
            int bit = Long.numberOfTrailingZeros(mask);
            pos.set(origin.getX() + (bit & 3), origin.getY() + ((bit >> 2) & 3), origin.getZ() + ((bit >> 4) & 3));
            var blockId = BuiltInRegistries.BLOCK.getKey(level.getBlockState(pos).getBlock());
            if ("central_heater".equals(blockId.getNamespace()) && blockId.getPath().contains("scorched_brick")) {
                return true;
            }
            mask &= mask - 1L;
        }
        return false;
    }

    private static HeatDebugOutline outline(BlockPos origin, ThermalGroup group, int color) {
        return new HeatDebugOutline(origin.getX(), origin.getY(), origin.getZ(), group.blockMask(), color);
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

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        removeColdCells();
        CompoundTag tag = new CompoundTag();
        tag.putInt("version", VERSION);
        ListTag sectionList = new ListTag();
        for (Map.Entry<Integer, ThermalCell[]> entry : cellsBySection.entrySet()) {
            CompoundTag sectionTag = new CompoundTag();
            sectionTag.putInt("y", entry.getKey());
            ListTag cellList = new ListTag();
            ThermalCell[] cells = entry.getValue();
            for (int i = 0; i < cells.length; i++) {
                ThermalCell cell = cells[i];
                if (cell != null && cell.shouldSave()) {
                    cellList.add(cell.save(i));
                }
            }
            if (!cellList.isEmpty()) {
                sectionTag.put("cells", cellList);
                sectionList.add(sectionTag);
            }
        }
        tag.put("sections", sectionList);

        ListTag nodeList = new ListTag();
        for (HeatNode node : heatNodes.values()) {
            if (node.heat() != 0) {
                nodeList.add(node.save());
            }
        }
        tag.put("nodes", nodeList);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        cellsBySection.clear();
        heatNodes.clear();
        ListTag sectionList = tag.getList("sections", Tag.TAG_COMPOUND);
        for (int i = 0; i < sectionList.size(); i++) {
            CompoundTag sectionTag = sectionList.getCompound(i);
            ThermalCell[] cells = new ThermalCell[64];
            ListTag cellList = sectionTag.getList("cells", Tag.TAG_COMPOUND);
            for (int j = 0; j < cellList.size(); j++) {
                CompoundTag cellTag = cellList.getCompound(j);
                cells[Byte.toUnsignedInt(cellTag.getByte("i"))] = ThermalCell.load(cellTag);
            }
            cellsBySection.put(sectionTag.getInt("y"), cells);
        }
        ListTag nodeList = tag.getList("nodes", Tag.TAG_COMPOUND);
        for (int i = 0; i < nodeList.size(); i++) {
            HeatNode node = HeatNode.load(nodeList.getCompound(i));
            heatNodes.put(node.pos().asLong(), node);
        }
    }

    private void markNeighborIfPresent(BlockPos original, BlockPos neighbor, boolean onBoundary) {
        if (!onBoundary) {
            return;
        }
        ThermalCell cell = getCell(neighbor);
        if (cell != null) {
            cell.markDirty();
        }
    }

    public void refreshHeatNode(ServerLevel level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof HeatBlockEntityBehavior behavior) {
            heatNodes.computeIfAbsent(pos.asLong(), ignored -> normalizeCreatedNode(level, pos, behavior.createHeatNode()));
        } else {
            heatNodes.remove(pos.asLong());
        }
    }

    private static HeatNode normalizeCreatedNode(ServerLevel level, BlockPos pos, HeatNode node) {
        int ambient = AmbientHeat.get(level, pos);
        if (node.heat() < ambient) {
            node.setHeat(ambient + Math.max(0, node.heat()));
        }
        return node;
    }

    private boolean tickHeatNodes(ServerLevel level, int sectionY, int cellIndex) {
        boolean active = false;
        Iterator<Map.Entry<Long, HeatNode>> iterator = heatNodes.entrySet().iterator();
        while (iterator.hasNext()) {
            HeatNode node = iterator.next().getValue();
            if (SectionPos.blockToSectionCoord(node.pos().getY()) != sectionY || ThermalCell.cellIndex(node.pos()) != cellIndex) {
                continue;
            }
            BlockEntity blockEntity = level.getBlockEntity(node.pos());
            if (!(blockEntity instanceof HeatBlockEntityBehavior behavior)) {
                iterator.remove();
                continue;
            }

            int ambient = AmbientHeat.get(level, node.pos());
            if (node.heat() == 0) {
                node.setHeat(ambient);
            }
            int oldHeat = node.heat();
            ThermalCell cell = cell(sectionY, cellIndex);
            if (cell != null) {
                exchangeNodeWithCell(node, cell, ambient);
            }
            HeatNodeContext ctx = new HeatNodeContext(level, node.pos(), node);
            NodeAccess access = new NodeAccess(node);
            HeatEmission emission = behavior.getEmission(ctx);
            int targetHeat = ambient + emission.targetHeat();
            int maxHeat = ambient + emission.maxHeat();
            if (emission.powerPerUpdate() > 0 && node.heat() < maxHeat) {
                int target = Math.min(targetHeat, maxHeat);
                node.setHeat(Math.min(target, node.heat() + emission.powerPerUpdate()));
            }
            HeatSink sink = behavior.getSink(ctx);
            int minRequiredHeat = ambient + sink.minRequiredHeat();
            if (sink.maxDrainPerUpdate() > 0 && (node.heat() > minRequiredHeat || sink.canCoolBelowAmbient())) {
                int drained = node.heat() - Math.min(sink.maxDrainPerUpdate(), sink.heatDemandPerUpdate());
                node.setHeat(sink.canCoolBelowAmbient() ? drained : Math.max(ambient, drained));
            }
            behavior.tickHeatNode(ctx, access);
            if (oldHeat != node.heat()) {
                behavior.onHeatChanged(ctx, access, oldHeat, node.heat());
            }
            if (cell != null) {
                exchangeNodeWithCell(node, cell, ambient);
            }
            active |= node.heat() != ambient || emission.powerPerUpdate() > 0 || sink.heatDemandPerUpdate() > 0;
        }
        return active;
    }

    private static void exchangeNodeWithCell(HeatNode node, ThermalCell cell, int ambient) {
        int localX = node.pos().getX() & 3;
        int localY = node.pos().getY() & 3;
        int localZ = node.pos().getZ() & 3;
        int groupHeat = cell.getTemperatureKelvin(localX, localY, localZ, ambient);
        int diff = node.heat() - groupHeat;
        if (diff == 0) {
            return;
        }
        int transfer = Math.max(1, Math.abs(diff) / 8);
        transfer = Math.min(transfer, 24);
        if (diff > 0) {
            node.setHeat(node.heat() - transfer);
            cell.setTemperatureKelvin(localX, localY, localZ, groupHeat + transfer);
        } else {
            node.setHeat(node.heat() + transfer);
            cell.setTemperatureKelvin(localX, localY, localZ, groupHeat - transfer);
        }
    }

    private static HeatDebugLabel debugLabel(BlockPos origin, ThermalGroup group, int ambient) {
        long mask = group.blockMask();
        int count = 0;
        double x = 0.0D;
        double y = 0.0D;
        double z = 0.0D;
        while (mask != 0L) {
            int bit = Long.numberOfTrailingZeros(mask);
            x += origin.getX() + (bit & 3) + 0.5D;
            y += origin.getY() + ((bit >> 2) & 3) + 0.5D;
            z += origin.getZ() + ((bit >> 4) & 3) + 0.5D;
            count++;
            mask &= mask - 1L;
        }
        int safeCount = Math.max(1, count);
        return new HeatDebugLabel(x / safeCount, y / safeCount, z / safeCount, HeatUnits.fixedToCelsiusRounded(group.temperature()));
    }

    private static class NodeAccess implements HeatNodeAccess {
        private final HeatNode node;

        private NodeAccess(HeatNode node) {
            this.node = node;
        }

        @Override
        public int getHeat() {
            return node.heat();
        }

        @Override
        public void setHeat(int heat) {
            node.setHeat(heat);
        }
    }
}
