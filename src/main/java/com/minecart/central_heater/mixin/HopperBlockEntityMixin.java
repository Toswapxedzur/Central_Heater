package com.minecart.central_heater.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.minecart.central_heater.item.similar_stack.SimilarStacker;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true)
@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {
    @Redirect(method = "tryMoveInItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getMaxStackSize()I"))
    private static int getMaxStackSize(ItemStack stack, @Local(ordinal = 1, argsOnly = true) Container destination){
        return Math.min(stack.getMaxStackSize(), destination.getMaxStackSize(stack));
    }

    @Inject(method = "tryMoveInItem", at = @At("HEAD"), cancellable = true)
    private static void centralHeater$tryMoveSimilarItem(@Nullable Container source, Container destination, ItemStack stack, int slot, @Nullable Direction direction, CallbackInfoReturnable<ItemStack> cir) {
        if (stack.isEmpty() || !destination.canPlaceItem(slot, stack)) {
            return;
        }
        if (destination instanceof WorldlyContainer worldlyContainer && !worldlyContainer.canPlaceItemThroughFace(slot, stack, direction)) {
            return;
        }

        ItemStack slotStack = destination.getItem(slot);
        if (!slotStack.isEmpty() && SimilarStacker.canMerge(slotStack, stack, destination.getMaxStackSize(slotStack))) {
            ItemStack merged = SimilarStacker.mergeStacks(slotStack, stack, destination.getMaxStackSize(slotStack));
            destination.setItem(slot, merged);
            destination.setChanged();
            cir.setReturnValue(stack);
        }
    }
}
