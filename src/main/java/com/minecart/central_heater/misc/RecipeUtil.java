package com.minecart.central_heater.misc;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class RecipeUtil {
    public static <T extends AbstractCookingRecipe> int getCookTime(Level level, RecipeType<T> type, ItemStack stack){
        int count = stack.getCount();
        RecipeManager manager = level.getRecipeManager();
        // 1.20.1 uses SimpleContainer for inventory checks
        Optional<T> recipe = manager.getRecipeFor(type, new SimpleContainer(stack), level);
        if(!recipe.isPresent())
            return 0;

        return recipe.get().getCookingTime() * count;
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
        Optional<T> recipe = manager.getRecipeFor(recipeType, new SimpleContainer(stack), level);
        if(!recipe.isPresent())
            return ItemStack.EMPTY;

        ItemStack result = recipe.get().getResultItem(level.registryAccess()).copy();
        return result;
    }

    public static <T extends AbstractCookingRecipe> ItemStack getCookResult(RecipeType<T> recipeType, ItemStack stack){
        return getCookResult(VirtualLevel.getLevel(), recipeType, stack);
    }

    // Return type changed from RecipeHolder<T> to T
    public static <T extends AbstractCookingRecipe> Optional<T> getCookRecipe(Level level, RecipeType<T> recipeType, ItemStack stack){
        RecipeManager manager = level.getRecipeManager();
        return manager.getRecipeFor(recipeType, new SimpleContainer(stack), level);
    }

    public static <T extends AbstractCookingRecipe> Optional<T> getCookRecipe(RecipeType<T> recipeType, ItemStack stack){
        return getCookRecipe(VirtualLevel.getLevel(), recipeType, stack);
    }
}