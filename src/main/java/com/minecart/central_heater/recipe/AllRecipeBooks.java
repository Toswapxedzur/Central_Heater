package com.minecart.central_heater.recipe;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.Items;

public class AllRecipeBooks {
    public static final RecipeBookType BLAZING_FURNACE_TYPE =
            RecipeBookType.create("CENTRAL_HEATER_BLAZING");

    public static final RecipeBookCategories BLAZING_SEARCH = RecipeBookCategories.create(
            "CENTRAL_HEATER_BLAZING_SEARCH",
            Items.COMPASS.getDefaultInstance()
    );

    public static final RecipeBookCategories BLAZING_MISC = RecipeBookCategories.create(
            "CENTRAL_HEATER_BLAZING_MISC",
            Items.SOUL_CAMPFIRE.getDefaultInstance()
    );

    public static RecipeBookType getBlazingType() {
        return BLAZING_FURNACE_TYPE;
    }

    public static RecipeBookCategories getBlazingSearch() {
        return BLAZING_SEARCH;
    }

    public static RecipeBookCategories getBlazingMisc() {
        return BLAZING_MISC;
    }
}