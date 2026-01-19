package com.minecart.central_heater.block_entity.cauldron;

import com.minecart.central_heater.block_entity.AllBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CauldronBlockEntity extends AbstractCauldronBlockEntity {
    public CauldronBlockEntity(BlockPos pos, BlockState blockState) {
        // Tier 3
        super(AllBlockEntity.iron_cauldron.get(), pos, blockState, 3);
    }
}