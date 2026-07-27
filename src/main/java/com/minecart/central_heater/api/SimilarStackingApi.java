package com.minecart.central_heater.api;

import com.minecart.central_heater.item.similar_stack.SimilarStackGroupProvider;
import com.minecart.central_heater.item.similar_stack.SimilarStacker;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.Optional;

public final class SimilarStackingApi {
    private SimilarStackingApi() {
    }

    /**
     * Registers concrete items as one similar-stack group.
     * Items in the same group can merge into one visible representative stack while retaining their real contents.
     */
    public static void registerGroup(ResourceLocation group, ItemLike... items) {
        SimilarStacker.registerGroup(group, items);
    }

    /**
     * Registers a dynamic grouping rule for mods that cannot enumerate every item up front.
     * Return {@link Optional#empty()} when the stack should not participate.
     */
    public static void registerGroupProvider(SimilarStackGroupProvider provider) {
        SimilarStacker.registerGroupProvider(provider);
    }

    /**
     * Returns the similar-stack group for this stack, including groups registered by this API and Central Heater's defaults.
     */
    public static Optional<ResourceLocation> groupOf(ItemStack stack) {
        return SimilarStacker.groupOf(stack);
    }

    /**
     * Checks whether origin can merge into destination without mutating either stack.
     */
    public static boolean canMerge(ItemStack destination, ItemStack origin, int maxCount) {
        return SimilarStacker.canMerge(destination, origin, maxCount);
    }

    /**
     * Merges origin into destination, returning the representative stack and shrinking origin by the moved amount.
     */
    public static ItemStack merge(ItemStack destination, ItemStack origin, int maxCount) {
        return SimilarStacker.mergeStacks(destination, origin, maxCount);
    }

    /**
     * Returns the real item stacks stored by a mixed stack, or the stack itself when it is not mixed.
     */
    public static List<ItemStack> contentsOf(ItemStack stack) {
        return SimilarStacker.contentsOf(stack);
    }

    /**
     * Replaces the real contents stored on a representative stack.
     */
    public static void setContents(ItemStack stack, List<ItemStack> contents) {
        SimilarStacker.setContents(stack, contents);
    }

    /**
     * Rotates which stored item is selected. Positive direction moves forward; negative direction moves backward.
     */
    public static boolean cycleSelected(ItemStack stack, int direction) {
        return SimilarStacker.cycleSelected(stack, direction);
    }
}
