package com.minecart.central_heater.mixin;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true)
@Mixin(BannerBlockEntity.class)
public abstract class BannerBlockEntityMixin {

    @Shadow private BannerPatternLayers patterns;

    @Inject(method = "loadAdditional", at = @At("TAIL"))
    private void central_heater$fixEmptyPatternSync(CompoundTag tag, HolderLookup.Provider registries, CallbackInfo ci) {
        if (!tag.contains("patterns")) {
            this.patterns = BannerPatternLayers.EMPTY;
        }
    }
    @Inject(method = "getUpdateTag", at = @At("RETURN"))
    private void central_heater$preventEmptyPacket(HolderLookup.Provider registries, CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag tag = cir.getReturnValue();
        if (tag.isEmpty()) {
            tag.putBoolean("central_heater_force_sync", true);
        }
    }

}