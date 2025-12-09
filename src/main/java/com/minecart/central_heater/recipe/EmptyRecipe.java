package com.minecart.central_heater.recipe;

import com.minecart.central_heater.AllRecipe;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

public class EmptyRecipe implements Recipe<SingleRecipeInput> {
    @Override
    public boolean matches(SingleRecipeInput singleRecipeInput, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(SingleRecipeInput singleRecipeInput, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AllRecipe.EMPTY_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AllRecipe.EMPTY.get();
    }

    public static class Serializer implements RecipeSerializer<EmptyRecipe>{
        private static final MapCodec<EmptyRecipe> CODEC = MapCodec.unit(EmptyRecipe::new);

        private static final StreamCodec<RegistryFriendlyByteBuf, EmptyRecipe> STREAM_CODEC = StreamCodec.of((buffer, val) -> {},
                (buffer) -> new EmptyRecipe());

        public MapCodec<EmptyRecipe> codec() {
            return CODEC;
        }

        public StreamCodec<RegistryFriendlyByteBuf, EmptyRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
