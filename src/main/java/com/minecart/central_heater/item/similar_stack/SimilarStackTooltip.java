package com.minecart.central_heater.item.similar_stack;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record SimilarStackTooltip(List<ItemStack> contents) implements TooltipComponent {
    public SimilarStackTooltip {
        contents = List.copyOf(contents);
    }
}
