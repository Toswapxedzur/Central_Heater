package com.minecart.central_heater.mixin;

import com.minecart.central_heater.item.similar_stack.SimilarStacker;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class SimilarStackSlotMixin {
    @Shadow
    public abstract boolean mayPlace(ItemStack stack);

    @Shadow
    public abstract ItemStack getItem();

    @Shadow
    public abstract int getMaxStackSize(ItemStack stack);

    @Shadow
    public abstract void setByPlayer(ItemStack stack);

    @Inject(method = "safeInsert(Lnet/minecraft/world/item/ItemStack;I)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    private void centralHeater$safeInsertSimilarStack(ItemStack stack, int increment, CallbackInfoReturnable<ItemStack> cir) {
        if (stack.isEmpty() || !this.mayPlace(stack)) {
            return;
        }

        ItemStack slotStack = this.getItem();
        int maxCount = this.getMaxStackSize(slotStack);
        int cappedIncrement = Math.min(increment, stack.getCount());
        if (cappedIncrement <= 0 || !SimilarStacker.canMerge(slotStack, stack, maxCount)) {
            return;
        }

        ItemStack mergeSource = stack;
        if (cappedIncrement < stack.getCount()) {
            mergeSource = stack.split(cappedIncrement);
        }

        ItemStack merged = SimilarStacker.mergeStacks(slotStack, mergeSource, maxCount);
        if (!mergeSource.isEmpty()) {
            stack.grow(mergeSource.getCount());
        }

        this.setByPlayer(merged);
        cir.setReturnValue(stack);
    }
}
