package com.minecart.central_heater.mixin;

import com.minecart.central_heater.item.similar_stack.SimilarStacker;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public abstract class SimilarStackInventoryMixin {
    @Shadow
    public abstract ItemStack getItem(int index);

    @Shadow
    public abstract int getMaxStackSize(ItemStack stack);

    @Shadow
    public abstract void setItem(int index, ItemStack stack);

    @Inject(method = "hasRemainingSpaceForItem", at = @At("HEAD"), cancellable = true)
    private void centralHeater$hasRemainingSpaceForSimilarItem(ItemStack destination, ItemStack origin, CallbackInfoReturnable<Boolean> cir) {
        if (SimilarStacker.canMerge(destination, origin, this.getMaxStackSize(destination))) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "addResource(ILnet/minecraft/world/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
    private void centralHeater$addSimilarResource(int slot, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        ItemStack itemStack = this.getItem(slot);
        if (itemStack.isEmpty() && SimilarStacker.canParticipate(stack)) {
            int moved = Math.min(stack.getCount(), this.getMaxStackSize(stack));
            ItemStack inserted = stack.split(moved);
            inserted.setPopTime(5);
            this.setItem(slot, inserted);
            cir.setReturnValue(stack.getCount());
            return;
        }

        if (SimilarStacker.canMerge(itemStack, stack, this.getMaxStackSize(itemStack))) {
            ItemStack merged = SimilarStacker.mergeStacks(itemStack, stack, this.getMaxStackSize(itemStack));
            merged.setPopTime(5);
            this.setItem(slot, merged);
            cir.setReturnValue(stack.getCount());
        }
    }
}
