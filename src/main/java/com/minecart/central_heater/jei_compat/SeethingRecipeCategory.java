package com.minecart.central_heater.jei_compat;

import com.minecart.central_heater.Central_heater;
import com.minecart.central_heater.recipe.SeethingRecipe;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;

public class SeethingRecipeCategory extends AbstractCookingCategory<SeethingRecipe>{
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(Central_heater.MODID, "seething");
    public static final RecipeType<RecipeHolder<SeethingRecipe>> RECIPE_TYPE = RecipeType.createRecipeHolderType(UID);

    public SeethingRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, RECIPE_TYPE, Blocks.SOUL_CAMPFIRE, "jei.central_heater.category.seething", 200);
    }
}
