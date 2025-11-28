package com.minecart.central_heater.data_generation;

import com.minecart.central_heater.AllRecipe;
import com.minecart.central_heater.recipe.SeethingRecipe;
import com.minecart.central_heater.recipe.SmolderingRecipeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    protected static void emptyRecipe(RecipeOutput output, String id){
        emptyRecipe(output, ResourceLocation.parse(id));
    }

    protected static void emptyRecipe(RecipeOutput output, ResourceLocation id){
//        dummy recipe
        SimpleCookingRecipeBuilder.generic(Ingredient.of(Items.BARRIER), RecipeCategory.MISC, Items.BARRIER,
                0f, 0, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new).group("ungroupable").unlockedBy(getHasName(Items.BARRIER), has(Items.BARRIER)).save(output, id);
    }

    protected static void oreSeething(
            RecipeOutput recipeOutput, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group){
        oreCooking(
                recipeOutput,
                AllRecipe.SEETHING_RECIPE_SERIALIZER.get(),
                SeethingRecipe::new,
                ingredients,
                category,
                result,
                experience,
                cookingTime,
                group,
                "_from_seething"
        );
    }

    protected static void smoldering(RecipeOutput output, NonNullList<Ingredient> ingredients, FluidStack fluidIngredient, ItemStack result, FluidStack fluidResult, int time, int tier, int fireLevel){
        RecipeBuilder builder = SmolderingRecipeBuilder.create(ingredients, fluidIngredient, result, fluidResult,time, tier,fireLevel).unlockedBy(getHasName(Items.CAULDRON), has(Items.CAULDRON));
        if(result.isEmpty()){
            builder.save(output, getFluidName(fluidResult.getFluid()) + "_smoldering_with_flame_level_" + fireLevel);
        }else{
            builder.save(output, getItemName(result.getItem()) + "_smoldering_with_flame_level_" + fireLevel);
        }
    }

    protected static String getFluidName(Fluid fluid) {
        return BuiltInRegistries.FLUID.getKey(fluid).getPath();
    }
}
