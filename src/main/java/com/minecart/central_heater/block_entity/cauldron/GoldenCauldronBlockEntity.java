package com.minecart.central_heater.block_entity.cauldron;

import com.minecart.central_heater.block_entity.AllBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class GoldenCauldronBlockEntity extends AbstractCauldronBlockEntity {
    public GoldenCauldronBlockEntity(BlockPos pos, BlockState blockState) {
        // Tier 4
        super(AllBlockEntity.golden_cauldron.get(), pos, blockState, 4);
    }
}