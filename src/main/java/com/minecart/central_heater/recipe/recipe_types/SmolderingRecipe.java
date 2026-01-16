package com.minecart.central_heater.recipe.recipe_types;

import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.recipe.recipe_input.SmolderingRecipeInput;
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
import net.neoforged.neoforge.common.util.RecipeMatcher;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class SmolderingRecipe implements Recipe<SmolderingRecipeInput> {

    protected final NonNullList<Ingredient> ingredients;
    protected final FluidStack fluidIngredient;
    protected final NonNullList<ItemStack> results;
    protected final FluidStack fluidResult;
    protected final int time;
    protected final int tier;
    protected final int fireLevel;

    public SmolderingRecipe(NonNullList<Ingredient> ingredients, FluidStack fluidIngredient, NonNullList<ItemStack> results, FluidStack fluidResult, int time, int tier, int fireLevel){
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

        if(!fluidIngredient.is(input.getFluid().getFluidType()) || fluidIngredient.getAmount() > input.getFluid().getAmount())
            return false;

        if(RecipeMatcher.findMatches(input.item, ingredients) == null)
            return false;

        return true;
    }

    @Override
    public ItemStack assemble(SmolderingRecipeInput input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
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
                        NonNullList.codecOf(Ingredient.CODEC).optionalFieldOf("ingredients", NonNullList.of(Ingredient.EMPTY))
                                .forGetter(recipe -> recipe.ingredients),
                        FluidStack.CODEC.optionalFieldOf("fluidIngredient", FluidStack.EMPTY).forGetter(recipe -> recipe.fluidIngredient),
                        NonNullList.codecOf(ItemStack.OPTIONAL_CODEC).optionalFieldOf("result", NonNullList.of(ItemStack.EMPTY))
                                .forGetter(recipe -> recipe.results),
                        FluidStack.OPTIONAL_CODEC.optionalFieldOf("fluidResult", FluidStack.EMPTY).forGetter(recipe -> recipe.fluidResult),
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
            size = buffer.readVarInt();
            NonNullList<ItemStack> results = NonNullList.withSize(size, ItemStack.EMPTY);
            results.replaceAll(itemStack -> ItemStack.STREAM_CODEC.decode(buffer));
            FluidStack fluidResult = FluidStack.OPTIONAL_STREAM_CODEC.decode(buffer);
            int time = buffer.readVarInt();
            int tier = buffer.readVarInt();
            int fireLevel = buffer.readVarInt();
            return new SmolderingRecipe(ingredients, fluidIngredient, results, fluidResult, time, tier, fireLevel);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, SmolderingRecipe recipe){
            buffer.writeVarInt(recipe.ingredients.size());
            recipe.ingredients.forEach(ingredient -> Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient));
            FluidStack.OPTIONAL_STREAM_CODEC.encode(buffer, recipe.fluidIngredient);
            buffer.writeVarInt(recipe.results.size());
            recipe.results.forEach(itemStack -> ItemStack.STREAM_CODEC.encode(buffer, itemStack));
            FluidStack.OPTIONAL_STREAM_CODEC.encode(buffer, recipe.fluidResult);
            buffer.writeVarInt(recipe.time);
            buffer.writeVarInt(recipe.tier);
            buffer.writeVarInt(recipe.fireLevel);
        }
    }

    /**
     *Can be fake in the case of custom recipes
     */
    @Nullable
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     *Can be dynamically changed
     */
    public NonNullList<Ingredient> getIngredients(SmolderingRecipeInput input, HolderLookup.Provider registries){
        return getIngredients();
    }

    public FluidStack getFluidIngredient() {
        return fluidIngredient;
    }

    /**
     *Can be dynamically changed
     */
    public FluidStack getFluidIngredient(SmolderingRecipeInput input, HolderLookup.Provider registries){
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
    public int getTime(SmolderingRecipeInput input, HolderLookup.Provider registries){
        return time;
    }

    public int getFireLevel() {
        return fireLevel;
    }

    public NonNullList<ItemStack> assembleResults(SmolderingRecipeInput input, HolderLookup.Provider registries) {
        return NonNullList.copyOf(results);
    }

    public FluidStack assembleFluidResult(SmolderingRecipeInput input, HolderLookup.Provider registries) {
        return fluidResult.copy();
    }
}
