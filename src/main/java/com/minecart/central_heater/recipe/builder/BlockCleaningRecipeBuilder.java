package com.minecart.central_heater.recipe.builder;

import com.google.gson.JsonObject;
import com.minecart.central_heater.recipe.AllRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 1.20.1 port of the BlockCleaningRecipeBuilder. Uses Forge's 1.20.1 recipe
 * data-gen API (Consumer&lt;FinishedRecipe&gt; instead of NeoForge's RecipeOutput).
 */
public class BlockCleaningRecipeBuilder implements RecipeBuilder {
    private final Block input;
    private final Block output;
    private final Item dyeItem;
    private final int count;
    private final float dropChance;
    private final Map<String, CriterionTriggerInstance> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;

    private BlockCleaningRecipeBuilder(Block input, Block output, Item dyeItem, int count, float dropChance) {
        this.input = input;
        this.output = output;
        this.dyeItem = dyeItem;
        this.count = count;
        this.dropChance = dropChance;
    }

    public static BlockCleaningRecipeBuilder cleaning(Block input, Block output, Item dyeItem, int count, float dropChance) {
        return new BlockCleaningRecipeBuilder(input, output, dyeItem, count, dropChance);
    }

    public static BlockCleaningRecipeBuilder cleaning(Block input, Block output, Item dyeItem, float dropChance) {
        return new BlockCleaningRecipeBuilder(input, output, dyeItem, 1, dropChance);
    }

    @Override
    public BlockCleaningRecipeBuilder unlockedBy(String name, CriterionTriggerInstance criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public BlockCleaningRecipeBuilder group(@Nullable String groupName) {
        this.group = groupName;
        return this;
    }

    @Override
    public Item getResult() {
        return this.output.asItem();
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        this.ensureValid(id);

        Advancement.Builder advancementBuilder = Advancement.Builder.advancement()
                .parent(new ResourceLocation("recipes/root"))
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(RequirementsStrategy.OR);

        this.criteria.forEach(advancementBuilder::addCriterion);

        consumer.accept(new Result(
                id, this.input, this.output, new ItemStack(this.dyeItem, this.count),
                this.dropChance, advancementBuilder,
                new ResourceLocation(id.getNamespace(), "recipes/block_cleaning/" + id.getPath())
        ));
    }

    public void save(Consumer<FinishedRecipe> consumer, String id) {
        this.save(consumer, new ResourceLocation(id));
    }

    private void ensureValid(ResourceLocation id) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Block input;
        private final Block output;
        private final ItemStack itemOutput;
        private final float dropChance;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, Block input, Block output, ItemStack itemOutput, float dropChance,
                      Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.input = input;
            this.output = output;
            this.itemOutput = itemOutput;
            this.dropChance = dropChance;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            // Matches BlockCleaningRecipe.Serializer.fromJson:
            // input/output are block registry IDs; itemOutput is a JSON object;
            // dropChance is a float.
            json.addProperty("input", ForgeRegistries.BLOCKS.getKey(input).toString());
            json.addProperty("output", ForgeRegistries.BLOCKS.getKey(output).toString());

            JsonObject itemJson = new JsonObject();
            itemJson.addProperty("item", ForgeRegistries.ITEMS.getKey(itemOutput.getItem()).toString());
            if (itemOutput.getCount() > 1) {
                itemJson.addProperty("count", itemOutput.getCount());
            }
            json.add("itemOutput", itemJson);

            json.addProperty("dropChance", dropChance);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return AllRecipe.BLOCK_CLEANING_RECIPE_SERIALIZER.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return advancement.serializeToJson();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return advancementId;
        }
    }
}
