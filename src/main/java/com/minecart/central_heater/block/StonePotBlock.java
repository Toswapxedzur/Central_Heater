package com.minecart.central_heater.block;

import com.minecart.central_heater.AllBlockEntity;
import com.minecart.central_heater.block_entity.pot.AbstractPotBlockEntity;
import com.minecart.central_heater.block_entity.pot.BrickPotBlockEntity;
import com.minecart.central_heater.block_entity.pot.StonePotBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class StonePotBlock extends PotBlock {
    public static final MapCodec<StonePotBlock> CODEC = simpleCodec(StonePotBlock::new);

    public StonePotBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StonePotBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide){
            return createTickerHelper(blockEntityType, AllBlockEntity.stone_pot_be.get(), AbstractPotBlockEntity::clientTick);
        }else{
            return createTickerHelper(blockEntityType, AllBlockEntity.stone_pot_be.get(), AbstractPotBlockEntity::serverTick);
        }
    }
}
