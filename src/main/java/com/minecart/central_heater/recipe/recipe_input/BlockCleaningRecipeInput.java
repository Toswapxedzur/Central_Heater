package com.minecart.central_heater.recipe.recipe_input;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

/**
 * Lightweight container-style input that carries only a Block reference,
 * used by {@code BlockCleaningRecipe}. We implement {@code Container} here
 * because 1.20.1 recipes are parameterised by {@code Container} rather than
 * by the 1.21+ {@code RecipeInput} interface.
 */
public class BlockCleaningRecipeInput implements Container {
    private final Block block;

    public BlockCleaningRecipeInput(Block block) {
        this.block = block;
    }

    public Block block() {
        return block;
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return block.asItem() == null;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return block.asItem().getDefaultInstance();
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(@NotNull net.minecraft.world.entity.player.Player player) {
        return true;
    }

    @Override
    public void clearContent() {
    }
}
