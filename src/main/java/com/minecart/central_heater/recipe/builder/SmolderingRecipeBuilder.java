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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SmolderingRecipeBuilder implements RecipeBuilder {
    protected final NonNullList<Ingredient> ingredients;
    protected final FluidStack fluidIngredient;
    protected final NonNullList<ItemStack> result;
    protected final FluidStack fluidResult;
    protected final int time;
    protected final int tier;
    protected final int fireLevel;

    private final Map<String, CriterionTriggerInstance> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;

    private SmolderingRecipeBuilder(NonNullList<Ingredient> ingredients, FluidStack fluidIngredient, NonNullList<ItemStack> result, FluidStack fluidResult, int time, int tier, int fireLevel){
        this.ingredients = ingredients;
        this.fluidIngredient = fluidIngredient;
        this.result = result;
        this.fluidResult = fluidResult;
        this.time = time;
        this.tier = tier;
        this.fireLevel = fireLevel;
    }

    public static SmolderingRecipeBuilder create(NonNullList<Ingredient> ingredients, FluidStack fluidIngredient, NonNullList<ItemStack> result, FluidStack fluidResult, int time, int tier, int fireLevel){
        return new SmolderingRecipeBuilder(ingredients, fluidIngredient, result, fluidResult, time, tier, fireLevel);
    }

    @Override
    public SmolderingRecipeBuilder unlockedBy(String name, CriterionTriggerInstance criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public SmolderingRecipeBuilder group(@Nullable String groupName) {
        this.group = groupName;
        return this;
    }

    @Override
    public Item getResult() {
        return result.size() > 0 ? result.get(0).getItem() : ItemStack.EMPTY.getItem();
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

        consumer.accept(new Result(id, ingredients, fluidIngredient, result, fluidResult, time, tier, fireLevel, advancementBuilder, new ResourceLocation(id.getNamespace(), "recipes/" + id.getPath())));
    }

    private void ensureValid(ResourceLocation id) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    // --- Inner Class for JSON Generation ---
    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final NonNullList<Ingredient> ingredients;
        private final FluidStack fluidIngredient;
        private final NonNullList<ItemStack> result;
        private final FluidStack fluidResult;
        private final int time;
        private final int tier;
        private final int fireLevel;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, NonNullList<Ingredient> ingredients, FluidStack fluidIngredient, NonNullList<ItemStack> result, FluidStack fluidResult, int time, int tier, int fireLevel, Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.ingredients = ingredients;
            this.fluidIngredient = fluidIngredient;
            this.result = result;
            this.fluidResult = fluidResult;
            this.time = time;
            this.tier = tier;
            this.fireLevel = fireLevel;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            // Ingredients
            if (!ingredients.isEmpty()) {
                JsonArray ingArray = new JsonArray();
                for (Ingredient ing : ingredients) {
                    ingArray.add(ing.toJson());
                }
                json.add("ingredients", ingArray);
            }

            // Fluid Ingredient
            if (!fluidIngredient.isEmpty()) {
                json.add("fluidIngredient", serializeFluid(fluidIngredient));
            }

            // Item Results
            if (!result.isEmpty()) {
                JsonArray resArray = new JsonArray();
                for (ItemStack stack : result) {
                    resArray.add(serializeItemStack(stack));
                }
                json.add("result", resArray);
            }

            // Fluid Result
            if (!fluidResult.isEmpty()) {
                json.add("fluidResult", serializeFluid(fluidResult));
            }

            // Ints
            if (time != 400) json.addProperty("time", time);
            if (tier != 0) json.addProperty("tier", tier);
            if (fireLevel != 1) json.addProperty("fireLevel", fireLevel);
        }

        // Helper: Serialize FluidStack to JSON (matches SmolderingRecipe.Serializer logic)
        private JsonObject serializeFluid(FluidStack stack) {
            JsonObject json = new JsonObject();
            json.addProperty("fluid", ForgeRegistries.FLUIDS.getKey(stack.getFluid()).toString());
            json.addProperty("amount", stack.getAmount());
            if (stack.hasTag()) {
                // Note: Simple JSON generation usually avoids complex NBT,
                // but if needed, you'd serialize the compound tag here.
                // json.addProperty("nbt", stack.getTag().toString());
            }
            return json;
        }

        // Helper: Serialize ItemStack result to JSON
        private JsonObject serializeItemStack(ItemStack stack) {
            JsonObject json = new JsonObject();
            json.addProperty("item", ForgeRegistries.ITEMS.getKey(stack.getItem()).toString());
            if (stack.getCount() > 1) {
                json.addProperty("count", stack.getCount());
            }
            if (stack.hasTag()) {
                json.addProperty("nbt", stack.getTag().toString());
            }
            return json;
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return AllRecipe.SMOLDERING_RECIPE_SERIALIZER.get();
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