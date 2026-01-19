package com.minecart.central_heater.block_entity.cauldron;

import com.minecart.central_heater.block_entity.AllBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BrickCauldronBlockEntity extends AbstractCauldronBlockEntity {
    public BrickCauldronBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlockEntity.brick_cauldron.get(), pos, blockState, 2);
    }
}