package com.minecart.central_heater.recipe.builder;

import com.minecart.central_heater.recipe.recipe_types.BlockCleaningRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.LinkedHashMap;
import java.util.Map;

public class BlockCleaningRecipeBuilder implements RecipeBuilder {
    private final Block input;
    private final Block output;
    private final Item dyeItem;
    private final int count; // Added count variable
    private final float dropChance;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    private String group;

    private BlockCleaningRecipeBuilder(Block input, Block output, Item dyeItem, int count, float dropChance) {
        this.input = input;
        this.output = output;
        this.dyeItem = dyeItem;
        this.count = count;
        this.dropChance = dropChance;
    }

    // New factory method that accepts a count
    public static BlockCleaningRecipeBuilder cleaning(Block input, Block output, Item dyeItem, int count, float dropChance) {
        return new BlockCleaningRecipeBuilder(input, output, dyeItem, count, dropChance);
    }

    // Original factory method (defaults to 1 so older recipes don't break)
    public static BlockCleaningRecipeBuilder cleaning(Block input, Block output, Item dyeItem, float dropChance) {
        return new BlockCleaningRecipeBuilder(input, output, dyeItem, 1, dropChance);
    }

    @Override
    public BlockCleaningRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public BlockCleaningRecipeBuilder group(String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getResult() {
        return this.output.asItem();
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        this.ensureValid(id);

        Advancement.Builder advancementBuilder = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);

        this.criteria.forEach(advancementBuilder::addCriterion);

        // Pass the count into the new ItemStack
        BlockCleaningRecipe recipe = new BlockCleaningRecipe(this.input, this.output, new ItemStack(this.dyeItem, this.count), this.dropChance);

        recipeOutput.accept(id, recipe, advancementBuilder.build(id.withPrefix("recipes/block_cleaning/")));
    }

    public void save(RecipeOutput recipeOutput, String id) {
        this.save(recipeOutput, ResourceLocation.parse(id));
    }

    private void ensureValid(ResourceLocation id) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id + ". Ensure you call .unlockedBy()");
        }
    }
}
