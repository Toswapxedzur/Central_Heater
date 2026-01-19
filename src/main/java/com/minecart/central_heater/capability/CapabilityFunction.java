package com.minecart.central_heater.capability;

import com.minecart.central_heater.block_entity.cauldron.AbstractCauldronBlockEntity;
import com.minecart.central_heater.block_entity.stove.AbstractStoveBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class CapabilityFunction {
    public static IItemHandler stoveCapability(AbstractStoveBlockEntity stove, Direction side){
        Level level = stove.getLevel();
        BlockPos pos = stove.getBlockPos();
        if(side.equals(Direction.DOWN) || side.equals(Direction.UP)){
            if(level.getBlockEntity(pos.above()) instanceof AbstractCauldronBlockEntity entity)
                return new CombinedInvWrapper(stove.items, entity.getContainer());
            return stove.items;
        }
        return stove.fuels;
    }
}