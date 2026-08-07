package com.minecart.central_heater.mixin;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(SmeltItemFunction.class)
public abstract class SmeltItemFunctionMixin extends LootItemConditionalFunction {
    protected SmeltItemFunctionMixin(LootItemCondition[] predicates) {
        super(predicates);
    }

    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/RecipeManager;getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;"), cancellable = true)
    public void central_heater$beforeSmelt(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> returnable) {
        Optional<CampfireCookingRecipe> optional = context.getLevel().getRecipeManager()
                .getRecipeFor(RecipeType.CAMPFIRE_COOKING, new SimpleContainer(stack), context.getLevel());
        if (optional.isPresent()) {
            ItemStack itemstack = optional.get().getResultItem(context.getLevel().registryAccess());
            if (!itemstack.isEmpty()) {
                ItemStack copy = itemstack.copy();
                copy.setCount(stack.getCount() * itemstack.getCount());
                returnable.setReturnValue(copy);
            }
        }
    }
}
