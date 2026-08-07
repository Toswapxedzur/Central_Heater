package com.minecart.central_heater.block.misc;

import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.AnvilBlock;

public class SturdyAnvilBlock extends AnvilBlock {
    public SturdyAnvilBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void falling(FallingBlockEntity fallingEntity) {
        fallingEntity.setHurtsEntities(6.0F, 120);
    }
}
