package com.minecart.central_heater.block.stove;

import com.minecart.central_heater.AllBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class WeatheringCopperStoveBlock extends CopperStoveBlock implements WeatheringCopper {

    public WeatheringCopperStoveBlock(WeatherState weatherState, Properties properties) {
        super(weatherState, properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.tickWeathering(state, level, pos, random);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return getNextStove(state.getBlock()).isPresent();
    }

    @Override
    public WeatherState getAge() {
        return this.getWeatherState();
    }

    private void tickWeathering(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // 1.20.1 NEXT_BY_BLOCK is an ImmutableBiMap and cannot be mutated, so we replicate the
        // weathering logic here for our blocks.
        float chance = 0.05688889F;
        if (random.nextFloat() < chance) {
            getNextStove(state.getBlock()).ifPresent(next -> level.setBlockAndUpdate(pos, copyStateProperties(next.defaultBlockState(), state)));
        }
    }

    private static Optional<Block> getNextStove(Block block) {
        if (block == AllBlockItem.COPPER_STOVE.get()) return Optional.of(AllBlockItem.EXPOSED_COPPER_STOVE.get());
        if (block == AllBlockItem.EXPOSED_COPPER_STOVE.get()) return Optional.of(AllBlockItem.WEATHERED_COPPER_STOVE.get());
        if (block == AllBlockItem.WEATHERED_COPPER_STOVE.get()) return Optional.of(AllBlockItem.OXIDIZED_COPPER_STOVE.get());
        return Optional.empty();
    }

    private static BlockState copyStateProperties(BlockState target, BlockState source) {
        for (net.minecraft.world.level.block.state.properties.Property<?> property : source.getProperties()) {
            if (target.hasProperty(property)) {
                target = copyProperty(target, source, property);
            }
        }
        return target;
    }

    private static <T extends Comparable<T>> BlockState copyProperty(BlockState target, BlockState source, net.minecraft.world.level.block.state.properties.Property<T> property) {
        return target.setValue(property, source.getValue(property));
    }
}
