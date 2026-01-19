package com.minecart.central_heater.mixin;

import com.minecart.central_heater.recipe.AllRecipe;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SimpleCookingRecipeBuilder.class)
public abstract class SimpleCookingRecipeBuilderMixin {

    /**
     * In 1.20.1, we must use the old item edible check instead of DataComponents.
     */
    @Inject(method = "determineRecipeCategory", at = @At("HEAD"), cancellable = true, remap = false)
    private static void central_heater$determineRecipeCategory(RecipeSerializer<? extends AbstractCookingRecipe> serializer, ItemLike result, CallbackInfoReturnable<CookingBookCategory> cir) {
        if (serializer == AllRecipe.HAUNTING_RECIPE_SERIALIZER.get()) {
            Item item = result.asItem();

            // Check if the item is food in 1.20.1
            if (item.isEdible()) {
                cir.setReturnValue(CookingBookCategory.FOOD);
            } else {
                // Determine if it's a block or miscellaneous item
                cir.setReturnValue(item instanceof BlockItem ? CookingBookCategory.BLOCKS : CookingBookCategory.MISC);
            }
        }
    }
}