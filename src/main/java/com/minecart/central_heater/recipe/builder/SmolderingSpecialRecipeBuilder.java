package com.minecart.central_heater.recipe.builder;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SmolderingSpecialRecipeBuilder {
    private final RecipeSerializer<?> serializer;

    public SmolderingSpecialRecipeBuilder(RecipeSerializer<?> serializer) {
        this.serializer = serializer;
    }

    public static SmolderingSpecialRecipeBuilder smolderingSpecial(RecipeSerializer<?> serializer) {
        return new SmolderingSpecialRecipeBuilder(serializer);
    }

    public void save(Consumer<FinishedRecipe> consumer, String id) {
        this.save(consumer, new ResourceLocation(id));
    }

    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new FinishedRecipe() {
            @Override
            public void serializeRecipeData(JsonObject json) {
            }

            @Override
            public RecipeSerializer<?> getType() {
                return SmolderingSpecialRecipeBuilder.this.serializer;
            }

            @Override
            public ResourceLocation getId() {
                return id;
            }

            @Nullable
            @Override
            public JsonObject serializeAdvancement() {
                return null;
            }

            @Nullable
            @Override
            public ResourceLocation getAdvancementId() {
                return null;
            }
        });
    }
}