package com.minecart.central_heater.recipe.recipe_types;

import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.recipe.recipe_input.BlockCleaningRecipeInput;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class BlockCleaningRecipe implements Recipe<BlockCleaningRecipeInput> {
    private final Block input;
    private final Block output;
    private final ItemStack itemOutput;
    private final float dropChance;

    public BlockCleaningRecipe(Block input, Block output, ItemStack itemOutput, float dropChance) {
        this.input = input;
        this.output = output;
        this.itemOutput = itemOutput;
        this.dropChance = dropChance;
    }

    @Override
    public boolean matches(BlockCleaningRecipeInput input, Level level) {
        // Safe check using Registry Keys to avoid Object Identity mismatch
        return BuiltInRegistries.BLOCK.getKey(input.block()).equals(BuiltInRegistries.BLOCK.getKey(this.input));
    }

    public Block getInputBlock() { return input; }
    public Block getOutputBlock() { return output; }
    public ItemStack getItemOutput() { return itemOutput; }
    public float getDropChance() { return dropChance; }

    @Override
    public ItemStack assemble(BlockCleaningRecipeInput input, HolderLookup.Provider provider) {
        // Must return a copy of the output stack, otherwise crafting yields air!
        return this.itemOutput.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) { return true; }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) { return itemOutput; }

    @Override
    public RecipeSerializer<?> getSerializer() { return AllRecipe.BLOCK_CLEANING_RECIPE_SERIALIZER.get(); }

    @Override
    public RecipeType<?> getType() { return AllRecipe.BLOCK_CLEANING.get(); }

    public static class Serializer implements RecipeSerializer<BlockCleaningRecipe> {
        private static final MapCodec<BlockCleaningRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        BuiltInRegistries.BLOCK.byNameCodec().fieldOf("input").forGetter(BlockCleaningRecipe::getInputBlock),
                        BuiltInRegistries.BLOCK.byNameCodec().fieldOf("output").forGetter(BlockCleaningRecipe::getOutputBlock),
                        ItemStack.STRICT_CODEC.fieldOf("itemOutput").forGetter(BlockCleaningRecipe::getItemOutput),
                        Codec.FLOAT.fieldOf("dropChance").forGetter(BlockCleaningRecipe::getDropChance)
                ).apply(instance, BlockCleaningRecipe::new)
        );

        private static final StreamCodec<RegistryFriendlyByteBuf, BlockCleaningRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<BlockCleaningRecipe> codec() { return CODEC; }
        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BlockCleaningRecipe> streamCodec() { return STREAM_CODEC; }

        private static BlockCleaningRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            Block input = ByteBufCodecs.idMapper(BuiltInRegistries.BLOCK).decode(buffer);
            Block output = ByteBufCodecs.idMapper(BuiltInRegistries.BLOCK).decode(buffer);
            ItemStack itemOutput = ItemStack.STREAM_CODEC.decode(buffer);
            float dropChance = buffer.readFloat();
            return new BlockCleaningRecipe(input, output, itemOutput, dropChance);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, BlockCleaningRecipe recipe) {
            ByteBufCodecs.idMapper(BuiltInRegistries.BLOCK).encode(buffer, recipe.getInputBlock());
            ByteBufCodecs.idMapper(BuiltInRegistries.BLOCK).encode(buffer, recipe.getOutputBlock());
            ItemStack.STREAM_CODEC.encode(buffer, recipe.getItemOutput());
            buffer.writeFloat(recipe.getDropChance());
        }
    }
}