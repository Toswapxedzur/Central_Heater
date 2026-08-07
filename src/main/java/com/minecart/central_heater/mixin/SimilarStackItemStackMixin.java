package com.minecart.central_heater.mixin;

import com.minecart.central_heater.item.similar_stack.SimilarStacker;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ItemStack.class)
public class SimilarStackItemStackMixin {
    @Inject(method = "split", at = @At("HEAD"), cancellable = true)
    private void centralHeater$splitSimilarContents(int amount, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (SimilarStacker.hasContents(stack)) {
            cir.setReturnValue(SimilarStacker.split(stack, amount));
        }
    }

    @Inject(method = "copyWithCount", at = @At("HEAD"), cancellable = true)
    private void centralHeater$copySimilarContentsWithCount(int count, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (SimilarStacker.hasContents(stack)) {
            cir.setReturnValue(SimilarStacker.copyWithCount(stack, count));
        }
    }

    @Inject(method = "copyAndClear", at = @At("HEAD"), cancellable = true)
    private void centralHeater$copyAndClearSimilarContents(CallbackInfoReturnable<ItemStack> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (SimilarStacker.hasContents(stack)) {
            cir.setReturnValue(SimilarStacker.copyAndClear(stack));
        }
    }

    @Inject(method = "setCount", at = @At("TAIL"))
    private void centralHeater$normalizeSimilarContents(int count, CallbackInfo ci) {
        SimilarStacker.normalizeCount((ItemStack) (Object) this);
    }

    @Inject(method = "limitSize", at = @At("TAIL"))
    private void centralHeater$normalizeSimilarContentsAfterLimit(int maxSize, CallbackInfo ci) {
        SimilarStacker.normalizeCount((ItemStack) (Object) this);
    }

    @Inject(method = "grow", at = @At("TAIL"))
    private void centralHeater$normalizeSimilarContentsAfterGrow(int increment, CallbackInfo ci) {
        SimilarStacker.normalizeCount((ItemStack) (Object) this);
    }

    @Inject(method = "shrink", at = @At("TAIL"))
    private void centralHeater$normalizeSimilarContentsAfterShrink(int decrement, CallbackInfo ci) {
        SimilarStacker.normalizeCount((ItemStack) (Object) this);
    }

    @Inject(method = "getTooltipLines", at = @At("RETURN"), cancellable = true)
    private void centralHeater$addSimilarContentsTooltip(Item.TooltipContext context, Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir) {
        List<Component> extraLines = SimilarStacker.tooltipLines((ItemStack) (Object) this);
        if (!extraLines.isEmpty()) {
            List<Component> lines = new ArrayList<>(cir.getReturnValue());
            lines.addAll(extraLines);
            cir.setReturnValue(lines);
        }
    }
}
