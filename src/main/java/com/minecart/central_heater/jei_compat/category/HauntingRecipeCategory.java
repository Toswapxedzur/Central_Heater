package com.minecart.central_heater.jei_compat.category;

import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.recipe.recipe_types.HauntingRecipe;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;

public class HauntingRecipeCategory extends AbstractCookingCategory<HauntingRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(CentralHeater.MODID, "haunting");
    public static final RecipeType<HauntingRecipe> RECIPE_TYPE = RecipeType.create(CentralHeater.MODID, "haunting", HauntingRecipe.class);

    public HauntingRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, Blocks.SOUL_CAMPFIRE, "jei.central_heater.category.haunting", 200);
    }

    @Override
    public RecipeType<HauntingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }
}