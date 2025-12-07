package com.minecart.central_heater.block_entity.pot;

import com.minecart.central_heater.AllBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class GoldenCauldronBlockEntity extends AbstractPotBlockEntity {
    public GoldenCauldronBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlockEntity.golden_cauldron.get(), pos, blockState,4);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.golden_cauldron");
    }
}
