package com.minecart.central_heater.mixin;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.misc.Config;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WorldGenRegion.class)
public class WorldGenRegionMixin {
    @ModifyVariable(
            method = "setBlock",
            at = @At("HEAD"),
            argsOnly = true
    )
    private BlockState central_heater$replaceVanillaCauldron(BlockState state) {
        if (Config.replaceCauldron && state.getBlock() instanceof CauldronBlock) {
            return AllBlockItem.IRON_CAULDRON.get().defaultBlockState();
        }
        return state;
    }
}