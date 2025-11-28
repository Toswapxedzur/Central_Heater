package com.minecart.central_heater.temperature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockHeat {
    public static final int MAX_HEAT_CONDUCTION = 5;

    public static int getHeatLevel(Level level, BlockPos pos){
        int cumulative = 0;
        for(BlockPos possiblePos : BlockPos.betweenClosed(0-MAX_HEAT_CONDUCTION, 0-MAX_HEAT_CONDUCTION, 0-MAX_HEAT_CONDUCTION,
                MAX_HEAT_CONDUCTION, MAX_HEAT_CONDUCTION, MAX_HEAT_CONDUCTION)){
            BlockState state = level.getBlockState(pos.offset(possiblePos));
            BlockEntity entity = level.getBlockEntity(pos.offset(possiblePos));
            if(state.getBlock() instanceof IBlockStateHeatable heatable){
                cumulative += heatable.provide(BlockPos.ZERO.subtract(possiblePos), state);
            }else if(entity instanceof IBlockEntityHeatable heatable){
                cumulative += heatable.provide(BlockPos.ZERO.subtract(possiblePos));
            }
        }
        return cumulative;
    }
}
