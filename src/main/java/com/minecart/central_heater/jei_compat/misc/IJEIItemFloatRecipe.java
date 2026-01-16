package com.minecart.central_heater.jei_compat.misc;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import javax.annotation.Nonnegative;
import java.util.List;

public interface IJEIItemFloatRecipe extends Comparable<IJEIItemFloatRecipe>{
    @Unmodifiable
    List<ItemStack> getInput();

    @Nonnegative
    float getValue();

    @Override
    default int compareTo(@NotNull IJEIItemFloatRecipe other){
        float t = this.getValue();
        float o = other.getValue();
        if(t < o)
            return -1;
        if(t > o)
            return 1;
        return 0;
    }
}
