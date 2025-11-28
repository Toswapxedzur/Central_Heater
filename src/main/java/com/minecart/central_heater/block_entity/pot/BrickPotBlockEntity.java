package com.minecart.central_heater.block_entity.pot;

import com.minecart.central_heater.AllBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class BrickPotBlockEntity extends AbstractPotBlockEntity {
    public BrickPotBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlockEntity.brick_pot_be.get(), pos, blockState,1, 8);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.brick_pot");
    }
}
