package com.minecart.central_heater.mixin;

import com.minecart.central_heater.item.similar_stack.SimilarStacker;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class SimilarStackBlockItemMixin {
    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void centralHeater$useSelectedSimilarContent(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = context.getItemInHand();
        if (!SimilarStacker.hasContents(stack)) {
            return;
        }

        ItemStack selected = SimilarStacker.selectedContent(stack);
        if (selected.isEmpty()) {
            cir.setReturnValue(InteractionResult.FAIL);
            return;
        }

        int before = selected.getCount();
        BlockHitResult hitResult = new BlockHitResult(context.getClickLocation(), context.getClickedFace(), context.getClickedPos(), context.isInside());
        InteractionResult result = selected.getItem().useOn(new UseOnContext(context.getLevel(), context.getPlayer(), context.getHand(), selected, hitResult));
        if (result.consumesAction() && selected.getCount() < before) {
            SimilarStacker.removeSelected(stack, before - selected.getCount());
        }
        cir.setReturnValue(result);
    }
}
