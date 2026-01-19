package com.minecart.central_heater.recipe;

import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.recipe.recipe_types.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AllRecipe {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, CentralHeater.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CentralHeater.MODID);

    // Empty Recipe
    public static final RegistryObject<RecipeSerializer<EmptyRecipe>> EMPTY_SERIALIZER = RECIPE_SERIALIZERS.register("empty", EmptyRecipe.Serializer::new);
    public static final RegistryObject<RecipeType<EmptyRecipe>> EMPTY = RECIPE_TYPES.register("empty", () -> new RecipeType<EmptyRecipe>() {
        @Override
        public String toString() { return "empty"; }
    });

    // Haunting Recipe
    // Note: SimpleCookingSerializer constructor signature in 1.20.1 is (Factory, int cookingTime)
    // Factory is (id, group, category, ingredient, result, exp, time) -> Recipe
    public static final RegistryObject<RecipeSerializer<HauntingRecipe>> HAUNTING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("haunting",
            () -> new SimpleCookingSerializer<HauntingRecipe>(HauntingRecipe::new, 200));

    public static final RegistryObject<RecipeType<HauntingRecipe>> HAUNTING = RECIPE_TYPES.register("haunting", () -> new RecipeType<HauntingRecipe>() {
        @Override
        public String toString() { return "haunting"; }
    });

    // Smoldering Recipe
    public static final RegistryObject<RecipeSerializer<SmolderingRecipe>> SMOLDERING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("smoldering", SmolderingRecipe.Serializer::new);
    public static final RegistryObject<RecipeType<SmolderingRecipe>> SMOLDERING = RECIPE_TYPES.register("smoldering", () -> new RecipeType<SmolderingRecipe>() {
        @Override
        public String toString() { return "smoldering"; }
    });

    // Block Smoldering Recipe
    public static final RegistryObject<RecipeSerializer<BlockSmolderingRecipe>> BLOCK_SMOLDERING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("block_smoldering", BlockSmolderingRecipe.Serializer::new);
    public static final RegistryObject<RecipeType<BlockSmolderingRecipe>> BLOCK_SMOLDERING_RECIPE = RECIPE_TYPES.register("block_smoldering", () -> new RecipeType<BlockSmolderingRecipe>() {
        @Override
        public String toString() { return "block_smoldering"; }
    });

    // Fire Brewing Recipe
    // Updated to use the factory that accepts ResourceLocation id
    public static final RegistryObject<RecipeSerializer<FireBrewingRecipe>> FIRE_BREWING_SERIALIZER = RECIPE_SERIALIZERS.register("smoldering_fire_brewing",
            () -> new SimpleSmolderingRecipeSerializer<>(FireBrewingRecipe::new));

    public static void register(IEventBus modEventbus){
        RECIPE_TYPES.register(modEventbus);
        RECIPE_SERIALIZERS.register(modEventbus);
    }
}