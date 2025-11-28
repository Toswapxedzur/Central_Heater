package com.minecart.central_heater.recipe;

import com.llamalad7.mixinextras.expression.impl.ast.expressions.UnaryExpression;
import com.minecart.central_heater.AllRecipe;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Comparator;
import java.util.function.UnaryOperator;

public class SmolderingRecipe implements Recipe<SmolderingRecipeInput> {

    protected final NonNullList<Ingredient> ingredients;
    protected final FluidStack fluidIngredient;
    protected final ItemStack result;
    protected final FluidStack fluidResult;
    protected final int time;
    protected final int tier;
    protected final int fireLevel;

    public SmolderingRecipe(NonNullList<Ingredient> ingredients, FluidStack fluidIngredient, ItemStack result, FluidStack fluidResult, int time, int tier, int fireLevel){
        this.ingredients = ingredients;
        this.fluidIngredient = fluidIngredient;
        this.result = result;
        this.fluidResult = fluidResult;
        this.time = time;
        this.tier = tier;
        this.fireLevel = fireLevel;
    }

    @Override
    public boolean matches(SmolderingRecipeInput input, Level level) {
        if(input.getTier() < tier)
            return false;

        if(!fluidIngredient.is(input.getFluid().getFluidType()) || fluidIngredient.getAmount() > input.getFluid().getAmount())
            return false;

        if(RecipeMatcher.findMatches(input.item, ingredients) == null)
            return false;

        return true;
    }

    @Override
    public ItemStack assemble(SmolderingRecipeInput input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.result;
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
        private static final MapCodec<SmolderingRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Ingredient.LIST_CODEC.fieldOf("ingredients").flatXmap(
                                list -> {
                                    return DataResult.success(NonNullList.of(Ingredient.EMPTY, list.toArray(Ingredient[]::new)));
                                },
                                DataResult::success
                        ).forGetter(recipe -> recipe.ingredients),
                        FluidStack.CODEC.optionalFieldOf("fluidIngredient", FluidStack.EMPTY).forGetter(recipe -> recipe.fluidIngredient),
                        ItemStack.OPTIONAL_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                        FluidStack.CODEC.optionalFieldOf("fluidResult", FluidStack.EMPTY).forGetter(recipe -> recipe.fluidResult),
                        Codec.INT.optionalFieldOf("time", 400).forGetter(recipe -> recipe.time),
                        Codec.INT.optionalFieldOf("tier", 0).forGetter(recipe -> recipe.tier),
                        Codec.INT.optionalFieldOf("fireLevel", 1).forGetter(recipe -> recipe.fireLevel)
                ).apply(instance, SmolderingRecipe::new)
        );

        private static final StreamCodec<RegistryFriendlyByteBuf, SmolderingRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<SmolderingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SmolderingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static SmolderingRecipe fromNetwork(RegistryFriendlyByteBuf buffer){
            int size = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
            ingredients.replaceAll(ingredient -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
            FluidStack fluidIngredient = FluidStack.OPTIONAL_STREAM_CODEC.decode(buffer);
            ItemStack result = ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer);
            FluidStack fluidResult = FluidStack.OPTIONAL_STREAM_CODEC.decode(buffer);
            int time = buffer.readVarInt();
            int tier = buffer.readVarInt();
            int fireLevel = buffer.readVarInt();
            return new SmolderingRecipe(ingredients, fluidIngredient, result, fluidResult, time, tier, fireLevel);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, SmolderingRecipe recipe){
            buffer.writeVarInt(recipe.ingredients.size());
            recipe.ingredients.forEach(ingredient -> Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient));
            FluidStack.OPTIONAL_STREAM_CODEC.encode(buffer, recipe.fluidIngredient);
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, recipe.result);
            FluidStack.OPTIONAL_STREAM_CODEC.encode(buffer, recipe.fluidResult);
            buffer.writeVarInt(recipe.time);
            buffer.writeVarInt(recipe.tier);
            buffer.writeVarInt(recipe.fireLevel);
        }
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    public FluidStack getFluidIngredient() {
        return fluidIngredient;
    }

    public ItemStack getResult() {
        return result;
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

    public int getFireLevel() {
        return fireLevel;
    }
}
