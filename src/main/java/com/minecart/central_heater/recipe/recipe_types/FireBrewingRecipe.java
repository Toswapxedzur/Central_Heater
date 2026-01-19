package com.minecart.central_heater.recipe.recipe_types;

import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.recipe.recipe_input.SmolderingRecipeInput;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

public class FireBrewingRecipe extends SmolderingRecipe {

    public FireBrewingRecipe(ResourceLocation id) {
        super(id, NonNullList.of(Ingredient.EMPTY), new FluidStack(Fluids.WATER, 1000), NonNullList.of(ItemStack.EMPTY), new FluidStack(Fluids.WATER, 1000), 400, 4, 2);
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean matches(SmolderingRecipeInput input, Level level) {
        if(input.fireLevel < 2 || input.getTier() < 4)
            return false;

        FluidStack fluid = input.getFluid();
        if(fluid.getFluid() != Fluids.WATER || fluid.getAmount() < 1000)
            return false;

        if(input.item.isEmpty())
            return false;

        // Get the last item (reagent)
        ItemStack reagent = input.item.get(input.item.size() - 1);

        // Extract potion from FluidStack Tag using PotionUtils
        Potion potion = Potions.WATER;
        if (fluid.hasTag()) {
            potion = PotionUtils.getPotion(fluid.getTag());
        }

        // Create a dummy potion item to check against PotionBrewing logic
        ItemStack potionItem = PotionUtils.setPotion(new ItemStack(Items.POTION), potion);

        // PotionBrewing is static in 1.20.1
        return PotionBrewing.hasMix(potionItem, reagent);
    }

    @Override
    public ItemStack assemble(SmolderingRecipeInput input, RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public FluidStack getFluidIngredient(SmolderingRecipeInput input, RegistryAccess registryAccess) {
        FluidStack fluidIngredient = input.getFluid().copy();
        fluidIngredient.setAmount(1000);
        return fluidIngredient;
    }

    @Override
    public NonNullList<ItemStack> assembleResults(SmolderingRecipeInput input, RegistryAccess registryAccess) {
        ItemStack reagent = input.item.get(input.item.size() - 1);

        // Reconstruct potion input
        Potion potion = Potions.WATER;
        if (input.getFluid().hasTag()) {
            potion = PotionUtils.getPotion(input.getFluid().getTag());
        }
        ItemStack potionItem = PotionUtils.setPotion(new ItemStack(Items.POTION), potion);

        // Calculate result (Static call)
        // mix(reagent, potion) -> returns the transformed potion stack
        // Note: In 1.20.1 PotionBrewing.mix signature is (reagent, potion) or (potion, reagent)?
        // It is PotionBrewing.mix(reagent, potion) -> result
        ItemStack potionResult = PotionBrewing.mix(reagent, potionItem);

        // Prepare item results: Copy all inputs except the reagent
        NonNullList<ItemStack> results = NonNullList.create();
        for (int i = 0; i < input.item.size() - 1; i++) {
            results.add(input.item.get(i).copy());
        }

        // Add container/remainder if applicable
        if(reagent.hasCraftingRemainingItem())
            results.add(reagent.getCraftingRemainingItem());

        return results;
    }

    @Override
    public FluidStack assembleFluidResult(SmolderingRecipeInput input, RegistryAccess registryAccess) {
        ItemStack reagent = input.item.get(input.item.size() - 1);

        // Get Input Potion
        Potion potion = Potions.WATER;
        if (input.getFluid().hasTag()) {
            potion = PotionUtils.getPotion(input.getFluid().getTag());
        }
        ItemStack potionItem = PotionUtils.setPotion(new ItemStack(Items.POTION), potion);

        // Mix
        ItemStack potionResultStack = PotionBrewing.mix(reagent, potionItem);
        Potion resultPotion = PotionUtils.getPotion(potionResultStack);

        // Create Result Fluid
        FluidStack fluidResult = new FluidStack(Fluids.WATER, 1000);

        // Write Potion to Fluid Tag (mimicking how PotionUtils writes to ItemStacks)
        ItemStack dummy = PotionUtils.setPotion(new ItemStack(Items.POTION), resultPotion);
        if (dummy.hasTag()) {
            fluidResult.setTag(dummy.getTag().copy());
        }

        return fluidResult;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
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