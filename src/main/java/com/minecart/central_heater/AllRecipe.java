package com.minecart.central_heater;

import com.minecart.central_heater.recipe.FireBrewingRecipe;
import com.minecart.central_heater.recipe.SeethingRecipe;
import com.minecart.central_heater.recipe.SimpleSmolderingRecipeSerializer;
import com.minecart.central_heater.recipe.SmolderingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AllRecipe {
    public static DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Central_heater.MODID);
    public static DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Central_heater.MODID);

    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SeethingRecipe>> SEETHING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("seething", ()->new SimpleCookingSerializer<>(SeethingRecipe::new, 200));
    public static DeferredHolder<RecipeType<?>, RecipeType<SeethingRecipe>> SEETHING = RECIPE_TYPES.register("seething", ()->new RecipeType<SeethingRecipe>(){
        public String toString(){ return "seething"; }});

    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SmolderingRecipe>> SMOLDERING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("smoldering", ()->new SmolderingRecipe.Serializer());
    public static DeferredHolder<RecipeType<?>, RecipeType<SmolderingRecipe>> SMOLDERING = RECIPE_TYPES.register("smoldering", ()->new RecipeType<SmolderingRecipe>() {
        @Override
        public String toString() {
            return "smoldering";
        }
    });

    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FireBrewingRecipe>> FIRE_BREWING_SERIALIZER = RECIPE_SERIALIZERS.register("smoldering_fire_brewing",
            ()->new SimpleSmolderingRecipeSerializer<>(()->new FireBrewingRecipe()));

    public static void register(IEventBus modEventbus){
        RECIPE_TYPES.register(modEventbus);
        RECIPE_SERIALIZERS.register(modEventbus);
    }
}
