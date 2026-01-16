package com.minecart.central_heater.mixin;

import com.minecart.central_heater.mixin_interface.IAshProducer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.world.level.block.AbstractFurnaceBlock.class)
public class AbstractFurnaceBlockMixin {
    @Inject(method = "onRemove", at = @At("HEAD"))
    private void central_heater$dropAshOnBreak(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving, CallbackInfo ci) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof IAshProducer ashFurnace) {
                ashFurnace.dropAsh(level, pos);
            }
        }
    }
}
