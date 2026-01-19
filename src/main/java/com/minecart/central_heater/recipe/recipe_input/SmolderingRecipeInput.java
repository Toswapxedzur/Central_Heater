package com.minecart.central_heater.recipe.recipe_input;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class SmolderingRecipeInput implements Container {
    protected final FluidStack fluid;
    public final NonNullList<ItemStack> item;
    protected final int tier;
    public final int fireLevel;

    public SmolderingRecipeInput(NonNullList<ItemStack> item, FluidStack fluid, int tier, int fireLevel){
        this.item = item;
        this.fluid = fluid;
        this.tier = tier;
        this.fireLevel = fireLevel;
    }

    public SmolderingRecipeInput(NonNullList<ItemStack> item, int tier, int fireLevel){
        this(item, FluidStack.EMPTY, tier, fireLevel);
    }

    // --- Custom Data Accessors ---

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

    // --- Container Implementation ---

    @Override
    public int getContainerSize() {
        return item.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : item) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return item.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return ContainerHelper.removeItem(item, index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(item, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        item.set(index, stack);
    }

    @Override
    public void setChanged() {
        // No-op for recipe input wrapper
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        item.clear();
    }
}