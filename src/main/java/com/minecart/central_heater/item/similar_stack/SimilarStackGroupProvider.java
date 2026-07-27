package com.minecart.central_heater.item.similar_stack;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

@FunctionalInterface
public interface SimilarStackGroupProvider {
    Optional<ResourceLocation> groupOf(ItemStack stack);
}
