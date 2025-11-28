package com.minecart.central_heater.block_entity.pot;

import com.minecart.central_heater.AllBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class StonePotBlockEntity extends AbstractPotBlockEntity {
    public StonePotBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlockEntity.stone_pot_be.get(), pos, blockState,2, 8);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.stone_pot");
    }
}
