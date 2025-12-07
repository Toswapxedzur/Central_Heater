package com.minecart.central_heater.block_entity.pot;

import com.minecart.central_heater.AllBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class CauldronBlockEntity extends AbstractPotBlockEntity {
    public CauldronBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlockEntity.cauldron.get(), pos, blockState,3);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.cauldron");
    }
}
