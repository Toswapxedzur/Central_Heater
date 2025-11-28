package com.minecart.central_heater.recipe;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.AllRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public class SeethingRecipe extends AbstractCookingRecipe{
//    RecipeSerializer<SeethingRecipe> SEETHING_RECIPE = RecipeSerializer.register("seething", new SimpleCookingSerializer<>(SeethingRecipe::new, 400));

    public SeethingRecipe(String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        super(AllRecipe.SEETHING.get(), group, category, ingredient, result, experience, cookingTime);
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(AllBlockItem.stone_stove.asItem());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AllRecipe.SEETHING_RECIPE_SERIALIZER.get();
    }
}
