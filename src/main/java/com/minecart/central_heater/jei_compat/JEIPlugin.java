package com.minecart.central_heater.jei_compat;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.AllRecipe;
import com.minecart.central_heater.Central_heater;
import com.minecart.central_heater.recipe.SeethingRecipe;
import com.minecart.central_heater.recipe.SmolderingRecipe;
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
        registration.addRecipeCategories(new SmolderingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<RecipeHolder<SeethingRecipe>> seething = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(AllRecipe.SEETHING.get());
        registration.addRecipes(SeethingRecipeCategory.RECIPE_TYPE, seething);
        List<RecipeHolder<SmolderingRecipe>> smoldering = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(AllRecipe.SMOLDERING.get());
        registration.addRecipes(SmolderingRecipeCategory.RECIPE_TYPE, smoldering);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(AllBlockItem.brick_stove, RecipeTypes.CAMPFIRE_COOKING);
        registration.addRecipeCatalyst(AllBlockItem.mud_brick_stove, RecipeTypes.CAMPFIRE_COOKING);

        registration.addRecipeCatalyst(AllBlockItem.stone_stove, RecipeTypes.SMELTING);
        registration.addRecipeCatalyst(AllBlockItem.deepslate_stove, RecipeTypes.SMELTING);

        registration.addRecipeCatalyst(AllBlockItem.nether_brick_stove, SeethingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.red_nether_brick_stove, SeethingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(Blocks.SOUL_CAMPFIRE, SeethingRecipeCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(AllBlockItem.mud_brick_pot, SmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.brick_pot, SmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.stone_pot, SmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.deepslate_pot, SmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.nether_brick_pot, SmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.red_nether_brick_pot, SmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.blackstone_pot, SmolderingRecipeCategory.RECIPE_TYPE);

    }
}
