package com.minecart.central_heater.jei_compat.misc;

import com.google.common.base.Preconditions;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.List;

public class AshDropChanceRecipe implements IJEIItemFloatRecipe {
    private final List<ItemStack> inputs;
    private final float dropChance;

    public AshDropChanceRecipe(Collection<ItemStack> inputs, float dropChance) {
        Preconditions.checkArgument(dropChance >= 0f, "Ash drop chance must not be negative");
        this.inputs = List.copyOf(inputs);
        this.dropChance = dropChance;
    }

    @Override
    public List<ItemStack> getInput() {
        return inputs;
    }

    @Override
    public float getValue() {
        return dropChance;
    }
}