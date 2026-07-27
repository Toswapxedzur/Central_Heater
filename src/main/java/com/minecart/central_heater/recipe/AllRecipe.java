package com.minecart.central_heater.recipe;

import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.recipe.recipe_types.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AllRecipe {
    public static DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, CentralHeater.MODID);
    public static DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, CentralHeater.MODID);

    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<EmptyRecipe>> EMPTY_SERIALIZER = RECIPE_SERIALIZERS.register("empty", ()->new EmptyRecipe.Serializer());
    public static DeferredHolder<RecipeType<?>, RecipeType<EmptyRecipe>> EMPTY = RECIPE_TYPES.register("empty", ()->new RecipeType<EmptyRecipe>(){
        public String toString(){ return "empty"; }});

    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<HauntingRecipe>> HAUNTING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("haunting", ()->new SimpleCookingSerializer<>(HauntingRecipe::new, 200));
    public static DeferredHolder<RecipeType<?>, RecipeType<HauntingRecipe>> HAUNTING = RECIPE_TYPES.register("haunting", ()->new RecipeType<HauntingRecipe>(){
        public String toString(){ return "haunting"; }});

    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SmolderingRecipe>> SMOLDERING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("smoldering", ()->new SmolderingRecipe.Serializer());
    public static DeferredHolder<RecipeType<?>, RecipeType<SmolderingRecipe>> SMOLDERING = RECIPE_TYPES.register("smoldering", ()->new RecipeType<SmolderingRecipe>() {
        @Override
        public String toString() {
            return "smoldering";
        }
    });

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BlockCleaningRecipe>> BLOCK_CLEANING_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("block_cleaning", BlockCleaningRecipe.Serializer::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<BlockCleaningRecipe>> BLOCK_CLEANING =
            RECIPE_TYPES.register("block_cleaning", () -> RecipeType.simple(CentralHeater.modLoc("block_cleaning")));

    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BlockSmolderingRecipe>> BLOCK_SMOLDERING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("block_smoldering", ()->new BlockSmolderingRecipe.Serializer());
    public static DeferredHolder<RecipeType<?>, RecipeType<BlockSmolderingRecipe>> BLOCK_SMOLDERING_RECIPE = RECIPE_TYPES.register("block_smoldering", ()->new RecipeType<BlockSmolderingRecipe>() {
        public String toString() {
            return "block_smoldering";
        }
    });

    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FireBrewingRecipe>> FIRE_BREWING_SERIALIZER = RECIPE_SERIALIZERS.register("smoldering_fire_brewing",
            ()->new SimpleSmolderingRecipeSerializer<>(()->new FireBrewingRecipe()));

    public static void register(IEventBus modEventbus){
        RECIPE_TYPES.register(modEventbus);
        RECIPE_SERIALIZERS.register(modEventbus);
    }
}
