package com.minecart.central_heater.recipe.recipe_input;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BlockSmolderingRecipeInput implements Container {
    public final BlockState state;
    protected final int fireLevel;
    protected final boolean fireBurn;

    public BlockSmolderingRecipeInput(BlockState state, int fireLevel, boolean fireBurn){
        this.state = state;
        this.fireLevel = fireLevel;
        this.fireBurn = fireBurn;
    }

    public int getFireLevel() {
        return fireLevel;
    }

    public boolean getFireBurn(){
        return fireBurn;
    }

    // --- Dummy Container Implementation ---
    // These methods satisfy the Container interface required by the Recipe system
    // but don't actually store items, since this recipe relies on BlockState.

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        // No-op
    }

    @Override
    public void setChanged() {
        // No-op
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        // No-op
    }
}