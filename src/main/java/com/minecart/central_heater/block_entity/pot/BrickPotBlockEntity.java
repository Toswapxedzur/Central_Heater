package com.minecart.central_heater.block_entity.pot;

import com.minecart.central_heater.AllBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class BrickPotBlockEntity extends AbstractPotBlockEntity {
    public BrickPotBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlockEntity.pot.get(), pos, blockState,1);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.brick_pot");
    }
}
