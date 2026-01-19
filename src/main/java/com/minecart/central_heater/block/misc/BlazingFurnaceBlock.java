package com.minecart.central_heater.block.misc;

import com.minecart.central_heater.block_entity.AllBlockEntity;
import com.minecart.central_heater.block_entity.misc.BlazingFurnaceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class BlazingFurnaceBlock extends AbstractFurnaceBlock {

    public BlazingFurnaceBlock(BlockBehaviour.Properties properties) {
        super(properties.lightLevel(state -> state.getValue(AbstractFurnaceBlock.LIT) ? 15 : 0));
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlazingFurnaceBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createFurnaceTicker(level, blockEntityType, AllBlockEntity.blazing_furnace.get());
    }

    protected void openContainer(Level level, BlockPos pos, Player player) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof BlazingFurnaceBlockEntity) {
            player.openMenu((MenuProvider)blockentity);
            player.awardStat(Stats.INTERACT_WITH_FURNACE);
        }
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if ((Boolean)state.getValue(LIT)) {
            double d0 = (double)pos.getX() + (double)0.5F;
            double d1 = (double)pos.getY();
            double d2 = (double)pos.getZ() + (double)0.5F;
            if (random.nextDouble() < 0.3) {
                level.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = (Direction)state.getValue(FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double d3 = 0.52;
            double d4 = random.nextDouble() * 0.6 - 0.3;
            double d5 = direction$axis == Direction.Axis.X ? (double)direction.getStepX() * 0.52 : d4;
            double d6 = ( random.nextDouble() * (double)3.0F + 11.0F ) / (double)16.0F;
            double d7 = direction$axis == Direction.Axis.Z ? (double)direction.getStepZ() * 0.52 : d4;
            level.addParticle(ParticleTypes.SOUL, d0 + d5, d1 + d6, d2 + d7, (double)0.0F, (double)0.0F, (double)0.0F);
            level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0 + d5, d1 + d6, d2 + d7, (double)0.0F, (double)0.0F, (double)0.0F);
        }
    }
}