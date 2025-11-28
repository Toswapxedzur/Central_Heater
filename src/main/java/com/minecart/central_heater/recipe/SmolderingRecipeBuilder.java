package com.minecart.central_heater.recipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class SmolderingRecipeBuilder implements RecipeBuilder {
    protected final NonNullList<Ingredient> ingredients;
    protected final FluidStack fluidIngredient;
    protected final ItemStack result;
    protected final FluidStack fluidResult;
    protected final int time;
    protected final int tier;
    protected final int fireLevel;

    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;

    private SmolderingRecipeBuilder(NonNullList<Ingredient> ingredients, FluidStack fluidIngredient, ItemStack result, FluidStack fluidResult,int time, int tier, int fireLevel){
        this.ingredients = ingredients;
        this.fluidIngredient = fluidIngredient;
        this.result = result;
        this.fluidResult = fluidResult;
        this.time = time;
        this.tier = tier;
        this.fireLevel = fireLevel;
    }

    public static SmolderingRecipeBuilder create(NonNullList<Ingredient> ingredients, FluidStack fluidIngredient, ItemStack result, FluidStack fluidResult,int time, int tier,int fireLevel){
        return new SmolderingRecipeBuilder(ingredients, fluidIngredient, result, fluidResult,time, tier,fireLevel);
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String groupName) {
        this.group = groupName;
        return this;
    }

    @Override
    public Item getResult() {
        return result.getItem();
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        Advancement.Builder advancement$builder = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement$builder::addCriterion);
        SmolderingRecipe recipe = new SmolderingRecipe(ingredients, fluidIngredient, result, fluidResult,time, tier,fireLevel);
        recipeOutput.accept(id, recipe, advancement$builder.build(id.withPrefix("recipes")));
    }
}
