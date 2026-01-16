package com.minecart.central_heater.recipe.recipe_input;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public class BlockSmolderingRecipeInput implements RecipeInput {
    public final BlockState state;
    protected final int fireLevel;
    protected final boolean fireBurn;

    public BlockSmolderingRecipeInput(BlockState state, int fireLevel, boolean fireBurn){
        this.state = state;
        this.fireLevel = fireLevel;
        this.fireBurn = fireBurn;
    }
    @Override
    public ItemStack getItem(int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return 0;
    }

    public int getFireLevel() {
        return fireLevel;
    }

    public boolean getfireBurn(){
        return fireBurn;
    }

    @Override
    public boolean isEmpty() {
        return state.isEmpty();
    }
}
