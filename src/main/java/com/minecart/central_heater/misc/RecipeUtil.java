package com.minecart.central_heater.misc;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class RecipeUtil {
    public static <T extends AbstractCookingRecipe> int getCookTime(Level level, RecipeType<T> type, ItemStack stack){
        int count = stack.getCount();
        RecipeManager manager = level.getRecipeManager();
        Optional<RecipeHolder<T>> recipeHolder = manager.getRecipeFor(type, new SingleRecipeInput(stack), level);
        if(!recipeHolder.isPresent())
            return 0;
        T recipe = recipeHolder.get().value();
        return recipe.getCookingTime() * count;
    }

    public static <T extends AbstractCookingRecipe> int getCookTime(RecipeType<T> type, ItemStack stack){
        return getCookTime(VirtualLevel.getLevel(), type, stack);
    }

    public static <T extends AbstractCookingRecipe> int getCookTime(Level level, RecipeType<T> type, ItemStack stack, float multiplier){
        return (int) Math.floor(getCookTime(level, type, stack) * multiplier);
    }

    public static <T extends AbstractCookingRecipe> int getCookTime(RecipeType<T> type, ItemStack stack, float multiplier){
        return (int) Math.floor(getCookTime(type, stack) * multiplier);
    }

    public static <T extends AbstractCookingRecipe> ItemStack getCookResult(Level level, RecipeType<T> recipeType, ItemStack stack){
        RecipeManager manager = level.getRecipeManager();
        Optional<RecipeHolder<T>> holder = manager.getRecipeFor(recipeType, new SingleRecipeInput(stack), level);
        if(!holder.isPresent())
            return ItemStack.EMPTY;
        T recipe = holder.get().value();
        ItemStack result = recipe.getResultItem(level.registryAccess()).copy();
        return result;
    }

    public static <T extends AbstractCookingRecipe> ItemStack getCookResult(RecipeType<T> recipeType, ItemStack stack){
        return getCookResult(VirtualLevel.getLevel(), recipeType, stack);
    }

    public static <T extends AbstractCookingRecipe> Optional<RecipeHolder<T>> getCookRecipe(Level level, RecipeType<T> recipeType, ItemStack stack){
        RecipeManager manager = level.getRecipeManager();
        return manager.getRecipeFor(recipeType, new SingleRecipeInput(stack), level);
    }

    public static <T extends AbstractCookingRecipe> Optional<RecipeHolder<T>> getCookRecipe(RecipeType<T> recipeType, ItemStack stack){
        return getCookRecipe(VirtualLevel.getLevel(), recipeType, stack);
    }
}
