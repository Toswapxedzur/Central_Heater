package com.minecart.central_heater.jei_compat.category;

import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.recipe.recipe_types.HauntingRecipe;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;

public class HauntingRecipeCategory extends AbstractCookingCategory<HauntingRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(CentralHeater.MODID, "haunting");
    public static final RecipeType<RecipeHolder<HauntingRecipe>> RECIPE_TYPE = RecipeType.createRecipeHolderType(UID);

    public HauntingRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, RECIPE_TYPE, Blocks.SOUL_CAMPFIRE, "jei.central_heater.category.haunting", 200);
    }
}
