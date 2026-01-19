package com.minecart.central_heater.mixin;

import com.minecart.central_heater.mixin_interface.IAshProducer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlock.class)
public class AbstractFurnaceBlockMixin {

    /**
     * Injects into the onRemove method of all furnace types.
     * This handles dropping the accumulated internal ash when the block is broken,
     * ensuring the player doesn't lose the resources stored in the mixin's 'IAshProducer' fields.
     */
    @Inject(method = "onRemove", at = @At("HEAD"))
    private void central_heater$dropAshOnBreak(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving, CallbackInfo ci) {
        // Only drop if the block itself is actually changing (not just a state update)
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof IAshProducer ashFurnace) {
                ashFurnace.dropAsh(level, pos);
            }
        }
    }
}