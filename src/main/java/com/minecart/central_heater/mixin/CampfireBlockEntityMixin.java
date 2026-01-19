package com.minecart.central_heater.mixin;

import com.minecart.central_heater.block_entity.AllBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CampfireBlockEntity.class)
public class CampfireBlockEntityMixin {

    /**
     * Redirects the BlockEntityType passed to the super constructor in CampfireBlockEntity.
     * This allows the vanilla CampfireBlockEntity (or its subclasses) to use our
     * custom BlockEntityType, which is necessary for custom data handling and syncing.
     */
    @Redirect(
            method = "<init>",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/block/entity/BlockEntityType;CAMPFIRE:Lnet/minecraft/world/level/block/entity/BlockEntityType;")
    )
    private static BlockEntityType<?> central_heater$replaceType() {
        // Return our custom burnable campfire type instead of the vanilla one
        return AllBlockEntity.burnable_campfire.get();
    }
}