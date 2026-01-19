package com.minecart.central_heater.mixin;

import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractFurnaceBlockEntity.class)
public interface AbstractFurnaceBlockEntityAccessor {
    /**
     * Invokes the private isLit() method from AbstractFurnaceBlockEntity.
     * This is useful for checking if a furnace is currently burning fuel
     * without having to manually check the remaining burn time.
     */
    @Invoker("isLit")
    boolean central_heater$isLit();
}