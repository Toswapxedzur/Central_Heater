package com.minecart.central_heater.mixin;

import com.minecart.central_heater.item.similar_stack.SimilarStacker;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SimpleContainer.class)
public abstract class SimilarStackSimpleContainerMixin {
    @Shadow
    public abstract int getContainerSize();

    @Shadow
    public abstract ItemStack getItem(int index);

    @Shadow
    public abstract void setItem(int index, ItemStack stack);

    @Shadow
    public abstract int getMaxStackSize(ItemStack stack);

    @Shadow
    public abstract void setChanged();

    @Inject(method = "canAddItem", at = @At("HEAD"), cancellable = true)
    private void centralHeater$canAddSimilarItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        for (int i = 0; i < this.getContainerSize(); i++) {
            ItemStack itemStack = this.getItem(i);
            if (itemStack.isEmpty() || SimilarStacker.canMerge(itemStack, stack, this.getMaxStackSize(itemStack))) {
                cir.setReturnValue(true);
                return;
            }
        }
    }

    @Inject(method = "moveItemToOccupiedSlotsWithSameType", at = @At("HEAD"), cancellable = true)
    private void centralHeater$moveSimilarItemToOccupiedSlots(ItemStack stack, CallbackInfo ci) {
        if (!SimilarStacker.canParticipate(stack)) {
            return;
        }

        for (int i = 0; i < this.getContainerSize() && !stack.isEmpty(); i++) {
            ItemStack itemStack = this.getItem(i);
            if (SimilarStacker.canMerge(itemStack, stack, this.getMaxStackSize(itemStack))) {
                ItemStack merged = SimilarStacker.mergeStacks(itemStack, stack, this.getMaxStackSize(itemStack));
                this.setItem(i, merged);
                this.setChanged();
            }
        }

        ci.cancel();
    }
}
