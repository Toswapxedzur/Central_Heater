package com.minecart.central_heater.recipe.recipe_types;

import com.google.gson.JsonObject;
import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.recipe.recipe_input.BlockCleaningRecipeInput;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class BlockCleaningRecipe implements Recipe<BlockCleaningRecipeInput> {
    private final ResourceLocation id;
    private final Block input;
    private final Block output;
    private final ItemStack itemOutput;
    private final float dropChance;

    public BlockCleaningRecipe(ResourceLocation id, Block input, Block output, ItemStack itemOutput, float dropChance) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.itemOutput = itemOutput;
        this.dropChance = dropChance;
    }

    @Override
    public boolean matches(BlockCleaningRecipeInput input, Level level) {
        ResourceLocation inputKey = ForgeRegistries.BLOCKS.getKey(input.block());
        ResourceLocation thisKey = ForgeRegistries.BLOCKS.getKey(this.input);
        return inputKey != null && inputKey.equals(thisKey);
    }

    public Block getInputBlock() {
        return input;
    }

    public Block getOutputBlock() {
        return output;
    }

    public ItemStack getItemOutput() {
        return itemOutput;
    }

    public float getDropChance() {
        return dropChance;
    }

    @Override
    public ItemStack assemble(BlockCleaningRecipeInput input, RegistryAccess registries) {
        return this.itemOutput.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registries) {
        return itemOutput;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AllRecipe.BLOCK_CLEANING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AllRecipe.BLOCK_CLEANING.get();
    }

    public static class Serializer implements RecipeSerializer<BlockCleaningRecipe> {

        @Override
        public BlockCleaningRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Block input = readBlock(GsonHelper.getAsString(json, "input"));
            Block output = readBlock(GsonHelper.getAsString(json, "output"));
            ItemStack itemOutput = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "itemOutput"));
            float dropChance = GsonHelper.getAsFloat(json, "dropChance", 1.0f);
            return new BlockCleaningRecipe(recipeId, input, output, itemOutput, dropChance);
        }

        @Override
        public @Nullable BlockCleaningRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Block input = Block.stateById(buffer.readVarInt()).getBlock();
            Block output = Block.stateById(buffer.readVarInt()).getBlock();
            ItemStack itemOutput = buffer.readItem();
            float dropChance = buffer.readFloat();
            return new BlockCleaningRecipe(recipeId, input, output, itemOutput, dropChance);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, BlockCleaningRecipe recipe) {
            buffer.writeVarInt(Block.getId(recipe.input.defaultBlockState()));
            buffer.writeVarInt(Block.getId(recipe.output.defaultBlockState()));
            buffer.writeItem(recipe.itemOutput);
            buffer.writeFloat(recipe.dropChance);
        }

        private static Block readBlock(String id) {
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(id));
            if (block == null) {
                throw new com.google.gson.JsonParseException("Invalid or missing block: " + id);
            }
            return block;
        }
    }
}
