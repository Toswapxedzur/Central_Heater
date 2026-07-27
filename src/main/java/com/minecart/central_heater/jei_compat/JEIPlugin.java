package com.minecart.central_heater.jei_compat;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.jei_compat.category.*;
import com.minecart.central_heater.jei_compat.misc.JEIUtil;
import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.recipe.recipe_types.BlockCleaningRecipe;
import com.minecart.central_heater.recipe.recipe_types.HauntingRecipe;
import com.minecart.central_heater.recipe.recipe_types.SmolderingRecipe;
import com.minecart.central_heater.misc.VirtualLevel;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.library.plugins.vanilla.anvil.AnvilRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RepairItemRecipe;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(CentralHeater.MODID, "jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new HauntingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new SmolderingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new BlockSmolderingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new NetherFuelCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new FireAshDropChanceCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ScorchedDustDropChanceCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new BlockCleaningRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IVanillaRecipeFactory vanillaFactory = registration.getVanillaRecipeFactory();
        IIngredientManager ingredientManager = registration.getIngredientManager();

        List<RecipeHolder<HauntingRecipe>> seething = VirtualLevel.getRecipeManager().getAllRecipesFor(AllRecipe.HAUNTING.get());
        registration.addRecipes(HauntingRecipeCategory.RECIPE_TYPE, seething);

        List<RecipeHolder<SmolderingRecipe>> smoldering = VirtualLevel.getRecipeManager().getAllRecipesFor(AllRecipe.SMOLDERING.get());
        registration.addRecipes(SmolderingRecipeCategory.RECIPE_TYPE, smoldering);
        registration.addRecipes(SmolderingRecipeCategory.RECIPE_TYPE, JEIUtil.fireBrewingSmolderingRecipe());
        registration.addRecipes(BlockSmolderingRecipeCategory.RECIPE_TYPE, JEIUtil.getBlocksSmolderingRecipes());

        List<RecipeHolder<BlockCleaningRecipe>> cleaning = VirtualLevel.getRecipeManager().getAllRecipesFor(AllRecipe.BLOCK_CLEANING.get());
        registration.addRecipes(BlockCleaningRecipeCategory.RECIPE_TYPE, cleaning);

        registration.addRecipes(NetherFuelCategory.RECIPE_TYPE, JEIUtil.getNetherFuelRecipes(ingredientManager));
        registration.addRecipes(FireAshDropChanceCategory.RECIPE_TYPE, JEIUtil.getFireAshDropChanceRecipes(ingredientManager));
        registration.addRecipes(ScorchedDustDropChanceCategory.RECIPE_TYPE, JEIUtil.getScorchedDustDropChanceRecipes(ingredientManager));

        registration.addRecipes(RecipeTypes.ANVIL, JEIUtil.getAllAnvilRecipes(vanillaFactory, ingredientManager));

        registration.addItemStackInfo(List.of(AllBlockItem.BURNT_WOOD.toStack(), AllBlockItem.BURNT_LOG.toStack()), Component.translatable("jei.central_heater.burnt_woods.description"));
        registration.addItemStackInfo(AllBlockItem.BLAZING_FURNACE.toStack(), Component.translatable("jei.central_heater.blazing_furnace.description"));
        registration.addItemStackInfo(AllBlockItem.BLAZING_FURNACE_MINECART.toStack(), Component.translatable("jei.central_heater.blazing_furnace_minecart.description"));
        registration.addItemStackInfo(AllBlockItem.STURDY_BRICK.toStack(), Component.translatable("jei.central_heater.sturdy_brick.description"));
        registration.addItemStackInfo(AllBlockItem.STURDY_TANK.toStack(), Component.translatable("jei.central_heater.sturdy_tank.description"));
        registration.addItemStackInfo(AllBlockItem.FIRE_ASH.toStack(), Component.translatable("jei.central_heater.fire_ash.description"));
        registration.addItemStackInfo(AllBlockItem.SCORCHED_DUST.toStack(), Component.translatable("jei.central_heater.scorched_dust.description"));
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        List<ItemStack> hiddenBurntVariants = new ArrayList<>();
        for (AllBlockItem.BurntWoodSet set : AllBlockItem.BURNT_WOOD_SETS) {
            for (var block : set.blocks()) {
                ItemStack stack = block.toStack();
                if (!stack.is(AllBlockItem.BURNT_LOG.asItem())) {
                    hiddenBurntVariants.add(stack);
                }
            }
        }
        jeiRuntime.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, hiddenBurntVariants);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(AllBlockItem.BRICK_STOVE, RecipeTypes.CAMPFIRE_COOKING, BlockSmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.MUD_BRICK_STOVE, RecipeTypes.CAMPFIRE_COOKING, BlockSmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.STONE_STOVE, RecipeTypes.CAMPFIRE_COOKING, BlockSmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.DEEPSLATE_STOVE, RecipeTypes.CAMPFIRE_COOKING, BlockSmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.NETHER_BRICK_STOVE, RecipeTypes.CAMPFIRE_COOKING, BlockSmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.RED_NETHER_BRICK_STOVE, RecipeTypes.CAMPFIRE_COOKING, BlockSmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.BLACKSTONE_STOVE, RecipeTypes.CAMPFIRE_COOKING, BlockSmolderingRecipeCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(AllBlockItem.STONE_STOVE, RecipeTypes.SMELTING, BlockSmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.DEEPSLATE_STOVE, RecipeTypes.SMELTING, BlockSmolderingRecipeCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(AllBlockItem.NETHER_BRICK_STOVE, HauntingRecipeCategory.RECIPE_TYPE, BlockSmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.RED_NETHER_BRICK_STOVE, HauntingRecipeCategory.RECIPE_TYPE, BlockSmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.BLACKSTONE_STOVE, HauntingRecipeCategory.RECIPE_TYPE, BlockSmolderingRecipeCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(AllBlockItem.MUD_BRICK_POT, SmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.BRICK_CAULDRON, SmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.IRON_CAULDRON, SmolderingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.GOLDEN_CAULDRON, SmolderingRecipeCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(AllBlockItem.GOLDEN_CAULDRON, NetherFuelCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.NETHER_BRICK_STOVE, NetherFuelCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.RED_NETHER_BRICK_STOVE, NetherFuelCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.BLACKSTONE_STOVE, NetherFuelCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(AllBlockItem.FIRE_ASH, FireAshDropChanceCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AllBlockItem.SCORCHED_DUST, ScorchedDustDropChanceCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(AllBlockItem.BLAZING_FURNACE, HauntingRecipeCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(AllBlockItem.SOAP, BlockCleaningRecipeCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(AllBlockItem.STURDY_ANVIL, RecipeTypes.ANVIL);
    }
}
