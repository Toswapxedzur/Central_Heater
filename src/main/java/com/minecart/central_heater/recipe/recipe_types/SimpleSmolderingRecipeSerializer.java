package com.minecart.central_heater.recipe.recipe_types;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

/**
 * A unit serializer for smoldering recipes that doesn't read/write custom data.
 * Useful for recipes that are hardcoded or static.
 */
public class SimpleSmolderingRecipeSerializer<T extends SmolderingRecipe> implements RecipeSerializer<T> {
    private final Factory<T> factory;

    public SimpleSmolderingRecipeSerializer(Factory<T> factory){
        this.factory = factory;
    }

    @Override
    public T fromJson(ResourceLocation id, JsonObject json) {
        return factory.create(id);
    }

    @Override
    public @Nullable T fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        return factory.create(id);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, T recipe) {
        // No-op: No data to sync for simple recipes
    }

    @FunctionalInterface
    public interface Factory<T extends SmolderingRecipe>{
        T create(ResourceLocation id);
    }
}