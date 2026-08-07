package com.minecart.central_heater.item.similar_stack;

import com.minecart.central_heater.item.AllDataComponents;
import com.minecart.central_heater.misc.Alltags;
import com.minecart.central_heater.mixin.ItemStackAccessor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class SimilarStacker {
    private static final ThreadLocal<Boolean> UPDATING = ThreadLocal.withInitial(() -> false);
    private static final Map<Item, ResourceLocation> REGISTERED_GROUPS = new LinkedHashMap<>();
    private static final List<SimilarStackGroupProvider> GROUP_PROVIDERS = new ArrayList<>();
    private static final String[] KIND_SUFFIXES = {
            "pressure_plate", "fence_gate", "trapdoor", "button", "door",
            "planks", "stairs", "slab", "fence", "wood", "log"
    };

    public static boolean isUpdating() {
        return UPDATING.get();
    }

    public static boolean canParticipate(ItemStack stack) {
        return !stack.isEmpty() && (hasContents(stack) || stack.is(Alltags.Items.SIMILAR_STACKABLE) || groupOf(stack).isPresent());
    }

    public static boolean hasContents(ItemStack stack) {
        return stack.has(contentsComponent());
    }

    public static boolean canMerge(ItemStack destination, ItemStack origin) {
        return canMerge(destination, origin, destination.getMaxStackSize());
    }

    public static boolean canMerge(ItemStack destination, ItemStack origin, int maxCount) {
        if (destination.isEmpty() || origin.isEmpty() || !canParticipate(destination) || !canParticipate(origin)) {
            return false;
        }

        Optional<ResourceLocation> destinationGroup = groupOf(destination);
        return destinationGroup.isPresent()
                && destinationGroup.equals(groupOf(origin))
                && destination.getCount() < stackLimit(destination, maxCount);
    }

    public static boolean canFullyMerge(ItemStack destination, ItemStack origin) {
        return canMerge(destination, origin) && destination.getCount() + origin.getCount() <= stackLimit(destination, destination.getMaxStackSize());
    }

    public static boolean canInteractiveMerge(ItemStack first, ItemStack second) {
        return canMerge(first, second) || canMerge(second, first);
    }

    public static ItemStack mergeForItemEntity(ItemStack destination, ItemStack origin, int amount) {
        return mergeStacks(destination, origin, amount);
    }

    public static int mergeInto(ItemStack destination, ItemStack origin, int maxCount) {
        int before = origin.getCount();
        mergeStacks(destination, origin, maxCount);
        return before - origin.getCount();
    }

    public static ItemStack mergeStacks(ItemStack destination, ItemStack origin, int maxCount) {
        if (!canMerge(destination, origin, maxCount)) {
            return destination;
        }

        int room = stackLimit(destination, maxCount) - destination.getCount();
        int moved = Math.min(room, origin.getCount());
        if (moved <= 0) {
            return destination;
        }

        List<ItemStack> combined = contentsOf(destination);
        SplitContents split = take(origin, moved);
        combined.addAll(split.taken());
        writeContents(origin, split.remaining());
        return stackFromContents(combined, destination);
    }

    public static ItemStack split(ItemStack stack, int amount) {
        if (stack.isEmpty() || amount <= 0) {
            return ItemStack.EMPTY;
        }

        SplitContents split = take(stack, Math.min(amount, stack.getCount()));
        writeContents(stack, split.remaining());
        return stackFromContents(split.taken(), stack);
    }

    public static ItemStack copyWithCount(ItemStack stack, int amount) {
        if (stack.isEmpty() || amount <= 0) {
            return ItemStack.EMPTY;
        }

        return stackFromContents(take(contentsOf(stack), Math.min(amount, stack.getCount())).taken(), stack);
    }

    public static ItemStack copyAndClear(ItemStack stack) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack copy = stack.copy();
        writeContents(stack, List.of());
        return copy;
    }

    public static void normalizeCount(ItemStack stack) {
        if (isUpdating() || !hasContents(stack)) {
            return;
        }

        int count = stack.getCount();
        if (count <= 0) {
            runUpdating(() -> stack.remove(contentsComponent()));
            return;
        }

        List<ItemStack> contents = contentsOf(stack);
        int total = totalCount(contents);
        if (total == count) {
            return;
        }

        if (total > count) {
            writeContents(stack, take(contents, count).taken());
        } else if (!contents.isEmpty()) {
            ItemStack first = contents.getFirst();
            first.grow(count - total);
            writeContents(stack, contents);
        }
    }

    public static boolean moveItemStackTo(ItemStack stack, List<Slot> slots, int startIndex, int endIndex, boolean reverseDirection) {
        if (!canParticipate(stack)) {
            return false;
        }

        boolean moved = false;
        int i = reverseDirection ? endIndex - 1 : startIndex;
        while (!stack.isEmpty() && (reverseDirection ? i >= startIndex : i < endIndex)) {
            Slot slot = slots.get(i);
            ItemStack slotStack = slot.getItem();
            if (canMerge(slotStack, stack, slot.getMaxStackSize(slotStack))) {
                ItemStack merged = mergeStacks(slotStack, stack, slot.getMaxStackSize(slotStack));
                slot.setByPlayer(merged);
                slot.setChanged();
                moved = true;
            }

            i += reverseDirection ? -1 : 1;
        }

        i = reverseDirection ? endIndex - 1 : startIndex;
        while (!stack.isEmpty() && (reverseDirection ? i >= startIndex : i < endIndex)) {
            Slot slot = slots.get(i);
            if (slot.getItem().isEmpty() && slot.mayPlace(stack)) {
                int limit = slot.getMaxStackSize(stack);
                slot.setByPlayer(stack.split(Math.min(stack.getCount(), limit)));
                slot.setChanged();
                return true;
            }

            i += reverseDirection ? -1 : 1;
        }

        return moved;
    }

    public static List<Component> tooltipLines(ItemStack stack) {
        return List.of();
    }

    public static Optional<TooltipComponent> tooltipImage(ItemStack stack) {
        if (!hasContents(stack)
                || stack.has(DataComponents.HIDE_TOOLTIP)
                || stack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP)) {
            return Optional.empty();
        }

        List<ItemStack> contents = contentsOf(stack);
        if (contents.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new SimilarStackTooltip(contents));
    }

    public static List<ItemStack> contentsOf(ItemStack stack) {
        ItemContainerContents contents = stack.get(contentsComponent());
        if (contents != null) {
            return consolidate(contents.nonEmptyStream().map(SimilarStacker::withoutSimilarContents).toList());
        }

        ItemStack copy = withoutSimilarContents(stack);
        return copy.isEmpty() ? new ArrayList<>() : new ArrayList<>(List.of(copy));
    }

    public static ItemStack stackFromContents(List<ItemStack> rawContents) {
        List<ItemStack> contents = consolidate(rawContents);
        int count = totalCount(contents);
        if (count <= 0) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = contents.getFirst().copyWithCount(count);
        writeContents(stack, contents);
        return stack;
    }

    private static ItemStack stackFromContents(List<ItemStack> rawContents, ItemStack representative) {
        List<ItemStack> contents = consolidate(rawContents);
        int count = totalCount(contents);
        if (count <= 0) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = withoutSimilarContents(representative).copyWithCount(count);
        writeContents(stack, contents);
        return stack;
    }

    public static ItemStack selectedContent(ItemStack stack) {
        List<ItemStack> contents = contentsOf(stack);
        return contents.isEmpty() ? ItemStack.EMPTY : contents.getFirst().copy();
    }

    public static boolean cycleSelected(ItemStack stack) {
        return cycleSelected(stack, 1);
    }

    public static boolean cycleSelected(ItemStack stack, int direction) {
        if (!hasContents(stack)) {
            return false;
        }

        List<ItemStack> contents = contentsOf(stack);
        if (contents.size() <= 1) {
            return false;
        }

        int steps = Math.floorMod(direction, contents.size());
        if (steps == 0) {
            return false;
        }

        for (int i = 0; i < steps; i++) {
            ItemStack first = contents.removeFirst();
            contents.add(first);
        }
        writeContents(stack, contents);
        return true;
    }

    public static ItemStack removeSelected(ItemStack stack, int amount) {
        return split(stack, amount);
    }

    public static ItemStack insertIntoStack(ItemStack stack, ItemStack inserted, int maxCount) {
        if (stack.isEmpty()) {
            return stackFromContents(contentsOf(inserted));
        }
        return mergeStacks(stack, inserted, maxCount);
    }

    public static Optional<ResourceLocation> groupOf(ItemStack stack) {
        if (hasContents(stack)) {
            List<ItemStack> contents = contentsOf(stack);
            return contents.isEmpty() ? Optional.empty() : groupOf(contents.getFirst());
        }

        ResourceLocation registeredGroup = REGISTERED_GROUPS.get(stack.getItem());
        if (registeredGroup != null) {
            return Optional.of(registeredGroup);
        }

        for (SimilarStackGroupProvider provider : GROUP_PROVIDERS) {
            Optional<ResourceLocation> group = provider.groupOf(stack);
            if (group.isPresent()) {
                return group;
            }
        }

        ResourceLocation key = BuiltInRegistries.ITEM.getKey(stack.getItem());
        String path = key.getPath();
        if (!path.startsWith("burnt_")) {
            return Optional.empty();
        }

        String burntPath = path.substring("burnt_".length());
        for (String suffix : KIND_SUFFIXES) {
            if (burntPath.equals(suffix) || burntPath.endsWith("_" + suffix)) {
                return Optional.of(ResourceLocation.fromNamespaceAndPath("central_heater", "burnt_" + suffix));
            }
        }
        return Optional.empty();
    }

    public static String kind(ItemStack stack) {
        return groupOf(stack).map(ResourceLocation::toString).orElse("");
    }

    public static void registerGroup(ResourceLocation group, ItemLike... items) {
        for (ItemLike item : items) {
            REGISTERED_GROUPS.put(item.asItem(), group);
        }
    }

    public static void registerGroupProvider(SimilarStackGroupProvider provider) {
        GROUP_PROVIDERS.add(provider);
    }

    public static void setContents(ItemStack target, List<ItemStack> contents) {
        writeContents(target, contents);
    }

    private static SplitContents take(ItemStack stack, int amount) {
        return take(contentsOf(stack), amount);
    }

    private static SplitContents take(List<ItemStack> contents, int amount) {
        List<ItemStack> taken = new ArrayList<>();
        List<ItemStack> remaining = new ArrayList<>();
        int left = amount;

        for (ItemStack content : contents) {
            if (left > 0) {
                int count = Math.min(left, content.getCount());
                taken.add(content.copyWithCount(count));
                left -= count;

                int rest = content.getCount() - count;
                if (rest > 0) {
                    remaining.add(content.copyWithCount(rest));
                }
            } else {
                remaining.add(content.copy());
            }
        }

        return new SplitContents(consolidate(taken), consolidate(remaining));
    }

    private static void writeContents(ItemStack target, List<ItemStack> rawContents) {
        List<ItemStack> contents = consolidate(rawContents);
        int count = totalCount(contents);

        runUpdating(() -> {
            if (!contents.isEmpty()) {
                ((ItemStackAccessor) (Object) target).centralHeater$setItem(contents.getFirst().getItem());
            }
            target.setCount(count);
            if (count <= 0) {
                target.remove(contentsComponent());
                return;
            }

            if (contents.size() == 1 && ItemStack.isSameItemSameComponents(withoutSimilarContents(target), contents.getFirst())) {
                target.remove(contentsComponent());
                return;
            }

            target.set(contentsComponent(), ItemContainerContents.fromItems(contents));
        });
    }

    private static ItemStack withoutSimilarContents(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.remove(contentsComponent());
        return copy;
    }

    private static List<ItemStack> consolidate(List<ItemStack> rawContents) {
        List<ItemStack> contents = new ArrayList<>();
        for (ItemStack rawContent : rawContents) {
            if (rawContent.isEmpty()) {
                continue;
            }

            if (hasContents(rawContent)) {
                contents.addAll(contentsOf(rawContent));
                continue;
            }

            ItemStack content = withoutSimilarContents(rawContent);
            boolean merged = false;
            for (ItemStack existing : contents) {
                if (ItemStack.isSameItemSameComponents(existing, content)) {
                    existing.grow(content.getCount());
                    merged = true;
                    break;
                }
            }

            if (!merged) {
                contents.add(content);
            }
        }
        return contents;
    }

    private static int totalCount(List<ItemStack> contents) {
        int count = 0;
        for (ItemStack content : contents) {
            count += content.getCount();
        }
        return count;
    }

    private static DataComponentType<ItemContainerContents> contentsComponent() {
        return AllDataComponents.SIMILAR_STACK_CONTENTS.get();
    }

    private static int stackLimit(ItemStack stack, int maxCount) {
        return Math.min(stack.getMaxStackSize(), maxCount);
    }

    private static void runUpdating(Runnable runnable) {
        runUpdating(() -> {
            runnable.run();
            return null;
        });
    }

    private static <T> T runUpdating(Supplier<T> supplier) {
        boolean wasUpdating = UPDATING.get();
        UPDATING.set(true);
        try {
            return supplier.get();
        } finally {
            UPDATING.set(wasUpdating);
        }
    }

    private record SplitContents(List<ItemStack> taken, List<ItemStack> remaining) {
    }
}
