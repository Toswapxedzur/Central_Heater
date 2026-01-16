package com.minecart.central_heater.recipe.builder;

import com.minecart.central_heater.recipe.recipe_types.BlockSmolderingRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class BlockSmolderingRecipeBuilder implements RecipeBuilder {
    protected final BlockState input;
    protected final BlockState output;
    protected final NonNullList<ItemStack> itemOutput;
    protected final int time;
    protected final boolean fireBurn;
    protected final int fireLevel;

    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;

    private BlockSmolderingRecipeBuilder(BlockState input, BlockState output, NonNullList<ItemStack> itemOutput, int time, int fireLevel, boolean fireBurn) {
        this.input = input;
        this.output = output;
        this.itemOutput = itemOutput;
        this.time = time;
        this.fireBurn = fireBurn;
        this.fireLevel = fireLevel;
    }

    /**
     * Standard creation method with full control using BlockStates
     */
    public static BlockSmolderingRecipeBuilder create(BlockState input, BlockState output, NonNullList<ItemStack> itemOutput, int time, int fireLevel, boolean fireBurn) {
        return new BlockSmolderingRecipeBuilder(input, output, itemOutput, time, fireLevel, fireBurn);
    }

    /**
     * Convenience method using simple Blocks (uses default states)
     */
    public static BlockSmolderingRecipeBuilder create(Block input, Block output, NonNullList<ItemStack> itemOutput, int time, int fireLevel, boolean fireBurn) {
        return new BlockSmolderingRecipeBuilder(input.defaultBlockState(), output.defaultBlockState(), itemOutput, time, fireLevel, fireBurn);
    }

    /**
     * Convenience method for recipes with NO item output (just block transformation)
     */
    public static BlockSmolderingRecipeBuilder create(Block input, Block output, int time, int fireLevel, boolean fireBurn) {
        return new BlockSmolderingRecipeBuilder(input.defaultBlockState(), output.defaultBlockState(), NonNullList.create(), time, fireLevel, fireBurn);
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
        return output.getBlock().asItem();
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        this.ensureValid(id);

        Advancement.Builder advancement$builder = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);

        this.criteria.forEach(advancement$builder::addCriterion);

        BlockSmolderingRecipe recipe = new BlockSmolderingRecipe(input, output, itemOutput, time, fireLevel, fireBurn);

        recipeOutput.accept(id, recipe, advancement$builder.build(id.withPrefix("recipes/")));
    }

    private void ensureValid(ResourceLocation id) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }
}
