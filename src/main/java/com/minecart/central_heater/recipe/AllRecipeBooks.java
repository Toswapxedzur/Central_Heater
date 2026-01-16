package com.minecart.central_heater.recipe;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.inventory.RecipeBookType;

public class AllRecipeBooks {
    public static RecipeBookType getBlazingType() {
        return RecipeBookType.valueOf("CENTRAL_HEATER_BLAZING");
    }

    public static RecipeBookCategories getBlazingSearch() {
        return RecipeBookCategories.valueOf("CENTRAL_HEATER_BLAZING_SEARCH");
    }

    public static RecipeBookCategories getBlazingMisc() {
        return RecipeBookCategories.valueOf("CENTRAL_HEATER_BLAZING_MISC");
    }
}
