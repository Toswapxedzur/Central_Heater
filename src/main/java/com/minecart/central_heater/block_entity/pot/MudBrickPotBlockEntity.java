package com.minecart.central_heater.block_entity.pot;

import com.minecart.central_heater.AllBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class MudBrickPotBlockEntity extends AbstractPotBlockEntity {
    public MudBrickPotBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlockEntity.pot.get(), pos, blockState,1);
    }
}
