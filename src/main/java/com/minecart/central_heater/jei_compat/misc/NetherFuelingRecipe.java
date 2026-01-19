package com.minecart.central_heater.jei_compat.misc;

import com.google.common.base.Preconditions;
import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.List;

public class NetherFuelingRecipe implements IJeiFuelingRecipe {
    private final List<ItemStack> inputs;
    private final int burnTime;

    public NetherFuelingRecipe(Collection<ItemStack> input, int burnTime) {
        Preconditions.checkArgument(burnTime > 0, "burn time must be greater than 0");
        this.inputs = List.copyOf(input);
        this.burnTime = burnTime;
    }

    @Override
    public List<ItemStack> getInputs() {
        return inputs;
    }

    @Override
    public int getBurnTime() {
        return burnTime;
    }
}