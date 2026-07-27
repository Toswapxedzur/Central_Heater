package com.minecart.central_heater.heat.sim;

import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class BlockStateHeatInteractions {
    private BlockStateHeatInteractions() {
    }

    public static int extraHeatFor(BlockState state) {
        if (state.is(Blocks.LAVA)) {
            return 560;
        }
        if (state.is(Blocks.MAGMA_BLOCK)) {
            return 95;
        }
        if (state.hasProperty(BlockStateProperties.LIT) && state.getValue(BlockStateProperties.LIT)) {
            if (state.getBlock() instanceof CampfireBlock) {
                return 190;
            }
            if (state.getBlock() instanceof AbstractFurnaceBlock) {
                return 240;
            }
            return 140;
        }
        if (state.getBlock() instanceof FireBlock) {
            return 280;
        }
        return 0;
    }

    public static int sourceHeatingPowerFor(BlockState state) {
        if (state.is(Blocks.LAVA)) {
            return 48;
        }
        if (state.is(Blocks.MAGMA_BLOCK)) {
            return 8;
        }
        if (state.hasProperty(BlockStateProperties.LIT) && state.getValue(BlockStateProperties.LIT)) {
            if (state.getBlock() instanceof CampfireBlock) {
                return 18;
            }
            if (state.getBlock() instanceof AbstractFurnaceBlock) {
                return 22;
            }
            return 12;
        }
        if (state.getBlock() instanceof FireBlock) {
            return 28;
        }
        return 0;
    }
}
