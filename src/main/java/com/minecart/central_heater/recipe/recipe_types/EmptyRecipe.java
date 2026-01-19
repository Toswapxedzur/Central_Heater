package com.minecart.central_heater.recipe.recipe_types;

import com.google.gson.JsonObject;
import com.minecart.central_heater.recipe.AllRecipe;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class EmptyRecipe implements Recipe<Container> {
    private final ResourceLocation id;

    public EmptyRecipe(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public boolean matches(Container container, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AllRecipe.EMPTY_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AllRecipe.EMPTY.get();
    }

    public static class Serializer implements RecipeSerializer<EmptyRecipe> {
        @Override
        public EmptyRecipe fromJson(ResourceLocation id, JsonObject json) {
            return new EmptyRecipe(id);
        }

        @Override
        public @Nullable EmptyRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            return new EmptyRecipe(id);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, EmptyRecipe recipe) {
            // Nothing to write
        }
    }

    public static class Builder {
        public static Builder empty() {
            return new Builder();
        }

        public void build(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
            consumer.accept(new Result(id));
        }
    }

    // --- FinishedRecipe Implementation (The JSON generator) ---

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;

        public Result(ResourceLocation id) {
            this.id = id;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return AllRecipe.EMPTY_SERIALIZER.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() { return null; }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() { return null; }
    }
}