package com.minecart.central_heater.mixin;

import com.minecart.central_heater.AllBlockItem;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Debug(export = true)
@Mixin(WorldGenRegion.class)
public class WorldGenRegionMixin {
    @ModifyVariable(method = "setBlock", at = @At(value = "HEAD", ordinal = 0), argsOnly = true)
    public BlockState setBlock(BlockState state){
        if(state.getBlock() instanceof CauldronBlock)
            return AllBlockItem.iron_cauldron.get().defaultBlockState();
        return state;
    }
}
