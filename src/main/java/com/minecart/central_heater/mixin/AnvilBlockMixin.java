package com.minecart.central_heater.mixin;

import com.minecart.central_heater.AllBlockItem;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilBlock.class)
public abstract class AnvilBlockMixin {

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private static void central_heater$handleSturdyAnvilDamage(BlockState state, CallbackInfoReturnable<BlockState> cir) {
        if (state.is(AllBlockItem.STURDY_ANVIL.get())) {
            cir.setReturnValue(AllBlockItem.CHIPPED_STURDY_ANVIL.get().defaultBlockState().setValue(AnvilBlock.FACING, state.getValue(AnvilBlock.FACING)));
        } else if (state.is(AllBlockItem.CHIPPED_STURDY_ANVIL.get())) {
            cir.setReturnValue(AllBlockItem.DAMAGED_STURDY_ANVIL.get().defaultBlockState().setValue(AnvilBlock.FACING, state.getValue(AnvilBlock.FACING)));
        }
    }
}
