package com.minecart.central_heater.recipe.recipe_types;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.recipe.recipe_input.SmolderingRecipeInput;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SmolderingRecipe implements Recipe<SmolderingRecipeInput> {

    private final ResourceLocation id;
    protected final NonNullList<Ingredient> ingredients;
    protected final FluidStack fluidIngredient;
    protected final NonNullList<ItemStack> results;
    protected final FluidStack fluidResult;
    protected final int time;
    protected final int tier;
    protected final int fireLevel;

    public SmolderingRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, FluidStack fluidIngredient, NonNullList<ItemStack> results, FluidStack fluidResult, int time, int tier, int fireLevel){
        this.id = id;
        this.ingredients = ingredients;
        this.fluidIngredient = fluidIngredient;
        this.results = results;
        this.fluidResult = fluidResult;
        this.time = time;
        this.tier = tier;
        this.fireLevel = fireLevel;
    }

    @Override
    public boolean matches(SmolderingRecipeInput input, Level level) {
        if(input.getTier() < tier)
            return false;

        if(input.fireLevel != fireLevel)
            return false;

        if(!fluidIngredient.isEmpty()) {
            if (input.getFluid().isEmpty() || !input.getFluid().isFluidEqual(fluidIngredient) || input.getFluid().getAmount() < fluidIngredient.getAmount()) {
                return false;
            }
        }

        // Convert input items (NonNullList<ItemStack>) to List<ItemStack> for RecipeMatcher
        List<ItemStack> inputs = new ArrayList<>();
        for(ItemStack stack : input.item) {
            if (!stack.isEmpty()) inputs.add(stack);
        }

        return RecipeMatcher.findMatches(inputs, ingredients) != null;
    }

    @Override
    public ItemStack assemble(SmolderingRecipeInput input, RegistryAccess registryAccess) {
        return ItemStack.EMPTY; // Usually returns the primary result, but this recipe has multiple outputs handled by custom methods
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return results.isEmpty() ? ItemStack.EMPTY : results.get(0);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AllRecipe.SMOLDERING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AllRecipe.SMOLDERING.get();
    }

    public static class Serializer implements RecipeSerializer<SmolderingRecipe> {

        @Override
        public SmolderingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            // Ingredients
            NonNullList<Ingredient> ingredients = NonNullList.create();
            if (json.has("ingredients")) {
                JsonArray ingArray = GsonHelper.getAsJsonArray(json, "ingredients");
                for (JsonElement e : ingArray) {
                    ingredients.add(Ingredient.fromJson(e));
                }
            }

            // Fluid Ingredient
            FluidStack fluidIngredient = FluidStack.EMPTY;
            if (json.has("fluidIngredient")) {
                fluidIngredient = readFluidStack(json.getAsJsonObject("fluidIngredient"));
            }

            // Item Results
            NonNullList<ItemStack> results = NonNullList.create();
            if (json.has("result")) {
                JsonArray resArray = GsonHelper.getAsJsonArray(json, "result");
                for (JsonElement e : resArray) {
                    results.add(ShapedRecipe.itemStackFromJson(e.getAsJsonObject()));
                }
            }

            // Fluid Result
            FluidStack fluidResult = FluidStack.EMPTY;
            if (json.has("fluidResult")) {
                fluidResult = readFluidStack(json.getAsJsonObject("fluidResult"));
            }

            int time = GsonHelper.getAsInt(json, "time", 400);
            int tier = GsonHelper.getAsInt(json, "tier", 0);
            int fireLevel = GsonHelper.getAsInt(json, "fireLevel", 1);

            return new SmolderingRecipe(recipeId, ingredients, fluidIngredient, results, fluidResult, time, tier, fireLevel);
        }

        @Override
        public @Nullable SmolderingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int size = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
            for (int i = 0; i < size; i++) {
                ingredients.set(i, Ingredient.fromNetwork(buffer));
            }

            FluidStack fluidIngredient = buffer.readFluidStack();

            size = buffer.readVarInt();
            NonNullList<ItemStack> results = NonNullList.withSize(size, ItemStack.EMPTY);
            for (int i = 0; i < size; i++) {
                results.set(i, buffer.readItem());
            }

            FluidStack fluidResult = buffer.readFluidStack();

            int time = buffer.readVarInt();
            int tier = buffer.readVarInt();
            int fireLevel = buffer.readVarInt();

            return new SmolderingRecipe(recipeId, ingredients, fluidIngredient, results, fluidResult, time, tier, fireLevel);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, SmolderingRecipe recipe) {
            buffer.writeVarInt(recipe.ingredients.size());
            for (Ingredient ing : recipe.ingredients) {
                ing.toNetwork(buffer);
            }

            buffer.writeFluidStack(recipe.fluidIngredient);

            buffer.writeVarInt(recipe.results.size());
            for (ItemStack stack : recipe.results) {
                buffer.writeItem(stack);
            }

            buffer.writeFluidStack(recipe.fluidResult);

            buffer.writeVarInt(recipe.time);
            buffer.writeVarInt(recipe.tier);
            buffer.writeVarInt(recipe.fireLevel);
        }

        // Helper for JSON parsing of FluidStacks
        private FluidStack readFluidStack(JsonObject json) {
            String fluidId = GsonHelper.getAsString(json, "fluid");
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidId));
            int amount = GsonHelper.getAsInt(json, "amount", 1000);
            if (fluid == null) return FluidStack.EMPTY;
            return new FluidStack(fluid, amount);
        }
    }

    /**
     *Can be fake in the case of custom recipes
     */
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     *Can be dynamically changed
     */
    public NonNullList<Ingredient> getIngredients(SmolderingRecipeInput input, RegistryAccess registryAccess){
        return getIngredients();
    }

    public FluidStack getFluidIngredient() {
        return fluidIngredient;
    }

    /**
     *Can be dynamically changed
     */
    public FluidStack getFluidIngredient(SmolderingRecipeInput input, RegistryAccess registryAccess){
        return getFluidIngredient();
    }

    public NonNullList<ItemStack> getResults() {
        return results;
    }

    public FluidStack getFluidResult() {
        return fluidResult;
    }

    public int getTier() {
        return tier;
    }

    public int getTime() {
        return time;
    }

    /**
     *Can be dynamically changed
     */
    public int getTime(SmolderingRecipeInput input, RegistryAccess registryAccess){
        return time;
    }

    public int getFireLevel() {
        return fireLevel;
    }

    public NonNullList<ItemStack> assembleResults(SmolderingRecipeInput input, RegistryAccess registryAccess) {
        NonNullList<ItemStack> copy = NonNullList.create();
        for(ItemStack stack : results) {
            copy.add(stack.copy());
        }
        return copy;
    }

    public FluidStack assembleFluidResult(SmolderingRecipeInput input, RegistryAccess registryAccess) {
        return fluidResult.copy();
    }
}