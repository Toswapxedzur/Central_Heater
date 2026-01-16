package com.minecart.central_heater.recipe.recipe_types;

import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.recipe.recipe_input.BlockSmolderingRecipeInput;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockSmolderingRecipe implements Recipe<BlockSmolderingRecipeInput> {
    protected final BlockState input;
    protected final BlockState output;
    protected final NonNullList<ItemStack> itemOutput;
    protected final int time;
    protected final int fireLevel;
    protected final boolean fireBurn;

    public BlockSmolderingRecipe(BlockState input, BlockState output, NonNullList<ItemStack> itemOutput, int time, int fireLevel, boolean fireBurn) {
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
    public ItemStack assemble(BlockSmolderingRecipeInput input, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
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
        private static final MapCodec<BlockSmolderingRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        BlockState.CODEC.fieldOf("input").forGetter(recipe -> recipe.input),
                        BlockState.CODEC.fieldOf("output").forGetter(recipe -> recipe.output),
                        NonNullList.codecOf(ItemStack.OPTIONAL_CODEC).optionalFieldOf("itemOutput", NonNullList.create())
                                .forGetter(recipe -> recipe.itemOutput),
                        Codec.INT.optionalFieldOf("time", 200).forGetter(recipe -> recipe.time),
                        Codec.INT.optionalFieldOf("fireLevel", 1).forGetter(recipe -> recipe.fireLevel),
                        Codec.BOOL.optionalFieldOf("fireBurn", false).forGetter(recipe -> recipe.fireBurn)
                ).apply(instance, BlockSmolderingRecipe::new)
        );

        private static final StreamCodec<RegistryFriendlyByteBuf, BlockSmolderingRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<BlockSmolderingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BlockSmolderingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static BlockSmolderingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            BlockState input = ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY).decode(buffer);
            BlockState output = ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY).decode(buffer);

            int size = buffer.readVarInt();
            NonNullList<ItemStack> itemOutput = NonNullList.withSize(size, ItemStack.EMPTY);
            itemOutput.replaceAll(ignored -> ItemStack.STREAM_CODEC.decode(buffer));

            int time = buffer.readVarInt();
            int fireLevel = buffer.readVarInt();
            boolean fireBurn = buffer.readBoolean();

            return new BlockSmolderingRecipe(input, output, itemOutput, time, fireLevel, fireBurn);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, BlockSmolderingRecipe recipe) {
            ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY).encode(buffer, recipe.input);
            ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY).encode(buffer, recipe.output);

            buffer.writeVarInt(recipe.itemOutput.size());
            recipe.itemOutput.forEach(stack -> ItemStack.STREAM_CODEC.encode(buffer, stack));

            buffer.writeVarInt(recipe.time);
            buffer.writeVarInt(recipe.fireLevel);
            buffer.writeBoolean(recipe.fireBurn);
        }
    }
}
