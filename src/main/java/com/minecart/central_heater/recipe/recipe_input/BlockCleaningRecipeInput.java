package com.minecart.central_heater.recipe.recipe_input;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.Block;

public record BlockCleaningRecipeInput(Block block) implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        return block.asItem().getDefaultInstance();
    }

    @Override
    public int size() {
        return 1;
    }
}
