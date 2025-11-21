package com.minecart.central_heater.compat;

import com.minecart.central_heater.AllRegistry;
import com.minecart.central_heater.Central_heater;
import com.minecart.central_heater.recipe.SeethingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Central_heater.MODID, "jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new SeethingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<RecipeHolder<SeethingRecipe>> list = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(AllRegistry.SEETHING.get());
        registration.addRecipes(SeethingRecipeCategory.RECIPE_TYPE, list);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(AllRegistry.brick_stove, RecipeTypes.CAMPFIRE_COOKING);
        registration.addRecipeCatalyst(AllRegistry.mud_brick_stove, RecipeTypes.CAMPFIRE_COOKING);

        registration.addRecipeCatalyst(AllRegistry.stone_stove, RecipeTypes.SMELTING);
        registration.addRecipeCatalyst(AllRegistry.deepslate_stove, RecipeTypes.SMELTING);

        registration.addRecipeCatalyst(AllRegistry.nether_brick_stove, SeethingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllRegistry.red_nether_brick_stove, SeethingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(Blocks.SOUL_CAMPFIRE, SeethingRecipeCategory.RECIPE_TYPE);
    }
}
