package com.minecart.central_heater.data_generation;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.AllRecipe;
import com.minecart.central_heater.Central_heater;
import com.minecart.central_heater.recipe.SeethingRecipe;
import com.minecart.central_heater.recipe.SmolderingRecipe;
import com.minecart.central_heater.recipe.SmolderingRecipeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HeaterRecipeProvider extends RecipeProvider {
    public HeaterRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    protected static void brickTileRecipe(RecipeOutput output, Item result, Item ingredient){
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 2).pattern("###").pattern("###").pattern("###")
                .define('#', Ingredient.of(ingredient)).group("building").unlockedBy(getHasName(ingredient), has(ingredient))
                .showNotification(true).save(output);
    }

    protected static void brickRecipe(RecipeOutput output, Item result, Item ingredient){
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result).pattern("##").pattern("##")
                .define('#', Ingredient.of(ingredient)).group("building").unlockedBy(getHasName(ingredient), has(ingredient))
                .showNotification(true).save(output);
    }

    protected static void stairSlabWallCraftingStoneCuttingRecipe(RecipeOutput output, Item ingredient, Item stair, Item slab, Item wall){
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, stair, ingredient);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, slab, ingredient, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, wall, ingredient);
        stairBuilder(stair, Ingredient.of(ingredient)).unlockedBy(getHasName(ingredient), has(ingredient)).save(output);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, slab, Ingredient.of(ingredient)).unlockedBy(getHasName(ingredient), has(ingredient)).save(output);
        wallBuilder(RecipeCategory.BUILDING_BLOCKS, wall, Ingredient.of(ingredient)).unlockedBy(getHasName(ingredient), has(ingredient)).save(output);
    }

    protected static void stoveCraftingRecipeBuilder(RecipeOutput output, ItemLike stove, ItemLike baseBrick, ItemLike ingot, ItemLike bars){
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, stove).pattern("*&*").pattern("# #").pattern("###")
                .define('#', baseBrick).define('*', ingot).define('&', bars).unlockedBy(getHasName(stove), has(stove)).group("misc").save(output);
    }

    protected static void potRecipe(RecipeOutput output, ItemLike pot, ItemLike item){
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, pot).pattern("# #").pattern("###").pattern("* *")
                .define('#', item).define('*', AllBlockItem.mud_brick).unlockedBy(getHasName(item), has(item)).save(output);
    }

    protected static void burntRecipe(RecipeOutput output, ItemLike raw, ItemLike cooked, ItemLike burnt){
        emptyRecipe(output, ResourceLocation.withDefaultNamespace(getItemName(cooked)));
        oreSmelting(output, List.of(raw, cooked), RecipeCategory.FOOD, burnt, 0.1f, 200, "burnt");
        oreSmoking(output, List.of(cooked), RecipeCategory.FOOD, burnt, 0.1f, 100, "burnt");
        oreCampfiring(output, List.of(cooked), RecipeCategory.FOOD, burnt, 0.1f, 600, "burnt");
    }

    protected static void pickaxeItem(RecipeOutput output, ItemLike result, ItemLike ingredient){
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result).pattern("###").pattern(" * ").pattern(" * ")
                .define('#', ingredient).define('*', Items.STICK).unlockedBy(getHasName(ingredient), has(ingredient)).save(output);
    }

    protected static void axeItem(RecipeOutput output, ItemLike result, ItemLike ingredient){
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result).pattern("##").pattern("#*").pattern(" *")
                .define('#', ingredient).define('*', Items.STICK).unlockedBy(getHasName(ingredient), has(ingredient)).save(output);
    }

    protected static void shovelItem(RecipeOutput output, ItemLike result, ItemLike ingredient){
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result).pattern("#").pattern("*").pattern("*")
                .define('#', ingredient).define('*', Items.STICK).unlockedBy(getHasName(ingredient), has(ingredient)).save(output);
    }

    protected static void hoeItem(RecipeOutput output, ItemLike result, ItemLike ingredient){
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result).pattern("##").pattern(" *").pattern(" *")
                .define('#', ingredient).define('*', Items.STICK).unlockedBy(getHasName(ingredient), has(ingredient)).save(output);
    }

    protected static void swordItem(RecipeOutput output, ItemLike result, ItemLike ingredient){
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result).pattern("#").pattern("#").pattern("*")
                .define('#', ingredient).define('*', Items.STICK).unlockedBy(getHasName(ingredient), has(ingredient)).save(output);
    }

    protected static void helmetItem(RecipeOutput output, ItemLike result, ItemLike ingredient){
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result).pattern("###").pattern("# #")
                .define('#', ingredient).unlockedBy(getHasName(ingredient), has(ingredient)).save(output);
    }

    protected static void chestplateItem(RecipeOutput output, ItemLike result, ItemLike ingredient){
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result).pattern("# #").pattern("###").pattern("###")
                .define('#', ingredient).unlockedBy(getHasName(ingredient), has(ingredient)).save(output);
    }

    protected static void leggingsItem(RecipeOutput output, ItemLike result, ItemLike ingredient){
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result).pattern("###").pattern("# #").pattern("# #")
                .define('#', ingredient).unlockedBy(getHasName(ingredient), has(ingredient)).save(output);
    }

    protected static void bootsItem(RecipeOutput output, ItemLike result, ItemLike ingredient){
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result).pattern("# #").pattern("# #")
                .define('#', ingredient).unlockedBy(getHasName(ingredient), has(ingredient)).save(output);
    }

    protected static void toolsBundle(RecipeOutput output, ItemLike ingredient, ItemLike pickaxe, ItemLike axe, ItemLike shovel, ItemLike hoe, ItemLike sword){
        pickaxeItem(output, pickaxe, ingredient);
        axeItem(output, axe, ingredient);
        shovelItem(output, shovel, ingredient);
        hoeItem(output, hoe, ingredient);
        swordItem(output, sword, ingredient);
    }

    protected static void armorsBundle(RecipeOutput output, ItemLike ingredient, ItemLike helmet, ItemLike chestplate, ItemLike leggings, ItemLike boots){
        helmetItem(output, helmet, ingredient);
        chestplateItem(output, chestplate, ingredient);
        leggingsItem(output, leggings, ingredient);
        bootsItem(output, boots, ingredient);
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

    protected static void oreSmoking(RecipeOutput recipeOutput, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group) {
        oreCooking(recipeOutput, RecipeSerializer.SMOKING_RECIPE, SmokingRecipe::new, ingredients, category, result, experience, cookingTime, group, "_from_smoking");
    }

    protected static void oreCampfiring(RecipeOutput recipeOutput, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group) {
        oreCooking(recipeOutput, RecipeSerializer.CAMPFIRE_COOKING_RECIPE, CampfireCookingRecipe::new, ingredients, category, result, experience, cookingTime, group, "_from_campfire_cooking");
    }

    protected static void smoldering(RecipeOutput output, NonNullList<Ingredient> ingredients, FluidStack fluidIngredient, NonNullList<ItemStack> result, FluidStack fluidResult, int time, int tier, int fireLevel){
        RecipeBuilder builder = SmolderingRecipeBuilder.create(ingredients, fluidIngredient, result, fluidResult,time, tier,fireLevel).unlockedBy(getHasName(Items.CAULDRON), has(Items.CAULDRON));
        StringBuilder name = new StringBuilder();
        name.append("recipe_with_");
        for(ItemStack stack : result)
            name.append(getItemName(stack.getItem()) + "_");
        if(!fluidResult.isEmpty())
            name.append(getFluidName(fluidResult.getFluid()) + "_");
        name.append("time_" + time);
        name.append("_tier_" + tier);
        name.append("_with_flame_level_" + fireLevel);
        builder.save(output, name.toString());
    }

    protected static String getFluidName(Fluid fluid) {
        return BuiltInRegistries.FLUID.getKey(fluid).getPath();
    }
}
