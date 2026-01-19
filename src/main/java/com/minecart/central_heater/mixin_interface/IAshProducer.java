package com.minecart.central_heater.mixin_interface;

import com.minecart.central_heater.AllBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IAshProducer {
    int getAshCount();

    void setAshCount(int count);

    default void dropAsh(Level level, BlockPos pos){
        if (this.getAshCount() <= 0) return;

        Item ashItem = AllBlockItem.FIRE_ASH.get();
        int maxStackSize = ashItem.getMaxStackSize();

        while (this.getAshCount() > 0) {
            int dropAmount = Math.min(this.getAshCount(), maxStackSize);

            ItemStack ashStack = new ItemStack(ashItem, dropAmount);
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), ashStack);

            setAshCount(getAshCount() - dropAmount);
        }
    }
}