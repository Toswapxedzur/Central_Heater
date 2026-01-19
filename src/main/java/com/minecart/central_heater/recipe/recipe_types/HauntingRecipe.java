package com.minecart.central_heater.recipe.recipe_types;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.recipe.AllRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class HauntingRecipe extends AbstractCookingRecipe {
    public HauntingRecipe(ResourceLocation id, String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        super(AllRecipe.HAUNTING.get(), id, group, category, ingredient, result, experience, cookingTime);
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(AllBlockItem.BLACKSTONE_STOVE.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AllRecipe.HAUNTING_RECIPE_SERIALIZER.get();
    }
}