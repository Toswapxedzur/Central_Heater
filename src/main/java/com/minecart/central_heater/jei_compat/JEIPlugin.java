package com.minecart.central_heater.jei_compat;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.jei_compat.category.*;
import com.minecart.central_heater.jei_compat.misc.JEIUtil;
import com.minecart.central_heater.misc.VirtualLevel;
import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.recipe.recipe_types.HauntingRecipe;
import com.minecart.central_heater.recipe.recipe_types.SmolderingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(CentralHeater.MODID, "jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new HauntingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new SmolderingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new BlockSmolderingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new NetherFuelCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new FireAshDropChanceCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ScorchedDustDropChanceCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IVanillaRecipeFactory vanillaFactory = registration.getVanillaRecipeFactory();
        IIngredientManager ingredientManager = registration.getIngredientManager();

        List<HauntingRecipe> haunting = VirtualLevel.getRecipeManager().getAllRecipesFor(AllRecipe.HAUNTING.get());
        registration.addRecipes(HauntingRecipeCategory.RECIPE_TYPE, haunting);

        List<SmolderingRecipe> smoldering = VirtualLevel.getRecipeManager().getAllRecipesFor(AllRecipe.SMOLDERING.get());
        registration.addRecipes(SmolderingRecipeCategory.RECIPE_TYPE, smoldering);

        registration.addRecipes(SmolderingRecipeCategory.RECIPE_TYPE, JEIUtil.fireBrewingSmolderingRecipe());
        registration.addRecipes(BlockSmolderingRecipeCategory.RECIPE_TYPE, JEIUtil.getBlocksSmolderingRecipes());

        registration.addRecipes(NetherFuelCategory.RECIPE_TYPE, JEIUtil.getNetherFuelRecipes(ingredientManager));
        registration.addRecipes(FireAshDropChanceCategory.RECIPE_TYPE, JEIUtil.getFireAshDropChanceRecipes(ingredientManager));
        registration.addRecipes(ScorchedDustDropChanceCategory.RECIPE_TYPE, JEIUtil.getScorchedDustDropChanceRecipes(ingredientManager));

        registration.addRecipes(RecipeTypes.ANVIL, JEIUtil.getAllAnvilRecipes(vanillaFactory, ingredientManager));

        // [Port] Create ItemStacks explicitly
        registration.addItemStackInfo(List.of(new ItemStack(AllBlockItem.BURNT_WOOD.get()), new ItemStack(AllBlockItem.BURNT_LOG.get())), Component.translatable("jei.central_heater.burnt_woods.description"));
        registration.addItemStackInfo(new ItemStack(AllBlockItem.BLAZING_FURNACE.get()), Component.translatable("jei.central_heater.blazing_furnace.description"));
        registration.addItemStackInfo(new ItemStack(AllBlockItem.BLAZING_FURNACE_MINECART.get()), Component.translatable("jei.central_heater.blazing_furnace_minecart.description"));
        registration.addItemStackInfo(new ItemStack(AllBlockItem.STURDY_BRICK.get()), Component.translatable("jei.central_heater.sturdy_brick.description"));
        registration.addItemStackInfo(new ItemStack(AllBlockItem.STURDY_TANK.get()), Component.translatable("jei.central_heater.sturdy_tank.description"));
        registration.addItemStackInfo(new ItemStack(AllBlockItem.FIRE_ASH.get()), Component.translatable("jei.central_heater.fire_ash.description"));
        registration.addItemStackInfo(new ItemStack(AllBlockItem.SCORCHED_DUST.get()), Component.translatable("jei.central_heater.scorched_dust.description"));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        // [Port] Wrap RegistryObjects in new ItemStack(...)

        registration.addRecipeCatalyst(new ItemStack(AllBlockItem.BRICK_STOVE.get()), RecipeTypes.CAMPFIRE_COOKING, BlockSmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(AllBlockItem.MUD_BRICK_STOVE.get()), RecipeTypes.CAMPFIRE_COOKING, BlockSmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(AllBlockItem.STONE_STOVE.get()), RecipeTypes.CAMPFIRE_COOKING, BlockSmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(AllBlockItem.DEEPSLATE_STOVE.get()), RecipeTypes.CAMPFIRE_COOKING, BlockSmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(AllBlockItem.NETHER_BRICK_STOVE.get()), RecipeTypes.CAMPFIRE_COOKING, BlockSmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(AllBlockItem.RED_NETHER_BRICK_STOVE.get()), RecipeTypes.CAMPFIRE_COOKING, BlockSmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(AllBlockItem.BLACKSTONE_STOVE.get()), RecipeTypes.CAMPFIRE_COOKING, BlockSmolderingRecipeCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(new ItemStack(AllBlockItem.STONE_STOVE.get()), RecipeTypes.SMELTING, BlockSmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(AllBlockItem.DEEPSLATE_STOVE.get()), RecipeTypes.SMELTING, BlockSmolderingRecipeCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(new ItemStack(AllBlockItem.NETHER_BRICK_STOVE.get()), HauntingRecipeCategory.RECIPE_TYPE, BlockSmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(AllBlockItem.RED_NETHER_BRICK_STOVE.get()), HauntingRecipeCategory.RECIPE_TYPE, BlockSmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(AllBlockItem.BLACKSTONE_STOVE.get()), HauntingRecipeCategory.RECIPE_TYPE, BlockSmolderingRecipeCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(new ItemStack(AllBlockItem.MUD_BRICK_POT.get()), SmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(AllBlockItem.BRICK_CAULDRON.get()), SmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(AllBlockItem.IRON_CAULDRON.get()), SmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(AllBlockItem.GOLDEN_CAULDRON.get()), SmolderingRecipeCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(new ItemStack(AllBlockItem.GOLDEN_CAULDRON.get()), NetherFuelCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(AllBlockItem.NETHER_BRICK_STOVE.get()), NetherFuelCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(AllBlockItem.RED_NETHER_BRICK_STOVE.get()), NetherFuelCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(AllBlockItem.BLACKSTONE_STOVE.get()), NetherFuelCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(new ItemStack(AllBlockItem.FIRE_ASH.get()), FireAshDropChanceCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(AllBlockItem.SCORCHED_DUST.get()), ScorchedDustDropChanceCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(new ItemStack(AllBlockItem.BLAZING_FURNACE.get()), HauntingRecipeCategory.RECIPE_TYPE);
    }
}