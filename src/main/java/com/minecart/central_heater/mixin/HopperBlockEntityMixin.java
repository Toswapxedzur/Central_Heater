package com.minecart.central_heater.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;

@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {

    /**
     * Redirects the max stack size check in the hopper's item movement logic.
     * This ensures that if a destination container (like a custom cauldron or tank)
     * has a smaller limit than the item's default max stack size, the hopper respects it.
     */
    @Redirect(
            method = "tryMoveInItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getMaxStackSize()I"),
            remap = true
    )
    private static int central_heater$getMaxStackSize(ItemStack stack, @Local(ordinal = 1, argsOnly = true) Container destination) {
        // We get the smaller of the two: the item's inherent limit or the container's specific limit for that stack
        int itemLimit = stack.getMaxStackSize();
        int containerLimit = destination.getMaxStackSize();

        return Math.min(itemLimit, containerLimit);
    }
}