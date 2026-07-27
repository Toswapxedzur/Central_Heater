package com.minecart.central_heater.mixin;

import com.minecart.central_heater.item.similar_stack.SimilarStacker;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.world.entity.item.ItemEntity.class)
public class SimilarStackItemEntityMixin {
    @Inject(method = "areMergable", at = @At("HEAD"), cancellable = true)
    private static void centralHeater$areSimilarStacksMergable(ItemStack destinationStack, ItemStack originStack, CallbackInfoReturnable<Boolean> cir) {
        if (SimilarStacker.canFullyMerge(destinationStack, originStack)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "merge(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;I)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    private static void centralHeater$mergeSimilarStacks(ItemStack destinationStack, ItemStack originStack, int amount, CallbackInfoReturnable<ItemStack> cir) {
        if (SimilarStacker.canMerge(destinationStack, originStack)) {
            cir.setReturnValue(SimilarStacker.mergeForItemEntity(destinationStack, originStack, amount));
        }
    }
}
