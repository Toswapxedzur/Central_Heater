package com.minecart.central_heater.block_entity.pot;

import com.minecart.central_heater.AllBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class NetherPotBlockEntity extends AbstractPotBlockEntity {
    public NetherPotBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlockEntity.nether_pot_be.get(), pos, blockState,3, 8);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.nether_pot");
    }
}
