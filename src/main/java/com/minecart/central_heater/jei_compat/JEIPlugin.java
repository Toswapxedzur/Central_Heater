package com.minecart.central_heater.jei_compat;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.AllRecipe;
import com.minecart.central_heater.Central_heater;
import com.minecart.central_heater.recipe.SeethingRecipe;
import com.minecart.central_heater.recipe.SmolderingRecipe;
import com.minecart.central_heater.util.VirtualLevel;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

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
        registration.addRecipeCategories(new NetherFuelCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IVanillaRecipeFactory vanillaFactory = registration.getVanillaRecipeFactory();
        IIngredientManager ingredientManager = registration.getIngredientManager();

        List<RecipeHolder<SeethingRecipe>> seething = VirtualLevel.getRecipeManager().getAllRecipesFor(AllRecipe.SEETHING.get());
        registration.addRecipes(SeethingRecipeCategory.RECIPE_TYPE, seething);

        List<RecipeHolder<SmolderingRecipe>> smoldering = VirtualLevel.getRecipeManager().getAllRecipesFor(AllRecipe.SMOLDERING.get());
        registration.addRecipes(SmolderingRecipeCategory.RECIPE_TYPE, smoldering);
        registration.addRecipes(SmolderingRecipeCategory.RECIPE_TYPE, JEIUtil.fireBrewingSmolderingRecipe());

        registration.addRecipes(NetherFuelCategory.RECIPE_TYPE, JEIUtil.getNetherFuelRecipes(ingredientManager));

        registration.addRecipes(RecipeTypes.ANVIL, JEIUtil.getAllAnvilRecipes(vanillaFactory, ingredientManager));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(AllBlockItem.brick_stove, RecipeTypes.CAMPFIRE_COOKING);
        registration.addRecipeCatalyst(AllBlockItem.mud_brick_stove, RecipeTypes.CAMPFIRE_COOKING);

        registration.addRecipeCatalyst(AllBlockItem.stone_stove, RecipeTypes.SMELTING);
        registration.addRecipeCatalyst(AllBlockItem.deepslate_stove, RecipeTypes.SMELTING);

        registration.addRecipeCatalyst(AllBlockItem.nether_brick_stove, SeethingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.red_nether_brick_stove, SeethingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.blackstone_stove, SeethingRecipeCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(AllBlockItem.mud_brick_pot, SmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.brick_cauldron, SmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.iron_cauldron, SmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.golden_cauldron, SmolderingRecipeCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(AllBlockItem.golden_cauldron, NetherFuelCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.nether_brick_stove, NetherFuelCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.red_nether_brick_stove, NetherFuelCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.blackstone_stove, NetherFuelCategory.RECIPE_TYPE);
    }
}
