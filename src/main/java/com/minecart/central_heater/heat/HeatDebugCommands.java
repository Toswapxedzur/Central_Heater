package com.minecart.central_heater.heat;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.minecart.central_heater.heat.api.HeatApi;
import com.minecart.central_heater.heat.debug.HeatDebugTempDisplay;
import com.minecart.central_heater.heat.storage.ChunkHeatData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class HeatDebugCommands {
    private HeatDebugCommands() {
    }

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("showtemp")
                .requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .executes(context -> showTemp(context.getSource())));
        event.getDispatcher().register(Commands.literal("hidetemp")
                .requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .executes(context -> hideTemp(context.getSource())));
        event.getDispatcher().register(Commands.literal("tempstat")
                .requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .executes(context -> tempStat(context.getSource())));
        event.getDispatcher().register(Commands.literal("centralheater")
                .requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("heat")
                        .then(Commands.literal("get")
                                .executes(context -> get(context.getSource(), sourcePos(context.getSource())))
                                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                        .executes(context -> get(context.getSource(), BlockPosArgument.getLoadedBlockPos(context, "pos")))))
                        .then(Commands.literal("ambient")
                                .executes(context -> ambient(context.getSource(), sourcePos(context.getSource())))
                                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                        .executes(context -> ambient(context.getSource(), BlockPosArgument.getLoadedBlockPos(context, "pos")))))
                        .then(Commands.literal("set")
                                .then(Commands.argument("kelvin", IntegerArgumentType.integer(Short.MIN_VALUE, Short.MAX_VALUE))
                                        .executes(context -> set(context.getSource(), sourcePos(context.getSource()), IntegerArgumentType.getInteger(context, "kelvin")))
                                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                                .executes(context -> set(context.getSource(), BlockPosArgument.getLoadedBlockPos(context, "pos"), IntegerArgumentType.getInteger(context, "kelvin"))))))
                        .then(Commands.literal("add")
                                .then(Commands.argument("delta", IntegerArgumentType.integer())
                                        .executes(context -> add(context.getSource(), sourcePos(context.getSource()), IntegerArgumentType.getInteger(context, "delta")))
                                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                                .executes(context -> add(context.getSource(), BlockPosArgument.getLoadedBlockPos(context, "pos"), IntegerArgumentType.getInteger(context, "delta"))))))
                        .then(Commands.literal("rebuild")
                                .executes(context -> rebuild(context.getSource(), sourcePos(context.getSource())))
                                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                        .executes(context -> rebuild(context.getSource(), BlockPosArgument.getLoadedBlockPos(context, "pos")))))
                        .then(Commands.literal("stats")
                                .executes(context -> stats(context.getSource())))
                        .then(Commands.literal("tempstat")
                                .executes(context -> tempStat(context.getSource())))
                        .then(Commands.literal("showtemp")
                                .executes(context -> showTemp(context.getSource())))
                        .then(Commands.literal("hidetemp")
                                .executes(context -> hideTemp(context.getSource())))));
    }

    private static int showTemp(CommandSourceStack source) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        net.minecraft.server.level.ServerPlayer player = source.getPlayerOrException();
        int labels = HeatDebugTempDisplay.show(player);
        source.sendSuccess(() -> Component.literal("Showing " + labels + " heat segment temperature labels in Celsius."), false);
        return labels;
    }

    private static int hideTemp(CommandSourceStack source) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        net.minecraft.server.level.ServerPlayer player = source.getPlayerOrException();
        HeatDebugTempDisplay.hide(player);
        source.sendSuccess(() -> Component.literal("Hidden heat segment temperatures."), false);
        return 1;
    }

    private static int get(CommandSourceStack source, BlockPos pos) {
        int actual = HeatApi.getActualHeat(source.getLevel(), pos);
        int extra = HeatApi.getExtraHeat(source.getLevel(), pos);
        int ambient = HeatApi.getAmbientHeat(source.getLevel(), pos);
        source.sendSuccess(() -> Component.literal("Heat at " + format(pos) + ": " + actual + "K actual, " + ambient + "K ambient, " + extra + " extra"), false);
        return actual;
    }

    private static int ambient(CommandSourceStack source, BlockPos pos) {
        short temp = HeatManager.getAmbientTemperature(source.getLevel(), pos);
        source.sendSuccess(() -> Component.literal("Ambient at " + format(pos) + ": " + temp + "K"), false);
        return temp;
    }

    private static int set(CommandSourceStack source, BlockPos pos, int kelvin) {
        HeatManager.setTemperature(source.getLevel(), pos, HeatManager.clampKelvin(kelvin));
        source.sendSuccess(() -> Component.literal("Set heat at " + format(pos) + " to " + kelvin + "K"), true);
        return kelvin;
    }

    private static int add(CommandSourceStack source, BlockPos pos, int delta) {
        HeatManager.addHeat(source.getLevel(), pos, delta);
        int actual = HeatApi.getActualHeat(source.getLevel(), pos);
        source.sendSuccess(() -> Component.literal("Added " + delta + " extra heat at " + format(pos) + "; now " + actual + "K actual"), true);
        return actual;
    }

    private static int rebuild(CommandSourceStack source, BlockPos pos) {
        ServerLevel level = source.getLevel();
        HeatManager.markDirty(level, pos);
        HeatApi.getActualHeat(level, pos);
        source.sendSuccess(() -> Component.literal("Rebuilt thermal cell near " + format(pos)), true);
        return 1;
    }

    private static int stats(CommandSourceStack source) {
        HeatTicker.HeatStats tickerStats = HeatTicker.stats(source.getLevel());
        BlockPos pos = sourcePos(source);
        ChunkHeatData data = HeatManager.getChunkData(source.getLevel().getChunkAt(pos));
        source.sendSuccess(() -> Component.literal(
                "Heat active cells: " + tickerStats.activeCells()
                        + ", due now: " + tickerStats.dueCells()
                        + ", processed last tick: " + tickerStats.processedCells()
                        + ", local populated cells: " + data.populatedCellCount()
                        + ", local dirty cells: " + data.dirtyCellCount()
        ), false);
        return tickerStats.activeCells();
    }

    private static int tempStat(CommandSourceStack source) {
        HeatTicker.HeatStats stats = HeatTicker.stats(source.getLevel());
        double millis = stats.lastTickNanos() / 1_000_000.0D;
        source.sendSuccess(() -> Component.literal(
                "Heat workload: active=" + stats.activeCells()
                        + ", due=" + stats.dueCells()
                        + ", processedLastTick=" + stats.processedCells()
                        + ", skippedUnloaded=" + stats.skippedUnloadedCells()
                        + ", backlog=" + stats.backlog()
                        + ", maxLate=" + stats.maxLatenessTicks() + "t"
                        + ", lastCost=" + String.format(java.util.Locale.ROOT, "%.3fms", millis)
        ), false);
        return stats.activeCells();
    }

    private static BlockPos sourcePos(CommandSourceStack source) {
        return BlockPos.containing(source.getPosition());
    }

    private static String format(BlockPos pos) {
        return pos.getX() + " " + pos.getY() + " " + pos.getZ();
    }
}
