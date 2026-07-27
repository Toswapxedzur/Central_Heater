package com.minecart.central_heater.mixin;

import com.minecart.central_heater.item.similar_stack.SimilarStacker;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerMenu.class)
public abstract class SimilarStackMenuMixin {
    @Shadow
    @Final
    public NonNullList<Slot> slots;

    @Inject(method = "moveItemStackTo", at = @At("HEAD"), cancellable = true)
    private void centralHeater$moveSimilarStackTo(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection, CallbackInfoReturnable<Boolean> cir) {
        if (SimilarStacker.canParticipate(stack)) {
            cir.setReturnValue(SimilarStacker.moveItemStackTo(stack, this.slots, startIndex, endIndex, reverseDirection));
        }
    }

    @Redirect(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isSameItemSameComponents(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private boolean centralHeater$treatSimilarStacksAsSameForClick(ItemStack stack, ItemStack other) {
        return ItemStack.isSameItemSameComponents(stack, other) || SimilarStacker.canInteractiveMerge(stack, other);
    }

    @Inject(method = "canItemQuickReplace", at = @At("HEAD"), cancellable = true)
    private static void centralHeater$canQuickReplaceSimilarStack(@Nullable Slot slot, ItemStack stack, boolean stackSizeMatters, CallbackInfoReturnable<Boolean> cir) {
        if (slot != null && SimilarStacker.canMerge(slot.getItem(), stack, slot.getMaxStackSize(slot.getItem()))) {
            int incoming = stackSizeMatters ? 0 : stack.getCount();
            cir.setReturnValue(slot.getItem().getCount() + incoming <= slot.getMaxStackSize(slot.getItem()));
        }
    }
}
