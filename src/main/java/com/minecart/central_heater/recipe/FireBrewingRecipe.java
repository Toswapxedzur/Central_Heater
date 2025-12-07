package com.minecart.central_heater.recipe;

import com.minecart.central_heater.AllRecipe;
import com.minecart.central_heater.util.VirtualLevel;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.brewing.BrewingRecipe;
import net.neoforged.neoforge.fluids.FluidStack;

public class FireBrewingRecipe extends SmolderingRecipe{
    public FireBrewingRecipe() {
        super(NonNullList.of(Ingredient.EMPTY), new FluidStack(Fluids.WATER, 1000), NonNullList.of(ItemStack.EMPTY), new FluidStack(Fluids.WATER, 1000), 400, 4, 2);
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean matches(SmolderingRecipeInput input, Level level) {
        if(input.fireLevel < 2 || input.tier < 4)
            return false;
        if(!input.fluid.is(Fluids.WATER) || input.fluid.getAmount() < 1000)
            return false;
        if(input.item.isEmpty())
            return false;
        ItemStack reagent = input.item.getLast();
        PotionContents potionInput = input.fluid.getOrDefault(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));
        ItemStack potionItem = new ItemStack(Items.POTION);
        potionItem.set(DataComponents.POTION_CONTENTS, potionInput);
        PotionBrewing brewingRecipes = level.potionBrewing();
        return brewingRecipes.hasMix(potionItem, reagent);
    }

    @Override
    public ItemStack assemble(SmolderingRecipeInput input, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public FluidStack getFluidIngredient(SmolderingRecipeInput input, HolderLookup.Provider registries) {
        FluidStack fluidIngredient = input.getFluid();
        fluidIngredient.setAmount(1000);
        return fluidIngredient;
    }

    @Override
    public NonNullList<ItemStack> assembleResults(SmolderingRecipeInput input, HolderLookup.Provider registries) {
        PotionBrewing brewingRecipes = VirtualLevel.getLevel().potionBrewing();
        ItemStack reagent = input.item.getLast();
        ItemStack potionItem = new ItemStack(Items.POTION);
        potionItem.set(DataComponents.POTION_CONTENTS, input.fluid.getOrDefault(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER)));
        ItemStack potionResult = brewingRecipes.mix(reagent, potionItem);
        NonNullList<ItemStack> results = NonNullList.create();
        results.addAll(input.item);
        results.removeLast();
        if(reagent.hasCraftingRemainingItem())
            results.add(reagent.getCraftingRemainingItem());
        return results;
    }

    @Override
    public FluidStack assembleFluidResult(SmolderingRecipeInput input, HolderLookup.Provider registries) {
        ItemStack reagent = input.item.getLast();
        ItemStack potionItem = new ItemStack(Items.POTION);
        potionItem.set(DataComponents.POTION_CONTENTS, input.fluid.getOrDefault(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER)));
        PotionBrewing brewingRecipes = VirtualLevel.getLevel().potionBrewing();
        ItemStack potionResult = brewingRecipes.mix(reagent, potionItem);
        FluidStack fluidResult = new FluidStack(Fluids.WATER, 1000);
        fluidResult.set(DataComponents.POTION_CONTENTS,
                potionResult.getOrDefault(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER)));
        return fluidResult;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public RecipeType<?> getType() {
        return AllRecipe.SMOLDERING.get();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AllRecipe.FIRE_BREWING_SERIALIZER.get();
    }
}
