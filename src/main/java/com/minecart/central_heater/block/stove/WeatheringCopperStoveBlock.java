package com.minecart.central_heater.block.stove;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

public class WeatheringCopperStoveBlock extends CopperStoveBlock implements WeatheringCopper {
    public static final MapCodec<WeatheringCopperStoveBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            WeatherState.CODEC.fieldOf("weathering_state").forGetter(WeatheringCopperStoveBlock::getAge),
            propertiesCodec()
    ).apply(instance, WeatheringCopperStoveBlock::new));

    public WeatheringCopperStoveBlock(WeatherState weatherState, Properties properties) {
        super(weatherState, properties);
    }

    @Override
    public MapCodec<WeatheringCopperStoveBlock> codec() {
        return CODEC;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }

    @Override
    public WeatherState getAge() {
        return this.getWeatherState();
    }
}