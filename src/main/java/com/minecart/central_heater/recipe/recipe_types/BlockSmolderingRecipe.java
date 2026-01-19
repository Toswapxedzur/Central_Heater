package com.minecart.central_heater.recipe.recipe_types;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.recipe.recipe_input.BlockSmolderingRecipeInput;
import net.minecraft.core.NonNullList;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class BlockSmolderingRecipe implements Recipe<BlockSmolderingRecipeInput> {
    private final ResourceLocation id; // 1.20.1 Recipes often track their ID
    protected final BlockState input;
    protected final BlockState output;
    protected final NonNullList<ItemStack> itemOutput;
    protected final int time;
    protected final int fireLevel;
    protected final boolean fireBurn;

    public BlockSmolderingRecipe(ResourceLocation id, BlockState input, BlockState output, NonNullList<ItemStack> itemOutput, int time, int fireLevel, boolean fireBurn) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.itemOutput = itemOutput;
        this.time = time;
        this.fireLevel = fireLevel;
        this.fireBurn = fireBurn;
    }

    @Override
    public boolean matches(BlockSmolderingRecipeInput input, Level level) {
        if (input.getFireLevel() != this.fireLevel)
            return false;
        return input.state.is(this.input.getBlock());
    }

    @Override
    public ItemStack assemble(BlockSmolderingRecipeInput input, RegistryAccess access) {
        return ItemStack.EMPTY; // Recipes returning BlockStates usually don't have an item result
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public BlockState getInputBlock(){
        return input;
    }

    public BlockState getResultBlock() {
        return output;
    }

    public NonNullList<ItemStack> getItemOutputs() {
        return itemOutput;
    }

    public int getTime() {
        return time;
    }

    public boolean isfireBurn() {
        return fireBurn;
    }

    public int getFireLevel() {
        return fireLevel;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AllRecipe.BLOCK_SMOLDERING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AllRecipe.BLOCK_SMOLDERING_RECIPE.get();
    }

    public static class Serializer implements RecipeSerializer<BlockSmolderingRecipe> {

        @Override
        public BlockSmolderingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            BlockState input = readBlockState(json.get("input"));
            BlockState output = readBlockState(json.get("output"));

            NonNullList<ItemStack> itemOutputs = NonNullList.create();
            if (json.has("itemOutput")) {
                JsonArray array = GsonHelper.getAsJsonArray(json, "itemOutput");
                for (JsonElement element : array) {
                    itemOutputs.add(ShapedRecipe.itemStackFromJson(element.getAsJsonObject()));
                }
            }

            int time = GsonHelper.getAsInt(json, "time", 200);
            int fireLevel = GsonHelper.getAsInt(json, "fireLevel", 1);
            boolean fireBurn = GsonHelper.getAsBoolean(json, "fireBurn", false);

            return new BlockSmolderingRecipe(recipeId, input, output, itemOutputs, time, fireLevel, fireBurn);
        }

        @Override
        public @Nullable BlockSmolderingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            BlockState input = Block.stateById(buffer.readVarInt());
            BlockState output = Block.stateById(buffer.readVarInt());

            int size = buffer.readVarInt();
            NonNullList<ItemStack> itemOutput = NonNullList.withSize(size, ItemStack.EMPTY);
            for (int i = 0; i < size; i++) {
                itemOutput.set(i, buffer.readItem());
            }

            int time = buffer.readVarInt();
            int fireLevel = buffer.readVarInt();
            boolean fireBurn = buffer.readBoolean();

            return new BlockSmolderingRecipe(recipeId, input, output, itemOutput, time, fireLevel, fireBurn);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, BlockSmolderingRecipe recipe) {
            buffer.writeVarInt(Block.getId(recipe.input));
            buffer.writeVarInt(Block.getId(recipe.output));

            buffer.writeVarInt(recipe.itemOutput.size());
            for (ItemStack stack : recipe.itemOutput) {
                buffer.writeItem(stack);
            }

            buffer.writeVarInt(recipe.time);
            buffer.writeVarInt(recipe.fireLevel);
            buffer.writeBoolean(recipe.fireBurn);
        }

        private static BlockState readBlockState(JsonElement json) {
            if (json == null || !json.isJsonPrimitive()) {
                throw new JsonParseException("BlockState must be a string resource location");
            }
            String id = json.getAsString();
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(id));
            if (block == null) {
                throw new JsonParseException("Invalid or missing block: " + id);
            }
            return block.defaultBlockState();
        }
    }
}