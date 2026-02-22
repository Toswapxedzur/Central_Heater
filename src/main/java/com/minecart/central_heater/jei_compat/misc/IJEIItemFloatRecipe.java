package com.minecart.central_heater.jei_compat.misc;

import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public interface IJEIItemFloatRecipe extends Comparable<IJEIItemFloatRecipe> {
    List<ItemStack> getInput();

    float getValue();

    @Override
    default int compareTo(@Nonnull IJEIItemFloatRecipe other) {
        return Float.compare(this.getValue(), other.getValue());
    }
}