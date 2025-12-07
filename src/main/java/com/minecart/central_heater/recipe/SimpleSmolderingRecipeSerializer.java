package com.minecart.central_heater.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 *An unit serializer for smoldering recipe that doesn't do anything
 */
public class SimpleSmolderingRecipeSerializer<T extends SmolderingRecipe> implements RecipeSerializer<T> {
    private final MapCodec<T> CODEC;
    private final StreamCodec<RegistryFriendlyByteBuf, T> STREAM_CODEC;

    public SimpleSmolderingRecipeSerializer(Factory<T> factory){
        this.CODEC = MapCodec.unit(factory::create);
        this.STREAM_CODEC = StreamCodec.of((buffer, val) -> {},
                (buffer) -> factory.create());
    }

    @Override
    public MapCodec<T> codec() {
        return this.CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
        return this.STREAM_CODEC;
    }

    @FunctionalInterface
    public interface Factory<T extends SmolderingRecipe>{
        T create();
    }
}
