package com.minecart.central_heater.recipe.recipe_types;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.recipe.AllRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public class HauntingRecipe extends AbstractCookingRecipe{
    public HauntingRecipe(String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        super(AllRecipe.HAUNTING.get(), group, category, ingredient, result, experience, cookingTime);
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(AllBlockItem.BLACKSTONE_STOVE.asItem());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AllRecipe.HAUNTING_RECIPE_SERIALIZER.get();
    }
}
