package com.minecart.central_heater.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(SmeltItemFunction.class)
public abstract class SmeltItemFunctionMixin extends LootItemConditionalFunction {
    protected SmeltItemFunctionMixin(List<LootItemCondition> predicates) {
        super(predicates);
    }

    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/RecipeManager;getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/item/crafting/RecipeInput;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;"), cancellable = true)
    public void beforeSmelt(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> returnable){
        Optional<RecipeHolder<CampfireCookingRecipe>> optional = context.getLevel().getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, new SingleRecipeInput(stack), context.getLevel());
        if (optional.isPresent()) {
            ItemStack itemstack = ((CampfireCookingRecipe)((RecipeHolder)optional.get()).value()).getResultItem(context.getLevel().registryAccess());
            if (!itemstack.isEmpty()) {
                returnable.setReturnValue(itemstack.copyWithCount(stack.getCount() * itemstack.getCount()));
            }
        }
    }
}
