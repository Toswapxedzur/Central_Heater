package com.minecart.central_heater.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.Optional;

public class SmolderingRecipeInput implements RecipeInput {
    protected final FluidStack fluid;
    protected final NonNullList<ItemStack> item;
    protected final int tier;
    protected final int fireLevel;

    public SmolderingRecipeInput(NonNullList<ItemStack> item, FluidStack fluid, int tier, int fireLevel){
        this.item = item;
        this.fluid = fluid;
        this.tier = tier;
        this.fireLevel = fireLevel;
    }

    public SmolderingRecipeInput(NonNullList<ItemStack> item, int tier, int fireLevel){
        this(item, FluidStack.EMPTY, tier, fireLevel);
    }

    @Override
    public ItemStack getItem(int index) {
        return item.get(index);
    }

    @Override
    public int size() {
        return item.size();
    }

    public FluidStack getFluid(){
        return fluid;
    }

    public boolean requireFluid(){
        return fluid.isEmpty();
    }

    public int getTier(){
        return tier;
    }

    public int getFireLevel() {
        return fireLevel;
    }
}
