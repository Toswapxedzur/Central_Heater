package com.minecart.central_heater.recipe.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.minecart.central_heater.recipe.AllRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class BlockSmolderingRecipeBuilder implements RecipeBuilder {
    protected final BlockState input;
    protected final BlockState output;
    protected final NonNullList<ItemStack> itemOutput;
    protected final int time;
    protected final boolean fireBurn;
    protected final int fireLevel;

    private final Map<String, CriterionTriggerInstance> criteria = new LinkedHashMap<>();
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
    public BlockSmolderingRecipeBuilder unlockedBy(String name, CriterionTriggerInstance criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public BlockSmolderingRecipeBuilder group(@Nullable String groupName) {
        this.group = groupName;
        return this;
    }

    @Override
    public Item getResult() {
        return output.getBlock().asItem();
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

        // In 1.20.1, we pass a FinishedRecipe object to the consumer
        consumer.accept(new Result(id, this.input, this.output, this.itemOutput, this.time, this.fireLevel, this.fireBurn, advancementBuilder, new ResourceLocation(id.getNamespace(), "recipes/" + id.getPath())));
    }

    private void ensureValid(ResourceLocation id) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    // Inner class to handle JSON serialization
    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final BlockState input;
        private final BlockState output;
        private final NonNullList<ItemStack> itemOutput;
        private final int time;
        private final int fireLevel;
        private final boolean fireBurn;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, BlockState input, BlockState output, NonNullList<ItemStack> itemOutput, int time, int fireLevel, boolean fireBurn, Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.input = input;
            this.output = output;
            this.itemOutput = itemOutput;
            this.time = time;
            this.fireLevel = fireLevel;
            this.fireBurn = fireBurn;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            // Note: Our Serializer reads BlockStates as Registry Name Strings (e.g. "minecraft:stone")
            json.addProperty("input", ForgeRegistries.BLOCKS.getKey(input.getBlock()).toString());
            json.addProperty("output", ForgeRegistries.BLOCKS.getKey(output.getBlock()).toString());

            if (!itemOutput.isEmpty()) {
                JsonArray array = new JsonArray();
                for (ItemStack stack : itemOutput) {
                    JsonObject stackJson = new JsonObject();
                    stackJson.addProperty("item", ForgeRegistries.ITEMS.getKey(stack.getItem()).toString());
                    if (stack.getCount() > 1) {
                        stackJson.addProperty("count", stack.getCount());
                    }
                    // Simple serializer doesn't handle NBT, add here if needed
                    array.add(stackJson);
                }
                json.add("itemOutput", array);
            }

            if (time != 200) json.addProperty("time", time);
            if (fireLevel != 1) json.addProperty("fireLevel", fireLevel);
            if (fireBurn) json.addProperty("fireBurn", true);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return AllRecipe.BLOCK_SMOLDERING_RECIPE_SERIALIZER.get();
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